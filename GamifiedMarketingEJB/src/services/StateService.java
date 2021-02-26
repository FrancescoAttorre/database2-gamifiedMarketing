package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.Question;
import entities.State;

@Stateless
public class StateService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	public StateService() {}
	
	public List<State> findAllStates(){
		return em.createNamedQuery("State.findAll", State.class)
				.setHint("javax.persistence.cache.storeMode", "REFRESH")
				.getResultList();
	}
}
