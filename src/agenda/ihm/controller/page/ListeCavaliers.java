package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import agenda.MainApp;
import agenda.ihm.controller.widget.Liste;
import agenda.process.sql.QueryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class ListeCavaliers extends AnchorPane {
	
	@FXML private Liste<String> listeCavaliers;
	private TextField nomTF;

	public ListeCavaliers(){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/ListeCavaliers.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
	}
	
	public void addCavaliers(ArrayList<String> cavaliers){
		for(String cavalier : cavaliers){
			listeCavaliers.add(cavalier);	
		}
	}
	
	public void reloadCavaliers(long idReprise){
		try {
			listeCavaliers.loadListe(QueryManager.selectCavaliersDeReprise(idReprise));
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des cavaliers");
			System.exit(0);
		}
	}
	
	public void handleSupprimer(ActionEvent e){
		ArrayList<String> cavaliers = listeCavaliers.getSelectedItems();
		if (cavaliers.size() == 1){
			supprimerCavalier(cavaliers.get(0));
		}
	}
	
	public void handleAjouter(ActionEvent e){
		Dialog<String> dialog = initDialog();
		dialog.setTitle("ajout d'un nouveau cavalier");
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			String cavalier = nomTF.getText();
			listeCavaliers.add(cavalier);
		}
	}
	
	public void supprimerCavalier(String cavalier){
		listeCavaliers.remove(cavalier);
	}
	
	private Dialog<String> initDialog(){
		//affichage de la boite de dialogue
		Dialog<String> dialog = new Dialog<>();
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		nomTF = new TextField();
		
		grid.add(new Label("cavalier:"), 0, 1);
		grid.add(nomTF, 1, 1);

		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		        return nomTF.getText();
		    }
		    return null;
		});
		return dialog;
	}

	public ArrayList<String> getCavaliers(){
		return listeCavaliers.getListe();
	}
	
}
