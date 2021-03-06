package com.ypitta.auctionsite.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ypitta.auctionsite.model.Auction;
import com.ypitta.auctionsite.model.Bid;
import com.ypitta.auctionsite.repository.AuctionRepository;

@Service
public class AuctionService {
	
	@Autowired
	AuctionRepository auctionRepostory;
	
	public void addProductToAuction(Auction auction) {
		
		auctionRepostory.addAuction(auction);
	}

	public void addProductToAuctionByName(Auction auction, int name) {
		auctionRepostory.addAuction(auction, name);
	}
	
	public List<Auction> getAllActiveAuctions(){
		return auctionRepostory.getAllActiveAuctions();
	}
	
	public boolean updateAuction(Auction auction) {
		return auctionRepostory.updateAuction(auction);
	}
	
	public Auction getAuctionByProductId(int name) {
		return auctionRepostory.getAuction(name);
	}
	
	public String placeBidUsername(String username, int id, Bid bid) {
		
		return auctionRepostory.placeBidByUserName(username, id, bid);
	}
	
	public List<Bid> getAllBids(int id){
		return auctionRepostory.getAllBidsForAuction(id);
	}
	
	public List<Bid> getAllUserBids(String username){
		return auctionRepostory.getUserBids(username);
	}
	
	public Object[] getLatestBid(int auctionId) {
		return auctionRepostory.getCurrentHighestBid(auctionId);
	}
	

	public static class sortBids implements Comparator<Bid>{
			@Override
			public int compare(Bid o1, Bid o2) {
				return Double.compare(o1.getPrice(), o2.getPrice());	
		}
	}
	
}
