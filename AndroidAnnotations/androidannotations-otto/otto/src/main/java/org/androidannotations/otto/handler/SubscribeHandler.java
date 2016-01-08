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

import javax.lang.model.element.ExecutableElement;

import org.androidannotations.AndroidAnnotationsEnvironment;
import org.androidannotations.ElementValidation;
import org.androidannotations.helper.ValidatorParameterHelper;
import org.androidannotations.otto.helper.OttoClasses;

public class SubscribeHandler extends AbstractOttoHandler {

	public SubscribeHandler(AndroidAnnotationsEnvironment environment) {
		super(OttoClasses.SUBSCRIBE, environment);
	}

	@Override
	protected ValidatorParameterHelper.Validator getParamValidator() {
		return validatorHelper.param.anyType();
	}

	@Override
	protected void validateReturnType(ExecutableElement executableElement, ElementValidation validation) {
		validatorHelper.returnTypeIsVoid(executableElement, validation);
	}
}
