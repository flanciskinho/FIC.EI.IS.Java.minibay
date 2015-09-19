/**
 * 
 */
package es.udc.is.isg016.minibay.model.product;


import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface ProductDao extends GenericDao<Product, Long> {
	
	/**
	 * Return a list of products grouped by category.
	 * @param user owner of the product. 
	 * @return a list of products grouped by category.
	 * @throws InstanceNotFoundException
	 */
	public List<Product> findByUser(long user, int start, int size);
	
	/**
	 * Returns the number of products grouped by <i>Category</i>.
	 * @param user owner of the product 
	 * @return the number of products grouped by <i>Category</i>.
	 */
	public int getNumberOfUser(long user);
	
	/**
	 * Find products by keywords filtering by category.
	 * @param keywords Words used to find a product by its name 
	 * @param categoryId Category to make the filter.
	 * @return a list containing products with keyword in its name filtered by category.
	 */
	public List<Product> findByNameByCategory(String keywords, Long categoryId, int start,
			int size);
	
	/**
	 * Returns de number of products with keyword in its name belonging to a category.
	 * @param keywords Words used to find a product by its name 
	 * @param categoryId Category to make the filter.
	 * @return de number of products with keyword in its name belonging to a category.
	 */
	public int getNumberOfNameByCategory(String keywords, Long categoryId);
	
}

