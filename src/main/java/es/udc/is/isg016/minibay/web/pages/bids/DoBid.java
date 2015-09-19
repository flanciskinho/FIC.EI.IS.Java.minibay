/**
 * 
 */
package es.udc.is.isg016.minibay.web.pages.bids;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import es.udc.is.isg016.minibay.model.bidservice.BidService;
import es.udc.is.isg016.minibay.model.bidservice.BiddingLateException;
import es.udc.is.isg016.minibay.model.bidservice.InsufficientAmountException;
import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.productservice.ProductService;
import es.udc.is.isg016.minibay.web.pages.products.ProductDetails;
import es.udc.is.isg016.minibay.web.services.AuthenticationPolicy;
import es.udc.is.isg016.minibay.web.services.AuthenticationPolicyType;
import es.udc.is.isg016.minibay.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * @author flanciskinho
 *
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class DoBid {
	@SessionState(create=false)
    private UserSession userSession;
	
	@Inject
    private BidService bidService;
	
	@Inject
	private ProductService productService;
	
	private long productId;
	
	@Property
	private Product product;
	
	@Property
    private String bidValue;
	
	@Inject
	private Locale locale;
	
	@Inject
	private Request request;
	
	@Inject
    private Messages messages;
	
	@Component(id = "bidInput")
    private TextField bidInputField;
	
	@InjectComponent
    private Zone zoneForm;
	
    @Component
	private Form makeBid;
	
    @InjectPage
    private ProductDetails productDetails;
    
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}

	public DateFormat getTimeFormat() {
		return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
	}
	
	public Format getNumberFormat() {
		return NumberFormat.getInstance(locale);
	}
	
	public BigDecimal getMinBid() {
		if (product.getWinBid() != null)
			if (product.getWinBid().getUser().getUserProfileId().equals(userSession.getUserProfileId()))
				return product.getWinBid().getMaxPrice().add(new BigDecimal("0.5"));
		return (product.getCurrentPrice() != null) ? product.getCurrentPrice().add(new BigDecimal("0.5")): product.getInitPrice();
	}
	
	public boolean getWinnerBid() {
		if (userSession != null && product!=null && product.getWinBid() != null)
			return userSession.getUserProfileId().equals(product.getWinBid().getUser().getUserProfileId());
		else
			return false;
	}
	
	public boolean getCanStillBid() {
		if (product != null)
			if (Calendar.getInstance().compareTo(product.getEndDate()) == -1)
				return true;
		
		return false;
	}
	
	Object onPassivate() {
        return productId;
    }
	
    void onActivate(String productId) {
    	try {
    		this.productId = new Long(productId);
			product = productService.getByProductId(this.productId);
		} catch (InstanceNotFoundException|NumberFormatException e) {
			product = null;
		}
    }
    
    void onValidateFromMakeBid()  {
        if (!makeBid.isValid()) {
            return;
        }
        
		try {
			NumberFormat nf = NumberFormat.getInstance(locale);
			Number parsed = nf.parse(bidValue);
			BigDecimal tmp = new BigDecimal(parsed.toString());
			
			if (tmp.compareTo(getMinBid()) == -1) {
				throw new InsufficientAmountException(tmp);
			}
			bidService.bidding(userSession.getUserProfileId(), productId, tmp);
			
		} catch (InsufficientAmountException | NumberFormatException e) {
			makeBid.recordError(messages.get("validateBid"));
		} catch (InstanceNotFoundException e) {
			makeBid.recordError(messages.get("noProductNoBid"));
		} catch (BiddingLateException e) {
			makeBid.recordError(messages.get("bidLate"));
		} catch (ParseException pe) {
			makeBid.recordError(messages.get("validateBid"));
		}
    }
    
    
 
    Object onSuccessFromMakeBid() {
    	productDetails.setProductId(productId);
    	return productDetails;
    }

	
	Object onFailureFromMakeBid() {
        return request.isXHR() ? zoneForm.getBody() : null;
    }
	
}
