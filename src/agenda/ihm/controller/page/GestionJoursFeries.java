package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import agenda.MainApp;
import agenda.ihm.controller.widget.Liste;
import agenda.process.object.JourFerie;
import agenda.process.sql.QueryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

public class GestionJoursFeries extends AnchorPane {
	
	@FXML private Liste<JourFerie> listeJoursFeries;
	private DatePicker dateDP;

	public GestionJoursFeries(){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/GestionJoursFeries.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		// rempli la liste avec les jours feries créés
		reloadJoursFeries();
	}
	
	private void reloadJoursFeries(){
		try {
			listeJoursFeries.loadListe(QueryManager.selectListeJoursFeries());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des jours feries");
			System.exit(0);
		}
	}
	
	public void handleSupprimer(ActionEvent e){
		ArrayList<JourFerie> joursFeries = listeJoursFeries.getSelectedItems();
		if (joursFeries.size() == 1){
			supprimerJourFerie(joursFeries.get(0));
		}
	}
	
	public void handleAjouter(ActionEvent e){
		Dialog<LocalDate> dialog = initDialog();
		dialog.setTitle("ajout d'un nouveau jour ferie");
		
		Optional<LocalDate> result = dialog.showAndWait();
		if (result.isPresent()){
			JourFerie jourFerie = new JourFerie(dateDP.getValue());
			try {
				QueryManager.ajoutJourFerie(jourFerie);
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.out.println("erreur lors de la creation du jour ferie");
			}
			listeJoursFeries.add(jourFerie);
		}
	}
	
	public void handleModifier(ActionEvent e){
		// récupération du lieu sélectionné
		ArrayList<JourFerie> jourFerieSelect = listeJoursFeries.getSelectedItems();
		
		if (jourFerieSelect.size()!=0){
			Dialog<LocalDate> dialog = initDialog();
			dialog.setTitle("modification du jourFerie");
			Optional<LocalDate> result = dialog.showAndWait();
			if (result.isPresent()){
				//jourFerie.setDate(newJourFerie.getDate());
			}
			
			// mise à jour des vacances
//			try {
//				QueryManager.modificationVacances(vacances);
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//				System.out.println("erreur lors de la modification du lieu");
//			}
			
			reloadJoursFeries();
			
		}
		

	}
	
	public void supprimerJourFerie(JourFerie jourFerie){
		//message de mise en garde pour la suppression
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Attention !");
		alert.setHeaderText("Etes vous certain de vouloir supprimer ce jourFerie?");
		alert.setContentText("Attention, cette suppression est définitive et n'affecte pas les reprises déjà créées !");
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				try{
					QueryManager.suppressionJourFerie(jourFerie);
				}catch (SQLException e){
					System.out.println("echec de la suppression du jour ferie");
					System.exit(0);
				}
				listeJoursFeries.remove(jourFerie);
			}
		});
	}
	
	private Dialog<LocalDate> initDialog(){
		//affichage de la boite de dialogue
		Dialog<LocalDate> dialog = new Dialog<>();
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		dateDP = new DatePicker();
		
		grid.add(new Label("date:"), 0, 1);
		grid.add(dateDP, 1, 1);

		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		        return dateDP.getValue();
		    }
		    return null;
		});
		return dialog;
	}

}
