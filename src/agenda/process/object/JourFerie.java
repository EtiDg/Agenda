package agenda.process.object;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JourFerie {
	
	private LocalDate date;
	
	public JourFerie(LocalDate date){
		this.date = date;
	}
	
	public LocalDate getDate(){
		return date;
	}
	
	public Date getSQLDate(){
		return Date.valueOf(date);
	}
	
	@Override
	public String toString(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return formatter.format(date);
	}
}
