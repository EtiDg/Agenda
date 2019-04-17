package agenda.ihm.controller.page;

import java.io.IOException;

import agenda.MainApp;
import agenda.ihm.event.AfficherModeleDeRepriseEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

public class GestionGroupes extends AnchorPane {
	@FXML private SplitPane splitPane;
	@FXML private ListeGroupes listeGroupes;
	private InfoModeleDeReprise infoModeleDeReprise;
	
	public GestionGroupes(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/GestionGroupes.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        
        addEventHandler(AfficherModeleDeRepriseEvent.AFFICHER_MODELE_DE_REPRISE,
                new EventHandler<AfficherModeleDeRepriseEvent>() {
					@Override
					public void handle(AfficherModeleDeRepriseEvent event) {
						splitPane.getItems().clear();
				        infoModeleDeReprise = new InfoModeleDeReprise(event.getModeleDeReprise(), true);
				        AnchorPane.setBottomAnchor(infoModeleDeReprise, 5.0);
				        AnchorPane.setTopAnchor(infoModeleDeReprise, 5.0);
				        AnchorPane.setRightAnchor(infoModeleDeReprise, 5.0);
				        AnchorPane.setLeftAnchor(infoModeleDeReprise, 5.0);
				        splitPane.getItems().addAll(listeGroupes, infoModeleDeReprise);
					}
        		}
        );
  
	}
	
}
