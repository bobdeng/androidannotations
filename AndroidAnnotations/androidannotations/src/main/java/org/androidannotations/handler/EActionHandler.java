package org.androidannotations.handler;

import com.sun.codemodel.JClass;
import org.androidannotations.api.mvc.EAction;
import org.androidannotations.holder.EActionHolder;
import org.androidannotations.holder.SharedPrefHolder;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;
import org.androidannotations.process.ProcessHolder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;

/**
 * Created by zhiguodeng on 14-7-31.
 */
public class EActionHandler extends BaseAnnotationHandler<EActionHolder> implements GeneratingAnnotationHandler<EActionHolder>{
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationHandlers.class);
    public EActionHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
        super(targetClass, processingEnvironment);
    }

    public EActionHandler( ProcessingEnvironment processingEnvironment) {
        super(EAction.class,processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        TypeElement typeElement = (TypeElement) element;
        validatorHelper.isInterface(typeElement,valid);
    }

    @Override
    public void process(Element element, EActionHolder holder) throws Exception {
        EAction eAction=element.getAnnotation(EAction.class);

        holder.createMvcField();
        holder.createSetMvcAdapter();
        try{
            eAction.action();
        }catch (MirroredTypeException exception){
            holder.setActionClass(
            refClass(exception.getTypeMirror().toString()+"_"));
        }
    }
    @Override
    public EActionHolder createGeneratedClassHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
        return new EActionHolder(processHolder, annotatedElement);
    }
}
