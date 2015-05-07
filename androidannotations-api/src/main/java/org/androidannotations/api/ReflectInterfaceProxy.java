package org.androidannotations.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zhiguodeng on 15-2-10.
 */
public class ReflectInterfaceProxy implements InvocationHandler {
    Object obj;

    public ReflectInterfaceProxy(Object obj) {
        this.obj = obj;
    }

    public static Object newInstance(Class interfaceClass, Object viewObject) {
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new ReflectInterfaceProxy(viewObject));
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if(o==null ) return null;
        Class[] args = method.getParameterTypes();

        Method targetMethod = getMethod(obj.getClass(), method.getName(), args);
        if (targetMethod == null) {
            targetMethod = getMethod(obj.getClass().getSuperclass(), method.getName(), args);
        }
        if (targetMethod != null) {
            targetMethod.setAccessible(true);
            return targetMethod.invoke(obj, objects);
        }
        return null;
    }

    private Method getMethod(Class clz, String methodName, Class[] args) {
        try {
            return clz.getDeclaredMethod(methodName, args);
        } catch (Exception e) {
            return null;
        }
    }
}
