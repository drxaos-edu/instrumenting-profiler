package com.example.demo;

import com.example.demo.profiler.transformer.Agent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DemoApplication {

    public static void main(String[] args) {
        //TODO Agent.enableProfiling();
        SpringApplication.run(DemoApplication.class, args);
    }
}
