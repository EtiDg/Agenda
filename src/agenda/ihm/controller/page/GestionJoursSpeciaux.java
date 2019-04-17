package agenda.ihm.controller.page;

import java.io.IOException;

import agenda.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class GestionJoursSpeciaux extends AnchorPane {

	public GestionJoursSpeciaux(){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/GestionJoursSpeciaux.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
}
