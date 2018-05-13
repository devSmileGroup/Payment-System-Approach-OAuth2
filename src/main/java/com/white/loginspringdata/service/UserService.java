package com.white.loginspringdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.white.loginspringdata.model.AppUser;
import com.white.loginspringdata.model.UserRole;
import com.white.loginspringdata.repository.RoleRepository;
import com.white.loginspringdata.repository.UserRepository;
import com.white.loginspringdata.repository.UserRoleRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public void saveUser(AppUser appUser) {

		appUser.setEncrytedPassword(bCryptPasswordEncoder.encode(appUser.getEncrytedPassword()));
		appUser.setEnabled(true);
		userRepository.save(appUser);

		Long roleId = roleRepository.findByRoleName("ROLE_USER").getRoleId();
		Long userId = userRepository.findByUserName(appUser.getUserName()).getUserId();

		UserRole userRole = new UserRole();
		userRole.setRoleId(roleId);
		userRole.setUserId(userId);
		userRoleRepository.save(userRole);
	}
}
