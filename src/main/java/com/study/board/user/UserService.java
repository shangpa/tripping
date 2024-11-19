package com.study.board.user;

import com.study.board.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        return userRepository.findByusername(username)
                .orElseThrow(() -> new DataIntegrityViolationException("사용자를 찾을 수 없습니다."));
    }
}