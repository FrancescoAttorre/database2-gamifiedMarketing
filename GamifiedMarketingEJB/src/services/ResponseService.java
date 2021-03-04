package services;

import entities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class ResponseService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;

	public ResponseService() {
	}
	
	/*
	public void createResponse(int writerId, int questionId, String text) {
		User writer = em.find(User.class, writerId);
		Question question = em.find(Question.class, questionId);
		Response response = new Response();
		response.setQuestion(question);
		response.setWriter(writer);
		response.setResponse(text);
		em.persist(response);
	}
	*/
	
	public void insertResponses(int writerId, Map<Integer,String> mandatoryResponses, Integer age, char sex, char expertise) {
		
				
		for(Integer questionId : mandatoryResponses.keySet()) {
			User writer = em.find(User.class, writerId);
			Question question = em.find(Question.class, questionId);
			Response response = new Response();
			response.setWriter(writer);
			response.setQuestion(question);
			response.setResponse(mandatoryResponses.get(questionId));
			em.persist(response);
		}
		
		OptionalResponse optionalResponse = new OptionalResponse();
		User writer = em.find(User.class, writerId);
		Questionnaire questionnaire = em.createNamedQuery("Questionnaire.findByDate",Questionnaire.class).setParameter("date", new Date()).getSingleResult();
		optionalResponse.setWriter(writer);
		optionalResponse.setQuestionnaire(questionnaire);
		
		//should set to null
		if(age != null)
			optionalResponse.setAge(age.intValue());
		if(expertise != '\u0000')
			optionalResponse.setExpertise(expertise);
		if(sex != '\u0000')
			optionalResponse.setSex(sex);
		em.persist(optionalResponse);
		
		
		
		//TODO: 
		//if has already submitted questionnaire should never arrive here.
				
		//if has already a state with cancelled should update state
		try {		
			State state = em.createNamedQuery("State.findByQuestionnaireAndUser",State.class)
				.setParameter("questionnaireId", questionnaire.getId())
				.setParameter("userId", writer.getId())
				.getSingleResult();

			state.setCancelled(false);
			state.setSubmitted(true);
			em.merge(state);			
			
		}catch(NoResultException e) {
			State newState = new State();
			newState.setSubmitted(true);
			newState.setCancelled(false);
			newState.setQuestionnaire(questionnaire);
			newState.setUser(writer);
			em.persist(newState);
		}
	}
	

	public List<Response> retrieveResponsesOfQuestionnaire(int writerId, int questionnaireId) {
		List<Question> questions = null;
		
		questions =em.createNamedQuery("Question.findQuestionsOfQuestionnaire", Question.class)
				.setHint("javax.persistence.cache.storeMode", "REFRESH")
				.setParameter("qid", questionnaireId).getResultList();
		
		List<Response> responses =  em.createNamedQuery("Response.findAllForUser", Response.class)
				.setParameter("user", writerId)
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
