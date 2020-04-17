package com.ypitta.auctionsite.repository;

import java.util.List;

import com.ypitta.auctionsite.model.User;

/**
 * User repository interface
 * @author yccha
 *
 */
public interface UserRepositoryInterface {
	
	/**
	 * Find user by username
	 * @param id
	 * @return
	 */
	public User findOne(String id);
	
	/**
	 * Check if user exists using username
	 * @param id
	 * @return
	 */
	public boolean exists(String id);
	
	/**
	 * get a count of number of users
	 * @return
	 */
	public long count();
	
	/**
	 * Delete user by username
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * Delete user by user object
	 * @param entity
	 */
	public void delete(User entity);
	
	/**
	 * Get all users
	 * @return
	 */
	public List<User> findAll();
	
}
