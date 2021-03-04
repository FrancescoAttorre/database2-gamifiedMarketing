package entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "optional_answers", schema = "db_gamified_marketing")
//@NamedQuery(name = "Response.findResponseOfQuestions", query = "SELECT r FROM Response r WHERE r.question IN :questions")
@NamedQueries({
	@NamedQuery(name = "OptionalResponse.findByUser", query = "SELECT r FROM OptionalResponse r WHERE r.writer = :writerId"),	
	@NamedQuery(name = "OptionalResponse.findByQuestionnaireAndUser", query = "SELECT r FROM OptionalResponse r WHERE r.questionnaire.id = :questionnaireId AND r.writer.id = :userId"),
	@NamedQuery(name = "OptionalResponse.findByQuestionnaire", query = "SELECT r FROM OptionalResponse r WHERE r.questionnaire.id = :questionnaireId")
})
public class OptionalResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne @JoinColumn(name="writer")
	private User writer;
	
	@OneToOne @JoinColumn(name="questionnaire")
	private Questionnaire questionnaire;
	
	private int age;
	
	private char sex;
	
	private char expertise;

	
	
	
	
	public OptionalResponse() {
	
	}
	
	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public char getExpertise() {
		return expertise;
	}

	public void setExpertise(char expertise) {
		this.expertise = expertise;
	}
	
	
	
	
}