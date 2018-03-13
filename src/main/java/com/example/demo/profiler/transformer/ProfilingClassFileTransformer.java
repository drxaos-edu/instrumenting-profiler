package com.example.demo.profiler.transformer;

import com.example.demo.profiler.records.Profiler;
import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

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

//TODO        boolean ignore = TransformerFilters.shouldIgnoreClass(cclass, className);

        boolean ignore = !className.startsWith("com.example.demo.") ||
                className.startsWith("com.example.demo.profiler.") ||
                className.startsWith("com.example.demo.data.") ||
                className.startsWith("com.example.demo.domain.");

        if (ignore) {
            return false;
        }

        System.err.println("Instrumenting class: " + className);

        // идем по всем методам
        for (CtMethod currentMethod : cclass.getDeclaredMethods()) {
            try {
//TODO                boolean ignoreMethod = TransformerFilters.shouldIgnoreMethod(currentMethod);

                boolean ignoreMethod = !TransformerFilters.checkMethod(currentMethod);


                if (ignoreMethod) {
                    continue;
                }

                System.err.println("  " + currentMethod.getName());
                currentMethod.insertBefore(createJavaString("startCall", currentMethod, className));
                currentMethod.insertAfter(createJavaString("endCall", currentMethod, className), true);

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

}
