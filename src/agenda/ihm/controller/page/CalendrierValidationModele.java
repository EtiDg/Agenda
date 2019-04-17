package agenda.ihm.controller.page;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;

import agenda.MainApp;
import agenda.ihm.controller.widget.DateManagement;
import agenda.ihm.event.ValiderModeleEvent;
import agenda.process.object.Lieu;
import agenda.process.object.Reprise;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class CalendrierValidationModele extends AnchorPane{
	
	ArrayList<Reprise> reprises;
	DateManagement calendrier;
	int annee;
	@FXML BorderPane borderPane;
	
	public CalendrierValidationModele(ArrayList<Reprise> reprises){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/CalendrierValidationModele.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.reprises = reprises;
		annee = Year.now().getValue();
		calendrier = new DateManagement(true);
		calendrier.displayYear(annee);
		setSelected();
		borderPane.setCenter(calendrier);
	}
		
	public void setSelected(){
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();  
		for (int i=0; i<reprises.size(); i++){ 
			dates.add(reprises.get(i).getDate());
		}
		calendrier.selectDates(dates);
	}
	
	public ArrayList<Reprise> getReprises(){
		ArrayList<LocalDate> selectedDates = calendrier.getSelectedDates();
		ArrayList<Reprise> nouvelleReprises = new ArrayList<Reprise>();
		Reprise reprise = reprises.get(0);
		for (int i =0; i<selectedDates.size(); i++){
			nouvelleReprises.add(new Reprise(reprise.getNom(), selectedDates.get(i), 
					reprise.getHeureDebut(), reprise.getHeureFin(), 
					reprise.getIdMR(), reprise.getLieu()));
		}
		return  nouvelleReprises;
	}
	

	public void handleValider(ActionEvent e){
		fireEvent(new ValiderModeleEvent());
	}
	
	public void handleAnneeSuivante(ActionEvent e){
		calendrier.addYear();
	}
	
	public void handleAnnePrecedente(ActionEvent e){
		calendrier.removeYear();
	}
}
