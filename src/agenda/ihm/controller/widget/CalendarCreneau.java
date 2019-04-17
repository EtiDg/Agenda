package agenda.ihm.controller.widget;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agenda.CalendarMaps;
import agenda.process.object.Creneau;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class CalendarCreneau extends CellCollection<WeekCell>{
	
	public static int HEURE_DEBUT = 6; 
	public static int HEURE_FIN = 21;
	public static int DUREE_CRENEAU = 15;

	LocalDate date; // the first day of the week
	protected GridPane calendrier;
	public Map<LocalDate, Map<Integer,Integer> > cellMap = new HashMap<LocalDate, Map<Integer, Integer>>();

	public CalendarCreneau(LocalDate date, boolean isMultiple){
		super(isMultiple);
		this.date = date;
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
		int c=0;
		LocalDate date = this.date;
		for(int i=0;i<7;i++){
			cellMap.put(date, new HashMap<Integer,Integer>() );
			calendrier.add(new Label(CalendarMaps.JOURS.get(i)),i, 0);
			int p=0;
			for(int j=HEURE_DEBUT; j<=HEURE_FIN;j++){
				for(int k=0; k<60/DUREE_CRENEAU;k++){
					WeekCell cell = new WeekCell(c,date,j*60+k*DUREE_CRENEAU);
					collection.add(cell);
					calendrier.add(cell, c, p);
					cellMap.get(date).put(j*60+k, c);
					c = c+1;
					p = p+1;
				}
			}
			date.plusDays(1);
		}

	}
	
	public void selectCreneau(LocalDate date, int heure){
		int cell = getCell(date, heure);
		if (cell>=0){
			selectCell(cell);
		}
	}
	
	public void selectCreneau(ArrayList<Creneau> creneaux){
		for (int i=0; i<creneaux.size();i++){
			selectCreneau(creneaux.get(i).getDate(), creneaux.get(i).getHeureDebut());
		}
	}

	private int getCell(LocalDate date, int heure){
		if (cellMap.containsKey(date) && cellMap.get(date).containsKey(heure)){
			return cellMap.get(date).get(heure);	
		}
		return -1;
		
	}
	
	public ArrayList<Integer> getSelectedHeures(){
		ArrayList<Integer> selectedHeures = new ArrayList<Integer>();
		for (int i=0; i<selectedCells.size(); i++){
			selectedHeures.add(collection.get(selectedCells.get(i)).getHeure());
		}
		return selectedHeures;
	}
	
	public LocalDate getWeekDate(){
		return date;
	}
	
}
