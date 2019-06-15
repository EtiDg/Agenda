package agenda.ihm.controller.widget;

import java.time.LocalDate;
import java.util.ArrayList;

import agenda.process.object.Creneau;
import javafx.scene.layout.AnchorPane;

public class CreneauManagement extends AnchorPane {
	
	CalendarCreneau calendarCreneau;
	long monitrice;
	boolean isMultiple;
	ArrayList<Creneau> selectedCreneaux = new ArrayList<Creneau>();
	
	public CreneauManagement(boolean isMultiple, long monitrice){
		this.monitrice = monitrice;
		this.isMultiple = isMultiple;
	}
	
	public void displayWeek(LocalDate date){
		getChildren().clear();
		calendarCreneau = new CalendarCreneau(date, monitrice, isMultiple);
		AnchorPane.setTopAnchor(calendarCreneau, 5.0);
		AnchorPane.setLeftAnchor(calendarCreneau, 5.0);
		AnchorPane.setRightAnchor(calendarCreneau, 5.0);
		AnchorPane.setBottomAnchor(calendarCreneau, 5.0);
		getChildren().add(calendarCreneau);
	}
	
	public void addWeek(){
		if(isMultiple){
			getSelectedCreneaux();
		}
		displayWeek(calendarCreneau.getWeekDate().plusWeeks(1));
		if(isMultiple){
			calendarCreneau.selectCreneau(selectedCreneaux);
		}
	}
	
	public void removeWeek(){
		if(isMultiple){
			getSelectedCreneaux();
		}
		displayWeek(calendarCreneau.getWeekDate().minusWeeks(1));
		if(isMultiple){
			calendarCreneau.selectCreneau(selectedCreneaux);
		}
	}
	
	public void selectCreneaux(ArrayList<Creneau> creneaux){
		this.selectedCreneaux = creneaux;
		calendarCreneau.selectCreneau(creneaux);
	}
	
	public ArrayList<Creneau> getSelectedCreneaux(){
		int c = 0;
		while(c<selectedCreneaux.size()){
			if (calendarCreneau.testCreneau(selectedCreneaux.get(c))){
				selectedCreneaux.remove(c);
			}else{
				c = c+1;
			}
		}
		selectedCreneaux.addAll(calendarCreneau.getSelectedCreneaux());
		return selectedCreneaux;
	}

}
