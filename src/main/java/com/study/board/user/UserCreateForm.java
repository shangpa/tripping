package com.study.board.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @Email
    private String email;

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickname;  // 추가된 필드

    @NotEmpty(message = "전화번호는 필수항목입니다.")
    private String phone;     // 추가된 필드

    @NotEmpty(message = "이름은 필수항목입니다.")
    private String name;      // 추가된 필드

    @NotEmpty(message = "생년월일은 필수항목입니다.")
    private String birthdate; // 추가된 필드
}