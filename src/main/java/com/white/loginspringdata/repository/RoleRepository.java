package com.white.loginspringdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.white.loginspringdata.model.AppRole;

@Repository
public interface RoleRepository extends JpaRepository<AppRole, Long> {

	AppRole findByRoleId(Long roleId);

	AppRole findByRoleName(String roleName);
}
