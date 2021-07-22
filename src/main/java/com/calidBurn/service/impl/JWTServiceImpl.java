package com.calidBurn.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.calidBurn.entity.User;
import com.calidBurn.model.Token;
import com.calidBurn.service.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTServiceImpl implements JWTService {
	@Value("${token.header}")
	public String HEADER;
	
	@Value("${token.prefix}")
	public String PREFIX;
	
	@Value("${token.authorities}")
	public String AUTHORITIES;
	
	@Value("${token.expiration.time}")
	public int EXPIRATION_TIME;
	
	@Value("${token.secret.key}")
	private String SECRET_KEY;
	
	@Override
    public Token createToken(User user) {
		String roles = "ROLE_USER";
		if (user.isAdmin()) {
			roles += ",ROLE_ADMIN";
		}
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(roles);
		
		String token = Jwts
				.builder()
				.setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512,
						SECRET_KEY.getBytes()).compact();

		return new Token(PREFIX + token);
    }
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setUpSpringAuthentication(Claims claims) {
		List<String> authorities = (List) claims.get(AUTHORITIES);

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Override
	public boolean existsJWTToken(HttpServletRequest request, HttpServletResponse res) {
		String authenticationHeader = request.getHeader(HEADER);
		if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
			return false;
		return true;
	}
	
	@Override
	public Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
		return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(jwtToken).getBody();
	}

	@Override
	public Claims validateToken(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean hasRole(Claims claims, String role) {
		return ((List) claims.get(AUTHORITIES)).contains(role);
	}
}
