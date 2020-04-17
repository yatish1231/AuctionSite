package com.ypitta.auctionsite.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ypitta.auctionsite.model.Auction;
import com.ypitta.auctionsite.model.Bid;
import com.ypitta.auctionsite.model.Product;
import com.ypitta.auctionsite.model.Seller;
import com.ypitta.auctionsite.model.User;

@Repository
@Transactional
public class AuctionRepositoryImpl implements AuctionRepository{

	@Autowired
	SessionFactory session;
	
	@Autowired
	CategoryRepository categoryRep;
	
	@Autowired
	UserRepositoryInterface userRepository;
	
	private Logger _LOGGER = LoggerFactory.getLogger(UserRepository.class);
	
	private Session session_cur = null;
	
	private Session getSession() {
        if (session_cur == null || !session_cur.isOpen()) {
            session_cur = session.openSession();
        }
        return session_cur;
    }

    private void beginTransaction() {
        getSession().beginTransaction();
    }

    private void commit() {
        getSession().getTransaction().commit();;
    }

    private void close() {
        getSession().close();
    }

    private void rollbackTransaction() {
        getSession().getTransaction().rollback();
    }

	
	@Override
	public void addAuction(Auction auction) {
		beginTransaction();
		getSession().save(auction);
		commit();
		close();
	}

	@Override
	public void addAuction(Auction auction, Product product) {
		
		beginTransaction();
		Product prod = (Product)getSession().get(Product.class, product.getId());
		prod.setInAuction(true);
		auction.setProduct(prod);
		getSession().save(prod);
		getSession().save(auction);
		commit();
		close();
	}

	@Override
	public void addAuction(Auction auction, int product_name) {
		
		beginTransaction();
		Product prod = (Product)getSession().createCriteria(Product.class).add(Restrictions.eq("id", product_name)).list();
		auction.setProduct(prod);
		getSession().save(auction);
		commit();
		close();
		
	}
	
	
	
	@Override
	public void addAuction(String username, int id, Auction auction) {
		
		_LOGGER.info("Add auction by username, id, auction");
		Seller seller = (Seller)userRepository.findOne(username);
		beginTransaction();
		Product prod = seller.getProducts().stream().filter(p -> p.getId() == id).findFirst().get();
		int i = seller.getProducts().indexOf(prod);
		seller.getProducts().get(i).setInAuction(true);
		getSession().update(seller.getProducts().get(i));
		auction.setProduct(seller.getProducts().get(i));
		getSession().save(auction);
		commit();
		close();
	}

	@Override
	public Auction getAuction(int id) {
		_LOGGER.info("get auction by id: "+id);
		Auction auc = (Auction)getSession().get(Auction.class, id);
		if(auc != null) {
		if(auc.isActive()) {
			return auc;
		}
		_LOGGER.debug("Auction is not active!");
		}
		_LOGGER.debug("Auction instace not found: Null value");
		return null;
	}


	@Override
	public List<Auction> getAllActiveAuctions() {
		
		_LOGGER.info("Getting active auctions...");
		List<Auction> active = new ArrayList<Auction>();
		List<Auction> temp = (List<Auction>) getSession().createCriteria(Auction.class).list();
		if(!temp.isEmpty()) {
			for (Auction auction : temp) {
				if(auction.isActive()) {
					active.add(auction);
					_LOGGER.debug("auction is active" + auction.getId());
				}
			}
			return active;
		}
		_LOGGER.debug("No active auctions found");
		return null;
	}

	@Override
	public boolean removeProductFromAuction(String name, int id) {
		_LOGGER.info("removing product from auction :" +id + "username" + name);
		beginTransaction();
		Product prod = (Product)getSession().get(Product.class, id);
		if(prod.getSeller().getUsername().equalsIgnoreCase(name)) {
			Auction auction = (Auction)getSession().get(Auction.class, id);
			if(auction != null && auction.getProduct() != null) {
				auction.getProduct().setInAuction(false);
				auction.setProduct(null);
				getSession().delete(auction);
				commit();
				close();
				return true;
			}
			_LOGGER.error("Auction or product instance is null");
		}
		_LOGGER.error("Unable to delete product. Couldn't confirm identity");
	return false;	
	}

	@Override
	public List<Auction> getAllInactiveAuctions() {
		// TODO Auto-generated method stub
		
		List<Auction> active = (List<Auction>)getSession().createCriteria(Auction.class).add(Restrictions.eqOrIsNull("isActive", false)).list();
		
		return active;
	}

	@Override
	public String placeBidByUserName(String name, int id, Bid bid) {
		_LOGGER.info("Placing user " + name + " bid...");
		beginTransaction();
		User user = (User)getSession().get(User.class, name);
		if(user == null) {
			commit();
			close();
			_LOGGER.error("User not found!");
			throw new NullPointerException("User Object is null");
		}
		Auction auction = getAuction(id);
		if(auction != null) {
			if(auction.getBids() == null) {
				
				auction.setBids(new ArrayList<Bid>());
			}
			
			if(auction.getBids().isEmpty()) {
				_LOGGER.info("Placing first bet for auction id: "+id);
				bid.setUser(user);
				bid.setAuction(auction);
				auction.getBids().add(bid);
				getSession().saveOrUpdate(auction);
				commit();
				close();
				return "Successfully Placed First Bid!";
			}
			
		Criteria crt = getSession().createCriteria(Bid.class);
		
		crt = crt.add(Restrictions.eq("auction.id", id)).setProjection(Projections.projectionList().add(Projections.max("price")).add(Projections.max("bid_time")));
		Object[] cur = (Object[])crt.uniqueResult();
		
		if(cur.length > 0) {
			
			if((double)cur[0] < bid.getPrice() && ((Date)cur[1]).before(bid.getBid_time())) {
				
				bid.setUser(user);
				bid.setAuction(auction);
				auction.getBids().add(bid);
				getSession().saveOrUpdate(auction);
				commit();
				close();
				_LOGGER.info("Successfully placed bid user" + user.getUsername());
				return "Successfully added new Bid!!";
			}
			_LOGGER.debug("Failed to place bid. Didn't meet requirements");
			commit();
			close();
			return "Failed to bid, another user might have bid higher or earlier";
			
		}
				
		}
		_LOGGER.error("Auction not found. Cannot place bid");
		commit();
		close();
		return "Auction timed out!";
	}

	@Override
	public List<Bid> getAllBidsForAuction(int id) {
		
		Auction auc = getAuction(id);
		if(auc == null) {
			return null;
		}
		return new ArrayList<Bid>(auc.getBids());
	}

	@Override
	public List<Bid> getUserBids(String username) {
		
		beginTransaction();
		List<Bid> bids = (List<Bid>) getSession().createCriteria(Bid.class).add(Restrictions.eq("user.username", username)).list();
		if(bids.isEmpty() || bids == null) {
			commit();
			close();
			return null;
		}
		commit();
		close();
		return bids;
		
	}

	@Override
	public Object[] getCurrentHighestBid(int auctionId) {
		_LOGGER.info("Getting current highest bid");
		beginTransaction();
		Criteria crt = getSession().createCriteria(Bid.class);
		crt = crt.add(Restrictions.eq("auction.id", auctionId)).setProjection(Projections.projectionList().add(Projections.max("price")).add(Projections.max("bid_time")));
		Object[] cur = (Object[])crt.uniqueResult();
		commit();
		close();
		return cur;
		}
	
	
}
