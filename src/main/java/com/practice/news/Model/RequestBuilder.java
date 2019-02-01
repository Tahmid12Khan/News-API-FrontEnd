package com.practice.news.Model;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RequestBuilder {
	public static HttpEntity<String> buildRequest(String json, String acceptType) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.add(HttpHeaders.ACCEPT, acceptType);
		return new HttpEntity<>(json, httpHeaders);
	}

	public static HttpEntity<String> buildRequest(String json) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(json, httpHeaders);
	}
}
