/**
 * 
 */
package es.udc.is.isg016.minibay.web.pages.user;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.productservice.ProductService;
import es.udc.is.isg016.minibay.web.util.ListUserProductsGridDataSource;
import es.udc.is.isg016.minibay.web.util.UserSession;


public class MyProducts {
	
	private final static int ROWS_PER_PAGE = 10;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private Locale locale;
	
    @SessionState(create=false)
    private UserSession userSession;

	private int start;
	
	private ListUserProductsGridDataSource listUserProductsGridDataSource;
	
	@Property
	private Product product;
	

	
	public ListUserProductsGridDataSource getListUserProductsGridDataSource() {
		return listUserProductsGridDataSource;
	}

	public int getRowsPerPage() {
		return ROWS_PER_PAGE;
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
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
	
	void onActivate() {
		
		this.listUserProductsGridDataSource = new ListUserProductsGridDataSource(productService,
				userSession.getUserProfileId());
	}
	
	
}
