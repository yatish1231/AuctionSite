package com.ypitta.auctionsite.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ypitta.auctionsite.model.Bid;

@Component
public class BidValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Bid.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		
	}
	
}
