package org.androidannotations.handler;

import android.os.Parcelable;
import com.sun.codemodel.*;
import org.androidannotations.api.mvc.Response;
import org.androidannotations.helper.ReadIntentHelper;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.holder.EComponentWithViewSupportHolder;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhiguodeng on 14-8-1.
 */
public class ResponseHandler extends BaseAnnotationHandler<EComponentHolder>{
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseMethodHandler.class);
    public ResponseHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
        super(targetClass, processingEnvironment);
    }

    public ResponseHandler(String target, ProcessingEnvironment processingEnvironment) {
        super(target, processingEnvironment);
    }
    public ResponseHandler( ProcessingEnvironment processingEnvironment) {
        super(Response.class, processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {

    }

    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {
        Response response=element.getAnnotation(Response.class);
        ExecutableElement executableElement = (ExecutableElement) element;
        String methodName=executableElement.getSimpleName().toString();
        List<? extends VariableElement> parameters = executableElement.getParameters();
        JMethod method=holder.getGeneratedClass().method(JMod.PUBLIC, codeModel().VOID, "doResponse_" + methodName);
        method.param(holder.classes().CONTEXT, "context");
        JVar intentParam=method.param(holder.classes().INTENT,"intent");
        JBlock body=method.body();
        List<JVar> vars=new ReadIntentHelper().readIntent(parameters,body,intentParam,this);
        JInvocation superInvoke=body.invoke(JExpr._this(),executableElement.getSimpleName().toString());
        for(JExpression var:vars){
            superInvoke.arg(var);
        }
    }


}
