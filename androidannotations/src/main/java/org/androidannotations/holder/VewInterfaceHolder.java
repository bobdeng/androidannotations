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
import org.androidannotations.process.ProcessHolder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

import static com.sun.codemodel.JExpr.*;
import static com.sun.codemodel.JMod.*;
import static javax.lang.model.element.ElementKind.CONSTRUCTOR;

public class VewInterfaceHolder extends EComponentWithViewSupportHolder {


    public VewInterfaceHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
        super(processHolder, annotatedElement);
    }

    JBlock initBody;
	@Override
	protected void setContextRef() {
		contextRef = invoke("getContext");
	}

	@Override
    public void setInit() {
		init = generatedClass.method(PRIVATE, codeModel().VOID, "init_");

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


}
