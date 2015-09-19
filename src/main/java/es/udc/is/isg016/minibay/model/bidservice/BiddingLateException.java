/**
 * 
 */
package es.udc.is.isg016.minibay.model.bidservice;

import java.util.Calendar;

/**
 * @author flanciskinho
 *
 */
@SuppressWarnings("serial")
public class BiddingLateException extends Exception {
	private String productName;
	private Calendar moment;
	private Calendar limit;
	
	public BiddingLateException(String productName, Calendar limit, Calendar moment) {
		super("bidding late exception => productName = '" + productName + "'" + 
				" limit = " +
				limit.get(Calendar.HOUR) + ":" + limit.get(Calendar.MINUTE) + ":" + limit.get(Calendar.SECOND) +
				" " +
				limit.get(Calendar.DAY_OF_MONTH) + "/" + limit.get(Calendar.MONTH) + "/" + limit.get(Calendar.YEAR) +
				" moment = " + 
				moment.get(Calendar.HOUR) + ":" + moment.get(Calendar.MINUTE) + ":" + moment.get(Calendar.SECOND) +
				" " +
				moment.get(Calendar.DAY_OF_MONTH) + "/" + moment.get(Calendar.MONTH) + "/" + moment.get(Calendar.YEAR));
		
		this.productName = productName;
		this.moment = moment;
		this.limit = limit;
	}
	
	public Calendar getLimit() {
		return this.limit;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public Calendar getMoment() {
		return moment;
	}
}
