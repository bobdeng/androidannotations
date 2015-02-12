package org.androidannotations.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhiguodeng on 15-2-11.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface EIntent {
    String value() default "";
}
