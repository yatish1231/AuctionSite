package com.ypitta.auctionsite.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ypitta.auctionsite.model.Product;
import com.ypitta.auctionsite.model.Seller;
import com.ypitta.auctionsite.model.User;
import com.ypitta.auctionsite.util.GeneralUtil;

import javassist.bytecode.Descriptor.Iterator;

@Repository
@Transactional
public class ProductsRepositoryImpl implements ProductsRepository{

	
	@Autowired
	SessionFactory session;
	
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
	public void saveOrUpdateProducts(Seller seller, List<Product> products) {
		
		beginTransaction();
		Seller ans = (Seller)getSession().get(seller.getClass(), seller.getUsername());
		for(Product prod: products) {
		prod.setSeller(ans);
		ans.getProducts().add(prod);
		}
		getSession().save(ans);
		commit();
		close();
	}

	@Override
	public void saveOrUpdateProducts(String seller, List<Product> products) {
		
		beginTransaction();
		Seller ans = (Seller)getSession().get(Seller.class, seller);
		for(Product prod: products) {
			prod.setSeller(ans);
		ans.getProducts().add(prod);
		}
		getSession().save(ans);
		commit();
		close();
		
	}

	@Override
	public Product getProduct(String name) {
		
		Product prod = (Product)getSession().createCriteria(Product.class).add(Restrictions.eq("name", name)).uniqueResult();
	
		return prod;
	}

	@Override
	public void saveOrUpdateProducts(String seller, Product product) {
		
		beginTransaction();
		Seller ans = (Seller)getSession().get(Seller.class, seller);
		for(Product prod: ans.getProducts()) {
			if(prod.getId() == product.getId()) {
				getSession().saveOrUpdate(product);
			}
		}
		commit();
		close();
	}

	@Override
	public List<Product> getAllProductsBySellerName(String name_seller) {
		
		
		Seller seller = (Seller)getSession().get(User.class, name_seller);
		List<Product> prod = seller.getProducts();
		
		return prod;
			
	}
	
	@Override
	public List<Product> getAllProducts(Seller seller_name) {
			
		
		Seller seller = (Seller)getSession().get(User.class, seller_name.getUsername());
		List<Product> prod = seller.getProducts();
		
		return prod;
	}

	@Override
	public List<Product> getAllProductsNotAuctioned(String name_seller) {
		
		
		Seller seller = (Seller)getSession().get(User.class, name_seller);
		if(seller != null) {
		getSession().refresh(seller);
		List<Product> prod = new ArrayList<Product>();
		for(Product	obj: seller.getProducts()) {
			if(prod != null) {
			getSession().refresh(obj);
			if(!obj.isInAuction()){
				prod.add(obj);	
			}
		}
		}
		return prod;
		}
		return null;
	}

	@Override
	public boolean deleteProduct(String username, int id) {
		
		beginTransaction();
		Seller seller = (Seller) getSession().get(User.class, username);
		if(seller != null) {
			System.out.println("delete product by id :" +id);
		Product prod = seller.getProducts().stream().filter(p -> p.getId() == id).findFirst().get();
		prod.setSeller(null);
		seller.getProducts().remove(prod);
		getSession().saveOrUpdate(seller);
		prod = null;
		commit();
		close();
		return true;
		}
		return false;
	}
	
	
	
}
