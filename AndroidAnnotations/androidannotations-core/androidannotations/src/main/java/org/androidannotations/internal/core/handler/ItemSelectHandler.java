/**
 * Copyright (C) 2010-2015 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.androidannotations.internal.core.handler;

import static com.helger.jcodemodel.JExpr._null;
import static com.helger.jcodemodel.JExpr.invoke;
import static com.helger.jcodemodel.JExpr.lit;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.androidannotations.AndroidAnnotationsEnvironment;
import org.androidannotations.ElementValidation;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.holder.EComponentWithViewSupportHolder;

import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.IJExpression;
import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JInvocation;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;

public class ItemSelectHandler extends AbstractViewListenerHandler {

	private JMethod onNothingSelectedMethod;

	public ItemSelectHandler(AndroidAnnotationsEnvironment environment) {
		super(ItemSelect.class, environment);
	}

	@Override
	public void validate(Element element, ElementValidation validation) {
		super.validate(element, validation);

		ExecutableElement executableElement = (ExecutableElement) element;

		validatorHelper.returnTypeIsVoid(executableElement, validation);

		validatorHelper.param.inOrder() //
				.primitiveOrWrapper(TypeKind.BOOLEAN) //
				.anyType().optional() //
				.validate(executableElement, validation);
	}

	@Override
	protected void makeCall(JBlock listenerMethodBody, JInvocation call, TypeMirror returnType) {
		listenerMethodBody.add(call);
	}

	@Override
	protected void processParameters(EComponentWithViewSupportHolder holder, JMethod listenerMethod, JInvocation itemSelectedCall, List<? extends VariableElement> parameters) {
		AbstractJClass narrowAdapterViewClass = getClasses().ADAPTER_VIEW.narrow(getCodeModel().wildcard());
		JVar onItemClickParentParam = listenerMethod.param(narrowAdapterViewClass, "parent");
		listenerMethod.param(getClasses().VIEW, "view");
		JVar onItemClickPositionParam = listenerMethod.param(getCodeModel().INT, "position");
		listenerMethod.param(getCodeModel().LONG, "id");

		itemSelectedCall.arg(JExpr.TRUE);
		boolean hasItemParameter = parameters.size() == 2;
		boolean secondParameterIsInt = false;
		String secondParameterQualifiedName = null;
		if (hasItemParameter) {
			VariableElement secondParameter = parameters.get(1);
			TypeMirror secondParameterType = secondParameter.asType();
			secondParameterQualifiedName = secondParameterType.toString();
			secondParameterIsInt = secondParameterType.getKind() == TypeKind.INT;
		}

		if (hasItemParameter) {

			if (secondParameterIsInt) {
				itemSelectedCall.arg(onItemClickPositionParam);
			} else {
				itemSelectedCall.arg(JExpr.cast(getJClass(secondParameterQualifiedName), invoke(onItemClickParentParam, "getAdapter").invoke("getItem").arg(onItemClickPositionParam)));
			}
		}

		onNothingSelectedMethod.param(narrowAdapterViewClass, "parent");
		IJExpression activityRef = holder.getGeneratedClass().staticRef("this");

		JInvocation nothingSelectedCall = invoke(activityRef, getMethodName());
		onNothingSelectedMethod.body().add(nothingSelectedCall);
		nothingSelectedCall.arg(JExpr.FALSE);
		if (hasItemParameter) {
			if (secondParameterIsInt) {
				nothingSelectedCall.arg(lit(-1));
			} else {
				nothingSelectedCall.arg(_null());
			}
		}
	}

	@Override
	protected JMethod createListenerMethod(JDefinedClass listenerAnonymousClass) {
		onNothingSelectedMethod = listenerAnonymousClass.method(JMod.PUBLIC, getCodeModel().VOID, "onNothingSelected");
		onNothingSelectedMethod.annotate(Override.class);
		return listenerAnonymousClass.method(JMod.PUBLIC, getCodeModel().VOID, "onItemSelected");
	}

	@Override
	protected String getSetterName() {
		return "setOnItemSelectedListener";
	}

	@Override
	protected AbstractJClass getListenerClass(EComponentWithViewSupportHolder holder) {
		return getClasses().ON_ITEM_SELECTED_LISTENER;
	}

	@Override
	protected AbstractJClass getListenerTargetClass(EComponentWithViewSupportHolder holder) {
		return getClasses().ADAPTER_VIEW.narrow(getCodeModel().wildcard());
	}

}
