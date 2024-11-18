package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.user.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.study.board.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.study.board.DataNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    // 글 작성 처리
    public void write(Board board) {
        boardRepository.save(board);
    }

    public Page<Board> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.boardRepository.findAll(pageable);
    }
    // 특정 게시글 불러오기
    public Board boardView(int id) {
        return boardRepository.findById(id).orElse(null);
    }

    // 게시글 삭제하기
    public void boardDelete(int id) {
        boardRepository.deleteById(id);
    }
    public Board getBoard(Integer id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new DataNotFoundException("board not found");
        }
    }
    public void vote(Board board, SiteUser siteUser) {
        board.getVoter().add(siteUser);
        this.boardRepository.save(board);
    }
}
