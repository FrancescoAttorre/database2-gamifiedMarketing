package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import exceptions.QueryException;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.joda.time.LocalDateTime;

import entities.Product;
import entities.Questionnaire;
import entities.Review;
import exceptions.CredentialsException;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	protected EntityManager em;
	
	public ProductService() {
    }
	
	public ProductService(EntityManager em) {
        this.em = em;
    }
	public Product findByProductID(int productID){
		System.out.println("Calling em.createNamedQuery...");
	
		return em.find(Product.class, productID);
	}
	
	public List<Product> findAllProductIds(){
		
		return em.createNamedQuery("Product.findAll",Product.class)
				.setHint("javax.persistence.cache.storeMode", "REFRESH")
				.getResultList();
	}
	
	public Product findProductOfTheDay() {
		LocalDateTime today = LocalDateTime.now();
		
		List<Questionnaire> topQuestionnaires = em.createNamedQuery("Questionnaire.findAll",Questionnaire.class).getResultList();
		List<Questionnaire> questionnaireOfTheDay = new ArrayList();
		
		for(Questionnaire q : topQuestionnaires) {
			if(q.getDate().getDayOfYear() == today.getDayOfYear() && q.getDate().getYear() == today.getYear()) {
				questionnaireOfTheDay.add(q);
			}
		}
		
		if(questionnaireOfTheDay.size() == 0) {
			//not found
			return null;
		}else if(questionnaireOfTheDay.size() > 1){
			//more than one questionnaire of the day error
			return null;			
		}else {
			return questionnaireOfTheDay.get(0).getProduct_of_the_day();
		}
		
	}
}
