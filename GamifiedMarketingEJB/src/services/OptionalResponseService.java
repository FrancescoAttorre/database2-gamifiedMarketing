package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.OptionalResponse;

@Stateless
public class OptionalResponseService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	public OptionalResponseService() {
		
	}
	
	public OptionalResponse getResponsesOfQuestionnaireForUser(int questionnaireId, int userId){
		return em.createNamedQuery("OptionalResponse.findByQuestionnaireAndUser",OptionalResponse.class)
				.setParameter("questionnaireId",questionnaireId)
				.setParameter("userId", userId)
				.getSingleResult();
	}
}
