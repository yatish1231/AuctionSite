package com.ypitta.auctionsite.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ypitta.auctionsite.model.Product;
import com.ypitta.auctionsite.model.Seller;
import com.ypitta.auctionsite.model.User;
import com.ypitta.auctionsite.model.UserRole;
import com.ypitta.auctionsite.service.AuctionService;
import com.ypitta.auctionsite.service.LoginSuccessHandlerImpl;
import com.ypitta.auctionsite.service.SecurityService;
import com.ypitta.auctionsite.service.SellerService;
import com.ypitta.auctionsite.service.UserService;
import com.ypitta.auctionsite.validator.UserValidator;

@Controller
public class UserController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private SellerService sellerService;
    
    @Autowired
    private LoginSuccessHandlerImpl loginHandle;

    @Autowired
    private AuctionService auctionService;
    
    
    
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, @RequestParam(required = true) String type, @RequestParam(required = false) String social_id, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        HashSet<UserRole> vals = new HashSet<UserRole>();
        
        if(type.equalsIgnoreCase("ROLE_SELLER")) {
        	
        		Seller seller = new Seller();
        		UserRole role = new UserRole();
				role.setRole("ROLE_SELLER");
				role.setUser(seller);
				vals.add(role);
				userForm.setUserroles(vals);
        		seller.setUserSuper(userForm);
        		seller.setProducts(new ArrayList<Product>());
        		seller.setSocial_id(social_id);
				userService.save(seller);
				securityService.autologin(seller.getUsername(), seller.getPassword());
				return "redirect:/welcome";
			}
        
	    UserRole role = new UserRole();
		role.setRole("ROLE_BUYER");
		role.setUser(userForm);
		vals.add(role);
		userForm.setUserroles(vals);
	    userService.save(userForm);
	    
        securityService.autologin(userForm.getUsername(), userForm.getPassword());

        return "redirect:/welcome";
    }

    
    
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if(!(auth instanceof AnonymousAuthenticationToken)) {
    		
    		return "redirect:/welcome";
    	}
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }
    
    @RequestMapping(value = "/seller/homepage", method = RequestMethod.GET)
    public String sellerPage(Model model) {
    	return "seller_home";
    }
    
    @RequestMapping(value = "/buyer/homepage", method = RequestMethod.GET)
    public String buyerPage(Model model) {
    	return "buyer_home";
    }

    @RequestMapping(value = "seller/products/add", method = RequestMethod.GET)
    public String addProductPage(Model model){
    	model.addAttribute("product", new Product());
    	model.addAttribute("categories", sellerService.getAllCategories());
    	return "addProduct";
    }
    
    @RequestMapping(value = "seller/products/add", method = RequestMethod.POST)
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam(required = true) String cat, BindingResult bindingResult){
    	
    	CommonsMultipartFile photo = product.getPhoto();
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	String fileNameGen = "img"+ username+product.getName().replaceAll("\\s+","")+ Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis()+".jpeg";
    	product.setFilepath(fileNameGen);
    	File file = new File("C:/auctionSiteImages", fileNameGen);
    	try {
			photo.transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	product.setCategory(sellerService.getCategory(cat));
    	List<Product> ans = new ArrayList<Product>();
    	ans.add(product);
    	sellerService.updateProducts(username, ans);
    	
    	return "redirect:/seller/homepage";
    }
    
    
    @RequestMapping(value = "seller/products/delete/{productName}/{id}", method = RequestMethod.POST)
    public String deleteProduct(@ModelAttribute("product") Product product, @PathVariable int id , BindingResult bindingResult, Model model){
    	
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	boolean ans = sellerService.deleteProductById(username, id);
    	if(ans) {
    		return "redirect:/seller/homepage";
    	}
    	model.addAttribute("message", "Failed to Delete the resource");
    	return "redirect:/error";
    }
    
    
    @RequestMapping(value = "seller/products/view", method = RequestMethod.GET)
    public String viewProducts(Model model) {
    	
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	List<Product> prods = sellerService.getAllProductsNotInAuction(username);
    	model.addAttribute("productsActive", prods);
    	return "view_products";
    }
    
    
    
    @RequestMapping(value = {"/", "/welcome", "/home"}, method = RequestMethod.GET)
    public String welcome(Model model) {
    	
    	final Map<String, String> roleUrlMap = new HashMap<String,String>();
	    roleUrlMap.put("ROLE_SELLER", "redirect:/seller/homepage");
	    roleUrlMap.put("ROLE_BUYER", "redirect:/buyer/homepage");
	    
    	Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    	for (GrantedAuthority grantedAuthority : authorities) {
	        String authorityName = grantedAuthority.getAuthority();
	        if(roleUrlMap.containsKey(authorityName)) {
	            return roleUrlMap.get(authorityName);
	        }
	    }
    	
        return "error";
    }
    
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorPage(Model model) {
    	return "error";
    }
    
}
