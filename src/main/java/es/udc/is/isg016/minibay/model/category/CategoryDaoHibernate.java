/**
 * 
 */
package es.udc.is.isg016.minibay.model.category;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Repository("categoryDao")
public class CategoryDaoHibernate extends GenericDaoHibernate<Category,Long>
	implements CategoryDao{

	public Category findByCategoryName(String categoryName) throws InstanceNotFoundException {
    	Category category = (Category) getSession().createQuery(
    			"SELECT c FROM Category c WHERE upper(c.categoryName) = :categoryName")
    			.setParameter("categoryName", categoryName.toUpperCase())
    			.uniqueResult();
    	if (category == null) {
   			throw new InstanceNotFoundException(categoryName, Category.class.getName());
    	} else {
    		return category;
    	}
	}

	public List<Category> findAll(){
    	return getSession().createQuery(
    			"SELECT c " +
    			"FROM Category c")
    			.list();
	}

}
