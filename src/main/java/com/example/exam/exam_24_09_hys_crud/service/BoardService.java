package com.example.exam.exam_24_09_hys_crud.service;

import com.example.exam.exam_24_09_hys_crud.repository.BoardRepository;
import com.example.exam.exam_24_09_hys_crud.vo.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board getBoardById(int boardId) {
        return boardRepository.getBoardById(boardId);
    }

}
