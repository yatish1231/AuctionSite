package com.ypitta.auctionsite.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ypitta.auctionsite.repository.UserRepository;
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
    
    private Logger _LOGGER = LoggerFactory.getLogger(UserRepository.class);
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
    	_LOGGER.info("id value at request add product to auction form "+id);
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
    		_LOGGER.debug("Product Value is null");
    		auction.setStart_time(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()));
    		auction.setEnd_time(Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()));
    		auction.setActive(true);
    		auction.setBids(new ArrayList<Bid>());
    	}
    	sellerService.addProductToAuction(username, id, auction);
    	model.addAttribute("message", "Successfully added product to auction!");
    	result = "seller_home";
    	}
    	catch (Exception e) {
    	e.printStackTrace();
    	model.addAttribute("message", "Could not add product to auction");	
		result = "error";
		}
    return result;
    }
    
    
    @RequestMapping(value = "seller/auction/update/{productName}/{id}",method = RequestMethod.GET)
    public String updateAuctionForm(Model model, @PathVariable int id) {
    	
    	try {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	_LOGGER.info("edit auction "+id);
    	Product prod = sellerService.getProductById(username, id);
    	if(prod != null) {
    		Auction auction = auctionService.getAuctionByProductId(id);
    		model.addAttribute("auctionObj", auction);
    		model.addAttribute("product", prod);
    		return "auction-edit-form";
    	}
    	model.addAttribute("message", "Could not get auction!");
    	return "error";
    	}
    	catch (Exception e) {
			model.addAttribute("message", "Unknown error occured");
			return "error";
		}
    }
    
    @RequestMapping(value = "seller/auction/update/{productName}/{id}",method = RequestMethod.POST)
    public String updateAuctionFormConfirm(@ModelAttribute("auctionObj") Auction auction, @PathVariable("productName") String prodName,
        	@RequestParam("startTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startTime, 
        	@RequestParam("endTime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endTime, @PathVariable int id,
        	Model model) {
    try {
    		auction.setStart_time(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()));
    		auction.setEnd_time(Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()));
    		boolean res = auctionService.updateAuction(auction);
    		if(res) {
    			model.addAttribute("message", "Successfully updated auction");
    			return "auction-view-seller";
    		}
    		_LOGGER.error("Auction update failed"); 
    		model.addAttribute("message", "Could not update auction!");
    		return "error";
    	}
    	catch (Exception e) {
    		
			model.addAttribute("message", "Unknown error occured");
			return "error";
		}
    }
    
    @RequestMapping(value = "seller/auction/remove/{productName}/{id}", method = RequestMethod.POST)
    public String removeProductFromAuction(@PathVariable("productName") String name, @PathVariable int id, Model model) {
    	
    	try {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	boolean result = sellerService.removeProductFromAuction(username,id);
    	if(result) {
    		model.addAttribute("message", "Removed product from auction");
    		return "seller_home";
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
    	List<Auction> auctions = sellerService.getAllProductsInAuction(username);
    	if(auctions.size() > 0) {
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
    
   
    @RequestMapping(value = "seller/auction/view/bids/{productName}/{id}", method = RequestMethod.GET)
    public String getAuctionBids(Model model, @PathVariable int id) {
    	try {
    	Auction auc = auctionService.getAuctionByProductId(id);
    	if(auc.isFinished()) {
    		if(!auc.getBids().isEmpty()) {
    			Bid max_bid = Collections.max(auc.getBids(), new AuctionService.sortBids());
    			model.addAttribute("auction", auc);
		    	List<Bid> bids = auc.getBids();
		    	Collections.sort(bids, Collections.reverseOrder(new AuctionService.sortBids()));
		    	model.addAttribute("placedBids", bids);
		    	model.addAttribute("winning_user", max_bid.getUser());
		    	model.addAttribute("auc_finished", true);
		    	return "seller-bid-view";
    		}
    		model.addAttribute("auction", auc);
    		model.addAttribute("message", "This auction has ended with no bids placed!");
    		model.addAttribute("auc_finished", false);
    		return "seller-bid-view";
    	}
    	if(!auc.isActive()) {
    		model.addAttribute("message", "Auction not yet active");
    		model.addAttribute("startingAt", auc.getStart_time());
    		model.addAttribute("auc_finished", false);
    		return "seller-bid-view";
    	}
    	if(auc.getBids().isEmpty()) {
    		model.addAttribute("auction", auc);
    		model.addAttribute("message", "No bids placed yet");
    		model.addAttribute("auc_finished", false);
    		return "seller-bid-view";
    	}
    	model.addAttribute("auction", auc);
    	List<Bid> bids = auc.getBids();
    	Collections.sort(bids, Collections.reverseOrder(new AuctionService.sortBids()));
    	model.addAttribute("placedBids", bids);
    	model.addAttribute("auc_finished", false);
    	}
    	catch (Exception e) {
			model.addAttribute("message", "Error occured");
			return "error";
		}
    	return "seller-bid-view";
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
    	if(!auc.isActive()) {
    		model.addAttribute("message", "Auction not active");
    		return "error";
    	}
    	if(auc.getBids().isEmpty()) {
    		model.addAttribute("auction", auc);
    		model.addAttribute("bidObj", new BidForm());
    		model.addAttribute("message", "You are the first bidder!!!!");
    		return "bid-view";
    	}
    	model.addAttribute("auction", auc);
    	model.addAttribute("bidObj", new BidForm());
    	Bid max_bid = Collections.max(auc.getBids(), new AuctionService.sortBids());
    	model.addAttribute("currentMaxBid", max_bid);
    	List<Bid> bids = auc.getBids();
    	Collections.sort(bids, Collections.reverseOrder(new AuctionService.sortBids()));
    	model.addAttribute("placedBids", bids);
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
    
    @RequestMapping(value = "buyer/auction/bid/placed/{productName}/{id}", method = RequestMethod.GET)
    public String getPlacedUserBidView(@PathVariable int id, @PathVariable("productName") String prodName ,Model model) {
    	try {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	Auction auc = auctionService.getAuctionByProductId(id);
    	if(auc == null) {
    		model.addAttribute("message", "Auction not found");
    		return "error";
    	}
    	if(auc.isActive()) {
    		return "redirect:/buyer/auction/bid/"+prodName+"/"+String.valueOf(id);
    	}
    	if(auc.isFinished()) {
    		if(!auc.getBids().isEmpty()) {
    			Bid max_bid = Collections.max(auc.getBids(), new AuctionService.sortBids());
    			if(max_bid.getUser().getUsername().equalsIgnoreCase(username)) {
    				model.addAttribute("auction", auc);
    		    	List<Bid> bids = auc.getBids();
    		    	Collections.sort(bids, Collections.reverseOrder(new AuctionService.sortBids()));
    		    	model.addAttribute("placedBids", bids);
    		    	model.addAttribute("activateAddToCart", true);
    		    	model.addAttribute("congrats", "Congratulation you've won the auction!");
    		    	return "buyer-auction-finished";
    			}else {
    				model.addAttribute("auction", auc);
    		    	List<Bid> bids = auc.getBids();
    		    	Collections.sort(bids, Collections.reverseOrder(new AuctionService.sortBids()));
    		    	model.addAttribute("placedBids", bids);
    		    	model.addAttribute("sorrymsg", "Another user has won the auction!");
    		    	model.addAttribute("activateAddToCart", false);
    		    	return "buyer-auction-finished";
    			}
    		}
    		model.addAttribute("auction", auc);
    		model.addAttribute("message", "This auction has ended with no bids placed!");
    		return "buyer-auction-finished";	
    	}
      }catch (Exception e) {
    	  e.printStackTrace();
    	  _LOGGER.info("Error occured: "+e.getMessage());
		model.addAttribute("message", "An error has occured");
		return "error";
	}
    model.addAttribute("message", "Auction not finished: Error");
    return "buyer_home";
    }
}
