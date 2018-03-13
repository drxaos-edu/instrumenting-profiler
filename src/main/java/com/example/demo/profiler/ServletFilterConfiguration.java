package com.example.demo.profiler;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class ServletFilterConfiguration {

    @Bean
    public FilterRegistrationBean profilingServletFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(profilingServletFilter());
        registration.addUrlPatterns("/*");
        registration.setName("profilingServletFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean threadNameServletFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(threadNameServletFilter());
        registration.addUrlPatterns("/*");
        registration.setName("threadNameServletFilter");
        registration.setOrder(3);
        return registration;
    }

    @Bean
    public Filter profilingServletFilter() {
        return new ProfilingServletFilter();
    }

    @Bean
    public Filter threadNameServletFilter() {
        return new ThreadNameServletFilter();
    }
}
