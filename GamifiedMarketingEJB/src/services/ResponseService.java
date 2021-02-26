package services;

import entities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ResponseService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;

	public ResponseService() {
	}

	public void createResponse(int writerId, int questionId, String text) {
		User writer = em.find(User.class, writerId);
		Question question = em.find(Question.class, questionId);
		Response response = new Response();
		response.setQuestion(question);
		response.setWriter(writer);
		response.setResponse(text);
		em.persist(response);
	}

	public List<Response> retrieveResponsesOfQuestionnaire(int writerId, int questionnaireId) {
		List<Question> questions = null;
		
		questions =em.createNamedQuery("Question.findQuestionsOfQuestionnaire", Question.class)
				.setHint("javax.persistence.cache.storeMode", "REFRESH")
				.setParameter("qid", questionnaireId).getResultList();
		
		List<Response> responses =  em.createNamedQuery("Response.findAllForUser", Response.class)
				.setParameter("user", em.find(User.class, writerId))
				.getResultList();
		
		List<Response> retList = new ArrayList<Response>();
		
		for (Response r : responses) {
			for(Question q : questions) {
				if (r.getQuestion().getIdquestions() == q.getIdquestions())
					retList.add(r);
			}
		}
		
		return retList;
	}

}
