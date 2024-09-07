package com.example.exam.exam_24_09_hys_crud.repository;


import com.example.exam.exam_24_09_hys_crud.vo.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BoardRepository {

    @Select("""
            SELECT *
            FROM board
            WHERE id = #{boardId}
            AND delStatus = 0
            	""")
    public Board getBoardById(int boardId);

}
