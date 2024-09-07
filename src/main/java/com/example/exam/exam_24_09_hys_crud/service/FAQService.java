package com.example.exam.exam_24_09_hys_crud.service;

import com.example.exam.exam_24_09_hys_crud.repository.FAQRepository;
import com.example.exam.exam_24_09_hys_crud.repository.MemberRepository;
import com.example.exam.exam_24_09_hys_crud.util.Ut;
import com.example.exam.exam_24_09_hys_crud.vo.FAQ;
import com.example.exam.exam_24_09_hys_crud.vo.Member;
import com.example.exam.exam_24_09_hys_crud.vo.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FAQService {

    @Autowired
    private FAQRepository faqRepository;

    // 검색 기능
    public List<FAQ> searchFAQs(String keyword) {
        return faqRepository.searchByKeyword(keyword);
    }

    // 전체 목록 가져오기
    public int getFAQsCount(String keyword) {
        return faqRepository.getFAQsCount(keyword);
    }

    public List<FAQ> getFAQsForPage(int offset, int itemsInAPage, String keyword) {
        log.info("Service Layer - Offset: {}", offset);
        log.info("Service Layer - Items per Page: {}", itemsInAPage);

        return faqRepository.getFAQsForPage(offset, itemsInAPage, keyword);
    }
}