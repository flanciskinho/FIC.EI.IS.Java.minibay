/**
 * 
 */
package es.udc.is.isg016.minibay.model.userservice;

import java.util.Calendar;

/**
 * @author flanciskinho
 *
 */
@SuppressWarnings("serial")
public class ExpireDateException extends Exception {
	private String login;
	private Calendar expire;
	
	public ExpireDateException(String login, Calendar expire) {
		super("expire date exception => login = " + login + " expire = " +
				expire.get(Calendar.DAY_OF_MONTH) + "/" + expire.get(Calendar.MONTH) + "/" + expire.get(Calendar.YEAR));
		
		this.login  = login;
		this.expire = expire;
	}
	
	public String getLogin() {
		return login;
	}

	public Calendar getExpire() {
		return expire;
	}

}
