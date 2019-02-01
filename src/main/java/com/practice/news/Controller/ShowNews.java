package com.practice.news.Controller;

import com.practice.news.Error.Invalid;
import com.practice.news.Model.News;
import com.practice.news.Model.RestAPI;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class ShowNews {

	@GetMapping(value = "/news/{id}")
	public String showNews(@PathVariable String id, Model model) {
		ResponseEntity<News> news = RestAPI.request("", HttpMethod.GET, "news/" + id, new ParameterizedTypeReference<News>() {
		});
		if (news.getStatusCode() == HttpStatus.OK) {
			model.addAttribute("news", news.getBody());
			return "details";
		}
		return "error";
	}

	@GetMapping(value = "/news/download/{option}/{id}")
	public ResponseEntity<Resource> formatView(@PathVariable Map<String, String> req) {
		ResponseEntity<String> news = RestAPI.request("", HttpMethod.GET, "news/" + req.get("id"),
				new ParameterizedTypeReference<String>() {
				},
				"application/" + req.get("option"));
		if (news.getStatusCode() != HttpStatus.OK) {
			throw new Invalid("News not found");
		}

		return download(news.getBody(), Long.parseLong(req.get("id")), req.get("option"));

	}

	private ResponseEntity<Resource> download(String s, Long id, String option) {
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +
						id + "." + option)
				.body(new ByteArrayResource(s.getBytes()));
	}


}

