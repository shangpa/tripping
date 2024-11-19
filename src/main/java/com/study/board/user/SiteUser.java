package com.study.board.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String nickname;

    private String phone;

    private String email;

    private String name;

    private String birthdate;

    private String intro;

    private String gender;

    @Column(nullable = false)
    private String address; // address 필드 추가

    // 사용자 정보를 초기화하는 생성자
    public SiteUser(String username, String email, String password, String nickname, String phone, String name, String birthdate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.name = name;
        this.birthdate = birthdate;
        this.address = address; // address 필드 초기화
    }

    // 기본 생성자
    public SiteUser() {
    }


    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

}
