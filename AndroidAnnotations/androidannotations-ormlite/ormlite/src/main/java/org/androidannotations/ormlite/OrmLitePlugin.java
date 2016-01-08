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
package org.androidannotations.ormlite;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.AndroidAnnotationsEnvironment;
import org.androidannotations.handler.AnnotationHandler;
import org.androidannotations.ormlite.handler.OrmLiteDaoHandler;
import org.androidannotations.plugin.AndroidAnnotationsPlugin;

public class OrmLitePlugin extends AndroidAnnotationsPlugin {

	private static final String NAME = "OrmLite";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<AnnotationHandler<?>> getHandlers(AndroidAnnotationsEnvironment androidAnnotationEnv) {
		List<AnnotationHandler<?>> annotationHandlers = new ArrayList<>();
		annotationHandlers.add(new OrmLiteDaoHandler(androidAnnotationEnv));
		return annotationHandlers;
	}
}
