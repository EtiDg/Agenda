package agenda.process.object;

import java.sql.Date;
import java.util.Calendar;

public class JourFerie {
	
	private Calendar date;
	
	public JourFerie(Date date){
		this.date.setTime(date);
	}
	
	public Date getDate(){
		return new java.sql.Date(date.getTimeInMillis());
	}
}
