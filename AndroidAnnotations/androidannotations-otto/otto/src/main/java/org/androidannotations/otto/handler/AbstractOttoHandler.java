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
package org.androidannotations.otto.handler;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import org.androidannotations.AndroidAnnotationsEnvironment;
import org.androidannotations.ElementValidation;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.helper.ValidatorParameterHelper;
import org.androidannotations.holder.EComponentHolder;

import com.helger.jcodemodel.JMethod;

public abstract class AbstractOttoHandler extends BaseAnnotationHandler<EComponentHolder> {

	public AbstractOttoHandler(String target, AndroidAnnotationsEnvironment environment) {
		super(target, environment);
	}

	@Override
	public void validate(Element element, ElementValidation valid) {
		if (!annotationHelper.enclosingElementHasEnhancedComponentAnnotation(element)) {
			// do nothing when otto annotations are used in non-enhanced classes
			return;
		}

		ExecutableElement executableElement = (ExecutableElement) element;

		validateReturnType(executableElement, valid);

		validatorHelper.isPublic(element, valid);

		validatorHelper.doesntThrowException(executableElement, valid);

		validatorHelper.isNotFinal(element, valid);

		getParamValidator().validate(executableElement, valid);
	}

	protected abstract ValidatorParameterHelper.Validator getParamValidator();

	protected abstract void validateReturnType(ExecutableElement executableElement, ElementValidation validation);

	@Override
	public void process(Element element, EComponentHolder holder) throws Exception {
		if (!annotationHelper.enclosingElementHasEnhancedComponentAnnotation(element)) {
			// do nothing when otto annotations are used in non-enhanced classes
			return;
		}
		ExecutableElement executableElement = (ExecutableElement) element;

		JMethod method = codeModelHelper.overrideAnnotatedMethod(executableElement, holder);

		addOttoAnnotation(executableElement, method);
	}

	private void addOttoAnnotation(ExecutableElement element, JMethod method) {
		for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
			if (annotationMirror.getAnnotationType().toString().equals(getTarget())) {
				codeModelHelper.copyAnnotation(method, annotationMirror);
				break;
			}
		}
	}

}
