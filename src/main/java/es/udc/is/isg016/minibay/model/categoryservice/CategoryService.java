/**
 * 
 */
package es.udc.is.isg016.minibay.model.categoryservice;

import java.util.List;


import es.udc.is.isg016.minibay.model.category.Category;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


public interface CategoryService {

	public List<Category> listCategories();

}
