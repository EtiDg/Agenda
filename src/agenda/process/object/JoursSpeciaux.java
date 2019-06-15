package agenda.process.object;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import agenda.process.sql.QueryManager;

public class JoursSpeciaux {
	static private TreveHivernale treveHivernale;
	static private ArrayList<Vacances> vacances;
	static private ArrayList<JourFerie> joursFeries;
	
	public static void load(){
		try {
			JoursSpeciaux.treveHivernale = QueryManager.selectTreveHivernale();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement de la trève hivernale");
			System.exit(0);
		}
		try {
			JoursSpeciaux.vacances = QueryManager.selectListeVacances();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des vacances");
			System.exit(0);
		}
		try {
			JoursSpeciaux.joursFeries = QueryManager.selectListeJoursFeries();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des jours fériés");
			System.exit(0);
		}
	}
	
	public static boolean isTreveHivernale(LocalDate date){
		if ( date.isAfter(treveHivernale.getDateDebut()) && date.isBefore(treveHivernale.getDateFin())){
			return true;
		}
		return false;
	}
	
	public static boolean isVacances(LocalDate date){
		boolean result = false;
		for (int i = 0; i < vacances.size();i++){
			if ( date.isAfter(vacances.get(i).getDateDebut()) && date.isBefore(vacances.get(i).getDateFin())){
				result = true;
			}
		}
		return result;
	}

	public static boolean isFerie(LocalDate date){
		for (int i = 0; i < joursFeries.size();i++){
			if ( date.isEqual(joursFeries.get(i).getDate()) ){
				return true;
			}
		}
		return false;
	}
	
}
