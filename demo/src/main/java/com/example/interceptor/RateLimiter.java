package com.example.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: demo
 * @description: access limitation
 * @author: louweiwei
 * @create: 2021-01-24 19:07
 */
@Component
public class RateLimiter implements HandlerInterceptor {

    private static AtomicInteger COUNTER = new AtomicInteger(0);

    // biggest request in 1 second
    private static final int LIMIT = 100;

    // time limit
    private static final int RANGE = 1000;

    // the time we need to refresh
    private static Long FRESH_TIME = System.currentTimeMillis();

    private static boolean rateLimit() {
        Long now = System.currentTimeMillis();
        if (now < FRESH_TIME + RANGE) {
            int current = COUNTER.incrementAndGet();
            return current <= LIMIT;
        }
        FRESH_TIME = now;
        COUNTER.set(0);
        return true;
    }


    /**
     * let's just put the rate limiting work here
     * @param request request
     * @param response response
     * @param handler handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // times this user has visited
        return rateLimit();
    }

}
