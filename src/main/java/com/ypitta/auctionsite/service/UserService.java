package com.ypitta.auctionsite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ypitta.auctionsite.model.User;
import com.ypitta.auctionsite.repository.UserRepository;

@Service
public class UserService {
	
	  	@Autowired
	    private UserRepository userRepository;
	  
	    @Autowired
	    private BCryptPasswordEncoder bCryptPasswordEncoder;

	   
	    public void save(User user) {
	        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	        userRepository.save(user);
	    }
	    
	    public User findByUsername(String id) {
	    	return userRepository.findOne(id);
	    }
}
