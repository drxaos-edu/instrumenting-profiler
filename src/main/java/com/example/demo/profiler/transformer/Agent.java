package com.example.demo.profiler.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class Agent {
    private static Instrumentation instrumentation;

    public static void premain(String agentArgument, Instrumentation instrumentation) {
        Agent.instrumentation = instrumentation;
    }

    public static void addTransformer(ClassFileTransformer transformer) {
        instrumentation.addTransformer(transformer);
    }
}
