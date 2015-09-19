/**
 * 
 */
package es.udc.is.isg016.minibay.web.pages.products;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.tapestry5.annotations.ContentType;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.productservice.ProductService;

@ContentType("application/xml")
public class XmlListProducts {

	private final static int ROWS_PER_PAGE = 10;
	
	@Property
	private int total;
	@Property
	private int show;
	
	@Property
	private Product product;
	
	@Property
	private List<Product> list;
	
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
	
    public BigDecimal getPrice() {
    	return product.getWinBid() == null ? product.getInitPrice(): product.getCurrentPrice();
    }
    
    //http://localhost:9090/minibay/products/XmlListProducts?start=0&size=3&keywords=do+te
	void onActivate(){
        /* parameter for search event */
        String keywords = request.getParameter("keywords");

        /* parameter for start PageByPage */
        String startAsText = request.getParameter("start");        
        
        /* parameter for count PageByPage */
        String sizeAsText = request.getParameter("size");       
        
        
        Integer start = null;
        try{
        	start = Integer.parseInt(startAsText);
        }catch(Exception e){    
        	start = 0;
        }
        
        
        Integer size = null;
        try{
        	size = Integer.parseInt(sizeAsText);
        }catch(Exception e){
        	size = ROWS_PER_PAGE;
        }
        
        
        list = productService.findByNameByCategory(keywords, null, start, size);
        total = productService.getNumberOfNameByCategory(keywords, null);
        
        show = list.size();
        
    }   
}
