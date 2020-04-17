package com.ypitta.auctionsite.validator;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ypitta.auctionsite.model.Bid;
import com.ypitta.auctionsite.model.User;
import com.ypitta.auctionsite.service.AuctionService;
import com.ypitta.auctionsite.util.BidForm;

@Component
public class BidValidator implements Validator{
	
	@Autowired
	AuctionService auctionService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return BidForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BidForm bid = (BidForm) target;
		System.out.println("id:" + bid.getId() + " price"+bid.getPrice());
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty");
		Object[] obj = auctionService.getLatestBid(bid.getId());
		if(obj!=null) {
		if(obj[0] != null && obj[1] != null) {
        if (bid.getPrice() < (double)obj[0] || ((Date)obj[1]).after(bid.getTime())) {
            errors.rejectValue("price", "Can't place bid! Another user bid higher or earlier!");            
        }
		}
		}
	}
	
}
