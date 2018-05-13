package com.white.loginspringdata.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.white.loginspringdata.model.AppUser;
import com.white.loginspringdata.model.UserRole;
import com.white.loginspringdata.repository.RoleRepository;
import com.white.loginspringdata.repository.UserRepository;
import com.white.loginspringdata.repository.UserRoleRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		AppUser appUser = userRepository.findByUserName(userName);

		if (appUser == null) {
			System.out.println("User not found! " + userName);
			throw new UsernameNotFoundException(
					"User " + userName + " was not found in the database");
		}

		System.out.println("Found User: " + appUser);

		List<UserRole> userRoles = userRoleRepository.findByUserId(appUser.getUserId());

		// [ROLE_USER, ROLE_ADMIN,..]
		List<String> roleNames = new ArrayList<>();

		// List<AppRole> roles = new ArrayList<>();

		for (UserRole userRole : userRoles) {
			roleNames.add(roleRepository.findByRoleId(userRole.getRoleId()).getRoleName());
		}

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				// ROLE_USER, ROLE_ADMIN,..
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}

		UserDetails userDetails = (UserDetails) new User(appUser.getUserName(),
				appUser.getEncrytedPassword(), grantList);

		return userDetails;
	}
}
