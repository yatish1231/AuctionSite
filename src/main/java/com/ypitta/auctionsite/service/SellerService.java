package com.ypitta.auctionsite.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ypitta.auctionsite.model.Auction;
import com.ypitta.auctionsite.model.Bid;
import com.ypitta.auctionsite.model.Category;
import com.ypitta.auctionsite.model.Product;
import com.ypitta.auctionsite.model.Seller;
import com.ypitta.auctionsite.repository.AuctionRepository;
import com.ypitta.auctionsite.repository.CategoryRepository;
import com.ypitta.auctionsite.repository.ProductsRepository;
import com.ypitta.auctionsite.repository.UserRepository;
import com.ypitta.auctionsite.repository.UserRepositoryInterface;

import javassist.NotFoundException;

@Service
public class SellerService {
	
	@Autowired
	ProductsRepository repository;
	
	@Autowired
	CategoryRepository repository2;
	
	@Autowired
	AuctionRepository auctionRepository;
	
	@Autowired
	UserRepositoryInterface userRepository;
	
	private Logger _LOGGER = LoggerFactory.getLogger(UserRepository.class);
	
	public void updateProducts(Seller seller, List<Product> products) {
		repository.saveOrUpdateProducts(seller, products);
		
	}
	
	public void updateProducts(String seller, List<Product> products) {
		
		repository.saveOrUpdateProducts(seller, products);
	}
	
	public void updateProducts(String seller, Product products) {
			
			repository.saveOrUpdateProducts(seller, products);
		}
	
	public List<Category> getAllCategories() {
		
		return repository2.listAll();
	}
	
	public List<Product> getAllProducts(String name){
		
		return repository.getAllProductsBySellerName(name);
	}
	
	public Category getCategory(String name) {
		return repository2.findByName(name);
	}
	
	public List<Product> getAllProductsNotInAuction(String name){
		return repository.getAllProductsNotAuctioned(name);
		
	}
	
	public List<Auction> getAllProductsInAuction(String name){
		return auctionRepository.getAllSellerAuctions(name);
	}
	
	public Product getProductById(String user, int id) {
		
		_LOGGER.info("getting product by id : " +id);
		try {
			return repository.getProductById(user, id);
		} catch (NotFoundException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public void addProductToAuction(String name, int id, Auction auction) {
		System.out.println("add product to auction id :" +id);
			auctionRepository.addAuction(name, id, auction);
		}
	
	public void addProductToAuction(Product product, Auction auction) {

			auctionRepository.addAuction(auction, product);
		}
	
	public boolean removeProductFromAuction(String name, int id) {
		return auctionRepository.removeProductFromAuction(name,id);
	}
	
	public boolean deleteProductById(String username, int id) {
		return repository.deleteProduct(username, id);
			
	}
	

	
}
	
