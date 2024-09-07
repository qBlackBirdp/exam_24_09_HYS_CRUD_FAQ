package com.example.exam.exam_24_09_hys_crud.controller;

import com.example.exam.exam_24_09_hys_crud.service.ReactionPointService;
import com.example.exam.exam_24_09_hys_crud.service.ReplyService;
import com.example.exam.exam_24_09_hys_crud.util.Ut;
import com.example.exam.exam_24_09_hys_crud.vo.Reply;
import com.example.exam.exam_24_09_hys_crud.vo.ResultData;
import com.example.exam.exam_24_09_hys_crud.vo.Rq;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsrReplyController {

    @Autowired
    private Rq rq;

    @Autowired
    private ReactionPointService reactionPointService;

    @Autowired
    private ReplyService replyService;

    @RequestMapping("/usr/reply/doWrite")
    @ResponseBody
    public String doWrite(HttpServletRequest req, String relTypeCode, int relId, String body) {

        rq = (Rq) req.getAttribute("rq");

        if (Ut.isEmptyOrNull(body)) {
            return Ut.jsHistoryBack("F-2", "내용을 입력해주세요");
        }

        ResultData writeReplyRd = replyService.writeReply(rq.getLoginedMemberId(), body, relTypeCode, relId);

        int id = (int) writeReplyRd.getData1();

        return Ut.jsReplace(writeReplyRd.getResultCode(), writeReplyRd.getMsg(), "../article/detail?id=" + relId);
    }

    @RequestMapping("/usr/reply/doModify")
    @ResponseBody
    public String doModify(HttpServletRequest req, int id, String body) {
        System.err.println(id);
        System.err.println(body);
        rq = (Rq) req.getAttribute("rq");

        Reply reply = replyService.getReply(id);

        if (reply == null) {
            return Ut.jsHistoryBack("F-1", Ut.f("%d번 댓글은 존재하지 않습니다", id));
        }

        ResultData loginedMemberCanModifyRd = replyService.userCanModify(rq.getLoginedMemberId(), reply);

        if (loginedMemberCanModifyRd.isSuccess()) {
            replyService.modifyReply(id, body);
        }

        reply = replyService.getReply(id);

        return reply.getBody();
    }

    @RequestMapping("/usr/reply/doDelete")
    @ResponseBody
    public String doDelete(HttpServletRequest req, int id) {
        System.err.println(id);

        rq = (Rq) req.getAttribute("rq");

        Reply reply = replyService.getReply(id);

        if (reply == null) {
            return Ut.jsHistoryBack("F-1", Ut.f("%d번 댓글은 존재하지 않습니다", id));
        }

        ResultData loginedMemberCanDeleteRd = replyService.userCanDelete(rq.getLoginedMemberId(), reply);

        if (loginedMemberCanDeleteRd.isSuccess()) {
//			replyService.deleteReply(id);
        }


        return reply.getBody();
    }

}