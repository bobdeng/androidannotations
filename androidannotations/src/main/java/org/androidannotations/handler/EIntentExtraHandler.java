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

import com.sun.codemodel.*;
import org.androidannotations.annotations.EIntent;
import org.androidannotations.annotations.IntentExtra;
import org.androidannotations.helper.APTCodeModelHelper;
import org.androidannotations.helper.ReadIntentHelper;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.EIntentHolder;
import org.androidannotations.holder.IntentBuilder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;
import org.androidannotations.process.ProcessHolder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.sun.codemodel.JExpr._this;
import static com.sun.codemodel.JExpr.ref;
import static org.androidannotations.helper.ModelConstants.GENERATION_SUFFIX;

public class EIntentExtraHandler extends BaseAnnotationHandler<IntentBuilder> {
    private TargetAnnotationHelper annotationHelper;
    private APTCodeModelHelper codeModelHelper = new APTCodeModelHelper();

	public EIntentExtraHandler(ProcessingEnvironment processingEnvironment) {
		super(IntentExtra.class, processingEnvironment);
        annotationHelper = new TargetAnnotationHelper(processingEnv, getTarget());
	}


	@Override
	public void validate(Element element, AnnotationElements validatedElements, IsValid valid) {

	}

	@Override
	public void process(Element element, IntentBuilder holder) {
       JMethod getIntent=holder.getGetIntentMethod();
       JMethod putIntent=holder.getPutIntentMethod();
       setPut(putIntent,element);
       TypeMirror elementType = element.asType();
        elementType = holder.processingEnvironment().getTypeUtils().erasure(elementType);
    
       JClass clz=codeModelHelper.typeMirrorToJClass(elementType,holder);
       setSetter(element,holder,clz);
       setGetter(element,holder,clz);
       setParse(holder.getParseIntentMethod(),element,elementType.toString());

	}
    private void setParse(JMethod method,Element element,String elementType){
        JFieldRef beanField = ref(element.getSimpleName().toString());
        method.body().assign(beanField, ReadIntentHelper.readIntent(method.listParams()[0],elementType,this,element));
    }
    private void setSetter(Element element,IntentBuilder holder,JClass clz){
        JMethod method=holder.getSetter(element,clz);
        JFieldRef beanField = ref(element.getSimpleName().toString());
        method.body().assign(beanField,method.listParams()[0]);
        method.body()._return(_this());
    }
    private void setGetter(Element element,IntentBuilder holder,JClass clz){
        JMethod method=holder.getGetter(element,clz);
        JFieldRef beanField = ref(element.getSimpleName().toString());
        method.body()._return(beanField);
    }
    private void setPut(JMethod putMethod,Element element){
        JVar intent=putMethod.listParams()[0];
        JFieldRef beanField = ref(element.getSimpleName().toString());
        putMethod.body().invoke(intent,"putExtra").arg(element.getSimpleName().toString()).arg(beanField);

    }
}
