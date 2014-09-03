package org.androidannotations.helper;

import com.sun.codemodel.*;
import org.androidannotations.handler.AnnotationHandlers;
import org.androidannotations.handler.BaseAnnotationHandler;
import org.androidannotations.handler.EntityHandler;
import org.androidannotations.logger.Logger;
import org.androidannotations.logger.LoggerFactory;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiguodeng on 14-8-2.
 */
public class ReadIntentHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadIntentHelper.class);
    public List<JVar> readIntent(List<? extends VariableElement> parameters,JBlock body,JVar intentParam,BaseAnnotationHandler handler)
    throws Exception{
        List<JVar> vars=new ArrayList<JVar>();

        for(VariableElement variableElement:parameters){
            String type=variableElement.asType().toString();
            LOGGER.debug("var type:{} {}",variableElement.getSimpleName().toString(),type);
            JVar var=null;
            if(type.equals("java.lang.String")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getStringExtra").arg(variableElement.getSimpleName().toString()));
            }else
            if(type.equals("int")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getIntExtra").arg(variableElement.getSimpleName().toString()).arg(JExpr.lit(0)));

            }else
            if(type.equals("float")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getFloatExtra").arg(variableElement.getSimpleName().toString()).arg(JExpr.lit(0)));

            }
            if(type.equals("double")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getDoubleExtra").arg(variableElement.getSimpleName().toString()).arg(JExpr.lit(0)));

            }
            if(type.equals("char")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getCharExtra").arg(variableElement.getSimpleName().toString()).arg(JExpr.lit((char)0)));
            }
            else if(type.equals("java.util.ArrayList<java.lang.Integer>")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getIntegerArrayListExtra").arg(variableElement.getSimpleName().toString()));

            }else if(type.equals("java.util.ArrayList<java.lang.String>")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getStringArrayListExtra").arg(variableElement.getSimpleName().toString()));

            }else if(type.equals("float[]")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getFloatArrayExtra").arg(variableElement.getSimpleName().toString()));

            }else if(type.equals("double[]")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getDoubleArrayExtra").arg(variableElement.getSimpleName().toString()));

            }else if(type.equals("char[]")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getCharArrayExtra").arg(variableElement.getSimpleName().toString()));

            }else if(type.equals("java.lang.String[]")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getStringArrayExtra").arg(variableElement.getSimpleName().toString()));

            }else if(type.equals("boolean[]")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getBooleanArrayExtra").arg(variableElement.getSimpleName().toString()));

            }else if(type.equals("boolean")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getBooleanExtra").arg(variableElement.getSimpleName().toString()).arg(JExpr.lit(false)));

            }else if(type.equals("int[]")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getIntArrayExtra").arg(variableElement.getSimpleName().toString()));

            }else if(type.equals("long[]")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getLongArrayExtra").arg(variableElement.getSimpleName().toString()));

            }else if (type.equals("long")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getLongExtra").arg(variableElement.getSimpleName().toString()).arg(JExpr.lit(0)));

            }else if (type.equals("byte[]")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getByteArrayExtra").arg(variableElement.getSimpleName().toString()));

            }else if (type.equals("byte")){
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        intentParam.invoke("getByteExtra").arg(variableElement.getSimpleName().toString()).arg(JExpr.lit(0)));

            }else if (type.endsWith("[]")){
                if(EntityHandler.isInterface(type.substring(0,type.length()-2),"android.os.Parcelable"))
                {
                    var=body.decl(handler.refClass(type),variableElement.getSimpleName().toString(),JExpr.cast(handler.refClass(type),
                                    intentParam.invoke("getParcelableArrayExtra").arg(variableElement.getSimpleName().toString())));
                }else{
                    var=body.decl(handler.refClass(type),variableElement.getSimpleName().toString(),JExpr.cast(handler.refClass(type),
                            intentParam.invoke("getSerializableExtra").arg(variableElement.getSimpleName().toString())));

                }
                //JConditional parcel=body._if(JExpr._new(handler.refClass(type.substring(0,type.length()-2)))._instanceof(handler.refClass("android.os.Parcelable")));
                //parcel._then().assign(var,JExpr.cast(handler.refClass(type),
                //        intentParam.invoke("getParcelableArrayExtra").arg(variableElement.getSimpleName().toString())));
                //JConditional serializable= parcel._else()._if(JExpr._new(handler.refClass(type.substring(0,type.length()-2)))._instanceof(handler.refClass("java.io.Serializable")));
                //serializable._then().assign(var, JExpr.cast(handler.refClass(variableElement.asType().toString()),
                //        intentParam.invoke("getSerializableExtra").arg(variableElement.getSimpleName().toString())));

            }
            else  if(type.startsWith("java.util.ArrayList")){

                String clz=type.substring(type.indexOf('<')+1,type.lastIndexOf('>'));
                var=body.decl(handler.codeModel().parseType(variableElement.asType().toString()),variableElement.getSimpleName().toString(),
                        JExpr.cast(handler.refClass("java.util.ArrayList").narrow(handler.refClass(clz)),intentParam.invoke("getSerializableExtra").arg(variableElement.getSimpleName().toString())));

            }
            if(var==null){
                if(EntityHandler.isInterface(type,"android.os.Parcelable"))
                {
                    var=body.decl(handler.refClass(type),variableElement.getSimpleName().toString(),JExpr.cast(handler.refClass(type),
                            intentParam.invoke("getParcelableExtra").arg(variableElement.getSimpleName().toString())));
                }else{
                    var=body.decl(handler.refClass(type),variableElement.getSimpleName().toString(),JExpr.cast(handler.refClass(type),
                            intentParam.invoke("getSerializableExtra").arg(variableElement.getSimpleName().toString())));

                }
                //var=body.decl(handler.refClass(type),variableElement.getSimpleName().toString(),JExpr._new(handler.refClass(type)));
                //JConditional parcel=body._if(var._instanceof(handler.refClass("android.os.Parcelable")));
                //parcel._then().assign(var,JExpr.cast(handler.refClass(variableElement.asType().toString()),
                //        intentParam.invoke("getParcelableExtra").arg(variableElement.getSimpleName().toString())));

                //JConditional serializable= parcel._else()._if(var._instanceof(handler.refClass("java.io.Serializable")));
                //serializable._then().assign(var, JExpr.cast(handler.refClass(variableElement.asType().toString()),
                //        intentParam.invoke("getSerializableExtra").arg(variableElement.getSimpleName().toString())));
            }
            vars.add(var);

        }
        return vars;
    }


}
