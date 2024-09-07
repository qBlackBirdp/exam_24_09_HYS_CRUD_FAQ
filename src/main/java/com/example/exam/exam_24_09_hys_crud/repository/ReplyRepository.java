package com.example.exam.exam_24_09_hys_crud.repository;

import com.example.exam.exam_24_09_hys_crud.vo.Reply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ReplyRepository {

    @Select("""
            SELECT R.*, M.nickname AS extra__writer
            FROM reply AS R
            INNER JOIN `member` AS M
            ON R.memberId = M.id
            WHERE relTypeCode = #{relTypeCode}
            AND relId = #{relId}
            ORDER BY R.id ASC;
            	""")
    public List<Reply> getForPrintReplies(int loginedMemberId, String relTypeCode, int relId);

    @Insert("""
            INSERT INTO reply
            SET regDate = NOW(),
            updateDate = NOW(),
            memberId = #{loginedMemberId},
            relTypeCode = #{relTypeCode},
            relId = #{relId},
            `body` = #{body}
            """)
    public void writeReply(int loginedMemberId, String body, String relTypeCode, int relId);

    @Select("SELECT LAST_INSERT_ID();")
    public int getLastInsertId();

    @Select("SELECT COUNT(*) FROM reply WHERE relTypeCode = 'article' AND relId = #{articleId}")
    public int getRepliesCountByArticleId(int articleId);

    @Select("""
            SELECT R.*
            FROM reply AS R
            WHERE R.id = #{id}
            """)
    Reply getReply(int id);

    @Update("""
            UPDATE reply
            SET `body` = #{body},
            updateDate = NOW()
            WHERE id = #{id}
            	""")
    public void modifyReply(int id, String body);
}