package org.androidannotations.palmwin;

import com.sun.codemodel.*;
import com.sun.codemodel.fmt.JBinaryFile;
import org.androidannotations.annotations.fragment.EEvent;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.holder.EComponentWithViewSupportHolder;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr._null;
/**
 * Created by zhiguodeng on 14-10-31.
 */
public class EventHandler extends BaseAnnotationHandler<EComponentWithViewSupportHolder> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);
    public EventHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
        super(targetClass, processingEnvironment);
    }
    public EventHandler(ProcessingEnvironment processingEnvironment) {
        super(EEvent.class
                ,processingEnvironment);
    }
    public EventHandler(String target, ProcessingEnvironment processingEnvironment) {
        super(target, processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {

    }

    @Override
    public void process(Element element, EComponentWithViewSupportHolder holder) throws Exception {
        try{
            JMethod onEvent=holder.getOnEvent();
            EEvent event=element.getAnnotation(EEvent.class);
            ExecutableElement executableElement = (ExecutableElement) element;
            int eventId=event.value();
            JSwitch switchBody=holder.getEventSwitch();
            JCase eventCase=switchBody._case(JExpr.lit(eventId));
            JInvocation invoke=eventCase.body().invoke(element.getSimpleName().toString());
            JVar eventArgs=onEvent.listVarParam();
            for(int i=0;i<executableElement.getParameters().size();i++){
                JExpression exp=eventArgs.component(JExpr.lit(i));
                VariableElement var=executableElement.getParameters().get(i);
                invoke.arg(JExpr.cast(getType(var),exp));
            }
            eventCase.body()._break();
        }catch (Exception e){
            LOGGER.error("Error",e);
        }

    }
    private JType getType(VariableElement element)throws Exception{
        LOGGER.info("EventVarType:{}",element.asType().toString());
        if(element.asType().toString().equals("int")){
            return codeModel().parseType("java.lang.Integer");
        }
        if(element.asType().toString().equals("float")){
            return codeModel().parseType("java.lang.Float");
        }
        if(element.asType().toString().equals("double")){
            return codeModel().parseType("java.lang.Double");
        }
        if(element.asType().toString().equals("boolean")){
            return codeModel().parseType("java.lang.Boolean");
        }
        if(element.asType().toString().equals("long")){
            return codeModel().parseType("java.lang.Long");
        }
        if(element.asType().toString().equals("char")){
            return codeModel().parseType("java.lang.Char");
        }
        return codeModel().parseType(element.asType().toString());
    }
}
