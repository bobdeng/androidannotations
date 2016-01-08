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
package org.androidannotations.handler;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.androidannotations.AndroidAnnotationsEnvironment;
import org.androidannotations.ElementValidation;
import org.androidannotations.holder.GeneratedClassHolder;

public abstract class BaseGeneratingAnnotationHandler<T extends GeneratedClassHolder> extends BaseAnnotationHandler<T> implements GeneratingAnnotationHandler<T> {

	public BaseGeneratingAnnotationHandler(Class<?> targetClass, AndroidAnnotationsEnvironment environment) {
		super(targetClass, environment);
	}

	public BaseGeneratingAnnotationHandler(String target, AndroidAnnotationsEnvironment environment) {
		super(target, environment);
	}

	@Override
	protected void validate(Element element, ElementValidation valid) {
		validatorHelper.isNotFinal(element, valid);

		if (isInnerClass(element)) {

			validatorHelper.isNotPrivate(element, valid);

			validatorHelper.isStatic(element, valid);

			validatorHelper.enclosingElementHasAndroidAnnotation(element, valid);

			validatorHelper.enclosingElementIsNotAbstractIfNotAbstract(element, valid);
		}
	}

	private boolean isInnerClass(Element element) {
		TypeElement typeElement = (TypeElement) element;
		return typeElement.getNestingKind().isNested();
	}
}
