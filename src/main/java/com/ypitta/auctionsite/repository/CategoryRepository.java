package com.ypitta.auctionsite.repository;

import java.util.List;
import java.util.Set;

import com.ypitta.auctionsite.model.Category;
import com.ypitta.auctionsite.model.Product;


/**
 * Category repository interface
 * @author yccha
 *
 */
public interface CategoryRepository {
	
	/**
	 * Find category by name
	 * @param category
	 * @return
	 */
	public Category findByName(String category);
	
	/**
	 * list all available categories
	 * @return
	 */
	public List<Category> listAll();
	
	/**
	 * add category to the list
	 * @param category
	 * @return
	 */
	public boolean addCategory(Category category);
	
	/**
	 * delete category by passing category object
	 * @param category
	 * @return
	 */
	public boolean deleteCategory(Category category);
	
	/**
	 * delete category by name
	 * @param name
	 * @return
	 */
	public boolean deleteCategoryByName(String name);
	
	/**
	 * get all product based on the category
	 * @param name
	 * @return
	 */
	public Set<Product> getAllProductsWithCategory(String name);
	
}
