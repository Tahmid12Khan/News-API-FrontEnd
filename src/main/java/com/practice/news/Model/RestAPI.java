package com.practice.news.Model;

import com.practice.news.Controller.Factory.FormatterFactory;
import com.practice.news.Controller.InterfaceFormatter.iFormatter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

public class RestAPI {
	private static final String url = "http://localhost:8100/api/";
	private static RestTemplate restTemplate;
	private static iFormatter formatter;

	private RestAPI() {

	}

	private static void check() {
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
			formatter = FormatterFactory.getFormatter("json");
		}
	}

	public static <T, S> ResponseEntity<S> request(T send, HttpMethod method, String extenedURL,
												   ParameterizedTypeReference<S> parameterizedTypeReference) {
		check();
		return restTemplate.exchange(url + extenedURL,
				method, RequestBuilder.buildRequest(formatter.stringFormat(send)),
				parameterizedTypeReference
		);

	}

	public static void addAuthentication(String username, String password) {
		check();
		restTemplate.getInterceptors().clear();
		System.out.println(restTemplate.getInterceptors().toString());
		restTemplate.getInterceptors().add(
				new BasicAuthenticationInterceptor(username, password));
		System.out.println(restTemplate.getInterceptors().toString());
	}


}
