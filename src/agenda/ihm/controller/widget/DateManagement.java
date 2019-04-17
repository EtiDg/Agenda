package agenda.ihm.controller.widget;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.layout.AnchorPane;

public class DateManagement extends AnchorPane {
	
	String type;
	CalendarYear calendarYear;
	boolean isMultiple;
	ArrayList<LocalDate> selectedDates;
	
	public DateManagement(boolean isMultiple){
		this.isMultiple = isMultiple;
	}
	
	public void displayYear(int annee){
		getChildren().clear();
		type = "week";
		calendarYear = new CalendarYear(annee, isMultiple);
		AnchorPane.setTopAnchor(calendarYear, 5.0);
		AnchorPane.setLeftAnchor(calendarYear, 5.0);
		AnchorPane.setRightAnchor(calendarYear, 5.0);
		AnchorPane.setBottomAnchor(calendarYear, 5.0);
		getChildren().add(calendarYear);
	}
	
	public void addYear(){
		if(type.equals("year")){
			if(isMultiple){
				getSelectedDates();
			}
			displayYear(calendarYear.getYear()+1);
			if(isMultiple){
				calendarYear.selectDate(selectedDates);
			}
		}

	}
	
	public void removeYear(){
		if(type.equals("year")){
			if(isMultiple){
				getSelectedDates();
			}
			displayYear(calendarYear.getYear()-1);
			if(isMultiple){
				calendarYear.selectDate(selectedDates);
			}
		}
		
	}
	
	
	public void selectDates(ArrayList<LocalDate> dates){
		selectedDates = dates;
		if (type.equals("year")){
			calendarYear.selectDate(dates);
		}
		
	}
	
	public ArrayList<LocalDate> getSelectedDates(){
		if (type.equals("year")){
			int c = 0;
			while(c<selectedDates.size()){
				if (selectedDates.get(c).getYear() == calendarYear.getYear()){
					selectedDates.remove(c);
				}else{
					c = c+1;
				}
			}
			selectedDates.addAll(calendarYear.getSelectedDates());
		}
		return selectedDates;
	}
	
}
