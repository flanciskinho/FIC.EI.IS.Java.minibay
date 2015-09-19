package es.udc.is.isg016.minibay.web.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.is.isg016.minibay.model.userservice.UserService;
import es.udc.is.isg016.minibay.web.pages.Index;
import es.udc.is.isg016.minibay.web.services.AuthenticationPolicy;
import es.udc.is.isg016.minibay.web.services.AuthenticationPolicyType;
import es.udc.is.isg016.minibay.web.util.CookiesManager;
import es.udc.is.isg016.minibay.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class Layout {
    @Property
    @Parameter(required = false, defaultPrefix = "message")
    private String menuExplanation;

    @Property
    @Parameter(required = true, defaultPrefix = "message")
    private String pageTitle;

    @Property
    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private Cookies cookies;

    @Inject
    private UserService userService;
    
    public boolean getSeller() {
    	if (userSession == null)
    		return false;
    	
    	try {
    		return userService.findUserProfile(userSession.getUserProfileId()).getCard() != null;
    	} catch (InstanceNotFoundException e) {
    		return false;
    	}
    	
    }
    
    @AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
   	Object onActionFromLogout() {
        userSession = null;
        CookiesManager.removeCookies(cookies);
        return Index.class;
	}
    
}