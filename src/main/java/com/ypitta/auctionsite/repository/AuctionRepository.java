package com.ypitta.auctionsite.repository;

import java.util.List;

import com.ypitta.auctionsite.model.Auction;
import com.ypitta.auctionsite.model.Bid;
import com.ypitta.auctionsite.model.Product;

public interface AuctionRepository {
	
	public void addAuction(Auction auction);
	public void addAuction(String username, int id, Auction auction);
	public void addAuction(Auction auction, Product product);
	public void addAuction(Auction auction, int product_name);
	public Auction getAuction(int id);
	public Auction getAuctionByProduct(int id);
	public List<Auction> getAllActiveAuctions();
	public List<Auction> getAllInactiveAuctions();
	public boolean removeProductFromAuction(String name, int id);
	
	/**
	 * Bid transaction methods
	 */
	public String placeBidByUserName(String name, int id, Bid bid);
	public List<Bid> getAllBidsForAuction(int id);
}
