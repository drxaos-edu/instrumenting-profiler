package com.example.demo.profiler.transformer;

import com.example.demo.profiler.Profiler;
import javassist.*;
import javassist.bytecode.SyntheticAttribute;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.util.proxy.ProxyObject;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Proxy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;

public class ProfilingClassFileTransformer implements ClassFileTransformer {

    private ClassPool pool;

    public ProfilingClassFileTransformer() {
        // инициализируем javassist
        pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Profiler.class));
    }

    public byte[] transform(ClassLoader loader, String className,
                            Class classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className == null) {
            return classfileBuffer;
        }
        try {
            // вставляем класс в classpath javassist-а
            pool.insertClassPath(new ByteArrayClassPath(className.replaceAll("/", "."), classfileBuffer));

            // получаем класс для модификации
            CtClass cclass;
            try {
                cclass = pool.get(className.replaceAll("/", "."));
            } catch (NotFoundException e) {
                cclass = pool.get(className);
            }

            // модифицируем
            boolean done = applyTransformations(cclass, className.replaceAll("/", "."));

            // возвращаем
            return done ? cclass.toBytecode() : classfileBuffer;

        } catch (Exception ignore) {
            System.out.println(ignore + " :: " + className);
        }
        return classfileBuffer;
    }

    public boolean applyTransformations(CtClass cclass, String className) throws Exception {

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

//            boolean aopProxy = "org.springframework.aop.SpringProxy".equals(iClass.getName());
            cglibProxy = "org.springframework.cglib.proxy.Factory".equals(iClass.getName()) ||
                    "org.springframework.cglib.proxy.Proxy".equals(iClass.getName()) ||
                    "org.springframework.cglib.reflect.FastClass".equals(iClass.getName());

        }

        boolean ignore = !((app && !profiler) || jsoup /*|| (springMvc && !springfilter && !springServlet)*/ || queryExecution);
        boolean proxy = javaProxy || javassistProxy || cglibProxy || cglibEnchanced || hibernateProxy;

        if (ignore || cannotModify || proxy) {
            return false;
        }


        System.out.println("Instrumenting class: " + className);

        // идем по всем методам
        for (CtMethod currentMethod : cclass.getDeclaredMethods()) {
            try {
                if (checkMethod(currentMethod)) { // если метод нам интересен

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

                    if (caller[0]) {
                        currentMethod.insertBefore(createJavaString("startCall", currentMethod, className));
                        currentMethod.insertAfter(createJavaString("endCall", currentMethod, className), true);
                    }
                }
            } catch (Exception e) {
                System.out.println(e + " :: " + className + "->" + currentMethod.getName());
            }
        }

        return true;
    }

    private String createJavaString(String method, CtMethod currentMethod, String className) {
        // вызов метода внутри отдельного блока
        return "{" + Profiler.class.getName() + "." + method + "(\"" + className + "\",\"" + currentMethod.getName() + "\");}";
    }

    private boolean checkMethod(CtMethod currentMethod) {
        // не меняем синтетические, нативные и прочие неинтересные методы
        return !Modifier.isNative(currentMethod.getModifiers()) &&
                !currentMethod.isEmpty() &&
                checkMethodName(currentMethod.getName()) &&
                currentMethod.getAttribute(SyntheticAttribute.tag) == null;
    }

    private boolean checkMethodName(String methodName) {
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
