package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import agenda.MainApp;
import agenda.ihm.controller.page.CreerGroupe;
import agenda.ihm.controller.widget.Liste;
import agenda.ihm.event.AfficherModeleDeRepriseEvent;
import agenda.ihm.event.NouvellePageEvent;
import agenda.process.object.Groupe;
import agenda.process.sql.QueryManager;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class ListeGroupes extends AnchorPane {

	@FXML private Liste<Groupe> listeGroupes;

	public ListeGroupes(){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/ListeGroupes.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		// ajoute un listener pour détecter les changements
		listeGroupes.getListView().getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Groupe>() {

			@Override
			public void onChanged(ListChangeListener.Change<? extends Groupe> change) {
				ArrayList<Groupe> groupe = listeGroupes.getSelectedItems();
				if (groupe.size() != 0){
					fireEvent(new AfficherModeleDeRepriseEvent(groupe.get(0).getModeleDeReprise()));	
				}
			}
		});
		
		// rempli la liste avec les modèles de reprises créés
		reloadGroupe();
		
	}

	private void reloadGroupe(){
		try {
			listeGroupes.loadListe(QueryManager.selectListeGroupe());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des groupes");
			System.exit(0);
		}
	}

	public void handleSupprimer(ActionEvent e){
		ArrayList<Groupe> groupe = listeGroupes.getSelectedItems();
		if (groupe.size() == 1){
			supprimerGroupe(groupe.get(0));
		}
	}

	public void handleNouveauGroupe(ActionEvent e){
		fireEvent(new NouvellePageEvent(new CreerGroupe()));
	}

	public void supprimerGroupe(Groupe groupe){
		//message de mise en garde pour la suppression
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Attention !");
		alert.setHeaderText("Etes vous certain de vouloir supprimer ce groupe?");
		alert.setContentText("Attention, la suppression sera définitive !");
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				try{
					QueryManager.suppressionGroupe(groupe);
				}catch (SQLException e){
					System.out.println("echec de la suppression du groupe");
					System.exit(0);
				}
				//System.out.println("Le groupe a bien été supprimé");
				listeGroupes.remove(groupe);
			}
		});
	}
	
}
