/**
 * 
 */
package es.udc.is.isg016.minibay.model.card;

import org.springframework.stereotype.Repository;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("cardDao")
	public class CardDaoHibernate extends GenericDaoHibernate<Card,Long> implements CardDao {

	
	/*public int getNumberOfUser(long card) {
		Long number = (Long) getSession().createQuery(
				"SELECT COUNT(u) " +
				"FROM UserProfile u " +
				"WHERE u.card.number = :cardId")
				.setParameter("cardId", card)
				.uniqueResult();
		
		return (number != null)? number.intValue():0;
		
	}*/

}
