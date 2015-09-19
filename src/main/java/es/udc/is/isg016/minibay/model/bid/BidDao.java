/**
 * 
 */
package es.udc.is.isg016.minibay.model.bid;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface BidDao extends GenericDao<Bid,Long> {
	
	/**
	 * Returns a list of product's bids.
	 * @param prodId the identifier of the product
	 * @param start start index to show results
	 * @param size number of results to be showed
	 * @return a list of product's bids
	 */
	public List<Bid> findByProductId(long prodId, int start, int size);
	
	/**
	 * Returns the number of product's bids.
	 * @param prodId identifier of the product
	 * @return the number of product's bids
	 */
	public int getNumberOfBidByProductId(long prodId);
	
	
	public Bid findLastBidByUserProfileIdAndProductId(long userProfileId, long productId)
		throws InstanceNotFoundException;
	
	/**
	 * 
	 * @param userProfileId
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Bid> findByUserProfileId(long userProfileId, int start, int size);

	
	/**
	 * 
	 * @param userProfileId
	 * @return
	 */
	public int getNumberOfBidByUserProfileId(long userProfileId);
}
