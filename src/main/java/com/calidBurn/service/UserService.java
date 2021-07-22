package com.calidBurn.service;

import com.calidBurn.entity.User;
import com.calidBurn.exception.InvalidUsernameOrPasswordException;
import com.calidBurn.exception.UserNotFoundException;
import com.calidBurn.exception.UsernameAlreadyExistsException;

public interface UserService {
	
	/**
	 * register new {@link User}
	 * @param {@link User} with data
	 * @return {@link User} created
	 * @throws {@link UsernameAlreadyExistsException}
	 */
	User signUp(User user) throws UsernameAlreadyExistsException;
	
	/**
	 * @param {@link User} with username and password
	 * @return {@link User} if login is successful
	 * @throws {@link UserNotFoundException} invalid username
	 * @throws {@link InvalidUsernameOrPasswordException} invalid password
	 */
	User logIn(User user) throws UserNotFoundException, InvalidUsernameOrPasswordException;
	
	/**
	 * Get one {@link User} by username
	 * @param username of the {@link User}
	 * @return {@link User}
	 * @throws {@link UserNotFoundException}
	 */
	User getUserByUsername(String username) throws UserNotFoundException;
	
	/**
	 * Update one {@link User}
	 * @param {@link User} to update
	 * @return {@link User} updated
	 */
	User update(User user);
}
