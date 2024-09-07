package com.example.exam.exam_24_09_hys_crud.repository;

import com.example.exam.exam_24_09_hys_crud.vo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberRepository {

    @Select("SELECT LAST_INSERT_ID();")
    public int getLastInsertId();

    @Insert("INSERT INTO `member` SET regDate = NOW(), updateDate = NOW(), loginId = #{loginId}, loginPw = #{loginPw}, `name` = #{name}, nickname = #{nickname}, cellphoneNum = #{cellphoneNum}, email = #{email}")
    public void doJoin(String loginId, String loginPw, String name, String nickname, String cellphoneNum, String email);

    @Select("SELECT * FROM `member` WHERE id = #{id}")
    public Member getMemberById(int id);

    @Select("SELECT * FROM `member` WHERE loginId = #{loginId}")
    public Member getMemberByLoginId(String loginId);

    @Select("SELECT * FROM `member` WHERE nickname = #{nickname}")
    public Member getMemberByNickname(String nickname);

    @Select("SELECT * FROM `member` WHERE email = #{email}")
    public Member getMemberByEmail(String email);

    @Select("SELECT * FROM `member` WHERE email = #{email} AND `name` = #{name}")
    public Member getMemberByNameAndEmail(String name, String email);

    @Select("SELECT * FROM `member` WHERE cellphoneNum = #{cellphoneNum} AND `name` = #{name}")
    public Member getMemberByNameAndCellphoneNum(String name, String cellphoneNum);

    @Select("SELECT COUNT(*) FROM `member` WHERE loginId = #{loginId} AND loginPw = #{loginPw}")
    public int doLogin(String loginId, String loginPw);

    @Select("SELECT nickname FROM `member` WHERE id = #{id}")
    public String getNickname(int id);
//	
//	@Select("SELECT * FROM `member` WHERE loginId = #{loginId}")
//	public int getMemberByLoginId1(String loginId);

}