package com.example.catalog.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Time;
import java.util.*;


@Component
public class RateLimit implements HandlerInterceptor {

    private static final String INTERNAL_ENDPOINT = "/internal";
    @Value("${rate-limit.algo}")
    private String rateLimitAlgo;

    @Value("${rate-limit.rpm}")
    private int rateLimitRPM;

    private static Hashtable<String,List<Long>> table= new Hashtable<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        String path = request.getRequestURI();
        int remainingAccess=10;
        if (path.startsWith(INTERNAL_ENDPOINT)) {
            response.setStatus(HttpStatus.OK.value()); // Set HTTP 200 for internal requests
            return true;
        }


        System.out.println(System.currentTimeMillis());
        remainingAccess=isAllowed(clientIp);
        if (remainingAccess<0) {

            response.setStatus(429);

            response.setHeader("X-Rate-Limit-Remaining", "0");
            response.setHeader("X-Rate-Limit-Retry-After-Seconds", ""+remainingAccess*-1);
            return false;
        }

        response.setStatus(HttpStatus.OK.value());
        response.setHeader("X-Rate-Limit-Remaining", ""+remainingAccess);
        return true;
    }

    private int isAllowed(String clientIp) {
        Long currentTime=System.currentTimeMillis();

        if (table.containsKey(clientIp))
        {
            int MinInMill = 60000;
            List<Long> list = table.get(clientIp);
            if(rateLimitAlgo.equals("moving")) {
                if (!list.isEmpty() && ((int) (currentTime - list.get(list.size() - 1)) <= MinInMill)) {
                    Iterator<Long> iterator = list.iterator();
                    int cnt = 0;

                    while (iterator.hasNext()) {
                        Long time = iterator.next();

                        if (currentTime - time <= MinInMill) {
                            if (cnt < rateLimitRPM)
                                cnt++;

                        } else {
                            iterator.remove();
                        }

                        if (cnt == rateLimitRPM) {
                            if (!list.isEmpty()) {
                                int result = (int) ((((list.get(0) - currentTime)) / 1000) + 60);
                                System.out.println(result);
                                return -1 * result;
                            }
                        }
                    }

                    list.add(currentTime);
                    table.put(clientIp, list);
                    return (rateLimitRPM - 1) - cnt;
                }
            } else {
                long oldestRequestTime = list.isEmpty() ? 0 : list.get(0);
                if (currentTime - oldestRequestTime > MinInMill) {
                    list.clear();
                } else {
                    Iterator<Long> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Long time = iterator.next();
                        if (currentTime - time > MinInMill) {
                            iterator.remove();
                        } else {
                            break;
                        }
                    }
                }

                if (list.size() >= rateLimitRPM) {
                    int waitTimeInSeconds = (int) ((MinInMill - (currentTime - oldestRequestTime)) / 1000);
                    System.out.println(waitTimeInSeconds);
                    return -1 * waitTimeInSeconds;
                }

                list.add(currentTime);
                table.put(clientIp, list);

                return rateLimitRPM - list.size();
            }


        }
        table.put(clientIp, new ArrayList<>(List.of(currentTime)));
        return rateLimitRPM-1;
    }


}