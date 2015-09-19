/**
 * 
 */
package es.udc.is.isg016.minibay.model.category;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@org.hibernate.annotations.BatchSize(size = 5)
public class Category {
	
	private Long categoryId;
	private String categoryName;
	
	public Category(){}
	
	public Category(String name){
		this.categoryName = name;
	}

	
	@SequenceGenerator( // It only takes effect for
	name = "CategoryIdGenerator", // databases providing identifier
	sequenceName = "CategroySeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CategoryIdGenerator")
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String toString() {
		return "id: " + categoryId + "\tname: " + categoryName;
	}
}
