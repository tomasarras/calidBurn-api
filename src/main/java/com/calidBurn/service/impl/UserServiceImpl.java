package com.calidBurn.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.calidBurn.entity.User;
import com.calidBurn.exception.InvalidUsernameOrPasswordException;
import com.calidBurn.exception.UserNotFoundException;
import com.calidBurn.exception.UsernameAlreadyExistsException;
import com.calidBurn.repository.UserRepository;
import com.calidBurn.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User signUp(User user) throws UsernameAlreadyExistsException{
		try {
			getUserByUsername(user.getUsername());
			throw new UsernameAlreadyExistsException();
		} catch (UserNotFoundException e) {
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user = User.builder()
					.username(user.getUsername())
					.password(encodedPassword)
					.admin(false)
					.purchased(null)
					.published(null)
					.build();
			
			return userRepository.save(user);
		}
	}

	@Override
	public User logIn(User user) throws UserNotFoundException, InvalidUsernameOrPasswordException {
		String rawPassword = user.getPassword();
		user = this.getUserByUsername(user.getUsername());
		if (passwordEncoder.matches(rawPassword, user.getPassword())) {
			return user;
		} else {
			throw new InvalidUsernameOrPasswordException();
		}
	}

	@Override
	public User getUserByUsername(String username) throws UserNotFoundException {
		try {
			return userRepository.findById(username).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException();
		}
	}

	@Override
	public User update(User user) {
		return userRepository.save(user);
	}
}
