package com.ypitta.auctionsite.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ypitta.auctionsite.model.Auction;
import com.ypitta.auctionsite.model.Bid;
import com.ypitta.auctionsite.model.Product;
import com.ypitta.auctionsite.service.AuctionService;
import com.ypitta.auctionsite.service.SecurityService;
import com.ypitta.auctionsite.service.SellerService;
import com.ypitta.auctionsite.service.UserService;
import com.ypitta.auctionsite.util.BidForm;
import com.ypitta.auctionsite.validator.BidValidator;
import com.ypitta.auctionsite.validator.UserValidator;

@Controller
public class AuctionController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private SellerService sellerService;

    @Autowired
    private AuctionService auctionService;
    
    @Autowired
    private BidValidator bidValidator;
    
    
    /**
     * Mapping 
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "seller/auction/add/{productName}/{id}",method = RequestMethod.GET)
    public String addProductToAuctionForm(Model model, @PathVariable int id) {
    	
    	try {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	System.out.println("id value at request add product to auction form "+id);
    	Product product = sellerService.getProductById(username, id);
    	if(product != null) {
    		Auction auction = new Auction();
    		model.addAttribute("auctionObj", auction);
    		model.addAttribute("product", product);
    		return "auction-form";
    	}
    	model.addAttribute("message", "Could not add product!");
    	return "error";
    	}
    	catch (Exception e) {
			model.addAttribute("message", "Unknown error occured");
			return "error";
		}
    }
    
    @RequestMapping(value = "seller/auction/add/{productName}/{id}", method = RequestMethod.POST)
    public String addProductToAuction(@ModelAttribute("auctionObj") Auction auction, @PathVariable("productName") String prodName,
    	@RequestParam("startTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startTime, 
    	@RequestParam("endTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endTime, @PathVariable int id,
    	Model model) {
    	String result;
    	try {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	if(auction.getProduct() == null) {
    		System.out.println("Product Value is null");
    		auction.setStart_time(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()));
    		auction.setEnd_time(Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()));
    		auction.setActive(true);
    		auction.setBids(new ArrayList<Bid>());
    	}
    	sellerService.addProductToAuction(username, id, auction);
    	model.addAttribute("message", "Successfully added product to auction!");
    	result = "redirect:/seller/homepage";
    	}
    	catch (Exception e) {
    	e.printStackTrace();
    	model.addAttribute("message", "Could not add product to auction");	
		result = "error";
		}
    return result;
    }
    
    
    @RequestMapping(value = "seller/auction/remove/{productName}/{id}", method = RequestMethod.POST)
    public String removeProductFromAuction(@PathVariable("productName") String name, @PathVariable int id, Model model) {
    	
    	try {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	boolean result = sellerService.removeProductFromAuction(username,id);
    	if(result) {
    		model.addAttribute("message", "Removed product from auction");
    		return "redirect:/seller/homepage";
    	}
    	}
    	catch (Exception e) {
    	e.printStackTrace();
		model.addAttribute("message", "Could not remove product from auction - Unknown error occured");	
		return "error";
		}
    	model.addAttribute("message", "Could not remove product from auction - Could not find auction");	
		return "error";
    }
    
    @RequestMapping(value = "seller/auction/viewall",method = RequestMethod.GET)
    public String getSellerProductsInAuction(Model model) {
    	try {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	List<Product> auctions = sellerService.getAllProductsInAuction(username);
    	if(auctions!= null) {
    		model.addAttribute("auctions", auctions);
    		return "auction-view-seller";
    	}
    	}
    	catch (Exception e) {
    		model.addAttribute("message", "Error occured");
        	return "error";
		}
    	model.addAttribute("message", "No Active Auctions now!!!!");
    	return "auction-view-seller";
    }
    
    
    /**
     * User controller method..
     */
    
    @RequestMapping(value = "buyer/auction/viewall",method = RequestMethod.GET)
    public String getProductsInAuction(Model model) {
    	List<Auction> auctions = auctionService.getAllActiveAuctions();
    	if(auctions!= null) {
    		model.addAttribute("auctions", auctions);
    		return "auction-view-buyer";
    	}
    	model.addAttribute("message", "No Active Auctions now!!!!");
    	return "auction-view-buyer";
    }
    
    @RequestMapping(value = "buyer/auction/view/bids",method = RequestMethod.GET)
    public String getUserBids(Model model) {
    	try {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	List<Bid> bids = auctionService.getAllUserBids(username);
    	if(bids == null || bids.isEmpty()) {
    		model.addAttribute("message", "You don't have any bids placed!");
    		return "bid-view-buyer";
    	}
    	model.addAttribute("userBids", bids);
    	}
    	catch (Exception e) {
			model.addAttribute("message", "Error occured");
			return "error";
		}
    	
    	return "bid-view-buyer";
    }
    
    @RequestMapping(value = "buyer/auction/bid/{productName}/{id}", method = RequestMethod.GET)
    public String getUserBidPage(Model model, @PathVariable int id) {
    	try {
    	Auction auc = auctionService.getAuctionByProductId(id);
    	if(auc == null) {
    		model.addAttribute("message", "Auction does not exist");
    		return "redirect:/error";
    	}
    	if(auc.getBids().isEmpty()) {
    		model.addAttribute("auction", auc);
    		model.addAttribute("bidObj", new BidForm());
    		model.addAttribute("message", "You are the first bidder!!!!");
    		return "bid-view";
    	}
    	model.addAttribute("auction", auc);
    	model.addAttribute("bidObj", new BidForm());
    	model.addAttribute("placedBids", auc.getBids());
    	}
    	catch (Exception e) {
			model.addAttribute("message", "Error occured");
			return "error";
		}
    	return "bid-view";
    }
    
    @RequestMapping(value = "buyer/auction/bid/{productName}/{id}", method = RequestMethod.POST)
    public String placeUserBid(@ModelAttribute("bidObj") BidForm bidForm, @PathVariable int id, @PathVariable("productName") String prodName,
    		BindingResult bindingresult, Model model){
    	try {
    		bidValidator.validate(bidForm, bindingresult);
    		if(bindingresult.hasErrors()) {
    			model.addAttribute("message", "Cannot place bid! Another user has bid higher or earlier!!");
    			return getUserBidPage(model, id);
    		}
    		Bid bid = new Bid();
    		bid.setPrice(bidForm.getPrice());
    		bid.setId(new Integer(0));
    		bid.setBid_time(bidForm.getTime());
    		String username = SecurityContextHolder.getContext().getAuthentication().getName();
    		String res = auctionService.placeBidUsername(username, id, bid);
    		model.addAttribute("message", res);
    	}
    	catch (Exception e) {
			model.addAttribute("message", "Error occured");
			return "error";
		}
    		return getUserBidPage(model, id);
    }
    
}
