/**
 * 
 */
package es.udc.is.isg016.minibay.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.productservice.ProductService;

public class ListProductGridDataSource implements GridDataSource {

	// Para almacenar informacion	
	private List<Product> products;
	private int startIndex;
	
	// Informacion para pasar al constructor necesaria para usar por los servicios necesarios
	private ProductService productService;
	private String keywords;
	private Long categoryId;
	
	
	public ListProductGridDataSource(ProductService productService, String keywords, Long categoryId) {
		this.productService = productService;
		this.keywords = keywords;
		this.categoryId = categoryId;
	}
	
	@Override
	public int getAvailableRows() {
		return productService.getNumberOfNameByCategory(keywords, categoryId);
	}

	@Override
	public Class<Product> getRowType() {
		return Product.class;
	}

	@Override
	public Object getRowValue(int index) {
		return products.get(index-this.startIndex);
	}

	@Override
	public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
		products = productService.findByNameByCategory(keywords, categoryId, startIndex, endIndex-startIndex+1);
		this.startIndex = startIndex;
	}

	public boolean getProductsNotFound() {
    	return getAvailableRows() == 0;
    }
	
}
