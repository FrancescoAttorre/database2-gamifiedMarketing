package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Review
 *
 */
@Entity
@Table(name = "reviews", schema = "db_gamified_marketing")
@NamedQueries({ @NamedQuery(name = "Review.findAll", query = "SELECT r FROM Review r"),
				@NamedQuery(name = "Review.findByProductID", query = "Select r FROM Review r WHERE r.product_reviewed.id = :productId"),
			})

public class Review implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne @JoinColumn(name="writer")
	private User writer;
	@ManyToOne @JoinColumn(name="product_reviewed")
	private Product product_reviewed;
	
	private String text;
	
	public Review() {
		super();
	}
	
	public int getIdreviews() {
		return id;
	}

	public void setIdreviews(int idreviews) {
		this.id = idreviews;
	}

	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}

	public Product getProduct_reviewed() {
		return product_reviewed;
	}

	public void setProduct_reviewed(Product product_reviewed) {
		this.product_reviewed = product_reviewed;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
   
}
