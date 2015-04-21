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
import org.androidannotations.annotations.ViewInterface;
import org.androidannotations.helper.APTCodeModelHelper;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.BaseGeneratedClassHolder;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EViewInterfaceMethodHandler extends BaseAnnotationHandler<BaseGeneratedClassHolder> {
    private TargetAnnotationHelper annotationHelper;
    private APTCodeModelHelper codeModelHelper = new APTCodeModelHelper();
    private static final Logger LOGGER = LoggerFactory.getLogger(EViewInterfaceMethodHandler.class);
    static List<String> viewInterfaceMethods=new ArrayList<String>();
    public EViewInterfaceMethodHandler(ProcessingEnvironment processingEnvironment) {
		super(EViewInterfaceMethod.class, processingEnvironment);
        annotationHelper = new TargetAnnotationHelper(processingEnv, getTarget());
	}
    public List<String> getViewInterfaceMethods(){
        return viewInterfaceMethods;
    }
    public static boolean exist(String method){
       LOGGER.info("ViewInterface check exist method {} methods{}",method,viewInterfaceMethods);
       method=getMethodNameAndParameters(method);
       return viewInterfaceMethods.contains(method);
    }
	@Override
	public void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        LOGGER.info("ViewInterface find method {}",element.toString());
        viewInterfaceMethods.add(getMethodNameAndParameters(element.toString()));
	}
    private static String getMethodNameAndParameters(String method){
        Pattern pattern= Pattern.compile("([\\w]+\\(.*\\))");
        Matcher m=pattern.matcher(method);
        if(m.find()){
            return m.group(1);
        }
        return null;

    }
	@Override
	public void process(Element element, BaseGeneratedClassHolder holder) {

	}
}
