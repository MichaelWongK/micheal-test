package com.micheal.wang.transtractiondemo.dao;

import com.micheal.wang.transtractiondemo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
}
