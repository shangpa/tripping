package com.study.board.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByusername(String username);

    // 사용자 이름이 존재하는지 확인하는 메서드
    boolean existsByUsername(String username);

    SiteUser findByUsername(String username);
}
