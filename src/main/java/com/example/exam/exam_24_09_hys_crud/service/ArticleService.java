package com.example.exam.exam_24_09_hys_crud.service;

import com.example.exam.exam_24_09_hys_crud.repository.ArticleRepository;
import com.example.exam.exam_24_09_hys_crud.util.Ut;
import com.example.exam.exam_24_09_hys_crud.vo.Article;
import com.example.exam.exam_24_09_hys_crud.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ReplyService replyService;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ResultData writeArticle(int memberId, String title, String body, int boardId) {

        articleRepository.writeArticle(memberId, title, body, boardId);

        int id = articleRepository.getLastInsertId();

        return ResultData.from("S-1", Ut.f("%d번 글이 등록되었습니다", id), "등록 된 게시글의 id", id);
    }

    public void deleteArticle(int id) {
        articleRepository.deleteArticle(id);
    }

    public void modifyArticle(int id, String title, String body) {
        articleRepository.modifyArticle(id, title, body);
    }

    public Article getArticleById(int id) {

        return articleRepository.getArticleById(id);
    }

    public List<Article> getArticles() {
        return articleRepository.getArticles();
    }

    public Article getForPrintArticle(int loginedMemberId, int id) {

        Article article = articleRepository.getForPrintArticle(id);

        controlForPrintData(loginedMemberId, article);

        return article;
    }

    private void controlForPrintData(int loginedMemberId, Article article) {
        if (article == null) {
            return;
        }
        ResultData userCanModifyRd = userCanModify(loginedMemberId, article);
        article.setUserCanModify(userCanModifyRd.isSuccess());

        ResultData userCanDeleteRd = userCanDelete(loginedMemberId, article);
        article.setUserCanDelete(userCanModifyRd.isSuccess());
    }

    public ResultData userCanDelete(int loginedMemberId, Article article) {
        if (article.getMemberId() != loginedMemberId) {
            return ResultData.from("F-2", Ut.f("%d번 게시글에 대한 삭제 권한이 없습니다", article.getId()));
        }
        return ResultData.from("S-1", Ut.f("%d번 게시글을 삭제했습니다", article.getId()));
    }

    public ResultData userCanModify(int loginedMemberId, Article article) {
        if (article.getMemberId() != loginedMemberId) {
            return ResultData.from("F-2", Ut.f("%d번 게시글에 대한 수정 권한이 없습니다", article.getId()));
        }
        return ResultData.from("S-1", Ut.f("%d번 게시글을 수정했습니다", article.getId()), "수정된 게시글", article);
    }

    public List<Article> getBIdArticles(int boardId) {
        return articleRepository.getArticlesByBorderId(boardId);
    }

    // 페이지네이션
    public List<Article> getArticlesByPage(int boardId, int offset, int limit) {
        return articleRepository.getArticlesByPage(boardId, offset, limit);
    }

    public int getTotalArticlesCount(int boardId) {
        return articleRepository.getTotalArticlesCount(boardId);
    }

    // 검색기능.
    public List<Article> getForPrintArticles(int boardId, int itemsInAPage, int page, String searchKeywordTypeCode,
                                             String searchKeyword) {

//		SELECT * FROM article WHERE boardId = 1 ORDER BY DESC LIMIT 0, 10; 1page
//		SELECT * FROM article WHERE boardId = 1 ORDER BY DESC LIMIT 10, 10; 2page

        int limitFrom = (page - 1) * itemsInAPage;
        int limitTake = itemsInAPage;

        return articleRepository.getForPrintArticles(boardId, limitFrom, limitTake, searchKeywordTypeCode,
                searchKeyword);
    }

    public int getArticlesCount(int boardId, String searchKeywordTypeCode, String searchKeyword) {
        return articleRepository.getArticleCount(boardId, searchKeywordTypeCode, searchKeyword);
    }

    // 조회수
    public ResultData increaseHitCount(int id) {
        int affectRows = articleRepository.increaseHitCount(id);

        if (affectRows == 0)
            return ResultData.from("F-1", "해당 게시글은 존재하지 않습니다.", "id", id);

        return ResultData.from("S-1", "해당 게시글의 조회수가 증가합니다.", "id", id);
    }

    public Object getArticleHitCount(int id) {
        return articleRepository.getArticleHitCount(id);
    }

}