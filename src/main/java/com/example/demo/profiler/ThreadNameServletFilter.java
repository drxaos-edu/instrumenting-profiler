package com.example.demo.profiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

public class ThreadNameServletFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        String defaultThreadName = Thread.currentThread().getName();
        try {
            String method = request.getMethod();
            String url = request.getRequestURI();
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            String cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("JSESSIONID")).map(Cookie::getValue).findFirst().orElse("-");
            Thread.currentThread().setName("(Request) " + method + url + " - " + cookie + " (" + user + ")");

            chain.doFilter(req, res);
        } finally {
            Thread.currentThread().setName(defaultThreadName);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}
