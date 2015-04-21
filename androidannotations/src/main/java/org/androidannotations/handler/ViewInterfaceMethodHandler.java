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

import org.androidannotations.annotations.EViewInterfaceMethod;
import org.androidannotations.annotations.ViewInterfaceMethod;
import org.androidannotations.helper.APTCodeModelHelper;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.BaseGeneratedClassHolder;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

public class ViewInterfaceMethodHandler extends BaseAnnotationHandler<BaseGeneratedClassHolder> {
    private TargetAnnotationHelper annotationHelper;
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewInterfaceMethodHandler.class);
    public ViewInterfaceMethodHandler(ProcessingEnvironment processingEnvironment) {
		super(ViewInterfaceMethod.class, processingEnvironment);
        annotationHelper = new TargetAnnotationHelper(processingEnv, getTarget());
	}

	@Override
	public void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        LOGGER.info("ViewInterface find method {}",element.toString());
        if(!EViewInterfaceMethodHandler.exist(element.toString())){
            valid.invalidate();
            annotationHelper.printAnnotationError(element,"The ViewInterface method is not declared in any ViewInterface method "+element.toString()+" methods "+EViewInterfaceMethodHandler.viewInterfaceMethods);
        }
	}

	@Override
	public void process(Element element, BaseGeneratedClassHolder holder) {

	}
}
