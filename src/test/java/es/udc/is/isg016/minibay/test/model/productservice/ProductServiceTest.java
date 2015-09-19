package es.udc.is.isg016.minibay.test.model.productservice;

import static es.udc.is.isg016.minibay.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.is.isg016.minibay.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg016.minibay.model.bidservice.InsufficientAmountException;
import es.udc.is.isg016.minibay.model.category.Category;
import es.udc.is.isg016.minibay.model.category.CategoryDao;
import es.udc.is.isg016.minibay.model.categoryservice.CategoryService;
import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.product.ProductDao;
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
public class ProductServiceTest {
	
	private final long NON_EXISTENT_PRODUCT_ID = -1;
	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	private final long NON_EXISTENT_CATEGORY_ID = -1;
	private final int  TYPICAL_SIZE = 10;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private ProductDao productDao;
	
	
	/********************************/
	/********** Util methos *********/
	/********************************/
	
	
	public Calendar setEndDate(){
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.YEAR, 1);
		
		return endDate;
	}
	
	public Calendar setCardDate(){
		Calendar exCardDate = Calendar.getInstance();
		exCardDate.add(Calendar.YEAR, 2);
		exCardDate.set(Calendar.MONTH,Calendar.JUNE);
		
		return exCardDate;
	}
	
	public UserProfile registerNonSellerUser(){
		try {
			return userService.registerUser("userNot", "userPasswordNot",
			        new UserProfileDetails("name1", "lastName1", "userNot@udc.es"));
		
		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}
	}
	
	public UserProfile registerSellerUser(){
		UserProfile user = null;
		
		try {
			user = userService.registerUser("user", "userPassword",
			        new UserProfileDetails("name", "lastName", "user@udc.es"));
			
			userService.setRoleSeller(user.getUserProfileId(), new Long(1234567890), setCardDate());
			
		} catch (DuplicateInstanceException | InstanceNotFoundException
				| CannotAddMoreCardsException | ExpireDateException e) {
			throw new RuntimeException(e);
		}
		
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
	
	
	
	/*******************/
	/****** Tests ******/
	/******************/
	
	@Test
	public void testFindProductByCategory()
		throws DuplicateInstanceException, InstanceNotFoundException, AnnounceLateException, InsufficientAmountException, NotSellerException {
		
		UserProfile user = registerSellerUser();
		Calendar endDate = setEndDate();
		//Create category
		
		long catId = addCategoryByName("Electronica").getCategoryId();

		
		Product product = productService.announceProduct(user.getUserProfileId(), catId, "Coche", "Coche nuevo", 
				"Pago con tarjeta", new BigDecimal(5000),endDate);
		
		
		//Buscamos el producto
		List<Product> prod = productService.findByNameByCategory(null, catId, 0, TYPICAL_SIZE);
		
		assertTrue(prod.size() == 1);
		assertTrue(prod.contains(product));
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentProduct()
			throws InstanceNotFoundException{
		
		productDao.find(NON_EXISTENT_PRODUCT_ID);
	}
	
	@Test(expected = NotSellerException.class)
	public void testAnnounceOfNonSellerUser()
			throws NotSellerException, DuplicateInstanceException, InstanceNotFoundException, AnnounceLateException, InsufficientAmountException{
		
		//Create category
		long catId = addCategoryByName("Electronica").getCategoryId();

		
		productService.announceProduct(registerNonSellerUser().getUserProfileId(), catId, "Coche", "Coche nuevo", 
					"Pago con tarjeta", new BigDecimal(5000),setEndDate());
	}
	
	@Test
	public void testListProductsOfNonExistentUser(){
		
			List<Product> prod = productService.myProducts(NON_EXISTENT_USER_PROFILE_ID, 0, TYPICAL_SIZE);
			long number = productService.getNumberOfMyProducts(NON_EXISTENT_USER_PROFILE_ID);
			
			assertTrue(prod.isEmpty());
			assertTrue(prod.size() == number);
			assertTrue(number == 0);
	}
	
	@Test
	public void testFindProductByKeyWord()
		throws DuplicateInstanceException, InstanceNotFoundException, AnnounceLateException, InsufficientAmountException, NotSellerException{
		
		long userId = registerSellerUser().getUserProfileId();
		Calendar endDate = setEndDate();
		
		//Create category
		long catId1 = addCategoryByName("Automovilismo").getCategoryId();
		long catId2 = addCategoryByName("Electronica")  .getCategoryId();

		productService.announceProduct(userId, catId2, "Lavadora ultima generacion", "Gilette", 
				"Pago por paypal", new BigDecimal(100),endDate);
		productService.announceProduct(userId, catId1, "Coche", "Automovil azul con 5 puertas", 
				"Pago por transferencia bancaria", new BigDecimal(10000),endDate);
		productService.announceProduct(userId, catId1, "Coche familiar", "Vehiculo seminuevo con 100000 km", 
				"Pago con tarjeta", new BigDecimal(5000),endDate);
		productService.announceProduct(userId, catId1, "Coche tipo familiar", "Vehiculo segunda mano", 
				"Pago con tarjeta", new BigDecimal(7000),endDate);
		productService.announceProduct(userId, catId1, "Televisor para coche", "Marca Electronica10 42 pulgadas", 
				"Envio solo a peninsula", new BigDecimal(1000),endDate);
		productService.announceProduct(userId, catId2, "Secador de pelo", "Secador negro de 3 velocidades", 
				"Pago contra reembolso", new BigDecimal(40),endDate);
	
		
		List<Product> lp;
		lp = productService.findByNameByCategory("coche", null, 0, TYPICAL_SIZE);
		assertTrue(lp.size() == 4);
		
		lp = productService.findByNameByCategory("familiar coche", null, 0, TYPICAL_SIZE);
		assertTrue(lp.size() == 2);
		
		lp = productService.findByNameByCategory("telivisor familiar", null, 0, TYPICAL_SIZE);
		assertTrue(lp.size() == 0);
	}
	
	@Test
	public void testFindProductById()
			throws InstanceNotFoundException, DuplicateInstanceException, AnnounceLateException, InsufficientAmountException, NotSellerException{
		
		long userId = registerSellerUser().getUserProfileId();
		Calendar endDate = setEndDate();
		Product p3,prod;
		
		//Create category
		long catId1 = addCategoryByName("Automovilismo").getCategoryId();
		long catId2 = addCategoryByName("Electronica")  .getCategoryId();
		
	
		productService.announceProduct(userId, catId2, "Lavadora ultima generacion", "Gilette", 
				"Pago por paypal", new BigDecimal(100),endDate);
		productService.announceProduct(userId, catId1, "Coche", "Automovil azul con 5 puertas", 
			"Pago por transferencia bancaria", new BigDecimal(10000),endDate);
		
		p3 = productService.announceProduct(userId, catId1, "Coche familiar", "Vehiculo seminuevo con 100000 km", 
			"Pago con tarjeta", new BigDecimal(5000),endDate);
		
		prod = productDao.find(p3.getProductId());
	
		assertEquals(prod.getProductId(),p3.getProductId());
		
		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testFindProductByNonExistentId() throws InstanceNotFoundException{
		
		productDao.find(NON_EXISTENT_PRODUCT_ID);
	}
	
	public void testListAllProducts()
			throws InstanceNotFoundException, AnnounceLateException, InsufficientAmountException, NotSellerException, DuplicateInstanceException{
		
		UserProfile user = registerSellerUser();
		Calendar endDate = setEndDate();
				
		//Create category
		Category cat, categ;
		cat = addCategoryByName("Automovilismo");
		categ = addCategoryByName("Electronica");
	
		productService.announceProduct(user.getUserProfileId(), categ.getCategoryId(), "Lavadora ultima generacion", "Gilette", 
				"Pago por paypal", new BigDecimal(100),endDate);
		productService.announceProduct(user.getUserProfileId(), cat.getCategoryId(), "Coche", "Automovil azul con 5 puertas", 
				"Pago por transferencia bancaria", new BigDecimal(10000),endDate);
		productService.announceProduct(user.getUserProfileId(), cat.getCategoryId(), "Coche familiar", "Vehiculo seminuevo con 100000 km", 
				"Pago con tarjeta", new BigDecimal(5000),endDate);
		productService.announceProduct(user.getUserProfileId(), cat.getCategoryId(), "Coche tipo familiar", "Vehiculo segunda mano", 
				"Pago con tarjeta", new BigDecimal(7000),endDate);
		productService.announceProduct(user.getUserProfileId(), cat.getCategoryId(), "Televisor para coche", "Marca Electronica10 42 pulgadas", 
				"Envio solo a peninsula", new BigDecimal(1000),endDate);
		productService.announceProduct(user.getUserProfileId(), categ.getCategoryId(), "Secador de pelo", "Secador negro de 3 velocidades", 
				"Pago contra reembolso", new BigDecimal(40),endDate);

		
		assertEquals(productService.getNumberOfNameByCategory(null, null), 6);
	}
	
	@Test
	public void testFindEmptyListOfAllProducts() {
		assertEquals(productService.getNumberOfNameByCategory(null, null),0);
		assertTrue(productService.findByNameByCategory(null, null, 0, TYPICAL_SIZE).isEmpty());
	}
	
	
	@Test
	public void testFindProductByKeywordFilteredByCategory()
			throws DuplicateInstanceException, InstanceNotFoundException, AnnounceLateException, InsufficientAmountException, NotSellerException {
		
		UserProfile user = registerSellerUser();
		long userId = user.getUserProfileId();
		
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.YEAR, 1);
		
		//Create category
		long catId1 = addCategoryByName("Automovilismo").getCategoryId();
		long catId2 = addCategoryByName("Electronica").getCategoryId();

		productService.announceProduct(userId, catId2, "Lavadora ultima generacion", "Gilette", 
					"Pago por paypal", new BigDecimal(100),endDate);
		productService.announceProduct(userId, catId1, "Coche", "Automovil azul con 5 puertas", 
					"Pago por transferencia bancaria", new BigDecimal(10000),endDate);
		productService.announceProduct(userId, catId1, "Coche familiar", "Vehiculo seminuevo con 100000 km", 
					"Pago con tarjeta", new BigDecimal(5000),endDate);
		productService.announceProduct(userId, catId1, "Coche tipo familiar", "Vehiculo segunda mano", 
					"Pago con tarjeta", new BigDecimal(7000),endDate);
		productService.announceProduct(userId, catId2, "Televisor para coche", "Marca Electronica10 42 pulgadas", 
					"Envio solo a peninsula", new BigDecimal(1000),endDate);
		productService.announceProduct(userId, catId2, "Secador de pelo", "Secador negro de 3 velocidades", 
					"Pago contra reembolso", new BigDecimal(40),endDate);

		assertTrue(productService.getNumberOfNameByCategory(null, null) == 6); // Todos insertados
		
		endDate = Calendar.getInstance();
		List<Product> lp;
		int number;
		
		lp = productService.findByNameByCategory("Coche", catId1, 0, TYPICAL_SIZE);
		number = productService.getNumberOfNameByCategory("cOche", catId1);
		assertTrue(lp.size() == 3);
		assertTrue(number == 3);
		
		lp = productService.findByNameByCategory("familiar coche", catId1, 0, TYPICAL_SIZE);
		number = productService.getNumberOfNameByCategory("coche familiar", catId1);
		assertTrue(lp.size() == 2);
		assertTrue(number == 2);
		
		lp = productService.findByNameByCategory("secador ultima generacion", catId2, 0, TYPICAL_SIZE);
		number = productService.getNumberOfNameByCategory("ultima generacion secador", catId2);
		assertTrue(lp.size() == 0);
		assertTrue(number == 0);
	}
	
	
	@Test(expected = InstanceNotFoundException.class)
	public void testFindProductByKeywordFilteredByNonExistentCategory()
			throws InstanceNotFoundException, AnnounceLateException, InsufficientAmountException, NotSellerException{
		
		UserProfile user = registerSellerUser();
		Calendar endDate = setEndDate();

		productService.announceProduct(user.getUserProfileId(),NON_EXISTENT_CATEGORY_ID, "Lavadora ultima generacion", "Gilette", 
					"Pago por paypal", new BigDecimal(100),endDate);
		
		List<Product> lp = productService.findByNameByCategory("coche", NON_EXISTENT_CATEGORY_ID, 0, TYPICAL_SIZE);

		assertTrue(lp.isEmpty());
		assertTrue(lp.size() == 0);
	}
	
}
