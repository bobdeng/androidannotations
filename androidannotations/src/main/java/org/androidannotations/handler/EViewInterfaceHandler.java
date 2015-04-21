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
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import org.androidannotations.annotations.EViewInterface;
import org.androidannotations.annotations.ViewInterface;
import org.androidannotations.api.ReflectInterfaceProxy;
import org.androidannotations.helper.APTCodeModelHelper;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.BaseGeneratedClassHolder;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun.codemodel.JExpr.cast;
import static com.sun.codemodel.JExpr.ref;

public class EViewInterfaceHandler extends BaseAnnotationHandler<BaseGeneratedClassHolder> {
    private TargetAnnotationHelper annotationHelper;
    private APTCodeModelHelper codeModelHelper = new APTCodeModelHelper();
    private static final Logger LOGGER = LoggerFactory.getLogger(EViewInterfaceHandler.class);
    private static final Map<String,List<String>> types=new HashMap<String,List<String>>();
    public EViewInterfaceHandler(ProcessingEnvironment processingEnvironment) {
		super(EViewInterface.class, processingEnvironment);
        annotationHelper = new TargetAnnotationHelper(processingEnv, getTarget());
	}
    public static void putMethod(String clzName,String method){
        types.get(clzName).add(method);
    }
    public static boolean isVaidateMethod(String clzName,String method){
        return types.get(clzName).contains(method);
    }
	@Override
	public void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        TypeElement typeElement = (TypeElement) element;
        types.put(typeElement.getQualifiedName().toString(),new ArrayList<String>());
        LOGGER.info("ViewInterface find interface {}",typeElement.getQualifiedName().toString());
	}

	@Override
	public void process(Element element, BaseGeneratedClassHolder holder) {

	}
}
