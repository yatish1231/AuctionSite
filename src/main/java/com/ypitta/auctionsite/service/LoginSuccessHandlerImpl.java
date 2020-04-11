package com.ypitta.auctionsite.service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LoginSuccessHandlerImpl implements AuthenticationSuccessHandler{
	
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		handlerReq(request,response,authentication);
		// TODO Auto-generated method stub
		
	}
	
	protected void handlerReq(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException{
		String target = getTargetUrl(authentication);
		
		if(response.isCommitted()) {
			redirectStrategy.sendRedirect(request, response, "/error");
		}
		redirectStrategy.sendRedirect(request, response, target);
	}
		
	protected String getTargetUrl(Authentication authentication) {
		
		
		Map<String, String> roleUrlMap = new HashMap<String,String>();
	    roleUrlMap.put("ROLE_SELLER", "/seller/homepage");
	    roleUrlMap.put("ROLE_BUYER", "/buyer/homepage");
	    
	    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	    
	    for (GrantedAuthority grantedAuthority : authorities) {
	        String authorityName = grantedAuthority.getAuthority();
	        if(roleUrlMap.containsKey(authorityName)) {
	            return roleUrlMap.get(authorityName);
	        }
	    }
		return "/error";
		
	}
	
}
