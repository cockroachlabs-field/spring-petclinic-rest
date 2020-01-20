package org.springframework.samples.petclinic.util;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * AuditedInterceptor
 */
@Audited
@Interceptor
public class AuditedInterceptor {
    private static Long calls_duration = 0L;
    private static Long calls_count = 0L;

    @AroundInvoke
    public Object auditMethod(InvocationContext ctx) throws Exception {
        LocalDateTime init = LocalDateTime.now();

        Object result = ctx.proceed();

        synchronized(this) {
          calls_duration += Duration.between(init, LocalDateTime.now()).toMillis();
          calls_count ++;
        }

        return result;
    }
    
}