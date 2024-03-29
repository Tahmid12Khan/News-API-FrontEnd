package com.practice.news.Controller.ConcreteFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.news.Controller.InterfaceFormatter.iFormatter;

public class JSONFormatter implements iFormatter {
	private ObjectMapper obj;

	public JSONFormatter(ObjectMapper obj) {
		this.obj = obj;
	}

	@Override
	public String stringFormat(Object news) {
		try {
			return obj.writeValueAsString(news);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "Error occured in JSON";
		}

	}
}
