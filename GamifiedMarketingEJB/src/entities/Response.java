package entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "responses", schema = "db_gamified_marketing")
@NamedQueries({
	@NamedQuery(name = "Response.findAllForUser", query = "Select r FROM Response r WHERE r.writer.id = :user"),
	@NamedQuery(
			name = "Response.findByQuestionnaire", 
			query = "SELECT r FROM Response r JOIN r.question q WHERE q.questionnaire.id = :questionnaireId")
})
public class Response implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne @JoinColumn(name="writer")
	private User writer;
	
	@ManyToOne @JoinColumn(name="question")
	private Question question;
	
	private String response;
	
	public Response() {
	}
	
	public Response(User writer,Question question,String response) {
		this.writer=writer;
		this.question=question;
		this.response=response;
		
	}
	public int getId() {
		return id;
	}

	public void setId(int idresponses) {
		this.id = idresponses;
	}

	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
