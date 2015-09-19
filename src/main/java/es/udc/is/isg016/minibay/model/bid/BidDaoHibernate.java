/**
 * 
 */
package es.udc.is.isg016.minibay.model.bid;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


@Repository("bidDao")
public class BidDaoHibernate extends GenericDaoHibernate<Bid,Long> implements BidDao{

	
	public Bid findLastBidByUserProfileIdAndProductId(long userProfileId, long productId)
			throws InstanceNotFoundException {
		
		Bid bid = (Bid) getSession().createQuery(
				"SELECT b " +
				"FROM Bid b " +
				"WHERE b.product.productId = :prodId " +
				"  AND b.user.userProfileId = :userId " +
				"ORDER BY b.maxPrice DESC ")
				.setParameter("prodId", productId)
				.setParameter("userId", userProfileId)
				.setFirstResult(0)
				.setMaxResults(1)
				.uniqueResult();
		
		if (bid == null)
			throw new InstanceNotFoundException(userProfileId, Bid.class.getName());
		
		return bid;
	}
	
	
	public List<Bid> findByProductId(long prodId, int start, int size) {
	  	return getSession().createQuery(
    			"SELECT b " +
    			"FROM Bid b " +
    			"WHERE b.product.productId = :prodId " +
    			"ORDER BY b.maxPrice")
    			.setParameter("prodId", prodId)
    			.setFirstResult(start)
    			.setMaxResults(size)
    			.list();
	}

	
	public int getNumberOfBidByProductId(long prodId) {
		Long number = (Long) getSession().createQuery(
    			"SELECT COUNT(b) FROM Bid b WHERE" +
    			"b.product.productId = :prodId")
    			.setParameter("prodId", prodId)
    			.uniqueResult();
		
		return number != null? number.intValue(): 0;
	}

	public List<Bid> findByUserProfileId(long userProfileId, int start, int size) {
		
		return getSession().createQuery(
				"SELECT b " +
				"FROM Bid b INNER JOIN b.product p1 " +
				"WHERE b.user.userProfileId = :userId " +
				"AND b.bidDate = (SELECT MAX(b2.bidDate) " +
				"				 FROM Bid b2 " +
				"				 WHERE b2.product.productId = b.product.productId " +
				"				 AND b2.user.userProfileId = :userId) " +
				"ORDER BY p1.endDate DESC"
			)
			.setParameter("userId", userProfileId)
			.setFirstResult(start)
			.setMaxResults(size)
			.list();
				
	}
	
	public int getNumberOfBidByUserProfileId(long userProfileId) {
		Long number = (Long) getSession().createQuery(
				"SELECT count(b) " +
				"FROM Bid b INNER JOIN b.product p1 " +
				"WHERE b.user.userProfileId = :userId " +
				"AND b.bidDate = (SELECT MAX(b2.bidDate) " +
				"				 FROM Bid b2 " +
				"				 WHERE b2.product.productId = b.product.productId " +
				"				 AND b2.user.userProfileId = :userId) " +
				"ORDER BY p1.endDate DESC"
				)
				.setParameter("userId", userProfileId)
				.uniqueResult();
		return number != null? number.intValue(): 0;
	}

}
