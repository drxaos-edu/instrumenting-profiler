package com.example.demo.filter;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

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
    public Filter profilingServletFilter() {
        return new ProfilingServletFilter();
    }
}
