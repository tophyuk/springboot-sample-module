package com.kiot.sample.repository;

import com.kiot.sample.domain.Board;
import com.kiot.sample.domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndDeleteYn(Long id, Character deleteYn);

    Optional<Member> findByMemberIdAndDeleteYn(String memberId, Character deleteYn);

    Optional<Member> findByMemberIdAndDeleteYnAndLoginType(String email, Character deleteYn, String loginType);

    boolean existsByMemberIdAndDeleteYn(String memberId, Character deleteYn);
}
