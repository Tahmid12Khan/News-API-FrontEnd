package com.practice.news.Controller;

import com.practice.news.Model.News;
import com.practice.news.Model.RestAPI;
import com.practice.news.Security.AuthenticationFacade;
import com.practice.news.Security.IAuthenticationFacade;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class NewsManagementController {

	private IAuthenticationFacade authentication;

	public NewsManagementController(AuthenticationFacade authentication) {
		this.authentication = authentication;
	}

	@GetMapping(value = "/add-news")
	public String addNews(Model model) {
		model.addAttribute("news", new News(authentication.getAuthentication().getName()));
		return "add-news";
	}

	@PostMapping(value = "/add-news")
	public String addNews(@Valid @ModelAttribute News news, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "add-news";
		}
		ResponseEntity<String> responseEntity = RestAPI.request(news, HttpMethod.POST,
				"/news", new ParameterizedTypeReference<String>() {
				});

		if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
			bindingResult.addError(new ObjectError(bindingResult.getObjectName(), responseEntity.getBody()));
			return "add-news";
		}
		return "redirect:/";
	}

	@GetMapping(value = "/edit-news/{id}")
	public String editNews(@PathVariable String id, Model model) {

		ResponseEntity<News> responseEntity = RestAPI.request("", HttpMethod.GET,
				"/news/" + id, new ParameterizedTypeReference<News>() {
				});
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			return "redirect:/";
		}
		News news = responseEntity.getBody();
		if (!news.getAuthor().equals(authentication.getAuthentication().getName())) {
			return "error";
		}
		model.addAttribute("news", responseEntity.getBody());
		return "edit-news";
	}

	@PutMapping(value = "/edit-news")
	public String editNews(@Valid @ModelAttribute("news") News news, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "edit-news";
		}
		ResponseEntity<String> responseEntity = RestAPI.request(news, HttpMethod.PUT,
				"/news/" + news.getId(), new ParameterizedTypeReference<String>() {
				});
		if (responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
			bindingResult.addError(new ObjectError(bindingResult.getObjectName(), responseEntity.getBody()));
			return "edit-news";
		}
		return "redirect:/";
	}

	@DeleteMapping(value = "/delete-news/{id}")
	public @ResponseBody
	String deleteNews(@PathVariable String id) {
		ResponseEntity<String> responseEntity = RestAPI.request(new News(Long.parseLong(id)), HttpMethod.DELETE,
				"/news/" + id, new ParameterizedTypeReference<String>() {
				});

		if (responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
			return "Not Success";
		}
		return "Success";
	}
}
