package org.androidannotations.handler;

import com.sun.codemodel.*;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.api.mvc.MVCAction;
import org.androidannotations.helper.TargetAnnotationHelper;
import org.androidannotations.holder.EActivityHolder;
import org.androidannotations.holder.EBeanHolder;
import org.androidannotations.holder.EComponentHolder;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import static com.sun.codemodel.JExpr.*;
import static org.androidannotations.helper.ModelConstants.GENERATION_SUFFIX;

/**
 * Created by zhiguodeng on 14-7-31.
 */
public class ActionHandler extends  BaseAnnotationHandler<EComponentHolder> {
    private  TargetAnnotationHelper annotationHelper;
    public ActionHandler( ProcessingEnvironment processingEnvironment) {
        super(MVCAction.class,processingEnvironment);
        annotationHelper = new TargetAnnotationHelper(processingEnv, getTarget());
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {

    }
    @Override
    public void process(Element element, EComponentHolder holder) throws Exception {

        TypeMirror elementType = annotationHelper.extractAnnotationClassParameter(element);
        if (elementType == null) {
            elementType = element.asType();
            elementType = holder.processingEnvironment().getTypeUtils().erasure(elementType);
        }
        String fieldName = element.getSimpleName().toString();
        String typeQualifiedName = elementType.toString();
        JClass injectedClass = refClass(typeQualifiedName + GENERATION_SUFFIX);

        JFieldRef beanField = ref(fieldName);

        holder.createMvcAdapter();

        JBlock block = holder.getInitBody();

        boolean hasNonConfigurationInstanceAnnotation = element.getAnnotation(NonConfigurationInstance.class) != null;
        if (hasNonConfigurationInstanceAnnotation) {
            block = block._if(beanField.eq(_null()))._then();
        }
        block.assign(beanField, _new(injectedClass).arg(holder.getMvcAdapterField()));

    }

    public ActionHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
        super(targetClass, processingEnvironment);

    }

    public ActionHandler(String target, ProcessingEnvironment processingEnvironment) {
        super(target, processingEnvironment);
    }

}
