package com.study.board.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Column(nullable = false)
    private LocalDate birthdate;

    private String intro;

    private String gender;

    @Column(nullable = false)
    private String address; // address 필드 추가

    @Lob // 대용량 데이터 저장을 위한 어노테이션
    private byte[] profileImage;

    // Getter, Setter 추가
    public byte[] getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
    // 기본 생성자
    public SiteUser() {
    }

    // 생성자에서 필요한 부분만
    public static SiteUser from(UserCreateForm userCreateForm, PasswordEncoder passwordEncoder) {
        LocalDate birthdate = userCreateForm.getBirthdateAsLocalDate();  // LocalDate로 변환된 생년월일 사용
        if (birthdate == null) {
            // 예외 처리나 기본 값 설정 등을 할 수 있습니다.
            throw new IllegalArgumentException("Invalid birthdate");
        }

        return SiteUser.builder()
                .username(userCreateForm.getUsername())
                .email(userCreateForm.getEmail())
                .password(passwordEncoder.encode(userCreateForm.getPassword1()))
                .nickname(userCreateForm.getNickname())
                .phone(userCreateForm.getPhone())
                .name(userCreateForm.getName())
                .birthdate(birthdate)
                .address(userCreateForm.getAddress())
                .build();
    }


}
