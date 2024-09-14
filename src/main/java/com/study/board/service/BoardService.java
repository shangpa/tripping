package com.study.board.service;

import com.study.board.entity.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.study.board.repository.BoardRepository;

import java.util.List;

@Service

public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    //글 작성 처리
    public void write(Board board){
        boardRepository.save(board);
    }
    //게시글 리스트 처리
    public List<Board> boardList(){

        return boardRepository.findAll();
    }
    //특정게시글 불러오기
    public Board boardView(int id){

        return boardRepository.findById(id).get();
    }
}

