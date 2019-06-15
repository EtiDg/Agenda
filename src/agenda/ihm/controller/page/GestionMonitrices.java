package agenda.ihm.controller.page;

import java.io.IOException;

import agenda.MainApp;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class GestionMonitrices extends AnchorPane{

	@FXML private ListeMonitrices listeMonitrices;
	
	public GestionMonitrices(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/GestionMonitrices.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
}
