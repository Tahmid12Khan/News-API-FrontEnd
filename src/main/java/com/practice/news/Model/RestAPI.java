package com.practice.news.Model;

import com.practice.news.Controller.Factory.FormatterFactory;
import com.practice.news.Controller.InterfaceFormatter.iFormatter;
import com.practice.news.Error.RestTemplateErrorHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class RestAPI {
	private static String url = "http://localhost:8100/api/";
	private static RestTemplate restTemplate;
	private static iFormatter formatter;
	private static Logger logger = LogManager.getLogger(RestAPI.class);


	private RestAPI() {

	}

	private static void check() {
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
			restTemplate.setErrorHandler(getErrorHandler());
			formatter = FormatterFactory.getFormatter("json");
		}
	}

	public static <T, S> ResponseEntity<S> request(T send, HttpMethod method, String extenedURL,
												   ParameterizedTypeReference<S> parameterizedTypeReference) {
		check();
//
		logger.info("Request " + url);
		return restTemplate.exchange(url + extenedURL,
				method, RequestBuilder.buildRequest(formatter.stringFormat(send)),
				parameterizedTypeReference
		);

	}

	public static <T, S> ResponseEntity<S> request(T send, HttpMethod method, String extenedURL,
												   ParameterizedTypeReference<S> parameterizedTypeReference,
												   String acceptType) {
		check();
		return restTemplate.exchange(url + extenedURL,
				method, RequestBuilder.buildRequest(formatter.stringFormat(send), acceptType),
				parameterizedTypeReference
		);

	}

	public static void addAuthentication(String username, String password) {
		check();
		clearAuthentication();
		restTemplate.getInterceptors().add(
				new BasicAuthenticationInterceptor(username, password));
		System.out.println(restTemplate.getInterceptors().toString());
	}

	public static void clearAuthentication() {
		restTemplate.getInterceptors().clear();

	}

	@Bean
	private static ResponseErrorHandler getErrorHandler() {
		return new RestTemplateErrorHandler();
	}


}
