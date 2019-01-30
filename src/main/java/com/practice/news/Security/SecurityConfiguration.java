package com.practice.news.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private CustomAuthenticationProvider customAuthenticationProvider;

	@Autowired
	public SecurityConfiguration(CustomAuthenticationProvider customAuthenticationProvider) {

		this.customAuthenticationProvider = customAuthenticationProvider;

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/register").anonymous()
				.antMatchers("/").permitAll()
				.antMatchers("/login").anonymous()
				.antMatchers("/news").permitAll()
				.antMatchers("/news/**").permitAll()
				.anyRequest().fullyAuthenticated()
				.and()
				.formLogin()
				.defaultSuccessUrl("/")
				.loginPage("/login")
				.usernameParameter("userid")
				.passwordParameter("password")
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/?logout")
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true).permitAll();
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) {
		auth.eraseCredentials(false).authenticationProvider(customAuthenticationProvider);
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring()
				.antMatchers("/resources/**", "/js/**", "/css/**", "/import-headers.html");
	}

	@Bean("authenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


}
