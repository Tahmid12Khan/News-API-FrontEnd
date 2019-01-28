package com.practice.news.Controller;

import com.practice.news.Controller.Factory.FormatterFactory;
import com.practice.news.Controller.InterfaceFormatter.iFormatter;
import com.practice.news.Model.User;
import com.practice.news.Security.AuthenticationFacade;
import com.practice.news.Security.IAuthenticationFacade;
import com.practice.news.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Controller
public class UserManagementController {
	private UserService userService;
	final String url = "http://localhost:8100/api";
	private IAuthenticationFacade authentication;
	private RestTemplate restTemplate;
	private AuthenticationManager authenticationManager;
	iFormatter formatter = FormatterFactory.getFormatter("json");
	@Autowired
	public UserManagementController(UserService userService,
									AuthenticationFacade authentication,
									RestTemplate restTemplate,
									AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.authentication = authentication;
		this.restTemplate = restTemplate;
		this.authenticationManager = authenticationManager;

	}

	@GetMapping(value = "/register")
	public String addUser(Model model) {
		if(!annonymousUser() ){
			return redirect();
		}
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping(value = "/register")
	public String addUser(@Valid @ModelAttribute("user") User user,
						  BindingResult bindingResult) {
		if(!annonymousUser() ){
			return redirect();
		}
		iFormatter formatter = FormatterFactory.getFormatter("json");
		System.out.println(user.toString());

		ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/register",
				HttpMethod.POST, RequestBuilder.buildRequest(formatter.stringFormat(user)),
				new ParameterizedTypeReference<String>(){}
				);
		if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
			System.out.println(responseEntity.getBody());
			return "register";
		}
		//userService.save(user);
		return redirect();
	}

	@GetMapping(value = "/login")
	public String logUser(Model model) {
//		System.out.println("GET Mapping: " + authentication.getAuthentication().getName());
		if(!annonymousUser() ){
			return redirect();
		}
		model.addAttribute("user", new User());
		return "login";
	}

	private boolean validUser(User user){
		return false;
	}

	@PostMapping(value = "/login")
	public String verifyUser(@ModelAttribute User user, BindingResult bindingResult) {
//		System.out.println(user.toString());
//		System.out.println(formatter.stringFormat(user));
		ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/user",
				HttpMethod.POST, RequestBuilder.buildRequest(formatter.stringFormat(user)),
				new ParameterizedTypeReference<String>(){}
		);
		System.out.println("Result: " + responseEntity.getStatusCode());
		if(responseEntity.getStatusCode() != HttpStatus.OK){
			return "login";
		}
		UsernamePasswordAuthenticationToken authReq
				= new UsernamePasswordAuthenticationToken(user.getUserid(), user.getPassword());
		Authentication auth = authenticationManager.authenticate(authReq);

		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(auth);

		System.out.println("Success " + authentication.getAuthentication().getName());
		//session.setAttribute("userid", user.getUserid());
		//userService.save(user);
		return "redirect:/?s=login+success";
	}

//	@GetMapping(value = "/logout")
//	public String logout(Model model, HttpSession httpSession) {
//		if (!loggedIn(httpSession)) throwException();
//		httpSession.invalidate();
//		model.addAttribute("logout-msg", "You have been successfully logged out");
//		return "redirect:/";
//	}

	private String redirect(){
		return "redirect:/";
	}
	private boolean annonymousUser(){
		return authentication.getAuthentication() instanceof AnonymousAuthenticationToken;
	}

}
