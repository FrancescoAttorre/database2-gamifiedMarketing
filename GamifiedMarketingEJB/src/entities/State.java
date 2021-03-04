package entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "state", schema = "db_gamified_marketing")
@NamedQueries({
	@NamedQuery(name = "State.findAll", query = "SELECT s FROM State s"),
	@NamedQuery(name = "State.findByQuestionnaire", query = "SELECT s FROM State s WHERE s.questionnaire.id = :questionnaireId"),
	@NamedQuery(name = "State.findByQuestionnaireAndUser", query = "SELECT s FROM State s WHERE s.questionnaire.id = :questionnaireId AND s.user.id = :userId")
})
public class State {
	@Id
	private int id;
	
	@ManyToOne @JoinColumn(name="user")
	private User user;
	
	@ManyToOne @JoinColumn(name="questionnaire")
	private Questionnaire questionnaire;
	
	
	private boolean submitted;
	private boolean cancelled;

	public State() {
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
}
