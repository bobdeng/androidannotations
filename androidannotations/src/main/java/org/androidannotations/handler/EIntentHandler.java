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

import android.content.Intent;
import com.sun.codemodel.*;
import org.androidannotations.annotations.EIntent;
import org.androidannotations.annotations.EView;
import org.androidannotations.holder.BaseGeneratedClassHolder;
import org.androidannotations.holder.EIntentHolder;
import org.androidannotations.holder.EViewHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;
import org.androidannotations.process.ProcessHolder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class EIntentHandler extends BaseGeneratingAnnotationHandler<EIntentHolder> {

	public EIntentHandler(ProcessingEnvironment processingEnvironment) {
		super(EIntent.class, processingEnvironment);
	}

	@Override
	public EIntentHolder createGeneratedClassHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
		return new EIntentHolder(processHolder, annotatedElement);
	}

	@Override
	public void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
		super.validate(element, validatedElements, valid);

	}

	@Override
	public void process(Element element, EIntentHolder holder) {
        EIntent eIntent=element.getAnnotation(EIntent.class);
        JMethod getIntent=holder.getGetIntentMethod();
        JMethod putIntent=holder.getPutIntentMethod();
        JMethod buildMethod=holder.getBuildMethod();
        JMethod parseIntent=holder.getParseIntentMethod();
        //getIntent
        buildGetIntent(getIntent.body(),putIntent,eIntent.value());
        buildBuildIntent(buildMethod,parseIntent,element,holder);


	}
    private void buildBuildIntent(JMethod buildMethod,JMethod parseIntent,Element element,EIntentHolder holder){
        JInvocation ret=JExpr._new(holder.getGeneratedClass());
        JVar varResult=buildMethod.body().decl(holder.getGeneratedClass(), "instance").init(ret);
        buildMethod.body().invoke(varResult, parseIntent).arg(buildMethod.listParams()[0]);
        buildMethod.body()._return(varResult);
    }
    private void buildGetIntent(JBlock block,JMethod putIntent,String action){
        JInvocation intent= JExpr._new(classes().INTENT).arg(action);
        JVar varIntent=block.decl(classes().INTENT,"intent").init(intent);
        block.invoke(putIntent).arg(varIntent);
        block._return(varIntent);

    }
}
