package org.androidannotations.palmwin;

import android.content.Intent;
import com.sun.codemodel.*;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;
import com.sun.codemodel.internal.*;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.mvc.Invoke;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.holder.EComponentWithViewSupportHolder;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static com.sun.codemodel.JExpr.*;

/**
 * Created by zhiguodeng on 14-7-31.
 */
public class InvokeHandler extends BaseAnnotationHandler<EActionHolder> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvokeHandler.class);
    public InvokeHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
        super(targetClass, processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {

    }

    public InvokeHandler(ProcessingEnvironment processingEnvironment) {
        super(Invoke.class, processingEnvironment);
    }

    @Override
    public void process(Element element, EActionHolder holder) throws Exception {
        Invoke invoke=element.getAnnotation(Invoke.class);
        ExecutableElement executableElement = (ExecutableElement) element;
        List<? extends VariableElement> parameters = executableElement.getParameters();
        JMethod method=holder.getGeneratedClass().method(JMod.PUBLIC,codeModel().VOID,executableElement.getSimpleName().toString());
        List<JVar> params=new java.util.ArrayList();
        for(VariableElement var:parameters){
            params.add(method.param(codeModel().parseType(var.asType().toString()), var.getSimpleName().toString()));
        }
        JBlock body=method.body();
        JVar intentVar=body.decl(holder.classes().INTENT,"intent");
        body.assign(intentVar,_new(holder.classes().INTENT));
        String responseConfig=holder.getGeneratedClass().name()+executableElement.getSimpleName().toString();
        //body.invoke(intentVar,"putExtra").arg("_RESPONSE_CONFIG").arg(responseConfig);
        JVar varBroadcast=null;
        JVar varPri=null;
        int i=0;
        for(VariableElement var:parameters){

            if(var.getSimpleName().toString().equals("broadcast")){
                varBroadcast=params.get(i++);
                continue;
            }
            if(var.getSimpleName().toString().equals("priority")){
                varPri=params.get(i++);
                continue;
            }
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
        JInvocation invokeMethod= body.invoke(holder.getMvcAdpater(),"invokeAction");
        invokeMethod.arg(dotclass(holder.getActionClass()));
        String actionMethod=executableElement.getSimpleName().toString();
        invokeMethod.arg(actionMethod+"_");
        invokeMethod.arg(intentVar);
        //invokeMethod.arg(JExpr._null());
        if (varBroadcast==null){
            invokeMethod.arg(JExpr.lit(false));
        }else
        {
            invokeMethod.arg(varBroadcast);
        }
        if(varPri==null){
            invokeMethod.arg(JExpr.lit(0));
        }
        else{
            invokeMethod.arg(varPri);
        }


    }
}
