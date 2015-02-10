package org.androidannotations.palmwin;

import com.sun.codemodel.*;
import org.androidannotations.annotations.mvc.MVCAdapter;
import org.androidannotations.helper.ModelConstants;
import org.androidannotations.holder.BaseGeneratedClassHolder;
import org.androidannotations.process.ProcessHolder;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;

import static com.sun.codemodel.JMod.FINAL;
import static com.sun.codemodel.JMod.PRIVATE;
import static com.sun.codemodel.JMod.PUBLIC;

/**
 * Created by zhiguodeng on 14-7-31.
 */
public class EActionHolder extends BaseGeneratedClassHolder {
    private JFieldVar mvcAdpater;
    private JClass actionClass;
    public EActionHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
        super(processHolder, annotatedElement);
    }
    protected void setGeneratedClass() throws Exception {
        String annotatedComponentQualifiedName = annotatedElement.getQualifiedName().toString();
        String subComponentQualifiedName = annotatedComponentQualifiedName + ModelConstants.GENERATION_SUFFIX;
        JClass annotatedComponent = codeModel().directClass(annotatedElement.asType().toString());

        generatedClass = codeModel()._class(PUBLIC | FINAL, subComponentQualifiedName, ClassType.CLASS);
        for (TypeParameterElement typeParam : annotatedElement.getTypeParameters()) {
            JClass bound = codeModelHelper.typeBoundsToJClass(this, typeParam.getBounds());
            generatedClass.generify(typeParam.getSimpleName().toString(), bound);
        }
        generatedClass._implements(annotatedComponent);
    }
    public void createMvcField(){
        mvcAdpater=generatedClass.field(PRIVATE, MVCAdapter.class,"mvc");
    }
    public void createSetMvcAdapter(){
        JMethod method=generatedClass.constructor(PUBLIC);
        JVar adapter=method.param(MVCAdapter.class, "mvcAdapter");
        JBlock body=method.body();
        body.assign(mvcAdpater, adapter); 
    }
    public JFieldVar getMvcAdpater(){
        return mvcAdpater;
    }

    public JClass getActionClass() {
        return actionClass;
    }

    public void setActionClass(JClass actionClass) {
        this.actionClass = actionClass;
    }
}
