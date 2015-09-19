/**
 * 
 */
package es.udc.is.isg016.minibay.model.card;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface CardDao extends GenericDao<Card,Long> {

	//Se usaba para eliminar la tarjeta
	//public int getNumberOfUser(long card);
}
