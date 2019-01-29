package com.practice.news.Security;

import com.practice.news.Model.User;
import com.practice.news.RestAPI;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		ResponseEntity<String> responseEntity = RestAPI.request(new User(username, password), HttpMethod.POST, "/user",
				new ParameterizedTypeReference<String>() {
				});
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			RestAPI.addAuthentication(username, password);
			return new UsernamePasswordAuthenticationToken(username, password);
		}
		return null;

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(
				UsernamePasswordAuthenticationToken.class);
	}
}