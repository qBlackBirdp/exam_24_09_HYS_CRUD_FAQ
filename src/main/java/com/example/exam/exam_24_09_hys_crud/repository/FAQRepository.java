package com.example.exam.exam_24_09_hys_crud.repository;

import com.example.exam.exam_24_09_hys_crud.vo.FAQ;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FAQRepository {
    // 검색 기능
    @Select("SELECT * FROM faq WHERE question LIKE CONCAT('%', #{keyword}, '%')")
    List<FAQ> searchByKeyword(String keyword);

    // 전체 목록 가져오기 및 페이지네이션
    // FAQ 전체 수 카운트 쿼리
    @Select("""
            <script>
                SELECT COUNT(*) FROM faq
                <if test='keyword != null and keyword != ""'>
                    WHERE question LIKE CONCAT('%', #{keyword}, '%')
                </if>
            </script>
            """)
    int getFAQsCount(@Param("keyword") String keyword);

    // FAQ 목록 페이징 처리 쿼리
    @Select("""
            <script>
                SELECT * FROM faq
                <if test='keyword != null and keyword != ""'>
                    WHERE question LIKE CONCAT('%', #{keyword}, '%')
                </if>
                ORDER BY id DESC
                LIMIT #{itemsInAPage} OFFSET #{offset}
            </script>
            """)
    List<FAQ> getFAQsForPage(@Param("offset") int offset,
                             @Param("itemsInAPage") int itemsInAPage,
                             @Param("keyword") String keyword);
}