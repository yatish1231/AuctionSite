package com.ypitta.auctionsite.repository;

import java.util.List;
import java.util.Set;

import com.ypitta.auctionsite.model.Category;
import com.ypitta.auctionsite.model.Product;

public interface CategoryRepository {
		
	public Category findByName(String category);
	public List<Category> listAll();
	public boolean addCategory(Category category);
	public boolean deleteCategory(Category category);
	public boolean deleteCategoryByName(String name);
	public Set<Product> getAllProductsWithCategory(String name);
	
}
