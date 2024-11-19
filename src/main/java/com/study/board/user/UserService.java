package com.study.board.user;

import com.study.board.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SiteUser create(UserCreateForm userCreateForm) {
        if (userRepository.existsByUsername(userCreateForm.getUsername())) {
            throw new DataIntegrityViolationException("이미 사용 중인 사용자명입니다.");
        }

        SiteUser user = SiteUser.builder()
                .username(userCreateForm.getUsername())
                .email(userCreateForm.getEmail())
                .password(passwordEncoder.encode(userCreateForm.getPassword1())) // 비밀번호 암호화
                .nickname(userCreateForm.getNickname())
                .phone(userCreateForm.getPhone())
                .name(userCreateForm.getName())
                .birthdate(userCreateForm.getBirthdate())
                .address(userCreateForm.getAddress())
                .build();

        return userRepository.save(user);
    }

    public SiteUser getUser(String username) {
        SiteUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return user;
    }


    public void updateUser(SiteUser siteUser) {
        userRepository.save(siteUser); // 수정된 사용자 정보를 저장
    }

    public SiteUser validateUser(String username, String password) {
        // 데이터베이스에서 사용자 검색 (null 처리)
        SiteUser user = userRepository.findByUsername(username);

        // 사용자와 비밀번호 검증
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user; // 비밀번호가 일치하면 사용자 반환
        } else {
            return null; // 유효하지 않은 경우 null 반환
        }
    }



    public SiteUser getCurrentUser(String username) {
        SiteUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new DataNotFoundException("현재 사용자를 찾을 수 없습니다.");
        }
        return user;
    }




}