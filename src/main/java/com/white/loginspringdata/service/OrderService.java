package com.white.loginspringdata.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.white.loginspringdata.model.AppUser;
import com.white.loginspringdata.model.Order;
import com.white.loginspringdata.repository.OrderRepository;
import com.white.loginspringdata.repository.UserRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;


	public List<Order> getOrdersByCurrentUser(String userName) {

		AppUser appUser = userRepository.findByUserName(userName);

		return orderRepository.findAllByUserId(appUser.getUserId());
	}
}
