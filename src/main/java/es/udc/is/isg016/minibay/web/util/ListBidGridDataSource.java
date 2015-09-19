package es.udc.is.isg016.minibay.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.is.isg016.minibay.model.bid.Bid;
import es.udc.is.isg016.minibay.model.bidservice.BidService;

public class ListBidGridDataSource implements GridDataSource{

	// Para almacenar informacion	
	private List<Bid> bids;
	private int startIndex;
	public long userProfileId;
	
	// Informacion para pasar al constructor necesaria para usar por los servicios necesarios
	private BidService bidService;

	
	
	public ListBidGridDataSource(BidService bidService, long userProfileId) {
		this.bidService = bidService;
		this.userProfileId = userProfileId;

	}
	
	@Override
	public int getAvailableRows() {
		return bidService.getNumberOfBidByUserProfileId(userProfileId);
	}
	
	@Override
	public Class<Bid> getRowType() {
		return Bid.class;
	}

	@Override
	public Object getRowValue(int index) {
		return bids.get(index-this.startIndex);
	}

	@Override
	public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
		bids = bidService.findByUserProfileId(userProfileId, startIndex, endIndex-startIndex+1);
		this.startIndex = startIndex;
	}

	public boolean getProductsNotFound() {
    	return getAvailableRows() == 0;
    }
	
}