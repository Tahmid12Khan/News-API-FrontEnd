package com.practice.news;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

public class AuthenticationTest {

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void givenMemUsers_whenGetPingWithValidUser_thenOk() {
		ResponseEntity<String> result
				= makeRestCallToGetPing("saha", "saha");

		Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(200);
		//Assertions.assertThat(result.getBody()).isEqualTo("OK");
	}

	@Test
	public void givenExternalUsers_whenGetPingWithValidUser_thenOK() {
		ResponseEntity<String> result
				= makeRestCallToGetPing("externaluser", "pass");

		Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(401);
		Assertions.assertThat(result.getBody()).isEqualTo("OK");
	}

	@Test
	public void givenAuthProviders_whenGetPingWithNoCred_then401() {
//		ResponseEntity<String> result = makeRestCallToGetPing();
//
//		Assertions.assertThat(result.getStatusCodeValue()).isEqualTo(401);
	}


	private ResponseEntity<String>
	makeRestCallToGetPing(String username, String password) {
		return restTemplate.withBasicAuth(username, password)
				.getForEntity("localhost:8080/login", String.class, Collections.emptyMap());
	}


}
