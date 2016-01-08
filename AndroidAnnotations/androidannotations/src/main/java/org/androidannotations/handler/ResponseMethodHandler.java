package org.androidannotations.handler;

import com.sun.codemodel.*;
import org.androidannotations.api.mvc.Invoke;
import org.androidannotations.api.mvc.ResponseMethod;
import org.androidannotations.holder.EActionHolder;
import org.androidannotations.holder.EBeanHolder;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr._null;

/**
 * Created by zhiguodeng on 14-8-1.
 */
public class ResponseMethodHandler extends BaseAnnotationHandler<EBeanHolder> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseMethodHandler.class);
    public ResponseMethodHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
        super(targetClass, processingEnvironment);
    }

    public ResponseMethodHandler(String target, ProcessingEnvironment processingEnvironment) {
        super(target, processingEnvironment);
    }
    public ResponseMethodHandler( ProcessingEnvironment processingEnvironment) {
        super(ResponseMethod.class, processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {

    }

    @Override
    public void process(Element element, EBeanHolder holder) throws Exception {
        Invoke invoke=element.getAnnotation(Invoke.class);
        ExecutableElement executableElement = (ExecutableElement) element;
        List<? extends VariableElement> parameters = executableElement.getParameters();
        JMethod method=holder.getGeneratedClass().method(JMod.PUBLIC, codeModel().VOID, executableElement.getSimpleName().toString());
        List<JVar> params=new java.util.ArrayList();
        for(VariableElement var:parameters){
            params.add(method.param(codeModel().parseType(var.asType().toString()), var.getSimpleName().toString()));
        }
        JBlock body=method.body();
        List<JExpression> vars=new ArrayList<JExpression>();
        JVar intentVar=body.decl(holder.classes().INTENT, "intent",_new(holder.classes().INTENT));
        int i=0;
        for(VariableElement var:parameters){
            JVar param=params.get(i);
            JInvocation putMethod=null;
            if(!var.asType().getKind().isPrimitive()){
                JBlock ifNotNull=body._if(param.ne(_null()))._then();
                putMethod=ifNotNull.invoke(intentVar,"putExtra");

            }else
            {
                putMethod=body.invoke(intentVar,"putExtra");
            }
            putMethod.arg(var.getSimpleName().toString());
            putMethod.arg(params.get(i++));
        }
        body.invoke(intentVar,"setAction").arg("doResponse_"+executableElement.getSimpleName().toString());
        body.invoke(holder.refClass("android.support.v4.content.LocalBroadcastManager").staticInvoke("getInstance").arg(holder.getContextField()),"sendBroadcast").arg(intentVar);
    }
}