package com.ericsson.rampup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String ADMIN = "0";
	private static final String OPERATOR = "1";
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/users/**").permitAll()
				.antMatchers("/customers/**","/orders/**", "/productOfferings/**").permitAll()
				.antMatchers("/roles/**", "/addresses/**", "/requests/**").hasRole(ADMIN)
				.antMatchers("/users/login").permitAll()
				.anyRequest().authenticated();
	}
}
