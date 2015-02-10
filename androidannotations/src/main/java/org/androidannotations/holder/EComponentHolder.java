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

import static com.sun.codemodel.JExpr.cast;
import static com.sun.codemodel.JMod.PRIVATE;
import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr._super;
import static com.sun.codemodel.JExpr._this;
import static com.sun.codemodel.JMod.PRIVATE;
import static com.sun.codemodel.JMod.PUBLIC;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import com.sun.codemodel.*;
import org.androidannotations.annotations.fragment.FragmentEventListener;
import org.androidannotations.annotations.mvc.MVCAdapter;
import org.androidannotations.handler.FragmentArgHandler;
import org.androidannotations.helper.CaseHelper;
import org.androidannotations.helper.ModelConstants;
import org.androidannotations.process.ProcessHolder;

public abstract class EComponentHolder extends BaseGeneratedClassHolder {

    private static final String METHOD_MAIN_LOOPER = "getMainLooper";

	protected JExpression contextRef;
	protected JMethod init;
	private JVar resourcesRef;
	private JFieldVar powerManagerRef;
	private Map<TypeMirror, JFieldVar> databaseHelperRefs = new HashMap<TypeMirror, JFieldVar>();
	private JVar handler;
    ////add by palmwin
    protected JFieldVar mvcAdapterField;
    protected  JMethod onRegister;
    protected  JMethod onEvent;
    protected JSwitch eventSwitch;
    public JMethod getOnEvent(){
        if(onEvent==null){
            generatedClass._implements(refClass(FragmentEventListener.class));
            onEvent=generatedClass.method(PUBLIC,codeModel().VOID,"onFragmentEvent");
            onEvent.param(int.class,"event");
            onEvent.varParam(Object.class,"args");
            onEvent.annotate(Override.class);
            eventSwitch=onEvent.body()._switch(onEvent.params().get(0));
            eventSwitch._default().body()._break();
        }
        return onEvent;
    }
    public JSwitch getEventSwitch(){
        return eventSwitch;
    }
    public void createMvcAdapter(){
        if(mvcAdapterField==null)
        {
            mvcAdapterField= generatedClass.field(PRIVATE,refClass(MVCAdapter.class),"mMvcAdapter");
            JBlock init= getCreateMvcMethod().body();
            init.assign(mvcAdapterField,getNewMvcAdapter());
            JBlock regBody=getOnRegister().body();
            if(this.needMvcAdapter()){
                regBody.invoke(getMvcAdapterField(),"register").arg(_this());
                JMethod onDestroy=getOnDestroy();
                if(onDestroy!=null){
                    JBlock destroyBody=onDestroy.body();
                    destroyBody.invoke(getMvcAdapterField(),"unregister");
                }
            }

        }
    }
    public abstract JMethod getOnDestroy();
    public JMethod getOnRegister(){
        return getInit();
    }
    public JMethod getCreateMvcMethod(){
        return getInit();
    }
    public  abstract JExpression getNewMvcAdapter();
    public abstract boolean needMvcAdapter();
    public JFieldVar getMvcAdapterField(){
        return mvcAdapterField;
    }
    ///end by palmwin
	public EComponentHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
		super(processHolder, annotatedElement);
	}

	public JExpression getContextRef() {
		if (contextRef == null) {
			setContextRef();
		}
		return contextRef;
	}

	protected abstract void setContextRef();

	public JMethod getInit() {
		if (init == null) {
			setInit();
		}
		return init;
	}

	protected abstract void setInit();

	public JBlock getInitBody() {
		return getInit().body();
	}

	public JVar getResourcesRef() {
		if (resourcesRef == null) {
			setResourcesRef();
		}
		return resourcesRef;
	}

	private void setResourcesRef() {
		resourcesRef = getInitBody().decl(classes().RESOURCES, "resources_", getContextRef().invoke("getResources"));
	}

	public JFieldVar getPowerManagerRef() {
		if (powerManagerRef == null) {
			setPowerManagerRef();
		}

		return powerManagerRef;
	}

	private void setPowerManagerRef() {
		JBlock methodBody = getInitBody();

		JFieldRef serviceRef = classes().CONTEXT.staticRef("POWER_SERVICE");
		powerManagerRef = getGeneratedClass().field(PRIVATE, classes().POWER_MANAGER, "powerManager_");
		methodBody.assign(powerManagerRef, cast(classes().POWER_MANAGER, getContextRef().invoke("getSystemService").arg(serviceRef)));
	}

	public JFieldVar getDatabaseHelperRef(TypeMirror databaseHelperTypeMirror) {
		JFieldVar databaseHelperRef = databaseHelperRefs.get(databaseHelperTypeMirror);
		if (databaseHelperRef == null) {
			databaseHelperRef = setDatabaseHelperRef(databaseHelperTypeMirror);
		}
		return databaseHelperRef;
	}

	private JFieldVar setDatabaseHelperRef(TypeMirror databaseHelperTypeMirror) {
		JClass databaseHelperClass = refClass(databaseHelperTypeMirror.toString());
		String fieldName = CaseHelper.lowerCaseFirst(databaseHelperClass.name()) + ModelConstants.GENERATION_SUFFIX;
		JFieldVar databaseHelperRef = generatedClass.field(PRIVATE, databaseHelperClass, fieldName);
		databaseHelperRefs.put(databaseHelperTypeMirror, databaseHelperRef);

		JExpression dbHelperClass = databaseHelperClass.dotclass();
		getInitBody().assign(databaseHelperRef, //
				classes().OPEN_HELPER_MANAGER.staticInvoke("getHelper").arg(getContextRef()).arg(dbHelperClass));

		return databaseHelperRef;
	}

	public JVar getHandler() {
		if (handler == null) {
			setHandler();
		}
		return handler;
	}

	private void setHandler() {
		JClass handlerClass = classes().HANDLER;
        JClass looperClass = classes().LOOPER;
        JInvocation arg = JExpr._new(handlerClass).arg(looperClass.staticInvoke(METHOD_MAIN_LOOPER));
        handler = generatedClass.field(JMod.PRIVATE, handlerClass, "handler_", arg);
	}

    public JExpression getViewField(){
        return null;
    }
}
