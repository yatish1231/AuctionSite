package com.ypitta.auctionsite.repository;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ypitta.auctionsite.model.User;

@Repository
@Transactional
public class UserRepository implements UserRepositoryInterface{
	
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
	
	public User findOne(String id) {
		User user = null;	
		beginTransaction();
		user = (User)getSession().get(User.class, id);
		if(user != null) {getSession().refresh(user);}
		commit();
		getSession().flush();
		close();
		return user;
	}

	
	public boolean exists(String id) {
		return true;
		
	}
	
	public boolean save(User user) {
		beginTransaction();
		getSession().saveOrUpdate(user);
		commit();
		close();
		return true;
	}
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public void delete(String id) {
		beginTransaction();
		User user = (User) getSession().get(User.class, id);
		getSession().delete(user);
		commit();
		close();
	}

	
	public void delete(User entity) {
		beginTransaction();
		getSession().delete(entity);
		commit();
		close();
	}

	public List<User> findAll() {
		
		return (List<User>) session.getCurrentSession().createQuery("from users").list();
	}


	public void flush() {
		// TODO Auto-generated method stub
		
	}


}
