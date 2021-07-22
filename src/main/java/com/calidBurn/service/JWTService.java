package com.calidBurn.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.calidBurn.entity.User;
import com.calidBurn.model.Token;

import io.jsonwebtoken.Claims;

public interface JWTService {
	/**
	 * Create a {@link Token} with details of the {@link User}
	 * @param {@link User} with the details
	 * @return {@link Token}
	 */
    public Token createToken(User user);
    
    /**
	 * Method to authenticate within the Spring flow
	 * 
	 * @param {@link Claims}
	 */
    public void setUpSpringAuthentication(Claims claims);
    
    /**
     * Return true if exists token in request
     * @param {@link HttpServletRequest}
     * @param {@link HttpServletResponse}
     * @return true if exists token in request
     */
    public boolean existsJWTToken(HttpServletRequest request, HttpServletResponse res);
    
    /**
     * Get claims if token is valid
     * @param {@link HttpServletRequest}
     * @return {@link Claims}
     */
    public Claims validateToken(HttpServletRequest request);
    
    /**
     * Get claims if token is valid
     * @param token
     * @return Claims
     */
    public Claims validateToken(String token);
    
    /**
     * Check if token has role
     * @param token
     * @param role
     * @return true if has role
     */
    public boolean hasRole(Claims claims, String role);
}
