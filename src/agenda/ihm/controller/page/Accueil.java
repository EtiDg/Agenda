package agenda.ihm.controller.page;

import java.io.IOException;

import agenda.MainApp;
import agenda.ihm.event.NouvellePageEvent;
import agenda.ihm.model.Page;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class Accueil extends AnchorPane {

	public Accueil() {
		// import du fxml
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/Accueil.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void handleParticuliers(ActionEvent e) {
		fireEvent(new NouvellePageEvent(new GestionParticuliers()));
	}

	public void handleGroupes(ActionEvent e) {
		fireEvent(new NouvellePageEvent(new GestionGroupes()));
	}

	public void handlePlanning(ActionEvent e) {
		fireEvent(new NouvellePageEvent(new Planning()));
	}

	public void handleMonitrices(ActionEvent e) {
		fireEvent(new NouvellePageEvent(new ListeMonitrices()));
	}

	public void handleLieux(ActionEvent e) {
		fireEvent(new NouvellePageEvent(new GestionLieux()));
	}

	public void handleJoursSpeciaux(ActionEvent e) {
		fireEvent(new NouvellePageEvent(new GestionJoursSpeciaux()));
	}

}
