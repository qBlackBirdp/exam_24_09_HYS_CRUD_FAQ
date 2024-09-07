package com.example.exam.exam_24_09_hys_crud.controller;

import com.example.exam.exam_24_09_hys_crud.service.MemberService;
import com.example.exam.exam_24_09_hys_crud.util.Ut;
import com.example.exam.exam_24_09_hys_crud.vo.Member;
import com.example.exam.exam_24_09_hys_crud.vo.ResultData;
import com.example.exam.exam_24_09_hys_crud.vo.Rq;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsrMemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private Rq rq;

    @RequestMapping("/usr/member/join")
    public String showJoin() {
        return "/usr/member/join";
    }

    @RequestMapping("/usr/member/doJoin")
    @ResponseBody
    public String doJoin(HttpServletRequest req, String loginId, String loginPw,
                         String name, String nickname, String cellphoneNum, String email) {
        rq = (Rq) req.getAttribute("rq");

        if (Ut.isEmptyOrNull(loginId))
            return Ut.jsHistoryBack("F-1", Ut.f("아이디를 입력해주세요."));

        if (Ut.isEmptyOrNull(loginPw))
            return Ut.jsHistoryBack("F-2", Ut.f("비밀번호를 입력해주세요."));

        if (Ut.isEmptyOrNull(name))
            return Ut.jsHistoryBack("F-3", Ut.f("이름을 입력해주세요."));

        if (Ut.isEmptyOrNull(nickname))
            return Ut.jsHistoryBack("F-4", Ut.f("닉네임를 입력해주세요."));

        if (Ut.isEmptyOrNull(cellphoneNum))
            return Ut.jsHistoryBack("F-5", Ut.f("전화번호를 입력해주세요."));

        if (Ut.isEmptyOrNull(email))
            return Ut.jsHistoryBack("F-6", Ut.f("이메일을 입력해주세요."));

        ResultData doJoinRd = memberService.doJoin(loginId, loginPw, name, nickname, cellphoneNum, email);

        if (doJoinRd.isFail()) {
            return Ut.jsHistoryBack(doJoinRd.getResultCode(), doJoinRd.getMsg());
        }

        Member member = memberService.getMemberById((int) doJoinRd.getData1());

        return Ut.jsReplace(doJoinRd.getResultCode(), doJoinRd.getMsg(), "/usr/member/login");
    }

    @RequestMapping("/usr/member/login")
    public String showLogin(HttpServletRequest req) {
        return "/usr/member/login";
    }

    @RequestMapping("/usr/member/doLogin")
    @ResponseBody
    public String doLogin(HttpServletRequest req, String loginId, String loginPw, String afterLoginUri) {

        rq = (Rq) req.getAttribute("rq");

        Member member = memberService.getMemberByLoginId(loginId);

        if (member == null) {
            return Ut.jsHistoryBack("F-3", Ut.f("%s는(은) 존재 하지않습니다.", loginId));
        }

        if (member.getLoginPw().equals(loginPw) == false) {
            return Ut.jsHistoryBack("F-4", Ut.f("비밀번호가 틀렸습니다."));
        }

        rq.login(member);

        if (afterLoginUri.length() > 0)
            return Ut.jsReplace("S-1", Ut.f("%s님 환영합니다", member.getNickname()), afterLoginUri);

        return Ut.jsReplace("S-1", Ut.f("%s님 환영합니다", member.getNickname()), " / ");
    }

    @RequestMapping("/usr/member/doLogout")
    @ResponseBody
    public String doLogout(HttpServletRequest req) {

        // 로그아웃 처리
        rq.logout();

        return Ut.jsReplace("S-1", Ut.f("로그아웃 성공"), " / ");
    }
}