package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entities.Question;
import entities.Questionnaire;

@Stateless
public class QuestionService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;
	
	public QuestionService() {}
	
	public List<Question> findQuestionsOfQuestionnaire(int questionnaireId){
		return em.createNamedQuery("Question.findQuestionsOfQuestionnaire", Question.class)
			.setHint("javax.persistence.cache.storeMode", "REFRESH")
			.setParameter("qid", questionnaireId)
			.getResultList();
	}
	
	public void createQuestion(int questionnaireId, String questionText) {
		Questionnaire questionnaire = em.find(Questionnaire.class, questionnaireId);
		Question question = new Question();
		
		question.setQuestionnaire(questionnaire);
		question.setText(questionText);
		
		em.persist(question);
	}
}
