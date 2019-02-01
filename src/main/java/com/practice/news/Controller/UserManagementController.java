package com.practice.news.Controller;

import com.practice.news.Model.RestAPI;
import com.practice.news.Model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserManagementController {
	private Logger logger = LogManager.getLogger(UserManagementController.class);
	@GetMapping(value = "/register")
	public String addUser(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping(value = "/register")
	public String addUser(@Valid @ModelAttribute("user") User user,
						  BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "register";
		}
		logger.info("Register: " + user.toString());

		ResponseEntity<String> responseEntity = RestAPI.request(user, HttpMethod.POST, "/register",
				new ParameterizedTypeReference<String>() {
				});
		if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
			bindingResult.addError(new ObjectError(bindingResult.getObjectName(), responseEntity.getBody()));
			return "register";
		}
		return redirect();
	}

	@GetMapping(value = "/login")
	public String logUser(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}

	private String redirect() {
		return "redirect:/";
	}

}
