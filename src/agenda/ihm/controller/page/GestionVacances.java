package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import agenda.MainApp;
import agenda.ihm.controller.widget.Liste;
import agenda.process.object.Vacances;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

public class GestionVacances extends AnchorPane {
	
	@FXML private Liste<Vacances> listeVacances;
	private TextField nomTF;
	private DatePicker dateDebutDP;
	private DatePicker dateFinDP;

	public GestionVacances(){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/GestionVacances.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		// rempli la liste avec les vacances créés
		reloadVacances();
	}
	
	private void reloadVacances(){
		try {
			listeVacances.loadListe(QueryManager.selectListeVacances());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des vacances");
			System.exit(0);
		}
	}
	
	public void handleSupprimer(ActionEvent e){
		ArrayList<Vacances> vacances = listeVacances.getSelectedItems();
		if (vacances.size() == 1){
			supprimerVacances(vacances.get(0));
		}
	}
	
	public void handleAjouter(ActionEvent e){
		Dialog<Boolean> dialog = initDialog();
		dialog.setTitle("ajout de nouvelles vacances");
		
		Optional<Boolean> result = waitDialog(dialog);
		if (result.isPresent()){
			Vacances vacances = new Vacances(nomTF.getText(), dateDebutDP.getValue(), dateFinDP.getValue());
			try {
				QueryManager.ajoutVacances(vacances);
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.out.println("erreur lors de la creation des vacances");
			}
			listeVacances.add(vacances);
		}
	}
	
	public void handleModifier(ActionEvent e){
		// récupération du lieu sélectionné
		ArrayList<Vacances> vacancesSelect = listeVacances.getSelectedItems();
		
		if (vacancesSelect.size()!=0){
			Vacances vacances = vacancesSelect.get(0);
			Dialog<Boolean> dialog = initDialog();
			dialog.setTitle("modification des vacances");
			nomTF.setText(vacances.getNom());
			dateDebutDP.setValue(vacances.getDateDebut());
			dateDebutDP.setValue(vacances.getDateFin());
			Optional<Boolean> result = waitDialog(dialog);
			if (result.isPresent()){
				Vacances newVacances = new Vacances(nomTF.getText(), dateDebutDP.getValue(), dateFinDP.getValue());
				vacances.setNom(newVacances.getNom());
				vacances.setDateDebut(newVacances.getDateDebut());
				vacances.setDateFin(newVacances.getDateFin());
			}
			
			// mise à jour des vacances
//			try {
//				QueryManager.modificationVacances(vacances);
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//				System.out.println("erreur lors de la modification du lieu");
//			}
			
			reloadVacances();
			
		}
		

	}
	
	public void supprimerVacances(Vacances vacances){
		//message de mise en garde pour la suppression
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Attention !");
		alert.setHeaderText("Etes vous certain de vouloir supprimer ces vacances?");
		alert.setContentText("Attention, cette suppression est définitive et n'affecte pas les reprises déjà créées !");
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				try{
					QueryManager.suppressionVacances(vacances);
				}catch (SQLException e){
					System.out.println("echec de la suppression des vacances");
					System.exit(0);
				}
				listeVacances.remove(vacances);
			}
		});
	}
	
	private Dialog<Boolean> initDialog(){
		//affichage de la boite de dialogue
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		nomTF = new TextField();
		dateDebutDP = new DatePicker();
		dateFinDP = new DatePicker();
		
		grid.add(new Label("nom:"), 0, 0);
		grid.add(nomTF, 1, 0);
		grid.add(new Label("date de début:"), 0, 1);
		grid.add(dateDebutDP, 1, 1);
		grid.add(new Label("date de fin:"), 0, 2);
		grid.add(dateFinDP, 1, 2);

		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		        return true;
		    }
		    return null;
		});
		return dialog;
	}
	
	private Optional<Boolean> waitDialog(Dialog<Boolean> dialog){
		// on demande tant que l'on n'a pas un formulaire correct ou une annulation
		boolean isCompleted = false;
		boolean isDateOk = false;
		Optional<Boolean> result = null;
		
		while (!isCompleted || !isDateOk){
			isCompleted = true;
			isDateOk = true;
			result = dialog.showAndWait();
			if (result.isPresent() ){
				
				// vérification des champs de formulaire
				if(nomTF.getText().isEmpty() || dateDebutDP.getValue()==null || dateFinDP.getValue()==null){
					isCompleted = false;
				}
				if (isCompleted){	
					if(dateDebutDP.getValue().isAfter(dateFinDP.getValue()) ){
						isDateOk = false;
					}else{
						
						
					}
				}
			}
		}
		return result;
	}

}
