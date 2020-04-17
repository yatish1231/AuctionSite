package com.ypitta.auctionsite.repository;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ypitta.auctionsite.model.User;

@Repository
@Transactional
public class UserRepository implements UserRepositoryInterface{
	
	@Autowired
	SessionFactory session;
	
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
	
	public User findOne(String id) {
		User user = null;	
		_LOGGER.info("Fetching user - findOne");
		beginTransaction();
		user = (User)getSession().get(User.class, id);
		if(user != null) {
			_LOGGER.info("User found! Refreshing entity");
			getSession().refresh(user);
			}
		commit();
		close();
		return user;
	}

	
	public boolean exists(String id) {
		return true;
		
	}
	
	public boolean save(User user) {
		_LOGGER.info("saving user....");
		beginTransaction();
		getSession().saveOrUpdate(user);
		_LOGGER.info("new user saved: username" + user.getUsername());
		commit();
		close();
		return true;
	}
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public void delete(String id) {
		_LOGGER.info("Deleting user by Id: " + id);
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



}
