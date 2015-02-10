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

import org.androidannotations.annotations.mvc.MVCAdapter;
import org.androidannotations.process.ProcessHolder;

import javax.lang.model.element.TypeElement;

import static com.sun.codemodel.JExpr.*;
import static com.sun.codemodel.JMod.PRIVATE;
import static com.sun.codemodel.JMod.PUBLIC;

public class EViewHolderHolder extends EComponentWithViewSupportHolder {

    JVar initView;
    @Override
    public JMethod getOnDestroy() {
        return null;
    }

    @Override
    public JExpression getNewMvcAdapter() {
        return _new(refClass(MVCAdapter.class)).arg(contextRef);
    }

    @Override
    public boolean needMvcAdapter() {
        return false;
    }
    public void createMvcAdapter(){
        if(mvcAdapterField==null)
        {
            mvcAdapterField= generatedClass.field(PRIVATE,refClass(MVCAdapter.class),"mMvcAdapter");
            JBlock init= getCreateMvcMethod().body();
            init.assign(mvcAdapterField,getNewMvcAdapter());

        }
    }
    public EViewHolderHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
		super(processHolder, annotatedElement);
	}

    @Override
    protected void setContextRef() {
        contextRef = initView.invoke("getContext");
    }
    @Override
    public JBlock getOnViewChangedBody() {
       return this.getInit().body();
    }
    public JVar getOnViewChangedHasViewsParam() {
        return initView;
    }
    @Override
    protected void setInit() {
        init = generatedClass.method(PRIVATE, codeModel().VOID, "init_");
        initView=init.param(classes().VIEW,"view");
       // viewNotifierHelper.wrapInitWithNotifier();
    }

    public void createConstructor(){
        JMethod constructor = generatedClass.constructor(PUBLIC);
        JVar viewItemVar=constructor.param( classes().VIEW, "viewItem");
        constructor.body().invoke("super").arg(viewItemVar);
        constructor.body().invoke(getInit()).arg(viewItemVar);

    }

}
