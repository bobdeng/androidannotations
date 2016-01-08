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
package org.androidannotations.internal.rclass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.androidannotations.internal.exception.RClassNotFoundException;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.rclass.IRClass;

public class AndroidRClassFinder {

	private static final Logger LOGGER = LoggerFactory.getLogger(AndroidRClassFinder.class);

	private final ProcessingEnvironment processingEnv;

	public AndroidRClassFinder(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}

	public IRClass find() throws RClassNotFoundException {
		Elements elementUtils = processingEnv.getElementUtils();
		TypeElement androidRType = elementUtils.getTypeElement("android.R");
		if (androidRType == null) {
			LOGGER.error("The android.R class cannot be found");
			throw new RClassNotFoundException("The android.R class cannot be found");
		}

		LOGGER.info("Found Android class: {}", androidRType.toString());
		return new RClass(androidRType);
	}
}
