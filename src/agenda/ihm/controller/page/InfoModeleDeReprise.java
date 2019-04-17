package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import agenda.MainApp;
import agenda.ihm.controller.widget.Liste;
import agenda.ihm.event.NouvellePageEvent;
import agenda.ihm.event.NouvelleRepriseEvent;
import agenda.ihm.event.StringEvent;
import agenda.process.object.*;
import agenda.process.sql.QueryManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class InfoModeleDeReprise extends AnchorPane {
	/** TODO
	 * trier liste
	 */
	
	private ModeleDeReprise modeleDeReprise;
	
	private boolean isGroupe;
	@FXML private Button buttonAfficher;
	@FXML private Button buttonSupprimer;
	@FXML private Button buttonNouvelleReprise;
	@FXML private Liste<Reprise> listeReprises;
	
	public InfoModeleDeReprise(ModeleDeReprise modeleDeReprise, boolean isGroupe){
		// load the fxml
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/InfoModeleDeReprise.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        // rempli la liste avec les modèles de reprises créés
        this.modeleDeReprise = modeleDeReprise;
        this.isGroupe = isGroupe;
        try {
        	listeReprises.loadListe(QueryManager.selectReprisesDeModele(modeleDeReprise.getId()));
        } catch (SQLException e) {
        	e.printStackTrace();
        	System.out.println("erreur lors du chargement des reprises");
        	System.exit(0);
        }
        
        // eventHandler pour changer le nom du modèle
        addEventHandler(StringEvent.STRING_EVENT,
                new EventHandler<StringEvent>() {
					@Override
					public void handle(StringEvent event) {
						modeleDeReprise.setNom(event.getValeur());
						try {
							QueryManager.modificationModeleDeReprise(modeleDeReprise);
						} catch (SQLException e) {
							System.out.println("erreur lors de la modification du nom du modèle de reprise");
							e.printStackTrace();
							System.exit(0);
						};
					}
        		}
        );
        
	}
	
	public void handleAfficher(MouseEvent e){
		if (e.getButton() == MouseButton.PRIMARY){
			ArrayList<Reprise> reprises = listeReprises.getSelectedItems();
			if (reprises.size() == 1){
				fireEvent(new NouvellePageEvent(new InfoReprise(reprises.get(0), isGroupe)));
			}else if (reprises.size() > 1){
				//fireEvent(new NouvellePageEvent(new InfoMultiReprises(reprises)));
			}
		}
	}
	
	public void handleSupprimer(MouseEvent e){
		if (e.getButton() == MouseButton.PRIMARY){
			ArrayList<Reprise> reprises = listeReprises.getSelectedItems();
			supprimerReprises(reprises);
		}
	}
	
	public void handleNouvelleReprise(MouseEvent e){
		fireEvent(new NouvelleRepriseEvent());
	}
	
	public void supprimerReprises(ArrayList<Reprise> reprises){
		// message de mise en garde pour la suppression
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Attention !");
		alert.setHeaderText("Etes vous certain de vouloir supprimer ce modèle de reprise ?");
		alert.setContentText("Attention, la suppression sera définitive !");
		alert.showAndWait().ifPresent(rs -> {
		    if (rs == ButtonType.OK) {
				try{
					QueryManager.suppressionReprises(reprises);
				}catch (SQLException e){
					System.out.println("echec de la suppression des reprises");
					System.exit(0);
				}
				System.out.println("Les reprises ont bien été supprimées");
		    }
		});
	}
	
}
