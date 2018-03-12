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
import java.util.concurrent.atomic.AtomicBoolean;

public class ProfilingClassFileTransformer implements ClassFileTransformer {

    private ClassPool pool;

    public ProfilingClassFileTransformer() {
        pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Profiler.class));
    }

    public boolean applyTransformations(CtClass cclass, String className) throws Exception {

        // не записываем чужие классы и себя
        if (!className.startsWith("com.example.demo.") ||
                className.startsWith("com.example.demo.profiler.") ||
                cclass.isFrozen() || cclass.isInterface()) {
            return false;
        }

        ArrayList<CtClass> parents = new ArrayList<CtClass>();
        Collections.addAll(parents, cclass.getInterfaces());
        parents.add(cclass.getSuperclass());

        // не записываем прокси и другие обертки
        for (CtClass iClass : parents) {
            if (ProxyObject.class.getName().equals(iClass.getName()) ||
                    Proxy.class.getName().equals(iClass.getName()) ||
                    "org.hibernate.proxy.HibernateProxy".equals(iClass.getName()) ||
                    "org.springframework.aop.SpringProxy".equals(iClass.getName()) ||
                    "org.springframework.cglib.proxy.Factory".equals(iClass.getName()) ||
                    "org.springframework.cglib.proxy.Proxy".equals(iClass.getName()) ||
                    "org.springframework.cglib.reflect.FastClass".equals(iClass.getName()) ||
                    "org.hibernate.bytecode.javassist.FastClass".equals(iClass.getName()) ||
                    "net.sf.cglib.reflect.FastClass".equals(iClass.getName())) {
                return false;
            }
        }

        System.out.println("Instrumenting class: " + className);

        for (CtMethod currentMethod : cclass.getDeclaredMethods()) {
            try {
                if (checkMethod(currentMethod)) {

                    final AtomicBoolean caller = new AtomicBoolean(false);
                    currentMethod.instrument(new ExprEditor() {
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (checkMethodName(m.getMethodName())) {
                                caller.set(true);
                            }
                        }
                    });

                    if (caller.get()) {
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

    private boolean checkMethod(CtMethod currentMethod) {
        return !Modifier.isNative(currentMethod.getModifiers()) &&
                !currentMethod.isEmpty() &&
                checkMethodName(currentMethod.getName()) &&
                currentMethod.getAttribute(SyntheticAttribute.tag) == null;
    }

    private boolean checkMethodName(String methodName) {
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

    public byte[] transform(ClassLoader loader, String className,
                            Class classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className == null) {
            return classfileBuffer;
        }
        try {
            pool.insertClassPath(new ByteArrayClassPath(className.replaceAll("/", "."), classfileBuffer));
            CtClass cclass = null;
            try {
                cclass = pool.get(className.replaceAll("/", "."));
            } catch (NotFoundException e) {
                cclass = pool.get(className);
            }

            boolean done = applyTransformations(cclass, className.replaceAll("/", "."));

            return done ? cclass.toBytecode() : classfileBuffer;

        } catch (Exception ignore) {
            System.out.println(ignore + " :: " + className);
        }
        return classfileBuffer;
    }

    private String createJavaString(String type, CtMethod currentMethod, String className) {
        String info = Profiler.class.getName();
        return "{" + info + "." + type + "(\"" + className + "\",\"" + currentMethod.getName() + "\");}";
    }
}