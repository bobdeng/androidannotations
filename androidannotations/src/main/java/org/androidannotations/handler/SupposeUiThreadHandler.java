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
package org.androidannotations.handler;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JMethod;

import org.androidannotations.annotations.SupposeUiThread;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.helper.APTCodeModelHelper;
import org.androidannotations.holder.EComponentHolder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

public class SupposeUiThreadHandler extends SupposeThreadHandler {

	private static final String METHOD_CHECK_UI_THREAD = "checkUiThread";

	private final APTCodeModelHelper helper = new APTCodeModelHelper();

	public SupposeUiThreadHandler(ProcessingEnvironment processingEnvironment) {
		super(SupposeUiThread.class, processingEnvironment);
	}

	@Override
	public void process(Element element, EComponentHolder holder) throws Exception {
		ExecutableElement executableElement = (ExecutableElement) element;

		JMethod delegatingMethod = helper.overrideAnnotatedMethod(executableElement, holder);
		JBlock body = delegatingMethod.body();

		JClass bgExecutor = refClass(BackgroundExecutor.class);

		body.pos(0);
		body.staticInvoke(bgExecutor, METHOD_CHECK_UI_THREAD);
		body.pos(body.getContents().size());
	}
}
