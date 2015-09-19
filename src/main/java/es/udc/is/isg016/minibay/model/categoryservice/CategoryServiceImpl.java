/**
 * 
 */
package es.udc.is.isg016.minibay.model.categoryservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg016.minibay.model.category.Category;
import es.udc.is.isg016.minibay.model.category.CategoryDao;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("categoryService")
@Transactional
public class CategoryServiceImpl implements CategoryService{
	@Autowired
	private CategoryDao categoryDao;
	
	@Transactional(readOnly = true)
	public List<Category> listCategories() {
		
		return categoryDao.findAll();
	}

}
