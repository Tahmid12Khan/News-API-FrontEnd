package com.practice.news.Controller;

import com.practice.news.Model.News;
import com.practice.news.Model.PagerModel;
import com.practice.news.Model.RestAPI;
import com.practice.news.Model.RestPageImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class HomeController {

	@GetMapping("/")
	public String greeting(@RequestParam(defaultValue = "1") int page, Model model) {
		ResponseEntity<RestPageImpl<News>> responseEntity = RestAPI.request("",
				HttpMethod.GET,
				"/news?page=" + page + "&&size=4",
				new ParameterizedTypeReference<RestPageImpl<News>>() {
				});

		Page<News> newsList = responseEntity.getBody();
		Optional<Page<News>> news = Optional.ofNullable(newsList);
		PagerModel pager;
		if (news.isPresent()) {
			pager = new PagerModel(newsList.getTotalPages(), newsList.getNumber());
		} else {
			pager = new PagerModel(0, 0);
			newsList = new RestPageImpl<>();

		}
		model.addAttribute("newsList", newsList);
		model.addAttribute("selectedPageSize", 4);
		model.addAttribute("pageSizes", 4);
		model.addAttribute("pager", pager);

		return "homepage";
	}

}
