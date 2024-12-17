package com.gio.couponsystem.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MDCLoggingFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        MDC.put("traceId", "[" + UUID.randomUUID() + "]");
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }
}
