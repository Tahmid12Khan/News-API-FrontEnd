package com.practice.news.Controller;

import com.practice.news.Model.News;

import com.practice.news.Security.AuthenticationFacade;
import com.practice.news.Security.IAuthenticationFacade;
import com.practice.news.Service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class NewsManagementController {

	private NewsService newsService;
	private IAuthenticationFacade authentication;

	@Autowired
	public NewsManagementController(NewsService newsService, AuthenticationFacade authentication) {
		this.newsService = newsService;
		this.authentication = authentication;
	}

	@GetMapping(value = "/add-news")
	public String addNews(Model model) {
		System.out.println(authentication.getAuthentication().getName() + " " + authentication.getAuthentication().getCredentials());
		model.addAttribute("news", new News());
		return "add-news";
	}

	@PostMapping(value = "/add-news")
	public String addNews(@Valid @ModelAttribute News news, BindingResult bindingResult) {
		newsService.save(news, bindingResult);
		return "redirect:/";
	}

	@GetMapping(value = "/edit-news/{id}")
	public String editNews(@PathVariable String id, Model model) {
		model.addAttribute("news", newsService.findById(id));
		return "edit-news";
	}

	@PutMapping(value = "/edit-news")
	public String editNews(@Valid @ModelAttribute("news") News news, BindingResult bindingResult) {
		System.out.println("Here");
		System.out.println(news.getId() + " and " + news.getBody());
		newsService.save(news, bindingResult);
		return "redirect:/";
	}

	@DeleteMapping(value = "/delete-news/{id}")
	public @ResponseBody
	String deleteNews(@PathVariable String id) {

		newsService.delete(id);
		return "Success";
	}



}
