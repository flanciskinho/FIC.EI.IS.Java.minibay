/**
 * 
 */
package es.udc.is.isg016.minibay.model.bid;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.userprofile.UserProfile;

@Entity
public class Bid {
	
	private long bidId;
	private UserProfile user;
	private Product product;
	private BigDecimal maxPrice;
	private Calendar bidDate;
	
	public Bid(){}
	
	public Bid(UserProfile u, Product p, BigDecimal maxPrice, Calendar bidDate){
		this.user = u;
		this.product = p;
		this.maxPrice = maxPrice;
		this.bidDate = bidDate;
	}

	@SequenceGenerator( // It only takes effect for
	name = "BidIdGenerator", // databases providing identifier
	sequenceName = "BidSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "BidIdGenerator")
	public Long getBidId() {
		return bidId;
	}

	public void setBidId(long bidId) {
		this.bidId = bidId;
	}
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="owner")
	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}
	
	@ManyToOne(optional=false)
	@JoinColumn(name="product")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Calendar getBidDate() {
		return bidDate;
	}

	public void setBidDate(Calendar bidDate) {
		this.bidDate = bidDate;
	}
	
	public String toString() {
		String d1 = bidDate.get(Calendar.YEAR)+"-"+bidDate.get(Calendar.MONTH)+"-"+bidDate.get(Calendar.DAY_OF_MONTH);
		String d2 = bidDate.get(Calendar.HOUR)+":"+bidDate.get(Calendar.MINUTE)+":"+bidDate.get(Calendar.SECOND)+" "+bidDate.get(Calendar.MILLISECOND);
		return  "id: " + bidId +
				"\tuser: " + user.getUserProfileId() +
				"\tproduct: " + product.getProductId() +
				"\tmaxPrice: " + maxPrice +
				"\tbidDate: " + d1 + " " +d2;
	}
	
}
