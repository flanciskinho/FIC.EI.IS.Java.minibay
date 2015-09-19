package es.udc.is.isg016.minibay.model.userservice;

import java.util.Calendar;

import es.udc.is.isg016.minibay.model.card.Card;
import es.udc.is.isg016.minibay.model.userprofile.UserProfile;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface UserService {

    public UserProfile registerUser(String loginName, String clearPassword,
            UserProfileDetails userProfileDetails)
            throws DuplicateInstanceException;

    public UserProfile login(String loginName, String password,
            boolean passwordIsEncrypted) throws InstanceNotFoundException,
            IncorrectPasswordException;

    public UserProfile findUserProfile(Long userProfileId)
            throws InstanceNotFoundException;

    public void updateUserProfileDetails(Long userProfileId,
            UserProfileDetails userProfileDetails)
            throws InstanceNotFoundException;

    public void changePassword(Long userProfileId, String oldClearPassword,
            String newClearPassword) throws IncorrectPasswordException,
            InstanceNotFoundException;
    
    public Card setRoleSeller(Long userId, Long cardNum, Calendar cardExDate) 
    		throws InstanceNotFoundException, CannotAddMoreCardsException, ExpireDateException;
    
    public Card modifyCard(long userId, long cardNumber, Calendar cardExDate)
    	throws InstanceNotFoundException, ExpireDateException;

}
