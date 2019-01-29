package com.practice.news.Controller;

import com.practice.news.Controller.Factory.FormatterFactory;
import com.practice.news.Controller.InterfaceFormatter.iFormatter;
import com.practice.news.Error.Invalid;
import com.practice.news.Model.News;
import com.practice.news.RestAPI;
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
		ResponseEntity<News> news = RestAPI.request("", HttpMethod.GET, "news/" + req.get("id"), new ParameterizedTypeReference<News>() {
		});
		if (news.getStatusCode() != HttpStatus.OK) {
			throw new Invalid("News not found");
		}

		System.out.println(req.get("option"));
		iFormatter formatter = FormatterFactory.getFormatter(req.get("option"));
		String s = formatter.stringFormat(news.getBody());

		return download(s, Long.parseLong(req.get("id")));

	}

	private ResponseEntity<Resource> download(String s, Long id) {


		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + id + ".txt")
				.body(new ByteArrayResource(s.getBytes()));
	}


}

