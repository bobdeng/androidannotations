package org.androidannotations.annotations.fragment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhiguodeng on 14-10-31.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface EEvent {
    int value() default -1;
}
