package com.practice.news.Controller;

import com.practice.news.Model.RestAPI;
import com.practice.news.Model.User;
import com.practice.news.Security.AuthenticationFacade;
import com.practice.news.Security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserManagementController {
	private IAuthenticationFacade authentication;

	@Autowired
	public UserManagementController(AuthenticationFacade authentication) {
		this.authentication = authentication;
	}

	@GetMapping(value = "/register")
	public String addUser(Model model) {
		if (loggedIn()) {
			return redirect();
		}
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping(value = "/register")
	public String addUser(@Valid @ModelAttribute("user") User user,
						  BindingResult bindingResult) {
		if (loggedIn()) {
			return redirect();
		}
		if (bindingResult.hasErrors()) {
			return "register";
		}
		System.out.println("Register: " + user.toString());

		ResponseEntity<String> responseEntity = RestAPI.request(user, HttpMethod.POST, "/register",
				new ParameterizedTypeReference<String>() {
				});
		if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
			System.out.println(responseEntity.getBody());
			return "register";
		}
		return redirect();
	}

	@GetMapping(value = "/login")
	public String logUser(Model model) {
		if (loggedIn()) {
			return redirect();
		}
		model.addAttribute("user", new User());
		return "login";
	}

	@GetMapping(value = "/logout")
	public String logout(Model model) {
		model.addAttribute("logout-msg", "You have been successfully logged out");
		return redirect();
	}

	private String redirect() {
		return "redirect:/";
	}

	private boolean loggedIn() {
		return !(authentication.getAuthentication() instanceof AnonymousAuthenticationToken);
	}

}
