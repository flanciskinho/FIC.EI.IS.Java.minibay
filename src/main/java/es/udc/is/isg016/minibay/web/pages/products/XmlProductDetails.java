/**
 * 
 */
package es.udc.is.isg016.minibay.web.pages.products;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.tapestry5.annotations.ContentType;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.productservice.ProductService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@ContentType("application/xml")
public class XmlProductDetails {
	
	@Property
	private Product product;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private Request request;
	
	
	public Long getMinutesToEnd() {
		return TimeUnit.MILLISECONDS.toMinutes(
				product.getEndDate().getTimeInMillis() - Calendar.getInstance().getTimeInMillis()
				);
	}
	
    public SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    public String getEndDate() {
        return this.getDateFormat().
                format(this.product.
                        getEndDate().getTime());
    }
    
    public String getInitDate(){
    	return this.getDateFormat().
    			format(this.product.
    					getInitDate().getTime());
    }
	
    public BigDecimal getPrice() {
    	return product.getWinBid() == null ? product.getInitPrice(): product.getCurrentPrice();
    }
    
    public String getWinner(){
    	return product.getWinBid() == null ? "":product.getWinBid().getUser().getLoginName();
    }
    
    public String getWinnerId(){
    	return product.getWinBid() == null ? "":product.getWinBid().getUser().getUserProfileId()+"";
    }
    
    //http://localhost:9090/minibay/products/XmlListProducts?start=0&size=3&keywords=do+te
	void onActivate(){
		
        String productIdAsText = request.getParameter("productId");
        
        Long productId = null;
        
        try {
        	productId = Long.parseLong(productIdAsText);
			product = productService.getByProductId(productId);
		} catch (InstanceNotFoundException|NumberFormatException e) {
			product = null;
		}
        
    }   
}




