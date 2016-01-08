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

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.androidannotations.AndroidAnnotationsEnvironment;
import org.androidannotations.ElementValidation;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.helper.CanonicalNameConstants;
import org.androidannotations.holder.EComponentWithViewSupportHolder;

import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JInvocation;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;

public class EditorActionHandler extends AbstractViewListenerHandler {

	public EditorActionHandler(AndroidAnnotationsEnvironment environment) {
		super(EditorAction.class, environment);
	}

	@Override
	public void validate(Element element, ElementValidation validation) {
		super.validate(element, validation);

		ExecutableElement executableElement = (ExecutableElement) element;

		validatorHelper.returnTypeIsVoidOrBoolean(executableElement, validation);

		validatorHelper.param.anyOrder() //
				.extendsType(CanonicalNameConstants.TEXT_VIEW).optional() //
				.primitiveOrWrapper(TypeKind.INT).optional() //
				.type(CanonicalNameConstants.KEY_EVENT).optional() //
				.validate(executableElement, validation);
	}

	@Override
	protected void makeCall(JBlock listenerMethodBody, JInvocation call, TypeMirror returnType) {
		boolean returnMethodResult = returnType.getKind() != TypeKind.VOID;

		if (returnMethodResult) {
			listenerMethodBody._return(call);
		} else {
			listenerMethodBody.add(call);
			listenerMethodBody._return(JExpr.TRUE);
		}
	}

	@Override
	protected void processParameters(EComponentWithViewSupportHolder holder, JMethod listenerMethod, JInvocation call, List<? extends VariableElement> userParameters) {
		JVar textView = listenerMethod.param(getClasses().TEXT_VIEW, "textView");
		JVar actionId = listenerMethod.param(getCodeModel().INT, "actionId");
		JVar event = listenerMethod.param(getClasses().KEY_EVENT, "event");

		for (VariableElement param : userParameters) {
			String paramClassQualifiedName = param.asType().toString();
			if (isTypeOrSubclass(CanonicalNameConstants.TEXT_VIEW, param)) {
				call.arg(castArgumentIfNecessary(holder, CanonicalNameConstants.TEXT_VIEW, textView, param));
			} else if (paramClassQualifiedName.equals(CanonicalNameConstants.INTEGER) || paramClassQualifiedName.equals(getCodeModel().INT.fullName())) {
				call.arg(actionId);
			} else if (paramClassQualifiedName.equals(CanonicalNameConstants.KEY_EVENT)) {
				call.arg(event);
			}
		}
	}

	@Override
	protected JMethod createListenerMethod(JDefinedClass listenerAnonymousClass) {
		return listenerAnonymousClass.method(JMod.PUBLIC, getCodeModel().BOOLEAN, "onEditorAction");
	}

	@Override
	protected String getSetterName() {
		return "setOnEditorActionListener";
	}

	@Override
	protected AbstractJClass getListenerClass(EComponentWithViewSupportHolder holder) {
		return getClasses().TEXT_VIEW_ON_EDITOR_ACTION_LISTENER;
	}

	@Override
	protected AbstractJClass getListenerTargetClass(EComponentWithViewSupportHolder holder) {
		return getClasses().TEXT_VIEW;
	}
}
