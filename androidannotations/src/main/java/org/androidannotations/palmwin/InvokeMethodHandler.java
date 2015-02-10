package org.androidannotations.palmwin;

import com.sun.codemodel.*;
import com.sun.tools.javac.tree.JCTree;
import org.androidannotations.annotations.mvc.ActionMethod;
import org.androidannotations.annotations.mvc.Invoke;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.holder.EBeanHolder;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhiguodeng on 14-8-1.
 */
public class InvokeMethodHandler extends BaseAnnotationHandler<EBeanHolder> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvokeHandler.class);
    public InvokeMethodHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
        super(targetClass, processingEnvironment);
    }

    public InvokeMethodHandler(String target, ProcessingEnvironment processingEnvironment) {
        super(target, processingEnvironment);
    }
    public InvokeMethodHandler( ProcessingEnvironment processingEnvironment) {
        super(ActionMethod.class, processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {

    }

    @Override
    public void process(Element element, EBeanHolder holder) throws Exception {
        Invoke invoke=element.getAnnotation(Invoke.class);
        ExecutableElement executableElement = (ExecutableElement) element;
        List<? extends VariableElement> parameters = executableElement.getParameters();
        JMethod method=holder.getGeneratedClass().method(JMod.PUBLIC, codeModel().VOID, executableElement.getSimpleName().toString() + "_");
        method.param(holder.classes().CONTEXT, "context");
        JVar intentParam=method.param(holder.classes().INTENT,"intent");
        JBlock body=method.body();
        for(TypeParameterElement tve:executableElement.getTypeParameters()){
           LOGGER.debug("setGenericElement:{}", tve.getGenericElement());
        }
        List<JVar> vars=new ReadIntentHelper().readIntent(parameters,body,intentParam,this);
        JInvocation superInvoke=body.invoke(JExpr._this(),executableElement.getSimpleName().toString());
        for(JExpression var:vars){
            superInvoke.arg(var);
        }
    }

}
