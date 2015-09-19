package es.udc.is.isg016.minibay.test.model.userservice;

import static es.udc.is.isg016.minibay.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.is.isg016.minibay.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg016.minibay.model.card.Card;
import es.udc.is.isg016.minibay.model.card.CardDao;
import es.udc.is.isg016.minibay.model.userprofile.UserProfile;
import es.udc.is.isg016.minibay.model.userprofile.UserProfileDao;
import es.udc.is.isg016.minibay.model.userservice.CannotAddMoreCardsException;
import es.udc.is.isg016.minibay.model.userservice.ExpireDateException;
import es.udc.is.isg016.minibay.model.userservice.IncorrectPasswordException;
import es.udc.is.isg016.minibay.model.userservice.UserProfileDetails;
import es.udc.is.isg016.minibay.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class UserServiceTest {

    private final long NON_EXISTENT_USER_PROFILE_ID = -1;

    @Autowired
    private UserService userService;
    
    @Autowired
    private CardDao cardDao;
    
    @Autowired
    private UserProfileDao userProfileDao;
    
    
    private boolean isRoleSeller(long userId)
        	throws InstanceNotFoundException {
    	return userProfileDao.find(userId).getCard() != null;
    }

    @Test
    public void testIsRoleSeller()
    		throws DuplicateInstanceException, InstanceNotFoundException, CannotAddMoreCardsException, ExpireDateException {
    	
    	UserProfile userProfile = userService.registerUser(
    			"user", "PassW0rd.",
    			new UserProfileDetails("name", "lastname", "user@udc.es"));
    	
    	long userProfileId = userProfile.getUserProfileId();
    	assertFalse(isRoleSeller(userProfileId));
    	
    	Calendar moment = Calendar.getInstance();
    	moment.add(Calendar.YEAR, 1);
    	long cardNumber = 1;
    	
    	userService.setRoleSeller(userProfileId, cardNumber, moment);
    	assertTrue(isRoleSeller(userProfileId));
    	
    	Card card = cardDao.find(cardNumber);
    	Calendar aux = Calendar.getInstance();
    	aux.clear();
    	aux.set(Calendar.MONTH, moment.get(Calendar.MONTH));
    	aux.set(Calendar.YEAR, moment.get(Calendar.YEAR));
    	assertTrue(card.getExpiryDate().compareTo(aux) == 0);
    	
    	userProfileId = userService.registerUser("user2", "********", new UserProfileDetails("Name", "LastName", "User@ucv.udc.es")).getUserProfileId();
    	userService.setRoleSeller(userProfileId, cardNumber, moment);
    	assertTrue(isRoleSeller(userProfileId));
    }
    
    @Test
    public void testModifyCard()
    		throws DuplicateInstanceException, InstanceNotFoundException, ExpireDateException, CannotAddMoreCardsException {
    	
    	long userId1 = userService.registerUser("user1", "********", new UserProfileDetails("Name", "LastName", "User@ucv.udc.es")).getUserProfileId();
    	long userId2 = userService.registerUser("user2", "********", new UserProfileDetails("Name", "LastName", "User@ucv.udc.es")).getUserProfileId();
    	
    	UserProfile user1 = userService.findUserProfile(userId1);
    	UserProfile user2 = userService.findUserProfile(userId2);
    	
    	Calendar moment = Calendar.getInstance();
    	moment.add(Calendar.YEAR, 1);
    	
    	Long cardNum1 = new Long(1);
    	Long cardNum2 = new Long(2);
    	
    	userService.modifyCard(userId1, cardNum1, moment);
    	assertTrue(user1.getCard().getNumber() == cardNum1.longValue());
    	
    	userService.modifyCard(userId1, cardNum2, moment);
    	assertTrue(user1.getCard().getNumber() == cardNum2.longValue());
    	
    	
    	userService.setRoleSeller(userId2, cardNum2, moment);
    	assertTrue(user2.getCard().getNumber() == cardNum2.longValue());
    	
    	userService.modifyCard   (userId2, cardNum1, moment);
    	assertTrue(user2.getCard().getNumber() == cardNum1.longValue());
    	
    	//modify date
    	int month = moment.get(Calendar.MONTH);
    	int year  = moment.get(Calendar.YEAR) + 5;
    	
    	moment.clear();
    	moment.set(Calendar.MONTH, month);
    	moment.set(Calendar.YEAR, year);
    	
    	userService.modifyCard   (userId2, cardNum1, moment);
    	assertTrue(user2.getCard().getNumber() == cardNum1.longValue());
    	assertTrue(user2.getCard().getExpiryDate().compareTo(moment) == 0);
    }
    
    @Test
    public void testSellerNonExistentUser()
    	throws CannotAddMoreCardsException, ExpireDateException {
    	long userProfileId = NON_EXISTENT_USER_PROFILE_ID;
    	
    	Boolean bool = null;
    	
    	try {
			bool = new Boolean(isRoleSeller(userProfileId));
		} catch (InstanceNotFoundException e) { }
    	assertTrue(bool == null);
    	
    	try {
			userService.setRoleSeller(userProfileId, (long) 1, Calendar.getInstance());
		} catch (InstanceNotFoundException e) { }
    	
    }

	@Test(expected = CannotAddMoreCardsException.class)
    public void testCannotAddMoreCardsException()
    		throws DuplicateInstanceException, InstanceNotFoundException, CannotAddMoreCardsException, ExpireDateException {
    	
    	UserProfile userProfile = userService.registerUser(
    			"user", "PassW0rd.",
    			new UserProfileDetails("name", "lastname", "user@udc.es"));
    	
    	long userProfileId = userProfile.getUserProfileId();
    	
    	Calendar moment = Calendar.getInstance();
    	moment.add(Calendar.YEAR, 2);
    	
    	userService.setRoleSeller(userProfileId, (long) 1, moment);
    	assertTrue(isRoleSeller(userProfileId));
    	assertTrue(userProfile.getCard().getNumber() == (long) 1);
    	
    	userService.setRoleSeller(userProfileId, (long) 1, moment);
    	
    }
    
    @Test
    public void testRegisterUserAndFindUserProfile()
        throws DuplicateInstanceException, InstanceNotFoundException {
        /* Register user and find profile. */
        UserProfile userProfile = userService.registerUser(
            "user", "userPassword",
            new UserProfileDetails("name", "lastName", "user@udc.es"));

        UserProfile userProfile2 = userService.findUserProfile(
            userProfile.getUserProfileId());

        /* Check data. */
        assertEquals(userProfile, userProfile2);

    }

    @Test(expected = DuplicateInstanceException.class)
    public void testRegisterDuplicatedUser() throws DuplicateInstanceException,
        InstanceNotFoundException {

        String loginName = "user";
        String clearPassword = "userPassword";
        UserProfileDetails userProfileDetails = new UserProfileDetails(
            "name", "lastName", "user@udc.es");

        userService.registerUser(loginName, clearPassword,
            userProfileDetails);

        userService.registerUser(loginName, clearPassword,
            userProfileDetails);

    }

    @Test
    public void testLoginClearPassword() throws IncorrectPasswordException,
        InstanceNotFoundException {

        String clearPassword = "userPassword";
        UserProfile userProfile = registerUser("user", clearPassword);

        UserProfile userProfile2 = userService.login(userProfile.getLoginName(),
            clearPassword, false);

        assertEquals(userProfile, userProfile2);

    }


    @Test
    public void testLoginEncryptedPassword() throws IncorrectPasswordException,
            InstanceNotFoundException {

        UserProfile userProfile = registerUser("user", "clearPassword");

        UserProfile userProfile2 = userService.login(userProfile.getLoginName(),
            userProfile.getEncryptedPassword(), true);

        assertEquals(userProfile, userProfile2);

    }

    @Test(expected = IncorrectPasswordException.class)
    public void testLoginIncorrectPasword() throws IncorrectPasswordException,
            InstanceNotFoundException {

        String clearPassword = "userPassword";
        UserProfile userProfile = registerUser("user", clearPassword);

        userService.login(userProfile.getLoginName(), 'X' + clearPassword,
             false);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testLoginWithNonExistentUser()
            throws IncorrectPasswordException, InstanceNotFoundException {

        userService.login("user", "userPassword", false);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testFindNonExistentUser() throws InstanceNotFoundException {

        userService.findUserProfile(NON_EXISTENT_USER_PROFILE_ID);

    }

    @Test
    public void testUpdate() throws InstanceNotFoundException,
            IncorrectPasswordException {

        /* Update profile. */
        String clearPassword = "userPassword";
        UserProfile userProfile = registerUser("user", clearPassword);

        UserProfileDetails newUserProfileDetails = new UserProfileDetails(
            'X' + userProfile.getFirstName(), 'X' + userProfile.getLastName(),
            'X' + userProfile.getEmail());

        userService.updateUserProfileDetails(userProfile.getUserProfileId(),
            newUserProfileDetails);

        /* Check changes. */
        userService.login(userProfile.getLoginName(), clearPassword, false);
        UserProfile userProfile2 = userService.findUserProfile(
            userProfile.getUserProfileId());

        assertEquals(newUserProfileDetails.getFirstName(),
            userProfile2.getFirstName());
        assertEquals(newUserProfileDetails.getLastName(),
            userProfile2.getLastName());
        assertEquals(newUserProfileDetails.getEmail(),
            userProfile2.getEmail());

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testUpdateWithNonExistentUser()
            throws InstanceNotFoundException {

        userService.updateUserProfileDetails(NON_EXISTENT_USER_PROFILE_ID,
            new UserProfileDetails("name", "lastName", "user@udc.es"));

    }

    @Test
    public void testChangePassword() throws InstanceNotFoundException,
            IncorrectPasswordException {

        /* Change password. */
        String clearPassword = "userPassword";
        UserProfile userProfile = registerUser("user", clearPassword);
        String newClearPassword = 'X' + clearPassword;

        userService.changePassword(userProfile.getUserProfileId(),
            clearPassword, newClearPassword);

        /* Check new password. */
        userService.login(userProfile.getLoginName(), newClearPassword, false);

    }

    @Test(expected = IncorrectPasswordException.class)
    public void testChangePasswordWithIncorrectPassword()
            throws InstanceNotFoundException, IncorrectPasswordException {

        String clearPassword = "userPassword";
        UserProfile userProfile = registerUser("user", clearPassword);

        userService.changePassword(userProfile.getUserProfileId(),
            'X' + clearPassword, 'Y' + clearPassword);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testChangePasswordWithNonExistentUser()
            throws InstanceNotFoundException, IncorrectPasswordException {

        userService.changePassword(NON_EXISTENT_USER_PROFILE_ID,
                "userPassword", "XuserPassword");

    }

    ////////////////////////////////////////
    /////////////////////////////////
    //////////////////////////
    ///////////////////
    // Util methods
    
    private UserProfile registerUser(String loginName, String clearPassword) {

        UserProfileDetails userProfileDetails = new UserProfileDetails(
            "name", "lastName", "user@udc.es");

        try {

            return userService.registerUser(
                loginName, clearPassword, userProfileDetails);

        } catch (DuplicateInstanceException e) {
            throw new RuntimeException(e);
        }

    }

}
