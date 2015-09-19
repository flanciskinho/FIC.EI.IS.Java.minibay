/**
 * 
 */
package es.udc.is.isg016.minibay.model.productservice;

import java.util.Calendar;

@SuppressWarnings("serial")
public class AnnounceLateException extends Exception {
	public Calendar getMoment() {
		return moment;
	}

	public Calendar getLimit() {
		return limit;
	}

	private Calendar moment, limit;
	
	public AnnounceLateException(Calendar moment, Calendar limit) {
		super("announce late exception ==>" +
		" endDate = " +
		limit.get(Calendar.HOUR) + ":" + limit.get(Calendar.MINUTE) + ":" + limit.get(Calendar.SECOND) +
		" " +
		limit.get(Calendar.DAY_OF_MONTH) + "/" + limit.get(Calendar.MONTH) + "/" + limit.get(Calendar.YEAR) +
		" now = " + 
		moment.get(Calendar.HOUR) + ":" + moment.get(Calendar.MINUTE) + ":" + moment.get(Calendar.SECOND) +
		" " +
		moment.get(Calendar.DAY_OF_MONTH) + "/" + moment.get(Calendar.MONTH) + "/" + moment.get(Calendar.YEAR));

		
		this.moment = moment;
		this.limit  = limit;
	}
	
	
}
