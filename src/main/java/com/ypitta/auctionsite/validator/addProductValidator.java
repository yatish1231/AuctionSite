package com.ypitta.auctionsite.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ypitta.auctionsite.model.Product;


@Component
public class addProductValidator implements Validator{

	
	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Product.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
	
		ValidationUtils.rejectIfEmpty(errors, "name", "Name cannot be empty");
		ValidationUtils.rejectIfEmpty(errors, "price", "price cannot be empty");
		ValidationUtils.rejectIfEmpty(errors, "photo", "Photo cannot be empty");
		
		
	}

}
