package com.example.exam.exam_24_09_hys_crud.controller;

import com.example.exam.exam_24_09_hys_crud.service.FAQService;
import com.example.exam.exam_24_09_hys_crud.vo.FAQ;
import com.example.exam.exam_24_09_hys_crud.vo.Rq;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UsrFAQController {

    private static final Logger log = LoggerFactory.getLogger(UsrFAQController.class);

    @Autowired
    private FAQService faqService;

    @Autowired
    private Rq rq;

    @RequestMapping("/usr/faq/list")
    public String listFAQs(Model model,
                           @RequestParam(defaultValue = "1") int page, // 기본 페이지는 1
                           @RequestParam(defaultValue = "") String keyword,
                           @RequestParam(defaultValue = "10") int itemsInAPage) { // 기본 검색어는 빈 문자열

        log.info("Current Page: {}", page);
        log.info("Search Keyword: '{}'", keyword);

        itemsInAPage = 10; // 한 페이지에 보여줄 항목 수
        // itemsInAPage가 음수로 설정되지 않도록 검증
        if (itemsInAPage <= 0) {
            itemsInAPage = 10;  // 기본값으로 설정
        }
        log.info("Items per Page: {}", itemsInAPage);

        // 페이지가 1보다 작은 값일 때 기본값으로 1을 설정
        if (page < 1) {
            page = 1;
        }

        // 검색어 공백 제거
        keyword = keyword.trim();

        // 전체 FAQ 수 가져오기 (검색어가 없으면 전체 FAQ 목록)
        int faqsCount = faqService.getFAQsCount(keyword);
        log.info("Total FAQs count: {}", faqsCount);

        // 페이지 수 계산
        int pagesCount = (int) Math.ceil(faqsCount / (double) itemsInAPage);
        log.info("Total Pages count: {}", pagesCount);

        // pagesCount가 0이면 최소 1페이지는 있어야 함
        if (pagesCount == 0) {
            pagesCount = 1;
        }

        // 현재 페이지가 전체 페이지보다 클 경우, 마지막 페이지로 설정
        if (page > pagesCount) {
            page = pagesCount > 0 ? pagesCount : 1;
        }
        log.info("Final Page (After Correction): {}", page);

        // offset 계산
        int offset = (page - 1) * itemsInAPage;
        if (offset < 0) {
            offset = 0; // offset이 음수로 계산되면 0으로 설정
        }
        log.info("Offset: {}", offset);

        // 해당 페이지의 FAQ 목록 가져오기
        List<FAQ> faqs = faqService.getFAQsForPage(offset, itemsInAPage, keyword);
        log.info("FAQs for Current Page: {}", faqs.size());

        // 상위 5개의 FAQ 가져오기
        List<FAQ> top5Faqs = faqService.getTop5FAQs();

        model.addAttribute("faqs", faqs);
        model.addAttribute("faqsCount", faqsCount);
        model.addAttribute("pagesCount", pagesCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword); // 검색어를 JSP로 전달
        model.addAttribute("itemsInAPage", itemsInAPage); // JSP에서 페이지 항목 수 확인 가능하도록 전달
        model.addAttribute("top5Faqs", top5Faqs); // 상위 5개 FAQ 데이터 추가


        log.info("Service - Offset: {}", offset);
        log.info("Service - Items per Page: {}", itemsInAPage);

        return "/usr/faq/list"; // FAQ 목록을 보여줄 JSP 페이지
    }
}
