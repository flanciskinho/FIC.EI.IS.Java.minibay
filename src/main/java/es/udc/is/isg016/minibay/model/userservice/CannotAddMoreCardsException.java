package es.udc.is.isg016.minibay.model.userservice;

@SuppressWarnings("serial")
public class CannotAddMoreCardsException extends Exception {
	
	private String login;
	
	public CannotAddMoreCardsException(String login) {
		super("cannot add more card expection => login = " + login);
		this.login = login;
	}

	public String getLogin() {
		return this.login;
	}
}
