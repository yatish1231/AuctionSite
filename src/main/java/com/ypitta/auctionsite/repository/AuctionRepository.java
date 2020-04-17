package com.ypitta.auctionsite.repository;

import java.util.List;

import com.ypitta.auctionsite.model.Auction;
import com.ypitta.auctionsite.model.Bid;
import com.ypitta.auctionsite.model.Product;

/**
 * auction repository interface to manage auctions
 * @author yccha
 *
 */
public interface AuctionRepository {
	
	/**
	 * add auction by passing an auction object
	 * @param auction
	 */
	public void addAuction(Auction auction);
	
	/**
	 * add auction by passing username, product id, auction obj. Preferred method.
	 * @param username
	 * @param id
	 * @param auction
	 */
	public void addAuction(String username, int id, Auction auction);
	
	/**
	 * add auction by passing auction object and product object
	 * @param auction
	 * @param product
	 */
	public void addAuction(Auction auction, Product product);
	
	/**
	 * add auction by passing auction object and product id
	 * @param auction
	 * @param product_name
	 */
	public void addAuction(Auction auction, int product_name);
	
	/**
	 * get auction by using product/auction id
	 * @param id
	 * @return
	 */
	public Auction getAuction(int id);
	
	/**
	 * get all active auctions
	 * @return
	 */
	public List<Auction> getAllActiveAuctions();
	
	/**
	 * get all inactive auctions
	 * @return
	 */
	public List<Auction> getAllInactiveAuctions();
	
	/**
	 * remove product from auction by passing username and product id
	 * @param name
	 * @param id
	 * @return
	 */
	public boolean removeProductFromAuction(String name, int id);
	
	/*
	 * Bid transaction methods
	 */
	
	/**
	 * Place user bid by passing username, product/auction id, and bid object
	 * @param name
	 * @param id
	 * @param bid
	 * @return
	 */
	public String placeBidByUserName(String name, int id, Bid bid);
	
	/**
	 * get all active bid of the auction by passing auction/product id
	 * @param id
	 * @return
	 */
	public List<Bid> getAllBidsForAuction(int id);
	
	/**
	 * get all bids placed by the user by username
	 * @param username
	 * @return
	 */
	public List<Bid> getUserBids(String username);
	
	/**
	 * get the current highest bid for the auction
	 * @param auctionId
	 * @return
	 */
	public Object[] getCurrentHighestBid(int auctionId);
	
	
	
}
