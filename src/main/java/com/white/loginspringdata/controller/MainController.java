package com.white.loginspringdata.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.white.loginspringdata.model.AppUser;
import com.white.loginspringdata.repository.UserRepository;
import com.white.loginspringdata.service.OrderService;
import com.white.loginspringdata.service.UserService;
import com.white.loginspringdata.utils.WebUtils;

@Controller
public class MainController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");
		model.addAttribute("message", "This is welcome page!");
		return "welcomePage";
		// return "redirect:loginPage";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {

		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "adminPage";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Model model) {

		return "loginPage";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {

		// After user login successfully.
		String userName = principal.getName();

		System.out.println("User Name: " + userName);

		// LinkedHashMap<String, Object> properties =
		// (LinkedHashMap<String, Object>) authentication.getUserAuthentication().getDetails();
		//
		// List<String> userDetails = new ArrayList<>();
		// userDetails.add((String) properties.get("sub"));
		//
		// String sub = (String) properties.get("sub");
		//
		// System.out.println("************************ sub: " + sub);

		// User loginedUser = (User) ((Authentication) principal).getPrincipal();
		//
		// String userInfo = WebUtils.toString(loginedUser);
		// model.addAttribute("userInfo", userInfo);

		return "userInfoPage";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			User loginedUser = (User) ((Authentication) principal).getPrincipal();

			String userInfo = WebUtils.toString(loginedUser);

			model.addAttribute("userInfo", userInfo);

			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);
		}
		return "403Page";
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public String orders(Model model, Principal principal) {

		String userName = principal.getName();

		model.addAttribute("orders", orderService.getOrdersByCurrentUser(userName));

		return "orderPage";
	}

	@RequestMapping(value = {"/chart"}, method = RequestMethod.GET)
	public ModelAndView login() throws IOException {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("chart");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		AppUser appUser = new AppUser();
		modelAndView.addObject("appUser", appUser);
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid AppUser appUser, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		AppUser userExists = userRepository.findByUserName(appUser.getUserName());
		if (userExists != null) {
			bindingResult.rejectValue("userName", "error.userName",
					"There is already a user registered with the username provided");
		}
		if (appUser.getEncrytedPassword() == null || appUser.getEncrytedPassword().equals("")) {
			bindingResult.rejectValue("encrytedPassword", "error.encrytedPassword",
					"Enter the password");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {

			userService.saveUser(appUser);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new AppUser());
			modelAndView.setViewName("registration");

		}
		return modelAndView;
	}
}
