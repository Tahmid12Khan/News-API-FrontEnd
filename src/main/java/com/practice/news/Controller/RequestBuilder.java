package com.practice.news.Controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RequestBuilder {
	public static HttpEntity<String> buildRequest(String json){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String>response = new HttpEntity<>(json, httpHeaders);
		return response;
	}
}