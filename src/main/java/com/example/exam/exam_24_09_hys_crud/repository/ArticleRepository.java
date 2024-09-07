package com.example.exam.exam_24_09_hys_crud.repository;

import com.example.exam.exam_24_09_hys_crud.vo.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ArticleRepository {

    //	@Insert("INSERT INTO article SET regDate = NOW(), updateDate = NOW(), title = #{title}, `body` = #{body}")
    public void writeArticle(int memberId, String title, String body, int boardId);

    @Delete("DELETE FROM article WHERE id = #{id}")
    public void deleteArticle(int id);

    //	@Update("UPDATE article SET updateDate = NOW(), title = #{title}, `body` = #{body} WHERE id = #{id}")
    public void modifyArticle(int id, String title, String body);

    //	@Select("SELECT * FROM article WHERE id = #{id}")
    public Article getArticleById(int id);

    //	@Select("SELECT * FROM article ORDER BY id DESC")
    public List<Article> getArticles();

    @Select("SELECT LAST_INSERT_ID();")
    public int getLastInsertId();

    @Select("""
            SELECT A.*, M.nickname AS extra__writer
            FROM article AS A
            INNER JOIN `member` AS M
            ON A.memberId = M.id
            WHERE A.id = #{id}
            	""")
    public Article getForPrintArticle(int id);

    @Select("""
            SELECT a.*, m.nickname AS extra__writer
            FROM article a
            INNER JOIN `member` m
            ON a.memberId = m.id
            WHERE a.boardId = #{boardId}
            ORDER BY
            a.id DESC
            	""")
    public List<Article> getArticlesByBorderId(int boardId);

    // 페이지네이션
    public List<Article> getArticlesByPage(int boardId, int offset, int limit);

    public int getTotalArticlesCount(int boardId);

    // 검색기능
    @Select("""
            <script>
            	SELECT A.*, M.nickname AS extra__writer, IFNULL(COUNT(R.id),0) AS extra__repliesCount
            	FROM article AS A
            	INNER JOIN `member` AS M
            	ON A.memberId = M.id
            	LEFT JOIN `reply` AS R
            	ON A.id = R.relId
            	WHERE 1
            	<if test="boardId != 0">
            		AND boardId = #{boardId}
            	</if>
            	<if test="searchKeyword != ''">
            		<choose>
            			<when test="searchKeywordTypeCode == 'title'">
            				AND A.title LIKE CONCAT('%', #{searchKeyword}, '%')
            			</when>
            			<when test="searchKeywordTypeCode == 'body'">
            				AND A.`body` LIKE CONCAT('%', #{searchKeyword}, '%')
            			</when>
            			<when test="searchKeywordTypeCode == 'writer'">
            				AND M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')
            			</when>
            			<otherwise>
            				AND A.title LIKE CONCAT('%', #{searchKeyword}, '%')
            				OR A.`body` LIKE CONCAT('%', #{searchKeyword}, '%')
            			</otherwise>
            		</choose>
            	</if>
            	GROUP BY A.id
            	ORDER BY A.id DESC
            	<if test="limitFrom >= 0">
            		LIMIT #{limitFrom}, #{limitTake}
            	</if>
            	</script>
            """)
    public List<Article> getForPrintArticles(int boardId, int limitFrom, int limitTake, String searchKeywordTypeCode,
                                             String searchKeyword);

    @Select("""
            <script>
            	SELECT COUNT(*), A.*, M.nickname AS extra__writer
            	FROM article AS A
            	INNER JOIN `member` AS M
            	ON A.memberId = M.id
            	WHERE 1
            	<if test="boardId != 0">
            		AND A.boardId = #{boardId}
            	</if>
            	<if test="searchKeyword != ''">
            		<choose>
            			<when test="searchKeywordTypeCode == 'title'">
            				AND A.title LIKE CONCAT('%', #{searchKeyword}, '%')
            			</when>
            			<when test="searchKeywordTypeCode == 'body'">
            				AND A.`body` LIKE CONCAT('%', #{searchKeyword}, '%')
            			</when>
            			<when test="searchKeywordTypeCode == 'writer'">
            				AND M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')
            			</when>
            			<otherwise>
            				AND A.title LIKE CONCAT('%', #{searchKeyword}, '%')
            				OR A.`body` LIKE CONCAT('%', #{searchKeyword}, '%')
            			</otherwise>
            		</choose>
            	</if>
            	ORDER BY A.id DESC;
            </script>
            """)
    public int getArticleCount(int boardId, String searchKeywordTypeCode, String searchKeyword);

    @Select("""
            SELECT hitCount
            FROM article
            WHERE id = #{id}
            	""")
    public int getArticleHitCount(int id);

    @Update("""
            UPDATE article
            SET hitCount = hitCount + 1
            WHERE id = #{id}
            """)
    public int increaseHitCount(int id);


}