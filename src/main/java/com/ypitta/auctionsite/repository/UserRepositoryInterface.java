package com.ypitta.auctionsite.repository;

import java.util.List;

import com.ypitta.auctionsite.model.User;

public interface UserRepositoryInterface {
	
	public User findOne(String id);	
	public boolean exists(String id);
	public long count();
	public void delete(String id);
	public void delete(User entity);
	public List<User> findAll();
	public void flush();
	
}
