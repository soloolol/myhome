package com.sol.myboard.repository;

import com.sol.myboard.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 지원하는 키워드  spring.io > spring project > spring data > spring JPA > learn > Doc > 검색

    List<Board> findByTitle(String title);

    List<Board> findByTitleOrContent(String title, String content);
}
