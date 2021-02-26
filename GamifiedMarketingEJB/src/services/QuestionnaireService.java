package services;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.*;

import entities.Product;
import entities.Questionnaire;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Stateless
public class QuestionnaireService {
	
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	public QuestionnaireService() {}
	
	public List<Questionnaire> findAllQuestionnaire() {
		return em.createNamedQuery("Questionnaire.findAll", Questionnaire.class)
				.setHint("javax.persistence.cache.storeMode", "REFRESH").getResultList();
	}
	
	public int createQuestionnaire(int podId, LocalDateTime date) {
		Product pod = em.find(Product.class, podId);
		Questionnaire q = new Questionnaire();
		
		q.setProduct_of_the_day(pod);
		q.setDate(date);
		
		em.persist(q);
		
		return q.getIdquestionnaires();
	}

	public Questionnaire findQuestionnaireOfTheDay() {
		Date date = new Date();
		return em.createNamedQuery("Questionnaire.findQOD",Questionnaire.class).setParameter("qsdate",date).getSingleResult();
	}
	
	public List<Questionnaire> findQuestionnaireByDate(Date date){
		return em.createNamedQuery("Questionnaire.findQuestionnaireByDate",Questionnaire.class)
				.setHint("javax.persistence.cache.storeMode", "REFRESH")
				.setParameter("date", date)
				.getResultList();
	}
	
	public void deleteQuestionnaire(int questionnaireId) {
		Questionnaire managedQ = em.find(Questionnaire.class, questionnaireId);
		em.remove(managedQ);
	}
	
	
	
	
}
