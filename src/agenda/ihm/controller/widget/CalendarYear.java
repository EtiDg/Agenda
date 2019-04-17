package agenda.ihm.controller.widget;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agenda.CalendarMaps;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class CalendarYear extends CellCollection<YearCell>{
	
	int year;
	protected GridPane calendrier;
	public Map<LocalDate, Integer> cellMap = new HashMap<LocalDate, Integer>();

	public CalendarYear(int annee, boolean isMultiple){
		super(isMultiple);
		this.year = annee;
		calendrier = new GridPane();
		AnchorPane.setTopAnchor(calendrier, 0.0);
		AnchorPane.setLeftAnchor(calendrier, 0.0);
		AnchorPane.setRightAnchor(calendrier, 0.0);
		AnchorPane.setBottomAnchor(calendrier, 0.0);

		//ajoute des lignes pour délimiter les cellules
		calendrier.getStyleClass().add("calendrier");
		calendrier.setVgap(1);
		calendrier.setHgap(1);

		buildCalendrier();

		getChildren().addAll(calendrier);

	}

	private void buildCalendrier(){
		//construction du calendrier
		LocalDate date = LocalDate.of(year, 1, 1);

		int c = 0;
		while (date.getYear() == year){
			if(date.getDayOfMonth()==1){
				calendrier.add(new Label(CalendarMaps.MOIS.get(date.getMonth())), date.getMonthValue(),0);
			}
			YearCell cell = new YearCell(c,date);
			collection.add(cell);
			calendrier.add(cell, date.getMonthValue(), date.getDayOfMonth());
			cellMap.put(date, c);
			c = c+1;
			date = date.plusDays(1);
		}

	}
	
	public void selectDate(LocalDate date){
		int cell = getCell(date);
		if (cell>=0){
			selectCell(cell);
		}
	}
	
	public void selectDate(ArrayList<LocalDate> dates){
		for (int i=0; i<dates.size();i++){
			selectDate(dates.get(i));
		}
	}

	private int getCell(LocalDate date){
		if (cellMap.containsKey(date)){
			return cellMap.get(date);
		}else{
			return -1;
		}
	}
	
	public ArrayList<LocalDate> getSelectedDates(){
		ArrayList<LocalDate> selectedDates = new ArrayList<LocalDate>();
		for (int i=0; i<selectedCells.size(); i++){
			selectedDates.add(collection.get(selectedCells.get(i)).getDate());
		}
		return selectedDates;
	}
	
	public int getYear(){
		return year;
	}

}
