package com.ypitta.auctionsite.service;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService{
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserDetailsServiceImpl userService;
	
	private static Logger LOGGER = LoggerFactory.logger(SecurityService.class);
	
	 public String findLoggedInUsername() {
	        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
	        if (userDetails instanceof UserDetails) {
	            return ((UserDetails)userDetails).getUsername();
	        }

	        return null;
	    }
	 
	 public void autologin(String username, String password) {
	        UserDetails userDetails = userService.loadUserByUsername(username);
	        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

	        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

	        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
	            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	            LOGGER.debug(String.format("Auto login %s successfully!", username));
	        }
	    }
}
