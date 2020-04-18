package com.ypitta.auctionsite.repository;

import java.util.List;

import com.ypitta.auctionsite.model.Product;
import com.ypitta.auctionsite.model.Seller;

import javassist.NotFoundException;


/**
 * Interface for product repository
 * @author yccha
 *
 */
public interface ProductsRepository {
	
	/**
	 * Save or update products by passing seller object and list of products
	 * @param seller
	 * @param products
	 */
	public void saveOrUpdateProducts(Seller seller, List<Product> products);
	
	/**
	 * Save or update products by passing seller username and list of products
	 * @param seller
	 * @param products
	 */
	public void saveOrUpdateProducts(String seller, List<Product> products);
	
	/**
	 * Save or update products by passing seller username and passing a single product
	 * @param seller
	 * @param product
	 */
	public void saveOrUpdateProducts(String seller, Product product);
	
	/**
	 * 
	 * @param username
	 * @param id
	 * @return
	 */
	public Product getProductById(String username, int id) throws NotFoundException;
	
	/**
	 * Get all products of the seller by passing seller username
	 * @param name_seller
	 * @return
	 */
	public List<Product> getAllProductsBySellerName(String name_seller);
	
	/**
	 * Get all products of seller by passing seller object
	 * @param seller_name
	 * @return
	 */
	public List<Product> getAllProducts(Seller seller_name);
	
	/**
	 * get all products not auctioned by the seller by passing seller username
	 * @param seller
	 * @return
	 */
	public List<Product> getAllProductsNotAuctioned(String seller);
	
	/**
	 * delete product by passing seller username and product id
	 * @param username
	 * @param id
	 * @return
	 */
	public boolean deleteProduct(String username, int id);
	
	
}
