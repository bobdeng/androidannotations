package org.androidannotations.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhiguodeng on 15-2-11.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface IntentExtra {
    IntentObjectType value() default IntentObjectType.SERIALIZABLE;
}
