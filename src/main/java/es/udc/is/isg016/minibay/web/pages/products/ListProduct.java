/**
 * 
 */
package es.udc.is.isg016.minibay.web.pages.products;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.productservice.ProductService;
import es.udc.is.isg016.minibay.web.util.ListProductGridDataSource;

public class ListProduct {
	
	private final static int ROWS_PER_PAGE = 10;
	
	@Inject
	private ProductService productService;
	
	private String keywords;
	private Long   idCategory;
	private int    start;
	
	private ListProductGridDataSource listProductGridDataSource;
	
	@Property
	private Product product;
	
	@Inject
	private Locale locale;
	
	public ListProductGridDataSource getListProductGridDataSource() {
		return listProductGridDataSource;
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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(Long idCategory) {
		this.idCategory = idCategory;
	}
	
	Object onPassivate() {
		return new Object[] {idCategory, keywords};
	}
	
	
	void onActivate(Long idCategory, String keywords) {
		
		this.keywords   = keywords;
		this.idCategory = idCategory;
		
		this.listProductGridDataSource = new ListProductGridDataSource(productService, keywords, idCategory);
	}
	
	
}
