package com.study.board.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import java.time.DateTimeException;
import java.time.LocalDate;

@Getter
@Setter
public class UserCreateForm {

    @Size(min = 3, max = 25, message = "아이디는 3자 이상 25자 이하로 입력해주세요.")
    @NotEmpty(message = "아이디는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickname;

    @NotEmpty(message = "전화번호는 필수항목입니다.")
    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식은 010-1234-5678이어야 합니다.")
    private String phone;

    @NotEmpty(message = "이름은 필수항목입니다.")
    private String name;

    // 연도, 월, 일 필드 추가
    @NotEmpty(message = "생년월일은 필수항목입니다.")
    private String year;  // 연도
    private String month; // 월
    private String day;   // 일

    @NotEmpty(message = "거주지는 필수항목입니다.")
    private String address;

    // 생년월일을 LocalDate로 반환하는 메서드 추가
    public LocalDate getBirthdateAsLocalDate() {
        if (year != null && !year.isEmpty() && month != null && !month.isEmpty() && day != null && !day.isEmpty()) {
            try {
                // month와 day가 1자리일 경우 2자리로 변환
                int monthValue = Integer.parseInt(month);
                int dayValue = Integer.parseInt(day);

                // LocalDate.of()로 LocalDate 객체 생성
                return LocalDate.of(Integer.parseInt(year), monthValue, dayValue);
            } catch (NumberFormatException | DateTimeException e) {
                return null; // 숫자 파싱 실패나 유효하지 않은 날짜일 경우 null 반환
            }
        }
        return null; // year, month, day가 모두 유효하지 않으면 null 반환
    }
}
