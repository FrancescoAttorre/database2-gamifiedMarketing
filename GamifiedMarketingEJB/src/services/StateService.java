package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import entities.Question;
import entities.Questionnaire;
import entities.State;
import entities.User;

@Stateless
public class StateService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;

	public StateService() {
	}

	public List<State> findAllStates() {
		return em.createNamedQuery("State.findAll", State.class).setHint("javax.persistence.cache.storeMode", "REFRESH")
				.getResultList();
	}

	public List<State> findStatesOfQuestionnaire(int questionnaireId) {
		return em.createNamedQuery("State.findByQuestionnaire", State.class)
				.setParameter("questionnaireId", questionnaireId)
				.setHint("javax.persistence.cache.storeMode", "REFRESH").getResultList();

	}

	public void insertState(int userId, int questionnaireId, boolean submitted, boolean cancelled) {
		try{
			State state = em.createNamedQuery("State.findByQuestionnaireAndUser", State.class)
				.setParameter("questionnaireId", questionnaireId)
				.setParameter("userId", userId)
				.getSingleResult();
			state.setSubmitted(submitted);
			state.setCancelled(cancelled);
			em.merge(state);
		
		}catch(NoResultException e) {
			State state = new State();
			state.setUser(em.find(User.class, userId));
			state.setQuestionnaire(em.find(Questionnaire.class,questionnaireId));
			state.setSubmitted(submitted);
			state.setCancelled(cancelled);
			em.persist(state);
		
		}
	}
}
