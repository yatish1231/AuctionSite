package com.ypitta.auctionsite.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ypitta.auctionsite.model.Auction;
import com.ypitta.auctionsite.model.Category;
import com.ypitta.auctionsite.model.Product;
import com.ypitta.auctionsite.model.Seller;
import com.ypitta.auctionsite.repository.AuctionRepository;
import com.ypitta.auctionsite.repository.CategoryRepository;
import com.ypitta.auctionsite.repository.ProductsRepository;
import com.ypitta.auctionsite.repository.UserRepositoryInterface;

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
	
	public List<Product> getAllProductsInAuction(String name){
		List<Product> products = new ArrayList<Product>();
		Seller seller = (Seller)userRepository.findOne(name);
		List<Product> prods = seller.getProducts();
		for (Product product : prods) {
			if(product.isInAuction()) {
				products.add(product);
			}
		}
		return products;
	}
	
	public Product getProductById(String user, int id) {
		
		Seller seller = (Seller)userRepository.findOne(user);
		System.out.println("get product by id :" +id);
		return seller.getProducts().stream().filter(p -> p.getId() == id).findFirst().get();
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
	
