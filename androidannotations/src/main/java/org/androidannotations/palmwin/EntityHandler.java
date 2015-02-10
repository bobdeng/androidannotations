package org.androidannotations.palmwin;

import org.androidannotations.annotations.mvc.Entity;
import org.androidannotations.handler.AnnotationHandlers;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.handler.GeneratingAnnotationHandler;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;
import org.androidannotations.model.AnnotationElements;
import org.androidannotations.process.IsValid;
import org.androidannotations.process.ProcessHolder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhiguodeng on 14-7-31.
 */
public class EntityHandler extends BaseAnnotationHandler<NullHandler> implements GeneratingAnnotationHandler<NullHandler> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationHandlers.class);
    private static final Map<String,TypeElement> types=new HashMap<String,TypeElement>();
    public static boolean isInterface(String clzName,String interName){
        LOGGER.debug("check entity:{} {}",clzName,interName);

        TypeElement element=types.get(clzName);
        if(element!=null){
            for(TypeMirror tm:element.getInterfaces()){
                LOGGER.debug("entity:{} {}",clzName,tm.toString());
                if(tm.toString().equals(interName)){
                    return true;
                }
            }
        }
        return false;
    }
    public EntityHandler(Class<?> targetClass, ProcessingEnvironment processingEnvironment) {
        super(targetClass, processingEnvironment);
    }

    public EntityHandler(ProcessingEnvironment processingEnvironment) {
        super(Entity.class,processingEnvironment);
    }

    @Override
    protected void validate(Element element, AnnotationElements validatedElements, IsValid valid) {
        TypeElement typeElement = (TypeElement) element;
        LOGGER.debug("find entity:{}",typeElement.getQualifiedName().toString());
        types.put(typeElement.getQualifiedName().toString(),typeElement);
    }

    @Override
    public void process(Element element, NullHandler holder) throws Exception {

    }
    @Override
    public NullHandler createGeneratedClassHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
        return new NullHandler(processHolder, annotatedElement);
    }
}
