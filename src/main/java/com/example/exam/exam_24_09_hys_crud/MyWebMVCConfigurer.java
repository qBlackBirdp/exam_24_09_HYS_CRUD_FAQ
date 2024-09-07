package com.example.exam.exam_24_09_hys_crud;

import com.example.exam.exam_24_09_hys_crud.interceptor.BeforeActionInterceptor;
import com.example.exam.exam_24_09_hys_crud.interceptor.NeedLoginInterceptor;
import com.example.exam.exam_24_09_hys_crud.interceptor.NeedLogoutInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMVCConfigurer implements WebMvcConfigurer {

    // BeforeActionInterceptor 불러오기(연결)
    @Autowired
    BeforeActionInterceptor beforeActionInterceptor;
    // NeedLoginInterceptor 불러오기(연결)
    @Autowired
    NeedLoginInterceptor needLoginInterceptor;
    // NeedLogoutInterceptor 불러오기(연결)
    @Autowired
    NeedLogoutInterceptor needLogoutInterceptor;

    // 인터셉터 등록(적용)
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**").excludePathPatterns("/static/resource/**")
                .excludePathPatterns("/error");

        registry.addInterceptor(needLoginInterceptor).addPathPatterns("/usr/article/write")
                .addPathPatterns("/usr/article/doWrite").addPathPatterns("/usr/article/modify")
                .addPathPatterns("/usr/article/doModify").addPathPatterns("/usr/article/doDelete")
                .addPathPatterns("/usr/member/doLogout");
//				.addPathPatterns("/usr/article/doReaction");

        registry.addInterceptor(needLogoutInterceptor).addPathPatterns("/usr/member/login")
                .addPathPatterns("/usr/member/doLogin").addPathPatterns("/usr/member/join")
                .addPathPatterns("/usr/member/doJoin");
    }

}