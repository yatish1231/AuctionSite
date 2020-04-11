package com.ypitta.auctionsite.repository;

import java.util.List;

import com.ypitta.auctionsite.model.Product;
import com.ypitta.auctionsite.model.Seller;

public interface ProductsRepository {
	
	public void saveOrUpdateProducts(Seller seller, List<Product> products);
	public void saveOrUpdateProducts(String seller, List<Product> products);
	public void saveOrUpdateProducts(String seller, Product product);
	public Product getProduct(String name);
	public List<Product> getAllProductsBySellerName(String name_seller);
	public List<Product> getAllProducts(Seller seller_name);
	public List<Product> getAllProductsNotAuctioned(String seller);
	public boolean deleteProduct(String username, int id);
}
