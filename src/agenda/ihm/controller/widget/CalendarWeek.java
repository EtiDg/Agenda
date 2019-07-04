
package agenda.ihm.controller.widget;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agenda.CalendarMaps;
import agenda.process.object.Reprise;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class CalendarWeek extends AnchorPane{
	
	public static final int HEURE_MIN = 6; 
	public static final int HEURE_MAX = 19;
	private static final int MINUTES[] = { 00, 15, 30, 45 };
	
	
	LocalDate date; // the first day of the week
	protected GridPane calendrier;
	protected ScrollPane scrollPane;
	public Map<LocalDate, Map<Integer, Integer>> cellMap = new HashMap<LocalDate, Map<Integer, Integer>>();

	
	public CalendarWeek(){
		scrollPane = new ScrollPane();
		calendrier = new GridPane();

		AnchorPane.setTopAnchor(scrollPane, 0.0);
		AnchorPane.setLeftAnchor(scrollPane, 0.0);
		AnchorPane.setRightAnchor(scrollPane, 0.0);
		AnchorPane.setBottomAnchor(scrollPane, 0.0);
		
		//ajoute des lignes pour délimiter les cellules
		//calendrier.getStyleClass().add("calendrier");
		//calendrier.setVgap(1);
		calendrier.setHgap(1);

		scrollPane.setContent(calendrier);
		getChildren().addAll(scrollPane);
	}
	
	public CalendarWeek(ArrayList<Reprise> reprises, LocalDate date){
		
		//convert the date into first day of the week
		this.date = date.minusDays(date.getDayOfWeek().getValue()-1);
		
		scrollPane = new ScrollPane();
		calendrier = new GridPane();

		AnchorPane.setTopAnchor(scrollPane, 0.0);
		AnchorPane.setLeftAnchor(scrollPane, 0.0);
		AnchorPane.setRightAnchor(scrollPane, 0.0);
		AnchorPane.setBottomAnchor(scrollPane, 0.0);
		
		//ajoute des lignes pour délimiter les cellules
		//calendrier.getStyleClass().add("calendrier");
		calendrier.setVgap(1);
		//calendrier.setHgap(1);

		buildCalendrier(reprises);
		scrollPane.setContent(calendrier);
		getChildren().addAll(scrollPane);

	}

//	public void selectReprise(Reprise reprise){
//		if (reprise.getHeureDebut() >= HEURE_MIN*60 && reprise.getHeureFin() < HEURE_MAX*60 );
//		int cell = getCell(reprise);
//		//collection.get(cell).getCreneau().setId(creneau.getId());
//		if (cell>=0){
//			GridPane.setColumnSpan(collection.get(cell), ( reprise.getHeureFin() - reprise.getHeureDebut() )/15);
//			selectCell(cell);
//		}
//	}
	
//	public void selectReprise(ArrayList<Reprise> reprises){
//		for (Reprise reprise : reprises){
//			selectReprise(reprise);
//		}
//	}

	private int getCell(Reprise reprise){
		if (cellMap.containsKey(reprise.getDate()) && cellMap.get(date).containsKey(reprise.getHeureDebut())){
			return cellMap.get(reprise.getDate()).get(reprise.getHeureDebut());	
		}
		return -1;
	}
	
//	public ArrayList<Creneau> getSelectedCreneaux(){
//		ArrayList<Creneau> selectedCreneaux = new ArrayList<Creneau>();
//		for (int i=0; i<selectedCells.size(); i++){
//			selectedCreneaux.add(collection.get(selectedCells.get(i)).getCreneau());
//		}
//		return selectedCreneaux;
//	}
	
	public LocalDate getWeekDate(){
		return date;
	}
	
	public void updateCalendrier(ArrayList<Reprise> reprises, LocalDate date){
		this.date = date.minusDays(date.getDayOfWeek().getValue()-1);
		calendrier.getChildren().clear();
		buildCalendrier(reprises);
	}
//************************************************************************************************
// construction du calendrier	
//************************************************************************************************	
	
	protected ArrayList<ArrayList<Reprise>> reprises;
	protected ArrayList<ArrayList<String>> lieux;
	
	private void buildCalendrier(ArrayList<Reprise> listeReprises){
		
		trierListes(listeReprises);
		
		//affiche les heures
		afficherHeures();
				
		//construction du calendrier
		int col = 1;
		for (int jour = 0; jour < 6; jour++) {
			afficherJour(jour, col);

			for (String lieu : lieux.get(jour)) {
				afficherLieu(col, lieu);
				afficherReprises(jour, col, lieu);
				col++;
			}
		}
		
//		int c=0;
//		LocalDate date = this.date;
//		for(int i=0;i<7;i++){
//			cellMap.put( date, new HashMap<Integer, Integer>() );
//			calendrier.add(new Label(CalendarMaps.JOURS.get(i)),i, 0);
//			int p=0;
//			for(int j=HEURE_MIN; j<HEURE_MAX;j++){
//				for(int k=0; k<4;k++){
//					CreneauCell cell = new CreneauCell(c, j, MINUTES[4]);
//					collection.add(cell);
//					calendrier.add(cell, c, p);
//					cellMap.get(date).put(j*60+k*15, c);
//					c = c+1;
//					p = p+1;
//				}
//			}
//			date.plusDays(1);
//		}

	}
	
	private void trierListes(ArrayList<Reprise> listeReprises){
		// tri des reprise par jours
		reprises = new ArrayList<ArrayList<Reprise>>();
		for (int i=0; i<7; i++){
			reprises.add(new ArrayList<Reprise>());
		}
		for (Reprise reprise : listeReprises) {
			int day = reprise.getDate().getDayOfWeek().getValue()-1;
			reprises.get(day).add(reprise);
		}
		
		// tri des lieux par jours
		lieux = new ArrayList<ArrayList<String>>();
		for (int i=0; i<7; i++){
			lieux.add(new ArrayList<String>());
		}
		// recherche des lieux par jours
		int j = 0;
		for (ArrayList<Reprise> jour : reprises) {
			for (Reprise reprise : jour) {
				if (!lieux.get(j).contains(reprise.getLieu().getNom()))
					lieux.get(j).add(reprise.getLieu().getNom());
			}
			j++;
		}
	}
	
	private void afficherHeures(){
		// ajout de l'année
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		calendrier.add(new Label(formatter.format(date)), 0, 0);
		int rowNum = 2;
		for (int i = HEURE_MIN; i < HEURE_MAX; i++) {
			for (int j = 0; j < 4; j++) {
				calendrier.add(new Label(String.valueOf(i) + "h" + MINUTES[j]), 0, rowNum++);
			}
		}
	}
	
	private void afficherJour(int jour, int col){
		if (reprises.get(jour).size() > 0){
			Label nomJour = new Label(CalendarMaps.JOURS.get(jour));
			if(lieux.get(jour).size()>=2){
				GridPane.setColumnSpan(nomJour, lieux.get(jour).size());
			}
			calendrier.add(nomJour, col, 0);
		}
	}
	
	public void afficherLieu(int col, String lieu){
		calendrier.add(new Label(lieu), col, 1);
	}
	
	public void afficherReprises(int jour, int col, String lieu){
		for (Reprise reprise : reprises.get(jour)) {
			if (reprise.getLieu().getNom().equals(lieu) && reprise.getHeureDebut() >= HEURE_MIN*60 && reprise.getHeureFin() < HEURE_MAX*60) {
				int numRowHeureDebut = 4*reprise.getHeureDebut()/60 - 4*HEURE_MIN + 2;
				//int numRowHeureFin = (4*reprise.getHeureFin()-1)/60 - 4*HEURE_MIN + 2;
				int numberOfRow = (4*reprise.getHeureFin()-1)/60 - 4*reprise.getHeureDebut()/60 + 1;
				RepriseCell cell = new RepriseCell(reprise);
				if(numberOfRow > 0){
					GridPane.setRowSpan(cell, numberOfRow);
				}
				calendrier.add(cell, col, numRowHeureDebut);
			}
		}
	}
	
	public LocalDate getDate(){
		return date;
	}
}


	
