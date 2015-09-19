
package es.udc.is.isg016.minibay.model.productservice;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import es.udc.is.isg016.minibay.model.bidservice.InsufficientAmountException;
import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface ProductService {
	
	public Product announceProduct(long userProfileId, Long categoryId, String productName, String descriptcion, 
			String dShipment, BigDecimal initPrize, Calendar endDate)
			throws InstanceNotFoundException, AnnounceLateException, InsufficientAmountException, NotSellerException;
	
	public List<Product> myProducts(long userId, int start, int size);
	
	public int getNumberOfMyProducts(long userProfileId);
	
	public List<Product> findByNameByCategory(String keywords,Long categoryId,int start,int size);

	public int getNumberOfNameByCategory(String keywords, Long categoryId);
	
	public Product getByProductId(long productId)
		throws InstanceNotFoundException;
}
