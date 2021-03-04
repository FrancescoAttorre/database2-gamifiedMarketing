package services;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.Access;
import entities.User;

@Stateless
public class AccessService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	protected EntityManager em;
	
	public AccessService() {
		
	}
	
	public void insertNewAccess(int userId) {
		Access access = new Access();
		access.setDate(new Date());
		access.setUser(em.find(User.class, userId));
		em.persist(access);
	}
	
}
