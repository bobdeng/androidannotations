package org.androidannotations.helper;

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JVar;
import org.androidannotations.annotations.IntentExtra;
import org.androidannotations.annotations.IntentObjectType;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;

import javax.lang.model.element.Element;

/**
 * Created by zhiguodeng on 14-8-2.
 */
public class ReadIntentHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadIntentHelper.class);

    public static JExpression readIntent(JVar intentParam, String elementType, BaseAnnotationHandler handler, Element element)

    {
        IntentObjectType objectType = element.getAnnotation(IntentExtra.class).value();
        String type = elementType;
        if (type.equals("java.lang.String")) {
            return intentParam.invoke("getStringExtra").arg(element.getSimpleName().toString());
        } else if (type.equals("int")) {

            return
                    intentParam.invoke("getIntExtra").arg(element.getSimpleName().toString()).arg(JExpr.lit(0));

        } else if (type.equals("float")) {
            return
                    intentParam.invoke("getFloatExtra").arg(element.getSimpleName().toString()).arg(JExpr.lit(0));

        }
        if (type.equals("double")) {
            return
                    intentParam.invoke("getDoubleExtra").arg(element.getSimpleName().toString()).arg(JExpr.lit(0));

        }
        if (type.equals("char")) {
            return
                    intentParam.invoke("getCharExtra").arg(element.getSimpleName().toString()).arg(JExpr.lit((char) 0));
        } else if (type.equals("java.util.ArrayList<java.lang.Integer>")) {
            return
                    intentParam.invoke("getIntegerArrayListExtra").arg(element.getSimpleName().toString());

        } else if (type.equals("java.util.ArrayList<java.lang.String>")) {
            return
                    intentParam.invoke("getStringArrayListExtra").arg(element.getSimpleName().toString());

        } else if (type.equals("float[]")) {
            return
                    intentParam.invoke("getFloatArrayExtra").arg(element.getSimpleName().toString());

        } else if (type.equals("double[]")) {
            return
                    intentParam.invoke("getDoubleArrayExtra").arg(element.getSimpleName().toString());

        } else if (type.equals("char[]")) {
            return
                    intentParam.invoke("getCharArrayExtra").arg(element.getSimpleName().toString());

        } else if (type.equals("java.lang.String[]")) {
            return
                    intentParam.invoke("getStringArrayExtra").arg(element.getSimpleName().toString());

        } else if (type.equals("boolean[]")) {
            return
                    intentParam.invoke("getBooleanArrayExtra").arg(element.getSimpleName().toString());

        } else if (type.equals("boolean")) {
            return
                    intentParam.invoke("getBooleanExtra").arg(element.getSimpleName().toString()).arg(JExpr.lit(false));

        } else if (type.equals("int[]")) {
            return
                    intentParam.invoke("getIntArrayExtra").arg(element.getSimpleName().toString());

        } else if (type.equals("long[]")) {
            return
                    intentParam.invoke("getLongArrayExtra").arg(element.getSimpleName().toString());

        } else if (type.equals("long")) {
            return
                    intentParam.invoke("getLongExtra").arg(element.getSimpleName().toString()).arg(JExpr.lit(0));

        } else if (type.equals("byte[]")) {
            return
                    intentParam.invoke("getByteArrayExtra").arg(element.getSimpleName().toString());

        } else if (type.equals("byte")) {
            return
                    intentParam.invoke("getByteExtra").arg(element.getSimpleName().toString()).arg(JExpr.lit(0));

        } else if (type.endsWith("[]")) {
            if (objectType == IntentObjectType.PARCELABLE) {
                return JExpr.cast(handler.refClass(type), intentParam.invoke("getParcelableArrayExtra").arg(element.getSimpleName().toString()));
            } else {
                return JExpr.cast(handler.refClass(type),
                        intentParam.invoke("getSerializableExtra").arg(element.getSimpleName().toString()));

            }
        } else if (type.startsWith("java.util.ArrayList")) {

            String clz = type.substring(type.indexOf('<') + 1, type.lastIndexOf('>'));
            return
                    JExpr.cast(handler.refClass("java.util.ArrayList").narrow(handler.refClass(clz)),
                            intentParam.invoke("getSerializableExtra").arg(element.getSimpleName().toString()));

        }
        if (objectType == IntentObjectType.PARCELABLE) {
            return JExpr.cast(handler.refClass(type),
                    intentParam.invoke("getParcelableExtra").arg(element.getSimpleName().toString()));
        } else {
            return JExpr.cast(handler.refClass(type),
                    intentParam.invoke("getSerializableExtra").arg(element.getSimpleName().toString()));

        }

    }

}
