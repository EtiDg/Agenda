package agenda.ihm.controller.widget;

import java.io.IOException;
import java.util.ArrayList;

import agenda.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;

public class Liste extends ScrollPane{
	
	private String titre;
	private ArrayList<String> liste;

	public Liste(String titre) {
		this.titre = titre;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/widget/Liste.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
	
	public void loadListe(ArrayList<String> liste){
		this.liste = liste;
	}
	
}
