package agenda.ihm.controller.widget;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agenda.CalendarMaps;
import agenda.process.object.Creneau;
import agenda.process.object.Monitrice;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class CalendarCreneau extends CellCollection<CreneauCell>{
	
	public static int HEURE_DEBUT = 6; 
	public static int HEURE_FIN = 21;
	public static int DUREE_CRENEAU = 15;

	LocalDate date; // the first day of the week
	long monitrice;
	protected GridPane calendrier;
	public Map<LocalDate, Map<Integer, Integer>> cellMap = new HashMap<LocalDate, Map<Integer, Integer>>();

	public CalendarCreneau(LocalDate date, long monitrice, boolean isMultiple){
		super(isMultiple);
		this.date = date;
		this.monitrice = monitrice;
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
			cellMap.put( date, new HashMap<Integer, Integer>() );
			calendrier.add(new Label(CalendarMaps.JOURS.get(i)),i, 0);
			int p=0;
			for(int j=HEURE_DEBUT; j<=HEURE_FIN;j++){
				for(int k=0; k<60/DUREE_CRENEAU;k++){
					CreneauCell cell = new CreneauCell(c, new Creneau(0, monitrice, date,j*60+k*DUREE_CRENEAU, j*60+(k+1)*DUREE_CRENEAU));
					collection.add(cell);
					calendrier.add(cell, c, p);
					cellMap.get(date).put(j*60+k*DUREE_CRENEAU, c);
					c = c+1;
					p = p+1;
				}
			}
			date.plusDays(1);
		}

	}
	
	public void selectCreneau(Creneau creneau){
		int cell = getCell(creneau);
		collection.get(cell).getCreneau().setId(creneau.getId());
		if (cell>=0){
			selectCell(cell);
		}
	}
	
	public void selectCreneau(ArrayList<Creneau> creneaux){
		for (int i=0; i<creneaux.size();i++){
			selectCreneau(creneaux.get(i));
		}
	}

	private int getCell(Creneau creneau){
		if (cellMap.containsKey(creneau.getDate()) && cellMap.get(date).containsKey(creneau.getHeureDebut())){
			return cellMap.get(creneau.getDate()).get(creneau.getHeureDebut());	
		}
		return -1;
	}
	
	public boolean testCreneau(Creneau creneau){
		return (cellMap.containsKey(creneau.getDate()) && cellMap.get(date).containsKey(creneau.getHeureDebut()));
	}
	
	public ArrayList<Creneau> getSelectedCreneaux(){
		ArrayList<Creneau> selectedCreneaux = new ArrayList<Creneau>();
		for (int i=0; i<selectedCells.size(); i++){
			selectedCreneaux.add(collection.get(selectedCells.get(i)).getCreneau());
		}
		return selectedCreneaux;
	}
	
	public LocalDate getWeekDate(){
		return date;
	}
	
}
