package com.practice.news.Service;


import com.practice.news.Error.Invalid;
import com.practice.news.Model.User;
import com.practice.news.Persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService{
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	@Autowired
	public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

		this.userRepository = userRepository;
		this.passwordEncoder = bCryptPasswordEncoder;
	}

//	public boolean findByUseridAndAndPassword(String userid, String password) {
////		return userRepository.findByUseridAndAndPassword(userid, password).isPresent();
////
////	}

	private Optional<User> findByUserid(User user) {
		return userRepository.findByUserid(user.getUserid());

	}

	public boolean findByUseridAndPassword(User u, BindingResult bindingResult) {
		Optional <User> user = findByUserid(u);
		if(!user.isPresent()){
			bindingResult.addError(new ObjectError(bindingResult.getObjectName(), "Invalid username"));
			return false;
		}
		User foundUser = user.get();
		if(!passwordEncoder.matches(u.getPassword(), foundUser.getPassword())){
			bindingResult.addError(new ObjectError(bindingResult.getObjectName(), "Invalid username or password"));
			return false;
		}
		return true;
	}

	public boolean save(User user, BindingResult bindingResult) {
		if (findByUserid(user.getUserid())) {
			bindingResult.addError(new FieldError(bindingResult.getObjectName(), "userid", "Username already exists"));
		}
		if (isValid(bindingResult)) {
			save(user);
			return true;
		}
		return false;
	}

	private void save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println(user.getPassword());
		user.setMatchingPassword(user.getPassword());
		userRepository.saveAndFlush(user);
	}

	public boolean findByUserid(String id) {
		Optional<User> user = userRepository.findByUserid(id);
		System.out.println("Present " + user.isPresent());
		return user.isPresent();
	}


	private boolean isValid(BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				System.out.println(fieldError.getField() + " ,hhh " + fieldError.getRejectedValue());
			}
			return false;
		}
		return true;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional <User> u = userRepository.findByUserid(username);
		if(!u.isPresent()){
			throw new Invalid("User is not present");
		}
		return new UserPrincipal(u.get());

	}
}

