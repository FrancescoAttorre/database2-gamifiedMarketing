package entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "access", schema = "db_gamified_marketing")
//@NamedQuery(name = "Response.findResponseOfQuestions", query = "SELECT r FROM Response r WHERE r.question IN :questions")
@NamedQueries({
	@NamedQuery(name = "Access.findByUser", query = "SELECT a FROM Access a WHERE a.user.id = :userId"),	
})
public class Access {

	private static final long serialVersionUID = 1L;
		
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	@ManyToOne @JoinColumn(name="user")
	private User user;
	
	@Temporal(TemporalType.DATE)
	private Date date;


	public Access() {
	
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}	
	
	
}