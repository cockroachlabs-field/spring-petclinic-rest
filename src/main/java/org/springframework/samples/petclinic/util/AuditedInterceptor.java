package org.springframework.samples.petclinic.util;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Timed;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * AuditedInterceptor
 */
@Audited
@Interceptor
public class AuditedInterceptor {
    @AroundInvoke
    public Object auditMethod(InvocationContext ctx) throws Exception {
        Object result = ctx.proceed();
        return result;
    }

}
