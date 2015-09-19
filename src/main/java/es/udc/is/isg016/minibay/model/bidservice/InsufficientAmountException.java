/**
 * 
 */
package es.udc.is.isg016.minibay.model.bidservice;

import java.math.BigDecimal;

/**
 * @author flanciskinho
 *
 */
@SuppressWarnings("serial")
public class InsufficientAmountException extends Exception {
	private String productName;
	private BigDecimal amount, minimum;
	
	public InsufficientAmountException(String productName, BigDecimal amount, BigDecimal minimum) {
		super("Indufficient amount exception => productName = '" + productName + "' amount = " + amount + " minimun = "+ minimum);
		
		this.productName = productName;
		this.amount = amount;
		this.minimum = minimum;
	}
	
	public InsufficientAmountException(BigDecimal amount) {
		super("Indufficient amount exception => amount = " + amount);
		
		this.productName = null;
		this.amount = amount;
	}
	
	public BigDecimal getMinimum() {
		return minimum;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
}
