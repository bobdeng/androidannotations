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
package org.androidannotations.helper;

import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.util.List;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

public abstract class ModelConstants {

	public static final String GENERATION_SUFFIX = "_";

	@SuppressWarnings("unchecked")
	public static final List<Class<? extends Annotation>> VALID_ENHANCED_VIEW_SUPPORT_ANNOTATIONS = asList(EActivity.class, EViewGroup.class, EView.class, EBean.class, EFragment.class, EViewHolder.class);

	@SuppressWarnings("unchecked")
	public static final List<Class<? extends Annotation>> VALID_ENHANCED_COMPONENT_ANNOTATIONS = asList(EApplication.class, EActivity.class, EViewGroup.class, EView.class, EBean.class, EService.class, EIntentService.class, EReceiver.class, EProvider.class, EFragment.class, EViewHolder.class);

	@SuppressWarnings("unchecked")
	public static final List<Class<? extends Annotation>> VALID_ANDROID_ANNOTATIONS = asList(EApplication.class, EActivity.class, EViewGroup.class, EView.class, EBean.class, EService.class, EReceiver.class, EProvider.class, EFragment.class, SharedPref.class, Rest.class, EViewHolder.class);

	private ModelConstants() {
	}

}
