/**
 * Copyright (C) 2010-2013 eBusiness Information, Excilys Group
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

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr._super;
import static com.sun.codemodel.JExpr._this;
import static com.sun.codemodel.JMod.PRIVATE;
import static com.sun.codemodel.JMod.PUBLIC;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.androidannotations.api.mvc.MVCAdapter;
import org.androidannotations.handler.AnnotationHandlers;
import org.androidannotations.helper.CaseHelper;
import org.androidannotations.helper.ModelConstants;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.process.ProcessHolder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public abstract class EComponentHolder extends BaseGeneratedClassHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationHandlers.class);
    private static final String METHOD_MAIN_LOOPER = "getMainLooper";

	protected JExpression contextRef;
	protected JMethod init;
	private JVar resourcesRef;
	private Map<TypeMirror, JFieldVar> databaseHelperRefs = new HashMap<TypeMirror, JFieldVar>();
	private JVar handler;
    private JFieldVar mvcAdapterField;
    protected  JMethod onRegister;
    public void createMvcAdapter(){
        if(mvcAdapterField==null)
        {
            mvcAdapterField= generatedClass.field(PRIVATE,refClass(MVCAdapter.class),"mMvcAdapter");
            JBlock init= getOnRegister().body();
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
    public  abstract JExpression getNewMvcAdapter();
    public abstract boolean needMvcAdapter();
    public JFieldVar getMvcAdapterField(){
        return mvcAdapterField;
    }
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
}
