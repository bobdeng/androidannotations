/**
 * Copyright (C) 2010-2014 eBusiness Information, Excilys Group
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
package org.androidannotations.holder;

import com.sun.codemodel.*;
import org.androidannotations.annotations.mvc.MVCAdapter;
import org.androidannotations.process.ProcessHolder;
import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr._this;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

import static com.sun.codemodel.JExpr.invoke;
import static com.sun.codemodel.JMod.*;
import static javax.lang.model.element.ElementKind.CONSTRUCTOR;

public class EViewHolder extends EComponentWithViewSupportHolder {

	protected static final String ALREADY_INFLATED_COMMENT = "" // +
			+ "The mAlreadyInflated_ hack is needed because of an Android bug\n" // +
			+ "which leads to infinite calls of onFinishInflate()\n" //
			+ "when inflating a layout with a parent and using\n" //
			+ "the <merge /> tag." //
	;

	private static final String SUPPRESS_WARNING_COMMENT = "" //
			+ "We use @SuppressWarning here because our java code\n" //
			+ "generator doesn't know that there is no need\n" //
			+ "to import OnXXXListeners from View as we already\n" //
			+ "are in a View." //
	;

	protected JBlock initBody;
	protected JMethod onFinishInflate;
	protected JFieldVar alreadyInflated;

    //palmwin start
    @Override
    public JMethod getOnDestroy() {
        return null;
    }

    @Override
    public JExpression getNewMvcAdapter() {
        return _new(refClass(MVCAdapter.class)).arg(_this().invoke("getContext"));
    }

    @Override
    public boolean needMvcAdapter() {
        return false;
    }
    //palmwin end

	public EViewHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
		super(processHolder, annotatedElement);
		addSuppressWarning();
		createConstructorAndBuilder();
	}

	private void addSuppressWarning() {
		generatedClass.annotate(SuppressWarnings.class).param("value", "unused");
		generatedClass.javadoc().append(SUPPRESS_WARNING_COMMENT);
	}

	private void createConstructorAndBuilder() {
		List<ExecutableElement> constructors = new ArrayList<ExecutableElement>();
		for (Element e : annotatedElement.getEnclosedElements()) {
			if (e.getKind() == CONSTRUCTOR) {
				constructors.add((ExecutableElement) e);
			}
		}

		for (ExecutableElement userConstructor : constructors) {
			JMethod copyConstructor = generatedClass.constructor(PUBLIC);
			JMethod staticHelper = generatedClass.method(PUBLIC | STATIC, generatedClass._extends(), "build");

			codeModelHelper.generifyStaticHelper(this, staticHelper, getAnnotatedElement());

			JBlock body = copyConstructor.body();
			JInvocation superCall = body.invoke("super");
			JInvocation newInvocation = JExpr._new(generatedClass);
			for (VariableElement param : userConstructor.getParameters()) {
				String paramName = param.getSimpleName().toString();
				JClass paramType = codeModelHelper.typeMirrorToJClass(param.asType(), this);
				copyConstructor.param(paramType, paramName);
				staticHelper.param(paramType, paramName);
				superCall.arg(JExpr.ref(paramName));
				newInvocation.arg(JExpr.ref(paramName));
			}

			JVar newCall = staticHelper.body().decl(generatedClass, "instance", newInvocation);
			staticHelper.body().invoke(newCall, getOnFinishInflate());
			staticHelper.body()._return(newCall);
			body.invoke(getInit());
		}
	}

	@Override
	protected void setContextRef() {
		contextRef = invoke("getContext");
	}

	@Override
	protected void setInit() {
		init = generatedClass.method(PRIVATE, codeModel().VOID, "init_");
		viewNotifierHelper.wrapInitWithNotifier();
	}

	@Override
	public JBlock getInitBody() {
		if (initBody == null) {
			setInit();
		}
		return initBody;
	}

	public void setInitBody(JBlock initBody) {
		this.initBody = initBody;
	}

	public JMethod getOnFinishInflate() {
		if (onFinishInflate == null) {
			setOnFinishInflate();
		}
		return onFinishInflate;
	}

	protected void setOnFinishInflate() {
		onFinishInflate = generatedClass.method(PUBLIC, codeModel().VOID, "onFinishInflate");
		onFinishInflate.annotate(Override.class);
		onFinishInflate.javadoc().append(ALREADY_INFLATED_COMMENT);

		JBlock ifNotInflated = onFinishInflate.body()._if(getAlreadyInflated().not())._then();
		ifNotInflated.assign(getAlreadyInflated(), JExpr.TRUE);

		getInit();
		viewNotifierHelper.invokeViewChanged(ifNotInflated);

		onFinishInflate.body().invoke(JExpr._super(), "onFinishInflate");
	}

	public JFieldVar getAlreadyInflated() {
		if (alreadyInflated == null) {
			setAlreadyInflated();
		}
		return alreadyInflated;
	}

	private void setAlreadyInflated() {
		alreadyInflated = generatedClass.field(PRIVATE, JType.parse(codeModel(), "boolean"), "alreadyInflated_", JExpr.FALSE);
	}
}
