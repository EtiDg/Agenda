package agenda.ihm.controller.page;

import java.io.IOException;

import agenda.MainApp;
import agenda.ihm.event.NouvellePageEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class RootLayout extends BorderPane{
	
	public RootLayout(){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/RootLayout.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	public void handleAccueil(ActionEvent e){
		fireEvent(new NouvellePageEvent(new Accueil()));
	}
}

