package es.udc.is.isg016.minibay.web.components;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg016.minibay.model.category.Category;
import es.udc.is.isg016.minibay.model.categoryservice.CategoryService;
import es.udc.is.isg016.minibay.web.pages.products.ListProduct;

public class SearchBar {
    
    // Parameters
    
    @Component
	private Form findProducts;
    
    @Property
	private String category;
	
    @Property
	private String keywordsValue;
    
    @Property
	private Long selectValue;
    
    @Property
    private String selectCategory;
    
	@Inject
	private CategoryService categoryService;
	
	@InjectPage
	private ListProduct listProduct;
	
	// Se llama aqui antes de crear el formulario
	void onPrepareForRender() {	
		List<Category> list = categoryService.listCategories();
		if (!list.isEmpty()) {
			category="";
			Category c;
			int i;
			for (i = 0; i < list.size() -1; i++) {
				c = list.get(i);
				category = category + c.getCategoryId() + "=" + c.getCategoryName() + ",";
			}
			c = list.get(i);
			category = category + c.getCategoryId() + "=" + c.getCategoryName();
		}
		
	}

    /*
    void onValidateFormFindProducts(){
		if (!findProducts.isValid())
			return;
	}
    */

    // Cuando se pulsa el submit
    Object onSuccessFromfindProducts() {
    	listProduct.setIdCategory(selectValue);
    	listProduct.setKeywords(keywordsValue);
    	
   		return listProduct;
    	
    }
}
