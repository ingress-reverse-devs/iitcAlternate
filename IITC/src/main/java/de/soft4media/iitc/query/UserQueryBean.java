package de.soft4media.iitc.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import de.soft4media.iitc.db.User;
import de.soft4media.iitc.util.HibernateUtil;


@ManagedBean(name="UserQuery")
@RequestScoped
public class UserQueryBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public User getUser(int id)
	{
		User user = new User();
		
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		 
        session.beginTransaction();

        
        Criteria crit = session.createCriteria(User.class);
        
        crit.add(Restrictions.like("id", id));
        
        user = (User) crit.list().get(0);
        
     
        tx.commit();
        session.close();
		
		return user;
	}
	
	
	public List<User> getAllUser()
	{
		List<User> user = new ArrayList<User>();
		
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.getTransaction();
		
		
		try 
		{
			tx.begin();

	        
	        Criteria crit = session.createCriteria(User.class);
	        
	        user = crit.list();
	        
	     
	        tx.commit();
		}
		catch (RuntimeException e) {
		    tx.rollback();
		    throw e;
		}		
        
        session.close();
		
		return user;
	}
}
