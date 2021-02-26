package entities;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "questionnaires", schema = "db_gamified_marketing")
@NamedQueries({
	@NamedQuery(name = "Questionnaire.findByDate", query = "SELECT q FROM Questionnaire q WHERE q.date= :d "),
	@NamedQuery(name = "Questionnaire.findAll", query = "SELECT q FROM Questionnaire q "),
	@NamedQuery(name = "Questionnaire.findQOD", query = "SELECT q FROM Questionnaire q WHERE q.date= :qsdate")
})
public class Questionnaire implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "product_of_the_day")
	private Product product_of_the_day;
	
	@OneToMany(mappedBy="questionnaire")
	private List<Question> questions;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	public Questionnaire() {
		
	}
	
	
	public Questionnaire(Product pod,LocalDateTime date) {
		this.product_of_the_day = pod;
		Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
		this.date = Date.from(instant);
	}
	
	public List<Question> getQuestions() {
		return questions;
	}
	
	public int getAmountOfQuestions() {
		return questions.size();
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Product getProduct_of_the_day() {
		return product_of_the_day;
	}

	public void setProduct_of_the_day(Product product_of_the_day) {
		this.product_of_the_day = product_of_the_day;
	}

	public void setIdquestionnaires(int idquestionnaires) {
		this.id = idquestionnaires;
	}

	public void setDate(LocalDateTime date) {
		Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
		this.date = Date.from(instant);	
	}

	public int getIdquestionnaires() {
		return id;
	}

	public LocalDateTime getDate() {
		Instant instant = date.toInstant();
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}
}
