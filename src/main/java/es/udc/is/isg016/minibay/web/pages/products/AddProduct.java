package es.udc.is.isg016.minibay.web.pages.products;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import es.udc.is.isg016.minibay.model.bidservice.InsufficientAmountException;
import es.udc.is.isg016.minibay.model.category.Category;
import es.udc.is.isg016.minibay.model.categoryservice.CategoryService;
import es.udc.is.isg016.minibay.model.productservice.AnnounceLateException;
import es.udc.is.isg016.minibay.model.productservice.NotSellerException;
import es.udc.is.isg016.minibay.model.productservice.ProductService;
import es.udc.is.isg016.minibay.web.services.AuthenticationPolicy;
import es.udc.is.isg016.minibay.web.services.AuthenticationPolicyType;
import es.udc.is.isg016.minibay.web.util.CookiesManager;
import es.udc.is.isg016.minibay.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class AddProduct {
	
		@InjectPage
		private ProductDetails productDetails;
	
	  	@Property
	    private String productName;

	    @Property
	    private String category;

	    @Property
	    private String productDescription;

	    @Property
	    private String dShipment;

	    @Property
	    private String initPrice;

	    @Property
	    private Calendar endDate;
	    
	    @Property
	    private String time;
	    
	    @Property
		private Long selectValue;

	    @SessionState(create=false)
	    private UserSession userSession;

	    @Inject
	    private Request request;
	    
	    @Inject
	    private Locale locale;

	    @Inject
	    private ProductService productService;
	    
	    @Inject
	    private CategoryService categoryService;
	    
	    @Inject
	    private Messages messages;
	    

	    @InjectComponent
	    private Zone zoneForm;
	    
	    @Component
	    private Form addProductForm;

	    @Component(id = "time")
	    private TextField timeField;
	    
	    @Component(id = "productName")
	    private TextField productNameField;

	    @Component(id = "initPrice")
	    private TextField initPriceField;
	    
	    @Component(id = "endDate")
	    private DateField endDateField;
	     
	    private long productId;
	    
		void onPrepareForRender() {	
			List<Category> list = categoryService.listCategories();
			if (!list.isEmpty()) {
				category="";
				Category c;
				int i;
				for (i = 0; i < list.size() -1; i++) {
					c = list.get(i);
					category = category + c.getCategoryId() + "=" + c.getCategoryName() + ",";
				}
				c = list.get(i);
				category = category + c.getCategoryId() + "=" + c.getCategoryName();
			}
			
		}
		
		//Parsea de String a Calendar
		Calendar getTime (String d) throws ParseException{	
			DateFormat sdf = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
			Date dt = new Date();
			Calendar cal = Calendar.getInstance();
			dt = sdf.parse(d);		
			cal.setTime(dt);
			return cal;
		}
		
		void onValidateFromAddProductForm() {

	        if (!addProductForm.isValid()) {
	            return;
	        }
	        
	        NumberFormat nf = NumberFormat.getInstance(locale);
	        ParsePosition p = new ParsePosition(0);
	        Number n = nf.parse(initPrice,p);
	        
			if (p.getIndex() != initPrice.length()) {
				addProductForm.recordError(initPriceField, messages.format(
						"error-invalidPriceFormat", initPrice));
				return;
			}
	        BigDecimal bd = new BigDecimal(n.toString());
	        
	       if(selectValue == (new Long(0)))
	        	addProductForm.recordError(messages.get("error-noCategory"));
	       else{       
				try {
					
					Calendar tmp = getTime(time);
					endDate.add(Calendar.HOUR_OF_DAY, tmp.get(Calendar.HOUR_OF_DAY));
					endDate.add(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
					
					Calendar now = Calendar.getInstance();
					if(endDate.compareTo(now) <= 0){
						addProductForm.recordError(timeField,messages.get("error-outOfTime"));
					}
	
					productId = productService.announceProduct(userSession.getUserProfileId().longValue(), selectValue, 
							productName, productDescription, dShipment, bd, endDate).getProductId();
				} catch (InstanceNotFoundException e) {
					addProductForm.recordError(messages.get("error-noUser"));
				} catch (AnnounceLateException e) {
					addProductForm.recordError(endDateField, messages.get("error-dateOut"));
				} catch (InsufficientAmountException e) {
					addProductForm.recordError(initPriceField, messages.get("error-notValidPrice"));
				} catch (NotSellerException e) {
					addProductForm.recordError(messages.get("error-isNotSeller"));
				} catch (ParseException e) {
					addProductForm.recordError(timeField, messages.get("time-regexp-message"));
				}
	       }
	    }

	    
	    Object onSuccess() {
	    	productDetails.setProductId(productId);
	    	//return request.isXHR() ? productDetails : null;
	    	return productDetails;
	    }
	    
	    Object onFailure() {
	    	//okPassword=okPassword+" MAL pass";
	        return request.isXHR() ? zoneForm.getBody() : null;
	    }
	    
}
