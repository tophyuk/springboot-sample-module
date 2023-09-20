package com.kiot.sample.repository;

import com.kiot.sample.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByIdAndDeleteYn(Long id, char n);
    Page<Board> findAllByDeleteYn(Pageable pageable, char n);
    Page<Board> findByTitleContainingAndDeleteYn(String keyword, char n, Pageable pageable);
    Page<Board> findByContentContainingAndDeleteYn(String keyword, char n, Pageable pageable);
    Page<Board> findByWriterContainingAndDeleteYn(String keyword, char n, Pageable pageable);
}
