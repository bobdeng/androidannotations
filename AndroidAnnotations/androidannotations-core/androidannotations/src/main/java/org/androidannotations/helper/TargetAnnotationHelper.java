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
package org.androidannotations.helper;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;

import org.androidannotations.AndroidAnnotationsEnvironment;

public class TargetAnnotationHelper extends AnnotationHelper {

	private String annotationName;

	public TargetAnnotationHelper(AndroidAnnotationsEnvironment environment, String annotationName) {
		super(environment);
		this.annotationName = annotationName;
	}

	@SuppressWarnings("unchecked")
	public <T> T extractAnnotationValueParameter(Element element) {
		return (T) extractAnnotationParameter(element, "value");
	}

	@SuppressWarnings("unchecked")
	public <T> T extractAnnotationParameter(Element element, String methodName) {
		return (T) extractAnnotationParameter(element, annotationName, methodName);
	}

	public DeclaredType extractAnnotationClassParameter(Element element) {
		return extractAnnotationClassParameter(element, annotationName);
	}

	public String getTarget() {
		return annotationName;
	}

	public String actionName() {
		return actionName(annotationName);
	}

	public static String annotationName(String annotationName) {
		return "@" + annotationName;
	}

	public static String annotationName(Class<? extends Annotation> annotation) {
		return annotationName(annotation.getName());
	}

	public String annotationName() {
		return annotationName(annotationName);
	}
}
