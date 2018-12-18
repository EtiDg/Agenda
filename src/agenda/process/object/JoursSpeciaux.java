package agenda.process.object;

import java.util.ArrayList;

public class JoursSpeciaux {
	private TreveHivernale treveHivernale;
	private ArrayList<Vacances> vacances;
	private ArrayList<JourFerie> joursFeries;
	
	public JoursSpeciaux(TreveHivernale treveHivernale, ArrayList<Vacances> vacances, ArrayList<JourFerie> joursFeries){
		this.treveHivernale = treveHivernale;
		this.vacances = vacances;
		this.joursFeries = joursFeries;
	}
}
