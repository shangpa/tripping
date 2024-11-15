package com.study.board.entity;

import com.study.board.user.SiteUser;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import jakarta.persistence.CascadeType;
import java.util.Date;
import jakarta.persistence.OneToMany;
@Data
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 200)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    private Date date;
    public Board() {
        this.date = new Date(); // 게시글 생성 시 현재 날짜로 설정
    }

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> CommentList;

    @ManyToOne
    private SiteUser author;
}

