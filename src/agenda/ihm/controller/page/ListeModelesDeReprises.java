package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import agenda.MainApp;
import agenda.ihm.controller.page.CreerMRParticulier;
import agenda.ihm.controller.widget.Liste;
import agenda.ihm.event.AfficherModeleDeRepriseEvent;
import agenda.ihm.event.NouvellePageEvent;
import agenda.ihm.event.NouvelleRepriseExceptionnelleEvent;
import agenda.process.object.ModeleDeReprise;
import agenda.process.sql.QueryManager;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;


public class ListeModelesDeReprises extends AnchorPane {

	//public ObjectProperty<EventHandler<? super AfficherModeleDeRepriseEvent>> onAfficherModeleDeReprise;
	@FXML private Liste<ModeleDeReprise> listeModelesDeReprises;

	//NouveauModeleListener listener = new NouveauModeleListener();

	public ListeModelesDeReprises(){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/ListeModelesDeReprises.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		// ajoute un listener pour détecter les changements
		listeModelesDeReprises.getListView().getSelectionModel().getSelectedItems().addListener(new ListChangeListener<ModeleDeReprise>() {

			@Override
			public void onChanged(ListChangeListener.Change<? extends ModeleDeReprise> change) {
				ArrayList<ModeleDeReprise> modeleDeReprise = listeModelesDeReprises.getSelectedItems();
				if (modeleDeReprise.size() != 0){
					fireEvent(new AfficherModeleDeRepriseEvent(modeleDeReprise.get(0)));	
				}
//				if (change.getAddedSubList().size() != 0){
//					fireEvent(new AfficherModeleDeRepriseEvent(change.getAddedSubList().get(0)));	
//				}
			}
		});
		
		// rempli la liste avec les modèles de reprises créés
		reloadModeleDeReprise();
		
	}

	private void reloadModeleDeReprise(){
		try {
			listeModelesDeReprises.loadListe(QueryManager.selectListeMRParticulier());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des modeles de reprises");
			System.exit(0);
		}
	}

	public void handleSupprimer(ActionEvent e){
		ArrayList<ModeleDeReprise> modeleDeReprise = listeModelesDeReprises.getSelectedItems();
		if (modeleDeReprise.size() == 1){
			supprimerModeleDeReprise(modeleDeReprise.get(0));
		}
	}

	public void handleNouveauModele(ActionEvent e){
		//fireEvent(new NouveauModeleEvent());
		fireEvent( new NouvellePageEvent(new CreerMRParticulier()) );
	}

	public void handleNouvelleRepriseExceptionnelle(ActionEvent e){
		fireEvent(new NouvelleRepriseExceptionnelleEvent());
	}

	public void supprimerModeleDeReprise(ModeleDeReprise modeleDeReprise){
		//message de mise en garde pour la suppression
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Attention !");
		alert.setHeaderText("Etes vous certain de vouloir supprimer ce modèle de reprise ?");
		alert.setContentText("Attention, la suppression sera définitive !");
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				try{
					QueryManager.suppressionModeleDeReprise(modeleDeReprise);
				}catch (SQLException e){
					System.out.println("echec de la suppression du modèle de reprise");
					System.exit(0);
				}
				System.out.println("Le modèle de Reprise a bien été supprimé");
				//reloadModeleDeReprise();
				listeModelesDeReprises.remove(modeleDeReprise);
			}
		});
	}
	
//	public final ObjectProperty<EventHandler<? super AfficherModeleDeRepriseEvent>> onAfficherModeleDeRepriseProperty() {
//		if (onAfficherModeleDeReprise == null) {
//			onAfficherModeleDeReprise = new SimpleObjectProperty<>();
//		}
//		return onAfficherModeleDeReprise;
//	}
//	
//	public EventHandler<? super AfficherModeleDeRepriseEvent> getOnAfficherModeleDeReprise(){
//		return onAfficherModeleDeReprise.get();
//	}
//	
//	public void setOnAfficherModeleDeReprise(EventHandler<? super AfficherModeleDeRepriseEvent> eventHandler){
//		this.onAfficherModeleDeReprise.set(eventHandler);
//	}
}
