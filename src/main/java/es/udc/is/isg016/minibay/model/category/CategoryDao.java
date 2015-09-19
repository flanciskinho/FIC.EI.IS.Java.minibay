/**
 * 
 */
package es.udc.is.isg016.minibay.model.category;

import java.util.List;
import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface CategoryDao extends GenericDao<Category,Long> {

	/**
	 * Returns a Category by its name.
	 * @param CategoryName the name of the category 
	 * @return the Category
	 * @throws InstanceNotFoundException
	 */
	public Category findByCategoryName(String CategoryName) throws InstanceNotFoundException;
	/**
	 * Returns the name of all categories
	 * @return the name of all categories
	 */
	public List<Category> findAll();

}
