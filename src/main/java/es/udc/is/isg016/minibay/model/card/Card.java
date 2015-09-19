/**
 * 
 */
package es.udc.is.isg016.minibay.model.card;



import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Card {
	
	private Long number;
	private Calendar expiryDate;
	
	public Card(){}

	public Card(Long number, Calendar exDate){
		this.number = number;
		this.expiryDate = exDate;
	}
	@Id
	@Column(name = "numberId")
	public long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	@Column(name = "expiryDate")
	public Calendar getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Calendar expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public String toString() {
		return "number: "+number+"\texpiryDate: (mm-yyyy)" + expiryDate.get(Calendar.MONTH)+"-"+expiryDate.get(Calendar.YEAR);
	}
}
