package entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "questions", schema = "db_gamified_marketing")
@NamedQueries({
	@NamedQuery(name = "Question.findQuestionsOfQuestionnaire", query = "SELECT qs FROM Question qs JOIN qs.questionnaire qst WHERE qst.id = :qid")
})
public class Question implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "questionnaire")
	private Questionnaire questionnaire;
	
	private String text;
	
	public Question() {}
	
	public Question(Questionnaire questionnaire, String text) {
		this.questionnaire = questionnaire;
		this.text = text;
	}
	
	
	public int getIdquestions() {
		return id;
	}

	public void setIdquestions(int idquestions) {
		this.id = idquestions;
	}

	public int getQuestionnaire() {
		return questionnaire.getId();
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
