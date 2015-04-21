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

import static com.sun.codemodel.JExpr.*;
import static com.sun.codemodel.JMod.*;
import static org.androidannotations.helper.ModelConstants.GENERATION_SUFFIX;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import com.sun.codemodel.*;
import org.androidannotations.annotations.EBean;
import org.androidannotations.process.ProcessHolder;

public class EBeanHolder extends EComponentWithViewSupportHolder {

	public static final String GET_INSTANCE_METHOD_NAME = "getInstance" + GENERATION_SUFFIX;

	private JFieldVar contextField;
    private JFieldVar viewField;
	private JMethod constructor;
	public EBeanHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
		super(processHolder, annotatedElement);
        EBean ebean=annotatedElement.getAnnotation(EBean.class);
        setConstructor(ebean.scope()== EBean.Scope.Singleton);
	}

	private void setConstructor(boolean singleTone) {
		constructor = generatedClass.constructor(PRIVATE);
		JVar constructorContextParam = constructor.param(classes().CONTEXT, "context");
        JVar constructorViewParam = constructor.param(classes().OBJECT,"view");
		JBlock constructorBody = constructor.body();
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(annotatedElement.getEnclosedElements());
		ExecutableElement superConstructor = constructors.get(0);
		if (superConstructor.getParameters().size() == 1) {
			constructorBody.invoke("super").arg(constructorContextParam);
		}
        if(singleTone)
        {
            constructorBody.assign(getContextField(), constructorContextParam.invoke("getApplicationContext"));
            constructorBody.assign(getViewField(),_null());
        }else{
            constructorBody.assign(getContextField(), constructorContextParam);
            constructorBody.assign(getViewField(),constructorViewParam);
        }
	}

    public JAssignmentTarget getViewField() {
        if(viewField==null){
            viewField=generatedClass.field(PRIVATE,classes().OBJECT,"view_");
        }
        return viewField;
    }

    public JFieldVar getContextField() {
		if (contextField == null) {
			contextField = generatedClass.field(PRIVATE, classes().CONTEXT, "context_");
		}
		return contextField;
	}

	@Override
	protected void setContextRef() {
		contextRef = getContextField();
	}

	protected void setInit() {
		init = generatedClass.method(PRIVATE, processHolder.codeModel().VOID, "init_");
	}

    public void invokeInitInConstructor() {
        JBlock constructorBody = constructor.body();
        constructorBody.invoke(getInit());
    }

	public void createFactoryMethod(boolean hasSingletonScope) {

		JMethod factoryMethod = generatedClass.method(PUBLIC | STATIC, generatedClass, GET_INSTANCE_METHOD_NAME);

		JVar factoryMethodContextParam = factoryMethod.param(classes().CONTEXT, "context");
        JVar factoryViewParam =factoryMethod.param(classes().OBJECT,"view");
		JBlock factoryMethodBody = factoryMethod.body();
		/*
		 * Singletons are bound to the application context
		 */
		if (hasSingletonScope) {

			JFieldVar instanceField = generatedClass.field(PRIVATE | STATIC, generatedClass, "instance_");

			JBlock creationBlock = factoryMethodBody //
					._if(instanceField.eq(_null())) //
					._then();
 			JVar previousNotifier = viewNotifierHelper.replacePreviousNotifierWithNull(creationBlock);
			viewNotifierHelper.resetPreviousNotifier(creationBlock, previousNotifier);
            creationBlock._return(creationBlock.invoke(createSyncNewInstanceMethod(instanceField)).arg(factoryMethodContextParam));
 			factoryMethodBody._return(instanceField);
		} else {

			factoryMethodBody._return(_new(generatedClass).arg(factoryMethodContextParam).arg(factoryViewParam));
		}
        JMethod factoryMethod1=generatedClass.method(PUBLIC | STATIC, generatedClass, GET_INSTANCE_METHOD_NAME);
        JVar factoryMethodContextParam1 = factoryMethod1.param(classes().CONTEXT, "context");
        factoryMethod1.body()._return(invoke(factoryMethod).arg(factoryMethodContextParam1).arg(_null()));

	}
    public JMethod createSyncNewInstanceMethod(JFieldVar instanceField) {
        JMethod syncNewInstanceMethod=generatedClass.method(PUBLIC|STATIC|SYNCHRONIZED,generatedClass,"newInstance_");
        JVar factoryMethodContextParam = syncNewInstanceMethod.param(classes().CONTEXT, "context");
        JBlock creationBlock = syncNewInstanceMethod.body()
                ._if(instanceField.eq(_null())) //
                ._then();
        creationBlock.assign(instanceField, _new(generatedClass).arg(factoryMethodContextParam.invoke("getApplicationContext")).arg(_null()));
        creationBlock.invoke(instanceField, getInit());
        syncNewInstanceMethod.body()._return(instanceField);
        return syncNewInstanceMethod;
    }
  	public void createRebindMethod() {
		JMethod rebindMethod = generatedClass.method(PUBLIC, codeModel().VOID, "rebind");
		JVar contextParam = rebindMethod.param(classes().CONTEXT, "context");
		JBlock body = rebindMethod.body();
		body.assign(getContextField(), contextParam);
		body.invoke(getInit());
	}
}
