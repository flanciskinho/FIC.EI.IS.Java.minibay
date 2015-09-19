/**
 * 
 */
package es.udc.is.isg016.minibay.model.bidservice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg016.minibay.model.bid.Bid;
import es.udc.is.isg016.minibay.model.bid.BidDao;
import es.udc.is.isg016.minibay.model.product.Product;
import es.udc.is.isg016.minibay.model.product.ProductDao;
import es.udc.is.isg016.minibay.model.userprofile.UserProfile;
import es.udc.is.isg016.minibay.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * 
 *
 */
@Service("bidService")
@Transactional
public class BidServiceImpl implements BidService{

	@Autowired
	private BidDao bidDao;
	
    @Autowired
    private UserProfileDao userProfileDao;
    
    @Autowired
    private ProductDao productDao;
	
    
    @Transactional
    public List<Bid> findByUserProfileId(long userProfileId, int start, int size) {
    	return bidDao.findByUserProfileId(userProfileId, start, size);
    }
    
    @Transactional
    public int getNumberOfBidByUserProfileId(long userProfileId) {
    	return bidDao.getNumberOfBidByUserProfileId(userProfileId);
    }
    
	@Transactional(readOnly = false)
	public Bid bidding(long userProfileId, long productId, BigDecimal amount)
			throws InstanceNotFoundException, InsufficientAmountException, BiddingLateException {
		
		//Cogemos el usuario
		UserProfile user = userProfileDao.find(userProfileId);
		//Cogemos el producto
		Product product = productDao.find(productId);
		//La puja que va ganando
		Bid oldBid = product.getWinBid();
		
		//Comprobamos que apuesta mas de lo que vale el producto inicialmente y que el valor actual del producto
		if (amount.compareTo(product.getInitPrice()) == -1)
			throw new InsufficientAmountException(product.getProductName(), amount, product.getInitPrice());
		if (oldBid != null)
			if (amount.compareTo(product.getCurrentPrice()) != 1)
				throw new InsufficientAmountException(product.getProductName(), amount, product.getCurrentPrice());
		
		//Se ha realizado la apuesta demasiado tarde
		Calendar now = Calendar.getInstance();
		if (now.compareTo(product.getEndDate()) != -1)
			throw new BiddingLateException(product.getProductName(), product.getEndDate(), now);

		//Miramos si tiene una ya creada
		Bid lastBid, newBid = null;
		try {
			lastBid = bidDao.findLastBidByUserProfileIdAndProductId(userProfileId, productId);
			if (amount.compareTo(lastBid.getMaxPrice()) != 1)
				throw new InsufficientAmountException(product.getProductName(), amount, lastBid.getMaxPrice());
			
			newBid = new Bid(user, product, amount, now);
			// Si su apuesta ya era la que estaba ganando
			if (oldBid.getUser().getUserProfileId() == userProfileId) {
				bidDao.save(newBid);
				product.setWinBid(newBid);
				return newBid;
			}
			
			//Tiene apuesta anterior
			//newBid = lastBid;
			//newBid.setMaxPrice(amount);
			//newBid.setBidDate(now);
		} catch (InstanceNotFoundException infe) {
			// No tiene apuestas anteriores
			newBid = new Bid(user, product, amount, now);
		}
		
		//Llegados aqui podemos realizar la apuesta
		bidDao.save(newBid);
		
		//Es la primera apuesta
		if (oldBid == null) {
			product.setCurrentPrice(product.getInitPrice());
			product.setWinBid(newBid);
			return newBid;
		}
		
		// Si la apuesta que va ganando es la nuestra no modificamos el precio
		if (newBid.equals(oldBid))
			return newBid;
		
		Bid win  = null;
		Bid lost = null;		
		switch (oldBid.getMaxPrice().compareTo(amount)) {
			case -1: // oldBid < amount
				win  = newBid;
				lost = oldBid;
				break;
			case  0: // En el caso de que sean iguales se queda la vieja
				product.setCurrentPrice(oldBid.getMaxPrice());
				return oldBid;
			case  1: //oldBid > amout
				win  = oldBid;
				lost = newBid;
				break;
			default:
				throw new RuntimeException(); // Internal server error
		}
		
		//Ahora esta la parte para hacer los incrementos de 0.5 pero con una sola atacada
		BigInteger aux = lost.getMaxPrice().subtract(product.getCurrentPrice()).divide(INCREMENT).toBigInteger();
		aux = aux.add(BigInteger.ONE);
		
		BigDecimal value = INCREMENT.multiply(new BigDecimal(aux));
		value = value.add(product.getCurrentPrice());
		
		// tenemos que mirar que no se pase del valor maximo que se apostaria
		if (win.getMaxPrice().compareTo(value) != 1)
			value = win.getMaxPrice();
		
		product.setCurrentPrice(value);
		product.setWinBid(win);
		return win;
		
	}

	

}
