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
		
		beginTransaction();
		Seller seller = (Seller)userRepository.findOne(username);
		Product prod = seller.getProducts().stream().filter(p -> p.getId() == id).findFirst().get();
		int i = seller.getProducts().indexOf(prod);
		seller.getProducts().get(i).setInAuction(true);
		auction.setProduct(seller.getProducts().get(i));
		getSession().saveOrUpdate(seller);
		getSession().save(auction);
		commit();
		close();
	}

	@Override
	public Auction getAuction(int id) {

		Auction auc = (Auction)getSession().get(Auction.class, id);
		if(auc.isActive()) {
			return auc;
		}
		return null;
	}

	@Override
	public Auction getAuctionByProduct(int name) {
		// TODO Auto-generated method stub
		Auction auc = (Auction)getSession().get(Auction.class, name);
		return auc;
	}

	@Override
	public List<Auction> getAllActiveAuctions() {
		// TODO Auto-generated method stub
		List<Auction> active = new ArrayList<Auction>();
		List<Auction> temp = (List<Auction>)getSession().createCriteria(Auction.class).list();
		if(!temp.isEmpty()) {
			for (Auction auction : temp) {
				System.out.println("state: " + auction.getId()+ auction.isActive());
				if(auction.isActive()) {
					active.add(auction);
				}
			}
			return active;
		}
		return null;
	}

	@Override
	public boolean removeProductFromAuction(String name, int id) {
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
		}
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
		
		beginTransaction();
		User user = (User)getSession().get(User.class, name);
		Auction auction = getAuction(id);
		if(auction != null) {
			if(auction.getBids() == null) {
				System.out.println("it is null");
				auction.setBids(new ArrayList<Bid>());
			}
			
			if(auction.getBids().isEmpty()) {
				System.out.println("Empty entry "+bid.getPrice() + bid.getBid_time());
				bid.setUser(user);
				bid.setAuction(auction);
				auction.getBids().add(bid);
				getSession().save(auction);
				commit();
				close();
				return "Successfully Placed First Bid!";
			}
			
		Criteria crt = getSession().createCriteria(Bid.class);
		crt = crt.add(Restrictions.eq("auction.id", id)).setProjection(Projections.projectionList().add(Projections.max("price")).add(Projections.max("bid_time")));
		Object[] cur = (Object[])crt.uniqueResult();
		if(cur.length > 0) {
			if((double)cur[0] > bid.getPrice() && ((Date)cur[1]).before(Calendar.getInstance().getTime())) {
				System.out.println("Not empty entry");
				bid.setUser(user);
				bid.setAuction(auction);
				auction.getBids().add(bid);
				getSession().save(auction);
				commit();
				close();
				return "Successfully added new Bid!!";
			}
			return "Failed to bid, another user might have bid higher or earlier";
			
		}
				
		}
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
	
	
}
