package services;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.*;

import entities.OptionalResponse;
import entities.Product;
import entities.Questionnaire;
import entities.Response;
import entities.State;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Stateless
public class QuestionnaireService {

	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;

	public QuestionnaireService() {
	}

	public List<Questionnaire> findAllQuestionnaire() {
		return em.createNamedQuery("Questionnaire.findAll", Questionnaire.class)
				.setHint("javax.persistence.cache.storeMode", "REFRESH").getResultList();
	}

	public Integer createQuestionnaire(int podId, LocalDateTime date) {

		// Should not insert two questionnaire of the day for the pod.
		// as the pod is identified only when a questionnaire with that product is
		// created this issue became:

		// TODO: not insert a questionnaire if there is already one with same date.
		Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
		if(em.createNamedQuery("Questionnaire.findByDate",Questionnaire.class).setParameter("date", Date.from(instant)).getResultList().size() > 0)
			return null;
		
		
		Product pod = em.find(Product.class, podId);
		if (pod != null) {
			Questionnaire q = new Questionnaire();
			q.setProduct_of_the_day(pod);
			q.setDate(date);

			em.persist(q);

			return q.getId();
		}else
			return null;
	}

	public Questionnaire findQuestionnaireOfTheDay() {
		Date date = new Date();
		return em.createNamedQuery("Questionnaire.findByDate", Questionnaire.class).setParameter("date", date)
				.getSingleResult();
	}

	public List<Questionnaire> findQuestionnaireByDate(Date date) {
		return em.createNamedQuery("Questionnaire.findQuestionnaireByDate", Questionnaire.class)
				.setHint("javax.persistence.cache.storeMode", "REFRESH").setParameter("date", date).getResultList();
	}

	public boolean deleteQuestionnaire(int questionnaireId) {
		if(questionnaireId == em.createNamedQuery("Questionnaire.findByDate",Questionnaire.class).setParameter("date", new Date()).getSingleResult().getId()) {
			return false;
		}
		Questionnaire managedQ = em.find(Questionnaire.class, questionnaireId);
		
		for(State state : em.createNamedQuery("State.findByQuestionnaire",State.class).setParameter("questionnaireId",questionnaireId).getResultList())
			em.remove(state);
		
		List<OptionalResponse> optionals = em.createNamedQuery("OptionalResponse.findByQuestionnaire",OptionalResponse.class).setParameter("questionnaireId", questionnaireId).getResultList();
		
		for(OptionalResponse optional : optionals)
			em.remove(optional);
		
		List<Response> responses = em.createNamedQuery("Response.findByQuestionnaire",Response.class).setParameter("questionnaireId", questionnaireId).getResultList();
		
		for(Response response : responses)
			em.remove(response);
		
		//delete questionnaire and related questions
		em.remove(managedQ);
		
		return true;
		//TODO: should be deleted also state, ecc........
		
	}
	

}
