package com.example.exam.exam_24_09_hys_crud.controller;

import com.example.exam.exam_24_09_hys_crud.service.ArticleService;
import com.example.exam.exam_24_09_hys_crud.service.BoardService;
import com.example.exam.exam_24_09_hys_crud.service.ReactionPointService;
import com.example.exam.exam_24_09_hys_crud.service.ReplyService;
import com.example.exam.exam_24_09_hys_crud.util.Ut;
import com.example.exam.exam_24_09_hys_crud.vo.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UsrArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private Rq rq;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ReactionPointService reactionPointService;

    @Autowired
    private ReplyService replyService;

    @RequestMapping("/usr/article/detail")
    public String showDetail(Model model, int id, HttpServletRequest req) {
        Rq rq = (Rq) req.getAttribute("rq");

        Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);

        model.addAttribute("article", article);

        int likeCount = reactionPointService.getTotalReactionPoints("article", id);
        model.addAttribute("likeCount", likeCount);

        // 댓글 데이터를 가져와서 모델에 추가
        List<Reply> replies = replyService.getForPrintReplies(rq.getLoginedMemberId(), "article", id);

        int repliesCount = replies.size();
        model.addAttribute("replies", replies);
        model.addAttribute("repliesCount", repliesCount);

        // 사용자의 반응 상태 가져오기
        int userReactionPoint = reactionPointService.getUserReactionPoint(rq.getLoginedMemberId(), "article", id);
        model.addAttribute("userReactionPoint", userReactionPoint);

        return "usr/article/detail";
    }

    @RequestMapping("/usr/article/doReaction")
    @ResponseBody
    public ResultData doReaction(HttpServletRequest req, Model model, int id, String relTypeCode, int relId, int newPoint) {
        Rq rq = (Rq) req.getAttribute("rq");
        int loginedMemberId = rq.getLoginedMemberId();
        model.addAttribute("loginedMemberId", loginedMemberId);
        relTypeCode = "article";

        int resultPoint = reactionPointService.toggleReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId,
                newPoint);
        reactionPointService.updateArticleReactionPoints();

        ResultData loginRd = ResultData.from("", "");

        if (resultPoint == -3) return ResultData.from("F-A", "로그인 필요.", "redirectUri", "/usr/member/login");
        //return Ut.jsReplace("F-A", "로그인 필요.", "/usr/member/login");
        // 최신의 좋아요/싫어요 수를 가져옴
        Article updatedArticle = articleService.getForPrintArticle(loginedMemberId, id);

        String status = resultPoint > 0 ? "liked" : "unliked";
        ResultData rd = ResultData.from("S-1", "리액션기능 실행완료.", "status", status);
        rd.setData2("reactedArticleId", id);

        Map<String, Object> reactionPoints = new HashMap<>();
        reactionPoints.put("goodReactionPoint", updatedArticle.getGoodReactionPoint());
        reactionPoints.put("badReactionPoint", updatedArticle.getBadReactionPoint());

        rd.setData2("reactionPoints", reactionPoints);

        return rd;
    }

    @RequestMapping("/usr/article/doIncreaseHitCountRd")
    @ResponseBody
    public ResultData doIncreaseHitCount(int id) {

        ResultData increaseHitCountRd = articleService.increaseHitCount(id);

        if (increaseHitCountRd.isFail()) {
            return increaseHitCountRd;
        }
        ResultData rd = ResultData.newData(increaseHitCountRd, "hitCount", articleService.getArticleHitCount(id));

        rd.setData2("조회수가 증가된 게시글의 id", id);

        return rd;
    }

    @RequestMapping("/usr/article/modify")
    public String showModify(Model model, int id) {
        Article article = articleService.getArticleById(id);

        if (article == null) {
            return "redirect:/usr/article/list";
        }

        model.addAttribute("article", article);
        return "usr/article/modify";
    }

    @RequestMapping("/usr/article/doModify")
    @ResponseBody
    public String doModify(HttpServletRequest req, int id, String title, String body) {

        Rq rq = (Rq) req.getAttribute("rq");

        Article article = articleService.getArticleById(id);

        if (article == null) {
            return Ut.jsReplace("F-1", Ut.f("%d번 게시글은 없습니다", id), " / ");
        }

        ResultData userCanModifyRd = articleService.userCanModify(rq.getLoginedMemberId(), article);

        if (userCanModifyRd.isFail()) {
            return Ut.jsHistoryBack(userCanModifyRd.getResultCode(), userCanModifyRd.getMsg());
        }

        if (userCanModifyRd.isSuccess()) {
            articleService.modifyArticle(id, title, body);
        }

        article = articleService.getArticleById(id);

        return Ut.jsReplace(userCanModifyRd.getResultCode(), userCanModifyRd.getMsg(), "/usr/article/detail?id=" + id);
    }

    @RequestMapping("/usr/article/doDelete")
    @ResponseBody
    public String doDelete(HttpServletRequest req, int id) {

        Rq rq = (Rq) req.getAttribute("rq");

        Article article = articleService.getArticleById(id);

        if (article == null) {
//			return ResultData.from("F-1", Ut.f("%d번 게시글은 없습니다", id), "입력한 id", id);
            return Ut.jsHistoryBack("F-1", Ut.f("%d번 게시글은 없습니다", id));
        }

        ResultData userCanDeleteRd = articleService.userCanDelete(rq.getLoginedMemberId(), article);

        if (userCanDeleteRd.isFail()) {
            return Ut.jsHistoryBack(userCanDeleteRd.getResultCode(), userCanDeleteRd.getMsg());
        }

        if (userCanDeleteRd.isSuccess()) {
            articleService.deleteArticle(id);
        }

        return Ut.jsReplace(userCanDeleteRd.getResultCode(), userCanDeleteRd.getMsg(), "../article/list");
    }

    @RequestMapping("/usr/article/write")
    public String showWrite(HttpServletRequest req) {

        return "usr/article/write";
    }

    @RequestMapping("/usr/article/doWrite")
    @ResponseBody
    public String doWrite(HttpServletRequest req, String title, String body, String boardId) {

        Rq rq = (Rq) req.getAttribute("rq");

        if (Ut.isEmptyOrNull(title)) {
            return Ut.jsHistoryBack("F-1", "제목을 입력해주세요");
        }
        if (Ut.isEmptyOrNull(body)) {
            return Ut.jsHistoryBack("F-2", "내용을 입력해주세요");
        }
        if (Ut.isEmptyOrNull(boardId)) {
            return Ut.jsHistoryBack("F-3", "게시판 선택해주세요.");
        }

        int boardIdInt = Integer.parseInt(boardId);
        ResultData writeArticleRd = articleService.writeArticle(rq.getLoginedMemberId(), title, body, boardIdInt);

        int id = (int) writeArticleRd.getData1();

        Article article = articleService.getArticleById(id);

        return Ut.jsReplace(writeArticleRd.getResultCode(), writeArticleRd.getMsg(), "/usr/article/detail?id=" + id);
    }

    @RequestMapping("/usr/article/list")
    public String showList(HttpServletRequest req, Model model, @RequestParam(defaultValue = "1") int boardId,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "title,body") String searchKeywordTypeCode,
                           @RequestParam(defaultValue = "") String searchKeyword) throws IOException {

        Rq rq = (Rq) req.getAttribute("rq");

        Board board = boardService.getBoardById(boardId);

        int articlesCount = articleService.getArticlesCount(boardId, searchKeywordTypeCode, searchKeyword);

        // 한페이지에 글 10개
        // 글 20개 -> 2page
        // 글 25개 -> 3page
        int itemsInAPage = 10;

        int pagesCount = (int) Math.ceil(articlesCount / (double) itemsInAPage);

        List<Article> articles = articleService.getForPrintArticles(boardId, itemsInAPage, page, searchKeywordTypeCode, searchKeyword);

        if (board == null) {
            return rq.historyBackOnView("없는 게시판임");
        }

        System.out.println(articles);

        model.addAttribute("articles", articles);

        model.addAttribute("articlesCount", articlesCount);
        model.addAttribute("pagesCount", pagesCount);
        model.addAttribute("board", board);
        model.addAttribute("page", page);
        model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("boardId", boardId);

        return "usr/article/list";
    }
}