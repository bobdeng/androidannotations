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
import org.androidannotations.annotations.ContextEvent;
import org.androidannotations.api.ReflectInterfaceProxy;
import org.androidannotations.helper.APTCodeModelHelper;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.EBeanHolder;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.holder.EFragmentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import static com.sun.codemodel.JExpr._this;
import static com.sun.codemodel.JExpr.cast;
import static com.sun.codemodel.JExpr.ref;
import static org.androidannotations.helper.ModelConstants.GENERATION_SUFFIX;

public class ContextEventHandler extends BaseAnnotationHandler<EComponentHolder> {
    private TargetAnnotationHelper annotationHelper;
    private APTCodeModelHelper codeModelHelper = new APTCodeModelHelper();

    public ContextEventHandler(ProcessingEnvironment processingEnvironment) {
		super(ContextEvent.class, processingEnvironment);
        annotationHelper = new TargetAnnotationHelper(processingEnv, getTarget());
	}

	@Override
	public void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        //validatorHelper.enclosingElementHasEFragment(element,validatedElements,valid);
	}

	@Override
	public void process(Element element, EComponentHolder holder) {
        TypeMirror elementType = annotationHelper.extractAnnotationClassParameter(element);
        if (elementType == null) {
            elementType = element.asType();
            elementType = holder.processingEnvironment().getTypeUtils().erasure(elementType);
        }
        String fieldName = element.getSimpleName().toString();
        String typeQualifiedName = elementType.toString();
        JClass injectedClass = refClass(typeQualifiedName + GENERATION_SUFFIX);
        JFieldRef beanField = ref(fieldName);
        JClass viewClass = codeModelHelper.typeMirrorToJClass(elementType, holder);
        JBlock block = holder.getInitBody();
        JClass proxy=refClass(ReflectInterfaceProxy.class);
        block.assign(beanField,cast(viewClass,proxy.staticInvoke("newInstance").arg(viewClass.dotclass()).arg(holder.getContextRef())));
	}
}
