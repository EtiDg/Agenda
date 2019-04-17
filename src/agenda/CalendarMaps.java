package agenda;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CalendarMaps {
	
	static public Map<DayOfWeek, String> JOURS_ABREGES = new HashMap<DayOfWeek, String>() {{
		put(DayOfWeek.MONDAY, "L");
		put(DayOfWeek.TUESDAY, "M");
		put(DayOfWeek.WEDNESDAY, "M");
		put(DayOfWeek.THURSDAY, "J");
		put(DayOfWeek.FRIDAY, "V");
		put(DayOfWeek.SATURDAY, "S");
		put(DayOfWeek.SUNDAY, "D");
	}};
	
	static public Map<Month, String> MOIS = new HashMap<Month, String>() {{
		put(Month.JANUARY, "Janvier");
		put(Month.FEBRUARY, "Fevrier");
		put(Month.MARCH, "Mars");
		put(Month.APRIL, "Avril");
		put(Month.MAY, "Mai");
		put(Month.JUNE, "Juin");
		put(Month.JULY, "Juillet");
		put(Month.AUGUST, "Août");
		put(Month.SEPTEMBER, "Septembre");
		put(Month.OCTOBER, "Octobre");
		put(Month.NOVEMBER, "Novembre");
		put(Month.DECEMBER, "Décembre");
	}};
	
	static public Map<String, DayOfWeek> JOURS_SEMAINE = new HashMap<String, DayOfWeek>() {{
		put("lundi", DayOfWeek.MONDAY);
		put("mardi", DayOfWeek.TUESDAY);
		put("mercredi", DayOfWeek.WEDNESDAY);
		put("jeudi", DayOfWeek.THURSDAY);
		put("vendredi", DayOfWeek.FRIDAY);
		put("samedi", DayOfWeek.SATURDAY);
		put("dimanche", DayOfWeek.SUNDAY);
	}};
    
    static public ObservableList<String> JOURS = FXCollections.observableArrayList(
    		"lundi",
    		"mardi",
    		"mercredi",
    		"jeudi",
    		"vendredi",
    		"samedi",
    		"dimanche"
    		);

}
