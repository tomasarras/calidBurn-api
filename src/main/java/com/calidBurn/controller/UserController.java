package com.calidBurn.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calidBurn.entity.User;
import com.calidBurn.exception.InvalidUsernameOrPasswordException;
import com.calidBurn.exception.UserNotFoundException;
import com.calidBurn.exception.UsernameAlreadyExistsException;
import com.calidBurn.model.Token;
import com.calidBurn.service.JWTService;
import com.calidBurn.service.UserService;

/**
 * Controller for {@link User}
 *
 */
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTService jwtService;
	
	/**
	 * SignUp one {@link User}
	 * @param {@link User} with username and password
	 * @return {@link ResponseEntity} with {@link HttpStatus#OK} and token
	 * @return {@link ResponseEntity} with {@link HttpStatus#CONFLICT} if username already exists
	 */
	@PostMapping("/signUp")
	public ResponseEntity<Token> signUp(@RequestBody User user) {
		try {
			userService.signUp(user);
			Token token = jwtService.createToken(user);
			return ResponseEntity.ok(token);
		} catch (UsernameAlreadyExistsException e) {
			return ResponseEntity.status(Response.SC_CONFLICT).build();
		}
	}

	/**
	 * LogIn {@link User}
	 * @param {@link User}
	 * @return {@link ResponseEntity} with {@link HttpStatus#OK} and token
	 * @return {@link ResponseEntity} with {@link HttpStatus#BAD_REQUEST} invalid username or password
	 */
	@PostMapping("/logIn")
	public ResponseEntity<Token> logIn(@RequestBody User user) {
		try {
			user = userService.logIn(user);
			Token token = jwtService.createToken(user);
			return ResponseEntity.ok(token);
		} catch (UserNotFoundException | InvalidUsernameOrPasswordException e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
