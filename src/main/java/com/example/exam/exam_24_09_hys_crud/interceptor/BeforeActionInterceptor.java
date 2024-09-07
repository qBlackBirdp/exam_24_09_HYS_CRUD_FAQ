package com.example.exam.exam_24_09_hys_crud.interceptor;

import com.example.exam.exam_24_09_hys_crud.vo.Rq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class BeforeActionInterceptor implements HandlerInterceptor {

    @Autowired
    private Rq rq;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        rq.initBeforeActionInterceptor();

        return HandlerInterceptor.super.preHandle(req, resp, handler);
    }

}