package com.calidBurn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.calidBurn.filter.JWTAuthorizationFilter;
import com.calidBurn.service.JWTService;
import com.calidBurn.service.impl.JWTServiceImpl;


@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
		  prePostEnabled = true, 
		  securedEnabled = true, 
		  jsr250Enabled = true)
@ComponentScan(value = "com.calidBurn")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
	
	/**
	 * Configure the public endpoints
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/h2-console/**").permitAll();

		http.csrf().disable();
		http.headers().frameOptions().disable();
		
		http.csrf().disable()
			.addFilterAfter(new JWTAuthorizationFilter(jwtService()), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/**" ).permitAll()
			.antMatchers(HttpMethod.GET, "/").permitAll()
			.antMatchers(HttpMethod.POST, "/users/signUp").permitAll()
			.antMatchers(HttpMethod.POST, "/users/logIn").permitAll()
			.antMatchers(HttpMethod.GET, "/products").permitAll()
			.antMatchers(HttpMethod.GET, "/products/*").permitAll()
			.antMatchers(HttpMethod.GET, "/images/products/*").permitAll()
			.anyRequest().authenticated();
			
	}
	
	@Bean
    public JWTService jwtService() {
        return new JWTServiceImpl();
    }
	
	/**
	 * Cors
	 */
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

}
