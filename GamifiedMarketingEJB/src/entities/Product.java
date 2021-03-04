package entities;

import java.io.Serializable;

import java.util.Base64;
import java.util.List;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Product
 *
 */
@Entity
@Table(name = "products", schema = "db_gamified_marketing")
@NamedQueries({ @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
				@NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name = :name"),
				@NamedQuery(name = "Product.findByProductID", query = "SELECT p FROM Product p WHERE p.id = :idp")
})

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	
	@Lob
	private byte[] image;
	
	public Product(int idproducts, String name) {
		super();
	}
	
	public Product() {}
	
	public int getIdproducts() {
		return id;
	}

	public void setIdproducts(int idproducts) {
		this.id = idproducts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public String getImageData() {
		return Base64.getMimeEncoder().encodeToString(image);
	}
   
}
