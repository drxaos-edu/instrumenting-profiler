package com.example.demo.profiler.servlet;

import com.example.demo.profiler.records.Profiler;
import com.example.demo.profiler.records.Record;
import com.example.demo.profiler.util.RecordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ProfilingServletFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        try {
            String url = request.getRequestURI();

            Profiler.beginRecord(url);
            chain.doFilter(req, res);
        } finally {
            try {
                Record record = Profiler.finishRecord();

                // Здесь можно отправить трейс в отдельный поток для обработки и отдать уже страницу
                if (record.duration() > 10) {
                    String dump = RecordUtil.dump(record, 3);
                    System.out.println("\n\n" + dump + "\n\n");
                }
            } catch (Exception e) {
                log.error("profiler error", e);
            }
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}
