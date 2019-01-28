package com.practice.news.Security;


import com.practice.news.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;


//
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Autowired
	private WebApplicationContext applicationContext;

	private UserService userDetailsService;
	private DataSource dataSource;

	private PasswordEncoder passwordEncoder;
	@Autowired
	public SecurityConfiguration(UserService userDetailsService, DataSource dataSource,
								 PasswordEncoder passwordEncoder){
		this.userDetailsService = userDetailsService;
		this.dataSource = dataSource;
		this.passwordEncoder = passwordEncoder;

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/register").permitAll()
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/news").permitAll()
				.antMatchers("/news/**").permitAll()
				.anyRequest().fullyAuthenticated()
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/?logout")
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true).permitAll();
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.eraseCredentials(false);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/resources/**", "/js/**", "/css/**", "/import-headers.html");
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}
	@Bean("authenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}




}
