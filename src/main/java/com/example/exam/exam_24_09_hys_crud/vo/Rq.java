package com.example.exam.exam_24_09_hys_crud.vo;

import com.example.exam.exam_24_09_hys_crud.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {

    @Getter
    private boolean isLogined = false;
    @Getter
    private int loginedMemberId = 0;

    private HttpServletRequest req;
    private HttpServletResponse resp;

    private HttpSession session;

    public Rq(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;
        this.session = req.getSession();

        HttpSession httpSession = req.getSession();

        if (httpSession.getAttribute("loginedMemberId") != null) {
            isLogined = true;
            loginedMemberId = (int) httpSession.getAttribute("loginedMemberId");
        }
        this.req.setAttribute("rq", this);
    }

    public void printHistoryBack(String msg) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        println("<script>");
        if (!Ut.isEmpty(msg)) {
            println("alert('" + msg + "');");
        }
        println("history.back();");
        println("</script>");
    }

    private void println(String str) {
        print(str + "\n");
    }

    private void print(String str) {
        try {
            resp.getWriter().append(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        session.removeAttribute("loginedMemberId");
    }

    public void login(Member member) {
        session.setAttribute("loginedMemberId", member.getId());
    }

    public void initBeforeActionInterceptor() {
        System.err.println("initBeforeActionInterceptor 실행");
    }

    public String historyBackOnView(String msg) {
        req.setAttribute("msg", msg);
        req.setAttribute("historyBack", true);
        return "usr/common/js";
    }

    public String getCurrentUri() {
        String currentUri = req.getRequestURI();
        String queryString = req.getQueryString();

        if (currentUri != null && queryString != null) {
            currentUri += "?" + queryString;
        }

        return currentUri;
    }
}
