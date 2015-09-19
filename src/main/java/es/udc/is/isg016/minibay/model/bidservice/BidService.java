package es.udc.is.isg016.minibay.model.bidservice;

import java.math.BigDecimal;
import java.util.List;

import es.udc.is.isg016.minibay.model.bid.Bid;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 *
 *
 */
public interface BidService {
    public static final BigDecimal INCREMENT = new BigDecimal("0.5");
	
	public List<Bid> findByUserProfileId(long userProfileId, int start, int size);
    
    public int getNumberOfBidByUserProfileId(long userProfileId);
    	
	
	public Bid bidding(long userProfileId, long productId, BigDecimal amount) 
			throws InstanceNotFoundException, InsufficientAmountException, BiddingLateException;
	
	
}
