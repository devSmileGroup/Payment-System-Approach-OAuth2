package com.white.loginspringdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.white.loginspringdata.model.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
	AppUser findByUserName(String userName);
}
