package com.example.demo.profiler.transformer;

import java.lang.instrument.Instrumentation;

public class Agent {
    private static Instrumentation instrumentation;

    public static void premain(String agentArgument, Instrumentation instrumentation) {
        //TODO Agent.instrumentation = instrumentation;
    }

    public static void enableProfiling() {
        if (instrumentation != null) {
            instrumentation.addTransformer(new ProfilingClassFileTransformer());
        }
    }
}
