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

import android.content.Intent;
import com.sun.codemodel.*;
import org.androidannotations.process.ProcessHolder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

import static com.sun.codemodel.JExpr._this;
import static com.sun.codemodel.JExpr.invoke;
import static com.sun.codemodel.JExpr.ref;
import static com.sun.codemodel.JMod.*;
import static javax.lang.model.element.ElementKind.CONSTRUCTOR;

public class EIntentHolder extends BaseGeneratedClassHolder implements IntentBuilder{

    JMethod buildMethod;
    JMethod getIntentMethod;
    JMethod putIntentMethod;
    JMethod parseIntentMethod;
    public EIntentHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
        super(processHolder, annotatedElement);
    }
    public  JMethod getBuildMethod(){
        if(buildMethod==null){
            JMethod method=generatedClass.method(PUBLIC|STATIC,generatedClass,"build");
            method.param(classes().INTENT,"intent");
            buildMethod=method;
        }
        return buildMethod;
    }
    public JMethod getGetIntentMethod(){
        if(getIntentMethod==null){
            JMethod method=generatedClass.method(PUBLIC,classes().INTENT,"getIntent_");
            getIntentMethod= method;
        }
        return getIntentMethod;
    }

    public JMethod getPutIntentMethod(){
        if(putIntentMethod==null){
            putIntentMethod=generatedClass.method(PUBLIC,codeModel().VOID,"putIntent_");
            putIntentMethod.param(classes().INTENT,"intent");
        }
        return putIntentMethod;
    }

    @Override
    public JMethod getGetter(Element element,JClass clz) {
        String fieldName=element.getSimpleName().toString().substring(0,1).toUpperCase()+element.getSimpleName().toString().substring(1);

        JMethod method=generatedClass.method(PUBLIC,clz,(clz.name().equals("boolean")?"is":"get")+fieldName);
        return method;
    }

    @Override
    public JMethod getSetter(Element element,JClass clz) {
        String fieldName=element.getSimpleName().toString().substring(0,1).toUpperCase()+element.getSimpleName().toString().substring(1);
        JMethod method=generatedClass.method(PUBLIC,generatedClass,"set"+fieldName);
        method.param(clz,"value");
        return method;
    }

    public JMethod getParseIntentMethod(){
        if(parseIntentMethod==null){
            parseIntentMethod=generatedClass.method(PRIVATE,codeModel().VOID,"parseIntent_");
            parseIntentMethod.param(classes().INTENT,"intent");
        }
        return parseIntentMethod;
    }
}
