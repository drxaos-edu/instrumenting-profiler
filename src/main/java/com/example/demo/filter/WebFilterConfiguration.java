package com.example.demo.filter;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

public class WebFilterConfiguration {
    @Bean
    public FilterRegistrationBean someFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(someFilter());
        registration.addUrlPatterns("/url/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("someFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public Filter someFilter() {
        return new ProfilingFilter();
    }
}
