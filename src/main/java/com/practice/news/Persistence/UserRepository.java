package com.practice.news.Persistence;

import com.practice.news.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserid(String userid);

	Optional<User> findByUseridAndAndPassword(String userid, String password);

}