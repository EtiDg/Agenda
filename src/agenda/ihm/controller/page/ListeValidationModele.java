package agenda.ihm.controller.page;


import java.io.IOException;
import java.util.ArrayList;

import agenda.MainApp;
import agenda.ihm.controller.widget.Liste;
import agenda.ihm.event.ValiderModeleEvent;
import agenda.process.object.Reprise;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class ListeValidationModele extends AnchorPane {

	@FXML private Liste<Reprise> listeReprises;

	public ListeValidationModele(ArrayList<Reprise> reprises){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/ListeValidationModele.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		// rempli la liste avec reprises créées
		listeReprises.loadListe(reprises);
	}

	public ArrayList<Reprise> getReprises(){
		return  listeReprises.getListe();
	}
	
	public void handleSupprimer(ActionEvent e){
		listeReprises.removeSelectedItems();
	}

	public void handleValider(ActionEvent e){
		fireEvent(new ValiderModeleEvent());
	}

}

