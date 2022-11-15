package com.sol.myboard.repository;

import com.sol.myboard.model.Board;
import com.sol.myboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
