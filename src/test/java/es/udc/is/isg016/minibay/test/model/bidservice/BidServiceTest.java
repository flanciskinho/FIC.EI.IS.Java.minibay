/**
 * 
 */
package es.udc.is.isg016.minibay.test.model.bidservice;

import static es.udc.is.isg016.minibay.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.is.isg016.minibay.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg016.minibay.model.bid.Bid;
import es.udc.is.isg016.minibay.model.bid.BidDao;
import es.udc.is.isg016.minibay.model.bidservice.BidService;
import es.udc.is.isg016.minibay.model.bidservice.BiddingLateException;
import es.udc.is.isg016.minibay.model.bidservice.InsufficientAmountException;
import es.udc.is.isg016.minibay.model.category.Category;
import es.udc.is.isg016.minibay.model.category.CategoryDao;
import es.udc.is.isg016.minibay.model.categoryservice.CategoryService;
import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.productservice.AnnounceLateException;
import es.udc.is.isg016.minibay.model.productservice.NotSellerException;
import es.udc.is.isg016.minibay.model.productservice.ProductService;
import es.udc.is.isg016.minibay.model.userprofile.UserProfile;
import es.udc.is.isg016.minibay.model.userservice.CannotAddMoreCardsException;
import es.udc.is.isg016.minibay.model.userservice.ExpireDateException;
import es.udc.is.isg016.minibay.model.userservice.UserProfileDetails;
import es.udc.is.isg016.minibay.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BidServiceTest {

	private final long NON_EXISTENT_BID_ID = -1;
	private final long NON_EXISTENT_PRODUCT_ID = -1;
	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	private final int  TYPICAL_SIZE = 10;
	
	@Autowired
	private BidDao bidDao;
	
	@Autowired
	private BidService bidService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryDao categoryDao;
	
	////////////////////////////////////////////////
	///// util functions to prove test          ////
	////////////////////////////////////////////////
	
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
	
	private UserProfile registerSellerUser(String loginName, long card)
			throws InstanceNotFoundException, CannotAddMoreCardsException, ExpireDateException {
		
		UserProfile user = registerUser(loginName, "*****");
		Calendar moment = Calendar.getInstance();
		moment.add(Calendar.YEAR, 1);
		userService.setRoleSeller(user.getUserProfileId(), new Long(card), moment);
		return user;
	}
	
	private Category addCategoryByName(String name)
			throws DuplicateInstanceException{
		Category cat = new Category(name);
		try {
			categoryDao.save(cat);
		} catch (DataIntegrityViolationException dive) {
			throw new DuplicateInstanceException(name, Category.class.getName());
		}
		return cat;
	}
	
	private void addSomeCategories(String categories[])
			throws DuplicateInstanceException {
		
		for (int cnt = 0; cnt < categories.length; cnt++)
			addCategoryByName(categories[cnt]);
	}
	
	private long getRandomCategoryId() {
		List<Category> categories = categoryService.listCategories();
		if (categories.size() == 0)
			throw new RuntimeException("categories not found");
		
		return categories.get(new Random().nextInt(categories.size())).getCategoryId();
	}
	
	private void print(String str) {
		try {
	        File nombre= new File("/tmp/info.txt");
	        FileWriter fichlog = new FileWriter(nombre,true);
	        fichlog.write(str + "\n");
	        fichlog.flush();
	        fichlog.close();
		} catch(Exception e){}
	}
	
	////////////////////////////////////////////////
	///// to prove bidService  ////
	////////////////////////////////////////////////
	
	/**
	 * Test de prueba para comprobar el funcionamiento de la busqueda de una puja que no existe
	 * @throws InstanceNotFoundException
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testGetNonExistentBid()
			throws InstanceNotFoundException {
		bidDao.find(NON_EXISTENT_BID_ID);
	}

	/**
	 * Test para comprobar el funcionamiento de una prueba de mostrar las pujas de un usuario que no existe
	 */
	@Test
	public void testFindByNonExistentUserProfile() {
		List<Bid> bids = bidService.findByUserProfileId(NON_EXISTENT_USER_PROFILE_ID, 0, TYPICAL_SIZE);
		long number = bidService.getNumberOfBidByUserProfileId(NON_EXISTENT_USER_PROFILE_ID);
		assertTrue(bids.isEmpty());
		assertTrue(bids.size() == number);
		assertTrue(number == 0);
	}
	
	/**
	 * Test que trata de comprobar el funcionamiento de una buscar las pujas de un usuario que no realizó ninguna
	 * @throws InstanceNotFoundException
	 * @throws CannotAddMoreCardsException
	 * @throws ExpireDateException
	 */
	@Test
	public void testFindByNonExistentBid()
			throws InstanceNotFoundException, CannotAddMoreCardsException, ExpireDateException {
		UserProfile userProfile = registerUser("login", "PassW0rd.");
		long userProfileId = userProfile.getUserProfileId();
		
		Calendar moment = Calendar.getInstance();
		moment.add(Calendar.YEAR, 1);
		userService.setRoleSeller(userProfileId, (long) 1, moment);
		
		List<Bid> bids = bidService.findByUserProfileId(userProfileId, 0, TYPICAL_SIZE);
		int number = bidService.getNumberOfBidByUserProfileId(userProfileId);
		
		assertTrue(number == 0);
		assertTrue(bids.isEmpty());
	}
	
	/**
	 * Test que comprueba el funcionamiento de encontrat una puja existente
	 * @throws DuplicateInstanceException
	 * @throws InstanceNotFoundException
	 * @throws AnnounceLateException
	 * @throws InsufficientAmountException
	 * @throws NotSellerException
	 * @throws BiddingLateException
	 * @throws CannotAddMoreCardsException
	 * @throws ExpireDateException
	 */
	@Test
	public void testFindByExistentBid()
			throws DuplicateInstanceException, InstanceNotFoundException, AnnounceLateException, InsufficientAmountException, NotSellerException, BiddingLateException, CannotAddMoreCardsException, ExpireDateException {
		
		String categories[] = {"Electronica", "Informatica", "Deportes", "Juegos", "Viajes", "Ropa"};
		addSomeCategories(categories);
		
		UserProfile userProfile = registerUser("login", "PassW0rd.");
		long userProfileId = userProfile.getUserProfileId();
		
		BigDecimal amount = new BigDecimal("3.5");
		Calendar moment = Calendar.getInstance();
		moment.add(Calendar.YEAR, 1);
		
		userService.setRoleSeller(userProfileId, (long) 1, moment);
		long categoryId = getRandomCategoryId();
		long productId  = NON_EXISTENT_PRODUCT_ID;

		productId = productService.announceProduct(userProfileId, categoryId, "product1", "desc", "dshipment", amount, moment).getProductId();

		
		Bid bid = null;
		bid = bidService.bidding(userProfileId, productId, amount.add(BigDecimal.TEN));
		assertEquals(bid, bidDao.find(bid.getBidId()));
		
		List<Bid> bids = bidService.findByUserProfileId(userProfileId, 0, TYPICAL_SIZE);
		int number = bidService.getNumberOfBidByUserProfileId(userProfileId);
		assertTrue(bids.size() == 1);
		assertTrue(number == 1);
		
		
	}

	/**
	 * Comprobar el funcionamiento de realizar una puja fuera de tiempo
	 * @throws BiddingLateException
	 * @throws InstanceNotFoundException
	 * @throws InsufficientAmountException
	 * @throws DuplicateInstanceException
	 * @throws AnnounceLateException
	 * @throws NotSellerException
	 * @throws CannotAddMoreCardsException
	 * @throws ExpireDateException
	 * @throws InterruptedException
	 */
	@Test(expected = BiddingLateException.class)
	public void testBiddingLate() 
		throws BiddingLateException, InstanceNotFoundException, InsufficientAmountException, DuplicateInstanceException, AnnounceLateException, NotSellerException, CannotAddMoreCardsException, ExpireDateException, InterruptedException {
		
		String categories[] = {"Electronica", "Informatica", "Deportes", "Juegos", "Viajes", "Ropa"};
		addSomeCategories(categories);
		UserProfile userProfile = registerUser("login", "PassW0rd.");
		long userProfileId = userProfile.getUserProfileId();
		
		BigDecimal amount = new BigDecimal("3.5");
		Calendar moment;
		
		long categoryId = getRandomCategoryId();
		long productId  = NON_EXISTENT_PRODUCT_ID;
		
		(moment = Calendar.getInstance()).add(Calendar.YEAR, 1);
		userService.setRoleSeller(userProfileId, (long) 1, moment);
		
		int MILISECONDS = 20;
		(moment = Calendar.getInstance()).add(Calendar.MILLISECOND, MILISECONDS);
		productId = productService.announceProduct(userProfileId, categoryId, "product1", "desc", "dshipment", amount, moment).getProductId();
		
		Thread.sleep(2*MILISECONDS); // need wait for do it late
		
		assertTrue(bidService.bidding(userProfileId, productId, amount.add(BigDecimal.TEN)) == null);
		
		assertTrue(bidService.findByUserProfileId(userProfileId, 0, TYPICAL_SIZE).isEmpty());
		assertTrue(bidService.getNumberOfBidByUserProfileId(userProfileId) == 0);
	}
	
	/**
	 * Test para comprobar el funcionamiento de realizar una apuesta insuficiente
	 * @throws DuplicateInstanceException
	 * @throws InstanceNotFoundException
	 * @throws CannotAddMoreCardsException
	 * @throws ExpireDateException
	 * @throws AnnounceLateException
	 * @throws NotSellerException
	 * @throws InsufficientAmountException
	 * @throws BiddingLateException
	 */
	@Test
	public void testBiddingInsufficientAmount()
			throws DuplicateInstanceException, InstanceNotFoundException, CannotAddMoreCardsException, ExpireDateException, AnnounceLateException, NotSellerException, InsufficientAmountException, BiddingLateException {
		
		String categories[] = {"Electronica", "Informatica", "Deportes", "Juegos", "Viajes", "Ropa"};
		addSomeCategories(categories);
		
		UserProfile userProfile = registerUser("login", "PassW0rd.");
		long userProfileId1 = userProfile.getUserProfileId();
		userProfile = registerUser("iName", "********");
		long userProfileId2 = userProfile.getUserProfileId();
		
		BigDecimal amount = new BigDecimal("3.5");
		Calendar moment = Calendar.getInstance();
		moment.add(Calendar.YEAR, 1);
		
		userService.setRoleSeller(userProfileId1, (long) 1, moment);
		long categoryId = getRandomCategoryId();
		long productId  = NON_EXISTENT_PRODUCT_ID;
		
		productId = productService.announceProduct(userProfileId1, categoryId, "product1", "desc", "dshipment", amount, moment).getProductId();
			
		//Apuesta Insuficiente
		Bid badBid = null;
		try {
			badBid = bidService.bidding(userProfileId1, productId, amount.subtract(BigDecimal.ONE));
		} catch (InsufficientAmountException e) { }
		assertTrue(badBid == null);
		
		// Primera apuesta
		Bid firstBid = null;
		try {
			firstBid = bidService.bidding(userProfileId1, productId, amount);
		} catch (InsufficientAmountException e) {
			throw new RuntimeException (e);
		}
		assertTrue(firstBid != null);
		
		// Segunda apuesta con el mismo valor del producto
		Bid secondBid = null;
		try {
			secondBid = bidService.bidding(userProfileId2, productId, amount);
		} catch (InstanceNotFoundException | BiddingLateException e) {
			throw new RuntimeException (e);
		} catch (InsufficientAmountException e) { }
		assertTrue(secondBid == null);	

	}
	
	/**
	 * Test para comprobar el funcionamiento de una apuesta sobre un producto o un usuario que no existe
	 * @throws InstanceNotFoundException
	 * @throws DuplicateInstanceException
	 * @throws CannotAddMoreCardsException
	 * @throws ExpireDateException
	 * @throws InsufficientAmountException
	 * @throws BiddingLateException
	 */
	@Test
	public void testBiddingUserOrProductNotFound()
		throws InstanceNotFoundException, DuplicateInstanceException, CannotAddMoreCardsException, ExpireDateException, InsufficientAmountException, BiddingLateException {
		
		String categories[] = {"Electronica", "Informatica", "Deportes", "Juegos", "Viajes", "Ropa"};
		addSomeCategories(categories);
		
		UserProfile userProfile = registerUser("login", "PassW0rd.");
		long userProfileId = userProfile.getUserProfileId();
		
		BigDecimal amount = new BigDecimal("3.5");
		Calendar moment = Calendar.getInstance();
		moment.add(Calendar.YEAR, 1);
		
		userService.setRoleSeller(userProfileId, (long) 1, moment);
		
		long categoryId = getRandomCategoryId();
		long productId  = NON_EXISTENT_PRODUCT_ID;
		
		try {
			productId = productService.announceProduct(userProfileId, categoryId, "product1", "desc", "dshipment", amount, moment).getProductId();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Bid bid = null;
		try {
			bid = bidService.bidding(NON_EXISTENT_USER_PROFILE_ID, productId, amount.subtract(BigDecimal.TEN));
		} catch (InstanceNotFoundException e) { }
		assertTrue(bid == null);
		
		try {
			bid = bidService.bidding(userProfileId, NON_EXISTENT_PRODUCT_ID, amount.subtract(BigDecimal.TEN));
		} catch (InstanceNotFoundException e) { }
		assertTrue(bid == null);
		
	}

	
	@Test
	public void testBidding()
			throws DuplicateInstanceException, InstanceNotFoundException, CannotAddMoreCardsException, ExpireDateException, AnnounceLateException, InsufficientAmountException, NotSellerException, BiddingLateException {
		
		String categories[] = {"Electronica", "Informatica", "Deportes", "Juegos", "Viajes", "Ropa"};
		addSomeCategories(categories);
		
		UserProfile user1 = registerUser("user1", "PassW0rd.1");
		UserProfile user2 = registerUser("user2", "PassW0rd.2");
		long userId1 = user1.getUserProfileId();
		long userId2 = user2.getUserProfileId();
		
		Calendar moment = Calendar.getInstance();
		moment.add(Calendar.YEAR, 1);
		userService.setRoleSeller(userId1,(long) 1234, moment);
		
		BigDecimal initPrice = new BigDecimal(4.2);
		Product product1 = productService.announceProduct(userId1, getRandomCategoryId(), "product1", "desc", "dshipment", initPrice, moment);
		long productId1 = product1.getProductId();
		
		Bid bid;
		bid = bidService.bidding(userId2, productId1, initPrice);
		assertEquals(product1, bid.getProduct()); // se apuesta sobre el producto que queremos
		assertTrue(product1.getCurrentPrice().compareTo(product1.getInitPrice()) == 0);
		assertTrue(bid.getProduct().getCurrentPrice().compareTo(initPrice) == 0);
		assertEquals(product1.getWinBid().getUser(), bid.getUser());
		assertEquals(product1.getWinBid().getUser(), user2);
		
		try { // No se puede hacer
			bidService.bidding(userId1, productId1, initPrice);
			assertTrue(false);
		} catch (InsufficientAmountException iae) { }
		
		bid = bidService.bidding(userId1, productId1, new BigDecimal(4.5));
		assertTrue(product1.getCurrentPrice().compareTo(new BigDecimal(4.5)) == 0); // La apuesta maxima esta en 4.5 (no se pudo hacer el incremento de 0.5)
		assertEquals(product1.getWinBid().getUser(), user1);
		
		
		bid = bidService.bidding(userId2, productId1, new BigDecimal(7)); // La apuesta se incrementa en 0.5. Ahora el valor esta en 5
		assertEquals(product1.getWinBid().getUser(), bid.getUser());
		assertEquals(product1.getWinBid().getUser(), user2);
		
		
		bid = bidService.bidding(userId1, productId1, new BigDecimal(5.5));
		assertTrue(bid.getProduct().getCurrentPrice().compareTo(new BigDecimal(6)) == 0);
		assertEquals(product1.getWinBid().getUser(), user2);
		
		bid = bidService.bidding(userId1, productId1, new BigDecimal(7));
		assertTrue(bid.getProduct().getCurrentPrice().compareTo(new BigDecimal(7.0)) == 0);
		assertEquals(product1.getWinBid().getUser(), user2);
		
		bid = bidService.bidding(userId1, productId1, new BigDecimal(7.15));
		assertTrue(bid.getProduct().getCurrentPrice().compareTo(new BigDecimal(7.15)) == 0);
		assertEquals(product1.getWinBid().getUser(), bid.getUser());
		assertEquals(product1.getWinBid().getUser(), user1);
		
		bid = bidService.bidding(userId2, productId1, new BigDecimal(8)); 
		assertTrue(bid.getProduct().getCurrentPrice().compareTo(new BigDecimal(7.65)) == 0);
		assertEquals(product1.getWinBid().getUser(), bid.getUser());
		assertEquals(product1.getWinBid().getUser(), user2);
		
		bid = bidService.bidding(userId1, productId1, new BigDecimal(8.25));
		assertTrue(bid.getProduct().getCurrentPrice().compareTo(new BigDecimal(8.15)) == 0);
		assertEquals(product1.getWinBid().getUser(), bid.getUser());
		assertEquals(product1.getWinBid().getUser(), user1);
	}
	
	@Test
	public void testDuplicateBidding()
			throws DuplicateInstanceException, InstanceNotFoundException, CannotAddMoreCardsException, ExpireDateException, AnnounceLateException, InsufficientAmountException, NotSellerException, BiddingLateException {
		
		String categories[] = {"Electronica", "Informatica", "Deportes", "Juegos", "Viajes", "Ropa"};
		addSomeCategories(categories);
		
		UserProfile user = registerSellerUser("user1", 1234);
		long userId = user.getUserProfileId();
		
		Calendar moment = Calendar.getInstance();
		moment.add(Calendar.YEAR, 1);
		
		BigDecimal initPrice = new BigDecimal("3.2");
		Product product = productService.announceProduct(userId, getRandomCategoryId(), "product1", "desc", "dshipment", initPrice, moment);
		long productId1 = product.getProductId();
		
		// Se realiza la primera puja
		Bid bid;
		bid = bidService.bidding(userId, productId1, initPrice);
		assertTrue(bid != null);
		assertTrue(bid.getMaxPrice().compareTo(initPrice) == 0);
		assertTrue(product.getCurrentPrice().compareTo(initPrice) == 0);
		assertEquals(bid.getUser(), bid.getProduct().getWinBid().getUser());// El que hizo esta apuesta es el que esta gandando
		
		// No poder realizar la apuesta con la misma cantidad
		try {
			bid = bidService.bidding(userId, productId1, initPrice);
			assertTrue(false);
		} catch (InsufficientAmountException iae) {
			assertTrue(true);
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Poder poner una apuesta con una cantidad mayor
		BigDecimal nextPrice = initPrice.add(BigDecimal.TEN);
		bid = bidService.bidding(userId, productId1, nextPrice);
		assertTrue(bid != null);
		assertTrue(bid.getMaxPrice().compareTo(nextPrice) == 0);
		assertTrue(product.getCurrentPrice().compareTo(initPrice) == 0);
		assertEquals(bid.getUser(), bid.getProduct().getWinBid().getUser());// El que hizo esta apuesta es el que esta gandando
			
		// No poder realizar una apuesta con una cantidad inferior
		try {
			bid = bidService.bidding(userId, productId1, nextPrice.subtract(BigDecimal.ONE));
			assertTrue(false);
		} catch (InsufficientAmountException iae) {
			assertTrue(true);
		}
		
		assertTrue(bid.getMaxPrice().compareTo(nextPrice) == 0);
		assertTrue(product.getCurrentPrice().compareTo(initPrice) == 0);
		assertEquals(bid.getUser(), bid.getProduct().getWinBid().getUser());// El que hizo esta apuesta es el que esta gandando

		List<Bid> bids = bidService.findByUserProfileId(userId, 0, TYPICAL_SIZE);
		assertTrue(bids.size() == 1);
		assertTrue(bidService.getNumberOfBidByUserProfileId(userId) == 1);
	}
}
