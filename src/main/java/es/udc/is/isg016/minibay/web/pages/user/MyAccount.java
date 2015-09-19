package es.udc.is.isg016.minibay.web.pages.user;


import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;

import es.udc.is.isg016.minibay.model.card.Card;
import es.udc.is.isg016.minibay.model.userprofile.UserProfile;
import es.udc.is.isg016.minibay.model.userservice.CannotAddMoreCardsException;
import es.udc.is.isg016.minibay.model.userservice.ExpireDateException;
import es.udc.is.isg016.minibay.model.userservice.IncorrectPasswordException;
import es.udc.is.isg016.minibay.model.userservice.UserProfileDetails;
import es.udc.is.isg016.minibay.model.userservice.UserService;
import es.udc.is.isg016.minibay.web.services.AuthenticationPolicy;
import es.udc.is.isg016.minibay.web.services.AuthenticationPolicyType;
import es.udc.is.isg016.minibay.web.util.CookiesManager;
import es.udc.is.isg016.minibay.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class MyAccount {
	
    @Property
    @SessionState(create=false)
    private UserSession userSession;
	
    
    
    @Inject
    private Cookies cookies;

    @Inject
    private Messages messages;
    
	@Inject
	private UserService userService;
	
	@Inject
	private Locale locale;
	
	@Inject
    private Request request;
    
	
	//Informacion basica
	@InjectComponent
    private Zone zoneFormInfo;
	
	@Component
	private Form changeInfo;
	
	@Property
	private String okInfo = "";
    
    @Property
    private String firstName;
    
    @Property
    private String lastName;
    
    @Property
    private String email;
	
	//Contrasena
	@InjectComponent
    private Zone zoneFormPassword;
    
	@Component
    private Form changePassword;
	
	@Property
	private String okPassword = "";
	
    @Property
    private String oldPassword;
    
    @Property
    private String newPassword;
    
    @Property
    private String retypeNewPassword;

	//Tarjeta
    @InjectComponent
    private Zone zoneFormCard;
	
    @Component
    private Form changeCard;
    
    @Property
    private String okCard = "";
    @Property
    private String months;
    @Property
    private String years;
    private static final int NUM_YEARS = 10;
    
    @Component(id = "cardNumber")
    private TextField cardNumberField;
    @Property
    private String cardNumber;

    @Property
    private String monthExpire;
    
    @Property
    private String yearExpire;




    
    // The code
    public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}
	
	
	public UserProfile getUserProfile() throws InstanceNotFoundException{
		return userService.findUserProfile(userSession.getUserProfileId());
	}
    
    
	void onPrepareForRender() throws InstanceNotFoundException {
    	String [] str = new DateFormatSymbols(locale).getMonths();
		String aux = "";
		for (int cnt = 0; cnt < str.length-1; cnt++) {
			aux = aux + "," + cnt + "=" + str[cnt];
		}
		months = aux.substring(1); // quitamos la coma del principio
		
		int n = Calendar.getInstance().get(Calendar.YEAR);
		aux = "";
		for (int cnt = n; cnt < n+NUM_YEARS; cnt++)
			aux = aux + "," + cnt + "=" + cnt;
		years = aux.substring(1); // quitamos la coma del principio
		
		UserProfile tmp = getUserProfile();
		firstName  = tmp.getFirstName();
		lastName   = tmp.getLastName();
		email      = tmp.getEmail();
		
		if (tmp.getCard() != null) {
			cardNumber  = new Long(tmp.getCard().getNumber()).toString();
			monthExpire = new Integer(tmp.getCard().getExpiryDate().get(Calendar.MONTH)).toString();
			yearExpire  = new Integer(tmp.getCard().getExpiryDate().get(Calendar.YEAR)).toString();
		}    
        
    }
    

//////////
// Para el formulario de cambiar/anadir la tarjeta
//////////
    
    void onValidateFromChangeCard() throws CannotAddMoreCardsException {
    	if (!changeCard.isValid()) {
            return;
        }

        try {
        	Calendar c = Calendar.getInstance();
        	c.clear();
        	c.set(Calendar.MONTH, new Integer(monthExpire));
        	c.set(Calendar.YEAR,  new Integer(yearExpire));
        	
        	Card card;
        	if (getUserProfile().getCard() == null)
				card = userService.setRoleSeller(userSession.getUserProfileId(), new Long(cardNumber), c);
        	else
        		card = userService.modifyCard(userSession.getUserProfileId(), new Long(cardNumber), c);
			
		} catch (InstanceNotFoundException e) {
			changeCard.recordError(messages.get("error-noUser"));
		} catch (ExpireDateException e) {
			changeCard.recordError(messages.get("error-notValidDate"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    Object onSuccessFromChangeCard() {
    	okCard = messages.get("ok-card");
        return request.isXHR() ? zoneFormCard.getBody() : null;
    }

    Object onFailureFromChangeCard() {
    	//okCard=okCard+" MAL";
        return request.isXHR() ? zoneFormCard.getBody() : null;
    }

//////////
// Para el formulario de cambiar la contrasena
//////////
    
    void onValidateFromChangePassword() throws InstanceNotFoundException {
        if (!changePassword.isValid()) {
            return;
        }


        if (!newPassword.equals(retypeNewPassword)) {
            changePassword
                    .recordError(messages.get("error-passwordsDontMatch"));
        } else {

            try {
                userService.changePassword(userSession.getUserProfileId(),
                        oldPassword, newPassword);
            } catch (IncorrectPasswordException e) {
                changePassword.recordError(messages
                        .get("error-invalidPassword"));
            }

        }

    }

    Object onSuccessFromChangePassword() {
        CookiesManager.removeCookies(cookies);
        okPassword = messages.get("ok-pass");
        return request.isXHR() ? zoneFormPassword.getBody() : null;
    }
    
    Object onFailureFromChangePassword() {
    	//okPassword=okPassword+" MAL pass";
        return request.isXHR() ? zoneFormPassword.getBody() : null;
    }
    
    
//////////
// Para el formulario de actualizar informacion
//////////

	void onValidateFromChangeInfo() {
	    if (!changeInfo.isValid()) {
	        return;
	    }
	    
	}
	
	Object onSuccessFromChangeInfo() throws InstanceNotFoundException {
		userService.updateUserProfileDetails(
	            userSession.getUserProfileId(), new UserProfileDetails(
	                    firstName, lastName, email));
	    userSession.setFirstName(firstName);
	    okInfo = messages.get("ok-info");
	    return request.isXHR() ? zoneFormInfo.getBody() : null;
	}
	
	Object onFailureFromChangeInfo() {
        return request.isXHR() ? zoneFormInfo.getBody() : null;
    }
    
}
