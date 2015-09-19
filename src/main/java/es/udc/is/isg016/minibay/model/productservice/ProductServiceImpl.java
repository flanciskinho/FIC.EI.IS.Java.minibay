/**
 * 
 */
package es.udc.is.isg016.minibay.model.productservice;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg016.minibay.model.bidservice.InsufficientAmountException;
import es.udc.is.isg016.minibay.model.category.Category;
import es.udc.is.isg016.minibay.model.category.CategoryDao;
import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.product.ProductDao;
import es.udc.is.isg016.minibay.model.userprofile.UserProfile;
import es.udc.is.isg016.minibay.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private UserProfileDao userProfileDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Transactional(readOnly = true) 
	public List<Product> myProducts(long userId, int start, int size){
		return productDao.findByUser(userId,start,size);
	}
	
	@Transactional(readOnly = true)
	public int getNumberOfMyProducts(long userProfileId) {
		return productDao.getNumberOfUser(userProfileId);
	}
	
	public Product announceProduct(long userProfileId, Long categoryId, String productName, String descriptcion, String dShipment, BigDecimal initPrize, Calendar endDate)
		throws InstanceNotFoundException, AnnounceLateException, InsufficientAmountException, NotSellerException{
	
		// Primero miramos que exista el producto
		UserProfile user = userProfileDao.find(userProfileId);
		
		// Mirar si tiene rol de vendedor
		if (user.getCard() == null) {
			throw new NotSellerException(user.getLoginName());
		}
		
		// Despues miramos que exista la categoria
		Category category = categoryDao.find(categoryId);
		
		Calendar now = Calendar.getInstance();
		Calendar tmp = Calendar.getInstance();
		tmp.set(Calendar.HOUR_OF_DAY,0);
		tmp.set(Calendar.MINUTE, 0);
		tmp.set(Calendar.SECOND,0);
		tmp.set(Calendar.MILLISECOND,0);
		
		if (endDate.compareTo(tmp) < 0) {
			throw new AnnounceLateException(tmp, endDate);
		}
		
		if (initPrize.compareTo(BigDecimal.ZERO) != 1) {
			throw new InsufficientAmountException(initPrize);
		}
		
		Product product = new Product(productName, descriptcion, dShipment, now, endDate, initPrize, null, user, category, null);
		productDao.save(product);
		
		return product;
	}

	@Transactional(readOnly = true)
	public List<Product> findByNameByCategory(String keywords,Long categoryId,int start,int size){
		return productDao.findByNameByCategory(keywords, categoryId, start, size);
	}
	
	@Transactional(readOnly = true)
	public int getNumberOfNameByCategory(String keywords, Long categoryId){
		return productDao.getNumberOfNameByCategory(keywords, categoryId);
	}

	@Override
	public Product getByProductId(long productId)
			throws InstanceNotFoundException {
		
		return productDao.find(productId);
	}
}
