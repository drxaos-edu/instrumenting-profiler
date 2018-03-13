package com.example.demo.profiler.transformer;

import javassist.*;
import javassist.bytecode.SyntheticAttribute;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;

public class TransformerFilters {

    public static boolean shouldIgnoreClass(CtClass cclass, String className) throws NotFoundException {
        // не записываем чужие классы и себя
        // здесь можно запретить еще какие-то пакеты или классы

        boolean app = className.startsWith("com.example.demo.");
        boolean profiler = className.startsWith("com.example.demo.profiler.");
        boolean jsoup = className.startsWith("org.jsoup.helper.HttpConnection");
        boolean springMvc = className.startsWith("org.springframework.web.");
        boolean springfilter = className.startsWith("org.springframework.web.filter.");
        boolean springServlet = className.startsWith("org.springframework.web.servlet.");
        boolean springData = className.startsWith("org.springframework.data.");
        boolean queryExecution = className.startsWith("org.springframework.data.jpa.repository.query.AbstractJpaQuery");
        boolean cannotModify = cclass.isFrozen() || cclass.isInterface();
        boolean cglibEnchanced = className.contains("$$KeyFactoryByCGLIB$$") ||
                className.contains("$$EnhancerBySpringCGLIB$$");

        // все интерфейсы и суперкласс
        ArrayList<CtClass> parents = new ArrayList<CtClass>();
        Collections.addAll(parents, cclass.getInterfaces());
        parents.add(cclass.getSuperclass());

        // не записываем прокси и другие обертки
        // сюда можно добавить больше суперклассов и интерфейсов
        boolean javaProxy = false;
        boolean javassistProxy = false;
        boolean hibernateProxy = false;
        boolean cglibProxy = false;
        for (CtClass iClass : parents) {
            javaProxy |= Proxy.class.getName().equals(iClass.getName());
            javassistProxy |= ProxyObject.class.getName().equals(iClass.getName());
            hibernateProxy |= "org.hibernate.proxy.HibernateProxy".equals(iClass.getName());

            cglibProxy = "org.springframework.cglib.proxy.Factory".equals(iClass.getName()) ||
                    "org.springframework.cglib.proxy.Proxy".equals(iClass.getName()) ||
                    "org.springframework.cglib.reflect.FastClass".equals(iClass.getName());

        }

        boolean ignorePackage = !((app && !profiler) || jsoup /*|| (springMvc && !springfilter && !springServlet)*/ || queryExecution);
        boolean isProxy = javaProxy || javassistProxy || cglibProxy || cglibEnchanced || hibernateProxy;

        return ignorePackage || cannotModify || isProxy;
    }


    public static boolean shouldIgnoreMethod(CtMethod currentMethod) throws CannotCompileException {
        // если метод нам интересен
        boolean significantMethod = checkMethod(currentMethod) && checkMethodName(currentMethod.getName());

        // если метод - обычный геттер/сеттер, то не надо его записывать
        final boolean[] caller = new boolean[1];
        String currentMethodName = currentMethod.getName();
        if (currentMethodName.startsWith("get") || currentMethodName.startsWith("set") || currentMethodName.startsWith("is")) {
            currentMethod.instrument(new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (checkMethodName(m.getMethodName())) {
                        caller[0] = true;
                    }
                }
            });
        } else {
            caller[0] = true;
        }

        return !significantMethod || !caller[0];
    }

    public static boolean checkMethod(CtMethod currentMethod) {
        // не меняем синтетические, нативные и прочие неинтересные методы
        return !Modifier.isNative(currentMethod.getModifiers()) &&
                !currentMethod.isEmpty() &&
                currentMethod.getAttribute(SyntheticAttribute.tag) == null;
    }

    public static boolean checkMethodName(String methodName) {
        // не меняем методы, которые могут вызываться слишком часто или являются обертками
        return !methodName.contains("$") &&
                !methodName.contains("__") &&
                !methodName.equals("invokeMethod") &&
                !methodName.equals("getProperty") &&
                !methodName.equals("setProperty") &&
                !methodName.equals("getTransactional") &&
                !methodName.equals("setTransactional") &&
                !methodName.equals("markDirty") &&
                !methodName.equals("equals") &&
                !methodName.equals("hashCode");
    }
}
