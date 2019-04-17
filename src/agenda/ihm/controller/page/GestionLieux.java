package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import agenda.MainApp;
import agenda.ihm.controller.widget.Liste;
import agenda.process.object.Lieu;
import agenda.process.sql.QueryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class GestionLieux extends AnchorPane{
	
	//TODO:
	//vérifier que le nouveau lieu n'est pas déjà dans la liste
	
	@FXML private Liste<Lieu> listeLieux;
	
	public GestionLieux(){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/GestionLieux.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		// rempli la liste avec les lieux créés
		reloadLieux();
	}
	
	private void reloadLieux(){
		try {
			listeLieux.loadListe(QueryManager.selectListeLieu());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des lieux");
			System.exit(0);
		}
	}
	
	public void handleSupprimer(ActionEvent e){
		ArrayList<Lieu> lieu = listeLieux.getSelectedItems();
		if (lieu.size() == 1){
			supprimerLieu(lieu.get(0));
		}
	}
	
	public void handleAjouter(ActionEvent e){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Creer un nouveau lieu");
		dialog.setHeaderText("Choisissez le nom du lieu");
		dialog.setContentText("nom du lieu :");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent() && !result.get().isEmpty()){
			Lieu lieu = new Lieu(result.get());
			try {
				QueryManager.ajoutLieu(lieu);
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.out.println("erreur lors de la creation du lieu");
			}
			listeLieux.add(lieu);
		}
	
	}
	
	public void handleModifier(ActionEvent e){
		// récupération du lieu sélectionné
		ArrayList<Lieu> lieuSelect = listeLieux.getSelectedItems();
		
		if (lieuSelect.size()!=0){
			Lieu lieu = lieuSelect.get(0);
			// changement de nom du lieu
			TextInputDialog dialog = new TextInputDialog(lieu.getNom());
			dialog.setTitle("Modifier un lieu");
			dialog.setHeaderText("Choisissez le nouveau nom du lieu");
			dialog.setContentText("nom du lieu :");

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent() && !result.get().isEmpty()){
				lieu.setNom(result.get());
			    try {
					QueryManager.modificationLieu(lieu);
				} catch (SQLException e1) {
					e1.printStackTrace();
					System.out.println("erreur lors de la modification du lieu");
				}
			}
			
			reloadLieux();
			
		}
		

	}
	
	public void supprimerLieu(Lieu lieu){
		//message de mise en garde pour la suppression
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Attention !");
		alert.setHeaderText("Etes vous certain de vouloir supprimer ce lieu?");
		alert.setContentText("Attention, sa suppression entraine la suppression des reprises associées !");
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				try{
					QueryManager.suppressionLieu(lieu);
				}catch (SQLException e){
					System.out.println("echec de la suppression du lieu");
					System.exit(0);
				}
				listeLieux.remove(lieu);
			}
		});
	}
}
