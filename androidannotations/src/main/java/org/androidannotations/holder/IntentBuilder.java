package org.androidannotations.holder;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JMethod;

import javax.lang.model.element.Element;

/**
 * Created by zhiguodeng on 15-2-12.
 */
public interface IntentBuilder extends GeneratedClassHolder{
    public JMethod getGetIntentMethod();
    public JMethod getPutIntentMethod();
    public JMethod getGetter(Element element,JClass clz);
    public JMethod getSetter(Element element,JClass clz);
    public JMethod getParseIntentMethod();
}
