/**
 * 
 */
package es.udc.is.isg016.minibay.model.product;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import es.udc.is.isg016.minibay.model.bid.Bid;
import es.udc.is.isg016.minibay.model.category.Category;
import es.udc.is.isg016.minibay.model.userprofile.UserProfile;


@Entity
public class Product {

	private Long productId;
	private String productName;
	private Calendar initDate;
	private Calendar endDate;
	private String description;
	private BigDecimal initPrice;
	private String shipmentDescription;
	private BigDecimal currentPrice;
	private UserProfile owner;
	private Category category;
	private Bid winBid;
	private long version;
	
	public Product(){}
	
	public Product(String productName, String description, String dShipment, Calendar iDate,
		Calendar eDate, BigDecimal iPrice, BigDecimal cPrice, UserProfile owner, Category cat, Bid wBid){
		
		this.productName = productName;
		this.description = description;
		this.shipmentDescription = dShipment;
		this.initDate = iDate;
		this.endDate = eDate;
		this.initPrice = iPrice;
		this.currentPrice = cPrice;
		this.owner =  owner;
		this.category = cat;
		this.winBid = wBid;
	}

	
	@SequenceGenerator( // It only takes effect for
	name = "ProductIdGenerator", // databases providing identifier
	sequenceName = "ProductSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ProductIdGenerator")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Calendar getInitDate() {
		return initDate;
	}

	public void setInitDate(Calendar initDate) {
		this.initDate = initDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(BigDecimal initPrice) {
		this.initPrice = initPrice;
	}

	public String getShipmentDescription() {
		return shipmentDescription;
	}

	public void setShipmentDescription(String shipmentDescription) {
		this.shipmentDescription = shipmentDescription;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	@ManyToOne(optional=false)
	@JoinColumn(name="owner")
	public UserProfile getOwner() {
		return owner;
	}

	public void setOwner(UserProfile owner) {
		this.owner = owner;
	}
	@ManyToOne(optional=false)
	@JoinColumn(name="category")
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="winBid")
	public Bid getWinBid() {
		return winBid;
	}

	public void setWinBid(Bid windBid) {
		this.winBid = windBid;
	}
	
	@Version
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}
	
	public String toString() {
		String d1, d2;
		
		d1 = initDate.get(Calendar.YEAR)+"-"+initDate.get(Calendar.MONTH)+"-"+initDate.get(Calendar.DAY_OF_MONTH);
		d2 = initDate.get(Calendar.HOUR)+":"+initDate.get(Calendar.MINUTE)+":"+initDate.get(Calendar.SECOND)+" "+initDate.get(Calendar.MILLISECOND);
		String init = d1 + " " + d2;
		
		d1 = endDate.get(Calendar.YEAR)+"-"+endDate.get(Calendar.MONTH)+"-"+endDate.get(Calendar.DAY_OF_MONTH);
		d2 = endDate.get(Calendar.HOUR)+":"+endDate.get(Calendar.MINUTE)+":"+endDate.get(Calendar.SECOND)+" "+endDate.get(Calendar.MILLISECOND);
		String end = d1 + " " + d2;
		
		return  "id: " + productId +
				"\tname:" + productName +
				"\tdesc: " + description +
				"\tshipmentDesc: " + shipmentDescription +
				"\tinitPrice: " + initPrice +
				"\tcurrentPrice: " + currentPrice == null ? "null" : currentPrice +
				"\towner: " + owner.getUserProfileId() +
				"\tcategory: " + category.getCategoryId() + 
				"\twinBid: " + winBid == null ? "null" : winBid.getBidId() +
				"\tinitDate: " + init +
				"\tendDate: " + end;
	}
	
}
