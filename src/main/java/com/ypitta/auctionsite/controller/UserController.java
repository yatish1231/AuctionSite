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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ypitta.auctionsite.repository.UserRepository;
import com.ypitta.auctionsite.service.AuctionService;
import com.ypitta.auctionsite.service.LoginSuccessHandlerImpl;
import com.ypitta.auctionsite.service.SecurityService;
import com.ypitta.auctionsite.service.SellerService;
import com.ypitta.auctionsite.service.UserService;
import com.ypitta.auctionsite.validator.UserValidator;
import com.ypitta.auctionsite.validator.addProductValidator;

/**
 * Controller for user registration, login and handling seller product operations
 */
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
    
    @Autowired
    private addProductValidator productValidator;
    
    private Logger _LOGGER = LoggerFactory.getLogger(UserRepository.class);
    
    /**
     * GET mapping = /registration
     * @param model
     * @return registration form
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }
    
    /**
     *POST mapping = /registration
     * Registration form post handler
     * Calls form-validator
     * @param userForm
     * @param type
     * @param social_id
     * @param bindingResult
     * @param model
     * @return
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, @RequestParam(required = true) String type, @RequestParam(required = false) String social_id, BindingResult bindingResult, Model model) {
        
    	try {
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
				securityService.autologin(userForm.getUsername(), userForm.getPassword());
				return "redirect:/welcome";
			}
        String pass = userForm.getPassword();
	    UserRole role = new UserRole();
		role.setRole("ROLE_BUYER");
		role.setUser(userForm);
		vals.add(role);
		userForm.setUserroles(vals);
	    userService.save(userForm);
	    _LOGGER.info("Username: "+userForm.getUsername()+" saved");
        securityService.autologin(userForm.getUsername(), pass);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		_LOGGER.error("Error occured while registering user");
			model.addAttribute("message", "Error has occured");
			return "error";
		}
        return "redirect:/welcome";
    }

    /**
     * GET Mapping = /login
     * login request handler
     * @param model
     * @param error
     * @param logout
     * @return /login if not logged in or else /welcome
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
    	
    	try {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if(!(auth instanceof AnonymousAuthenticationToken)) {
    		
    		return "redirect:/welcome";
    	}
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
    	}
    	catch (Exception e) {
			model.addAttribute("message", "Error occured");
			return "error";
		}
        return "login";
    }
    
    /**
     * GET mapping = /seller/homepage
     * @param model
     * @return seller homepage
     */
    @RequestMapping(value = "/seller/homepage", method = RequestMethod.GET)
    public String sellerPage(Model model) {
    	return "seller_home";
    }
    
    /**
     * GET mapping = /buyer/homepage
     * @param model
     * @return buyer homepage
     */
    @RequestMapping(value = "/buyer/homepage", method = RequestMethod.GET)
    public String buyerPage(Model model) {
    	return "buyer_home";
    }

    /**
     * GET mapping = /seller/products/add
     * @param model
     * @return add products form
     */
    @RequestMapping(value = "seller/products/add", method = RequestMethod.GET)
    public String addProductPage(Model model){
    	model.addAttribute("product", new Product());
    	model.addAttribute("categories", sellerService.getAllCategories());
    	return "addProduct";
    }
    
    /**
     * POST mapping = /seller/products/add
     * Adds products to seller account
     * @param product
     * @param cat
     * @param bindingResult
     * @param model
     * @return on success - home page, on failure - error page
     */
    @RequestMapping(value = "seller/products/add", method = RequestMethod.POST)
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam(required = true) String cat, BindingResult bindingResult,
    		Model model){
    	
    	_LOGGER.info("Adding product: " + product.getName());
    	try {
    		productValidator.validate(product, bindingResult);
    		if(bindingResult.hasErrors()) {
    			return "addProduct";
    		}
	    	CommonsMultipartFile photo = product.getPhoto();
	    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    	String fileNameGen = "img"+ username+product.getName().replaceAll("\\s+","")+ Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis()+".jpeg";
	    	product.setFilepath(fileNameGen);
	    	File file = new File("C:/auctionSiteImages", fileNameGen);
			photo.transferTo(file);
			product.setCategory(sellerService.getCategory(cat));
	    	List<Product> ans = new ArrayList<Product>();
	    	ans.add(product);
	    	sellerService.updateProducts(username, ans);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	catch (Exception e) {
    		e.printStackTrace();
			model.addAttribute("message", "Error occured");
			return "error";
		}
    	model.addAttribute("message", "Successfully added product");
    	return "seller_home";
    }
    
    /**
     * POST mapping = seller/products/delete/{productName}/{id}
     * Deletes product from seller account
     * @param product
     * @param id
     * @param bindingResult
     * @param model
     * @return on success - seller home page, on failure - error page
     */
    @RequestMapping(value = "seller/products/delete/{productName}/{id}", method = RequestMethod.POST)
    public String deleteProduct(@ModelAttribute("product") Product product, @PathVariable int id , BindingResult bindingResult, Model model){
    	
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	boolean ans = sellerService.deleteProductById(username, id);
    	if(ans) {
    		model.addAttribute("message", "Succesfully deleted product");
    		return "seller_home";
    	}
    	model.addAttribute("message", "Failed to Delete the resource");
    	return "error";
    }
    
    /**
     * GET mapping = /seller/products/view
     * Gets all seller products
     * @param model
     * @return view products page
     */
    @RequestMapping(value = "seller/products/view", method = RequestMethod.GET)
    public String viewProducts(Model model) {
    	
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	List<Product> prods = sellerService.getAllProductsNotInAuction(username);
    	model.addAttribute("productsActive", prods);
    	return "view_products";
    }
    
    
    /**
     * POST mapping = /seller/products/edit/{product name}/{product id}
     * Edit product
     * @param id
     * @param model
     * @return edit products form page
     */
    @RequestMapping(value = "/seller/products/edit/{product.name}/{product.id}", method = RequestMethod.POST)
    public String editSellerProduct(@RequestParam("editProductId") int id, Model model) {
    	
    	try {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	Product prod = sellerService.getProductById(username, id);
    	model.addAttribute("product", prod);
    	model.addAttribute("categories", sellerService.getAllCategories());
    	}
    	catch (Exception e) {
			model.addAttribute("message", "Error occured");
			return "error";
    	}
    	return "seller-edit-product";
    }
    
    /**
     * POST mapping = /seller/products/edit/confirm
     * handles edited product information
     * @param product
     * @param cat
     * @param bindingResult
     * @param model
     * @return on success - view products, on failure - error page
     */
    @RequestMapping(value = "seller/products/edit/confirm", method = RequestMethod.POST)
    public String editProductConfirm(@ModelAttribute("product") Product product, @RequestParam(required = true) String cat, BindingResult bindingResult,
    		Model model){
    	
    	
    	try {
    		productValidator.validate(product, bindingResult);
    		if(bindingResult.hasErrors()) {
    			return "seller-edit-product";
    		}
	    	CommonsMultipartFile photo = product.getPhoto();
	    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    	String fileNameGen = "img"+ username+product.getName().replaceAll("\\s+","")+ Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis()+".jpeg";
	    	product.setFilepath(fileNameGen);
	    	File file = new File("C:/auctionSiteImages", fileNameGen);
			photo.transferTo(file);
			product.setCategory(sellerService.getCategory(cat));
	    	sellerService.updateProducts(username, product);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	catch (Exception e) {
    		e.printStackTrace();
			model.addAttribute("message", "Error occured");
			return "error";
		}
    	model.addAttribute("message", "Successfully edited product");
    	return "view_products";
    }
    
    /**
     * GET mapping for redirecting
     * @param model
     * @return
     */
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
    
    /**
     * GET mapping = /error
     * @param model
     * @return error page
     */
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorPage(Model model) {
    	return "error";
    }
    
}
