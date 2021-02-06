package com.example.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @description: access limitation
 * @author: louweiwei
 * @create: 2021-02-06 15:07
 */
@Component
public class RateLimiter implements HandlerInterceptor {

    /**
     *  logger
     */
    private final static Logger logger = LoggerFactory.getLogger(RateLimiter. class);

    /**
     *  redisTemplate
     */
    @Autowired
    RedisTemplate redisTemplate;

    /**
     *  biggest request in 1 second
     */
    private static final Integer THRESHOLD = 100;

    /**
     *  the time we need to refresh
     */
    private static Long FRESH_TIME = System.currentTimeMillis();


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
        // see if we should stop the user here
        return shouldInterceptThisUser(request);
    }


    /**
     *  resolve the ip address from request, record times the user has accessed,
     *  and decide if we should let it in.
     *  @param request
     *  @return
     */
    private boolean shouldInterceptThisUser(HttpServletRequest request){

        // get the ip address from the request, which is used for identifying a user.
        String ipAddress = getIpAddress(request);

        // time of the current request
        Long now = System.currentTimeMillis();

        // whether this user has accessed before
        Boolean accessedBefore = redisTemplate.hasKey(ipAddress);

        // counter for a specific user
        Integer accessedTimes = (Integer) redisTemplate.opsForValue().get(ipAddress);

        // stop the user here
        if(accessedBefore && accessedTimes > THRESHOLD) return false;

        // the first access
        if(!accessedBefore){
            Date expireAt = new Date(now + FRESH_TIME);
            redisTemplate.opsForValue().set(ipAddress,1);
            redisTemplate.expireAt(ipAddress, expireAt);
        } else {
            redisTemplate.opsForValue().increment(ipAddress);
        }

        return true;
    }

    /**
     * get the ip address from current request;
     * @param request
     * @return ip address
     */
    private String getIpAddress(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (logger.isInfoEnabled()) {
            logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}
