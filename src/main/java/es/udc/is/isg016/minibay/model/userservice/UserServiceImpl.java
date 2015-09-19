package es.udc.is.isg016.minibay.model.userservice;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg016.minibay.model.card.Card;
import es.udc.is.isg016.minibay.model.card.CardDao;
import es.udc.is.isg016.minibay.model.userprofile.UserProfile;
import es.udc.is.isg016.minibay.model.userprofile.UserProfileDao;
import es.udc.is.isg016.minibay.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserProfileDao userProfileDao;

    @Autowired
    private CardDao cardDao;
    
    public UserProfile registerUser(String loginName, String clearPassword,
            UserProfileDetails userProfileDetails)
            throws DuplicateInstanceException {

        try {
            userProfileDao.findByLoginName(loginName);
            throw new DuplicateInstanceException(loginName,
                    UserProfile.class.getName());
        } catch (InstanceNotFoundException e) {
            String encryptedPassword = PasswordEncrypter.crypt(clearPassword);

            UserProfile userProfile = new UserProfile(loginName,
                    encryptedPassword, userProfileDetails.getFirstName(),
                    userProfileDetails.getLastName(), userProfileDetails
                        .getEmail(), null);

            userProfileDao.save(userProfile);
            return userProfile;
        }

    }

    @Transactional(readOnly = true)
    public UserProfile login(String loginName, String password,
            boolean passwordIsEncrypted) throws InstanceNotFoundException,
            IncorrectPasswordException {

        UserProfile userProfile = userProfileDao.findByLoginName(loginName);
        String storedPassword = userProfile.getEncryptedPassword();

        if (passwordIsEncrypted) {
            if (!password.equals(storedPassword)) {
                throw new IncorrectPasswordException(loginName);
            }
        } else {
            if (!PasswordEncrypter.isClearPasswordCorrect(password,
                    storedPassword)) {
                throw new IncorrectPasswordException(loginName);
            }
        }
        return userProfile;

    }

    @Transactional(readOnly = true)
    public UserProfile findUserProfile(Long userProfileId)
            throws InstanceNotFoundException {

        return userProfileDao.find(userProfileId);
    }

    public void updateUserProfileDetails(Long userProfileId,
            UserProfileDetails userProfileDetails)
            throws InstanceNotFoundException {

        UserProfile userProfile = userProfileDao.find(userProfileId);
        userProfile.setFirstName(userProfileDetails.getFirstName());
        userProfile.setLastName(userProfileDetails.getLastName());
        userProfile.setEmail(userProfileDetails.getEmail());

    }

    public void changePassword(Long userProfileId, String oldClearPassword,
            String newClearPassword) throws IncorrectPasswordException,
            InstanceNotFoundException {

        UserProfile userProfile;
        userProfile = userProfileDao.find(userProfileId);

        String storedPassword = userProfile.getEncryptedPassword();

        if (!PasswordEncrypter.isClearPasswordCorrect(oldClearPassword,
                storedPassword)) {
            throw new IncorrectPasswordException(userProfile.getLoginName());
        }

        userProfile.setEncryptedPassword(PasswordEncrypter
                .crypt(newClearPassword));

    }
    
    // save only month and year
    private Calendar clearDate(Calendar cardExDate) {
    	Calendar date = Calendar.getInstance(); // save only month and year
		date.clear();
		date.set(Calendar.MONTH, cardExDate.get(Calendar.MONTH));
		date.set(Calendar.YEAR, cardExDate.get(Calendar.YEAR));
		
		return date;
    }
    
    public Card setRoleSeller(Long userId, Long cardNum, Calendar cardExDate) 
    		throws InstanceNotFoundException, CannotAddMoreCardsException, ExpireDateException {
    	
    	UserProfile userProfile = userProfileDao.find(userId);
    	
		Calendar date = clearDate(cardExDate);
    	
    	if (date.compareTo(Calendar.getInstance()) != 1)
    		throw new ExpireDateException(userProfile.getLoginName(), date);
    	
    	if (userProfile.getCard() != null)
    		throw new CannotAddMoreCardsException(userProfile.getLoginName());
    	
    	Card c;
    	try {
    		c = cardDao.find(cardNum);
    	} catch (InstanceNotFoundException e) {
    		c = new Card(cardNum, date);
    		cardDao.save(c);
    	}
    	
    	userProfile.setCard(c);
    	
    	return c;
    }

    public Card modifyCard(long userId, long cardNumber, Calendar cardExDate)
        	throws InstanceNotFoundException, ExpireDateException {
    	UserProfile userProfile = userProfileDao.find(userId);
    	
    	Calendar date = clearDate(cardExDate);
    	
    	// Si no tiene tarjeta se le anade
    	try {
    		if (userProfile.getCard() == null)
    			return this.setRoleSeller(userId, cardNumber, date);
    	} catch (CannotAddMoreCardsException cadce) {
    		throw new RuntimeException();
    	}

    	if (date.compareTo(Calendar.getInstance()) != 1)
    		throw new ExpireDateException(userProfile.getLoginName(), date);
    	
    	
    	Card c;
    	try {
    		c = cardDao.find(cardNumber);
    		c.setExpiryDate(date);
    	} catch (InstanceNotFoundException e) {
    		c = new Card(cardNumber, date);
    		cardDao.save(c);
    	}
    	
    	userProfile.setCard(c);
    	
    	return c;
    }
    
}
