package com.ypitta.auctionsite.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ypitta.auctionsite.model.Category;
import com.ypitta.auctionsite.model.Product;

@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository{
	
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
	public Category findByName(String category) {
		
		beginTransaction();
		Category ans = (Category)getSession().createQuery("from Category where name=?").setParameter(0, category).uniqueResult();
		commit();
		close();
		return ans;
		
	}

	@Override
	public List<Category> listAll() {
		
		beginTransaction();
		List<Category> ans = (List<Category>)getSession().createQuery("from Category").list();
		commit();
		close();
		return ans;
	}

	@Override
	public boolean addCategory(Category category) {
		
		beginTransaction();
		getSession().save(category);
		commit();
		close();
		return true;
	}

	@Override
	public boolean deleteCategory(Category category) {
		
		beginTransaction();
		Category ans = (Category)getSession().get(category.getClass(),category.getName());
		getSession().delete(ans);
		commit();
		close();
		return true;	
	}

	@Override
	public boolean deleteCategoryByName(String name) {
		// TODO Auto-generated method stub
		beginTransaction();
		Category ans = (Category)getSession().get(Category.class,name);
		getSession().delete(ans);
		commit();
		close();
		return true;
	}

	@Override
	public Set<Product> getAllProductsWithCategory(String name) {
		
		beginTransaction();
		Category ans = (Category)getSession().get(Category.class, name);
		Set<Product> prods = ans.getProducts();
		commit();
		close();
		return prods;
	}

}
