package agenda.process.object;

import java.sql.Date;
import java.util.Calendar;

public class TreveHivernale {

	private Calendar dateDebut;
	private Calendar dateFin;
	
	public TreveHivernale(Date dateDebut, Date dateFin){
		this.dateDebut.setTime(dateDebut);
		this.dateFin.setTime(dateFin); 
	}

	public Date getDateDebut() {
		return new java.sql.Date(dateDebut.getTimeInMillis());
	}

	public Date getDateFin() {
		return new java.sql.Date(dateFin.getTimeInMillis());
	}
	
}
