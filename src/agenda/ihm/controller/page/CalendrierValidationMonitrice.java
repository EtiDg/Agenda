package agenda.ihm.controller.page;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;

import agenda.MainApp;
import agenda.ihm.controller.widget.CreneauManagement;
import agenda.ihm.controller.widget.DateManagement;
import agenda.ihm.event.ValiderModeleEvent;
import agenda.ihm.event.ValiderMonitriceEvent;
import agenda.process.object.Creneau;
import agenda.process.object.Reprise;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class CalendrierValidationMonitrice {
	
	ArrayList<Creneau> creneaux;
	CreneauManagement calendrier;
	long idMonitrice;
	@FXML BorderPane borderPane;
	
	public CalendrierValidationMonitrice(ArrayList<Creneau> creneaux, long idMonitrice){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/CalendrierValidationMonitrice.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		this.creneaux = creneaux;
		this.idMonitrice = idMonitrice;
		calendrier = new CreneauManagement(true, idMonitrice);
		calendrier.displayWeek(LocalDate.now());
		calendrier.selectCreneaux(creneaux);
		borderPane.setCenter(calendrier);
	}
	
	public ArrayList<Creneau> getCreneaux(){
		return calendrier.getSelectedCreneaux();
	}
	

	public void handleValider(ActionEvent e){
		//fireEvent(new ValiderMonitriceEvent());
	}
	
}
