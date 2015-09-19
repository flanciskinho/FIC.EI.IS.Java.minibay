/**
 * 
 */
package es.udc.is.isg016.minibay.test.model.categoryservice;

import static es.udc.is.isg016.minibay.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.is.isg016.minibay.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg016.minibay.model.category.Category;
import es.udc.is.isg016.minibay.model.category.CategoryDao;
import es.udc.is.isg016.minibay.model.categoryservice.CategoryService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * @author flanciskinho
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class UserCategoryService {

	private final long NON_EXISTENT_CATEGORY_ID = -1;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private CategoryDao categoryDao;
    
    
	private Category addCategoryByName(String name)
			throws DuplicateInstanceException{
		Category cat = new Category(name);
		try {
			categoryDao.save(cat);
		} catch (DataIntegrityViolationException dive) {
			throw new DuplicateInstanceException(name, Category.class.getName());
		}
		return cat;
	}
    
    

    @Test(expected = InstanceNotFoundException.class)
    public void testFindNonExistentCategory()
    		throws InstanceNotFoundException {
    	categoryDao.find(NON_EXISTENT_CATEGORY_ID);
    }
    
    @Test
    public void testRegisterAndFindCategory()
    		throws DuplicateInstanceException, InstanceNotFoundException{
    	
    	Category category  = addCategoryByName("Electrónica");
    	Category category2 = categoryDao.find(category.getCategoryId()); 
        
        assertEquals(category, category2);
    }
    
    @Test (expected = DuplicateInstanceException.class)
    public void testDuplicateCategory()
    		throws DuplicateInstanceException {
    	
    	String name = "Plantas";
    	addCategoryByName(name);
    	addCategoryByName(name);
    }
    
    @Test
    public void testNull()
    		throws DuplicateInstanceException {
    	
    	Category cat = null;
    	try {
    		cat = addCategoryByName(null);
    	} catch (Exception e) {}
    	
    	assertTrue(cat == null);
    		
    }
    
    
    @Test
    public void testGetListCategories()
    		throws DuplicateInstanceException {
    	
    	ArrayList<Category> list1 = new ArrayList<Category>();
    	list1.add(addCategoryByName("Electrónica"));
    	list1.add(addCategoryByName("Plantas"));
    	list1.add(addCategoryByName("Viajes"));
    	list1.add(addCategoryByName("Ropa"));
    	
    	List<Category> list2 = categoryService.listCategories();
    	
    	assertTrue(list1.size() == list2.size());
    	
    	for (int i = 0; i < list1.size(); i++) {
    		assertTrue(list2.contains(list1.get(i)));
    		assertTrue(list1.contains(list2.get(i)));
    	}
    		
    }
    
}
