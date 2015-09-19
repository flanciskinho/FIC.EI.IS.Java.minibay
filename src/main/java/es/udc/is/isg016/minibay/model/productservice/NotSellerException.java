/**
 * 
 */
package es.udc.is.isg016.minibay.model.productservice;

@SuppressWarnings("serial")
public class NotSellerException extends Exception {
	private String login;
	
	public NotSellerException(String login) {
		super("not seller exception => login = " + login);
		this.login = login;
	}
	
	public String getLogin() {
		return this.login;
	}
}
