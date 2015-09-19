/**
 * 
 */
package es.udc.is.isg016.minibay.web.pages.products;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.productservice.ProductService;
import es.udc.is.isg016.minibay.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * @author flanciskinho
 *
 */
public class ProductDetails {

	@Inject
	private ProductService productService;
	
	private long productId;
	
	@Property
	private Product product;
	
	@Inject
    private Locale locale;
	
    @Property
    @SessionState(create=false)
    private UserSession userSession;
	
    public Long getMinutesToEnd() {
		return TimeUnit.MILLISECONDS.toMinutes(
				product.getEndDate().getTimeInMillis() - Calendar.getInstance().getTimeInMillis()
				);
	}
    

    public void setProductId(long id) {
    	this.productId = id;
    }
    
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}

	public DateFormat getTimeFormat() {
		return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
	}

	public Format getNumberFormat() {
		return NumberFormat.getInstance(locale);
	}
	
	public boolean getWinnerBid() {
		try {
			return userSession.getUserProfileId().equals(product.getWinBid().getUser().getUserProfileId());
		} catch (NullPointerException npe) {
			return false;
		}
	}
	
	public boolean getCanStillBid() {
		if (product != null)
			if (Calendar.getInstance().compareTo(product.getEndDate()) == -1)
				return true;
		
		return false;
	}
	
	Object onPassivate() {
        return productId;
    }
	
    void onActivate(String productId) {
    	try {
    		this.productId = new Long(productId);
			product = productService.getByProductId(this.productId);
		} catch (InstanceNotFoundException|NumberFormatException e) {
			product = null;
		}
    }
    
}
