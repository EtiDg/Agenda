package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import agenda.MainApp;
import agenda.ihm.controller.widget.Liste;
import agenda.ihm.event.AfficherMonitriceEvent;
import agenda.process.object.Monitrice;
import agenda.process.sql.QueryManager;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

public class ListeMonitrices extends AnchorPane{

	//TODO
	//ajouter choix pour la suppression des reprises associées
	
	@FXML private Liste<Monitrice> listeMonitrices;
	private TextField nomTF;

	public ListeMonitrices(){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/ListeMonitrices.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		// ajoute un listener pour détecter les changements
		listeMonitrices.getListView().getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Monitrice>() {

			@Override
			public void onChanged(ListChangeListener.Change<? extends Monitrice> change) {
				ArrayList<Monitrice> monitrice = listeMonitrices.getSelectedItems();
				if (monitrice.size() != 0){
					fireEvent(new AfficherMonitriceEvent(monitrice.get(0)));	
				}
			}
		});
		
		// rempli la liste avec les monitrices créées
		reloadMonitrice();
		
	}

	private void reloadMonitrice(){
		try {
			listeMonitrices.loadListe(QueryManager.selectListeMonitrice());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des monitrices");
			System.exit(0);
		}
	}

	public void handleSupprimer(ActionEvent e){
		ArrayList<Monitrice> monitrices = listeMonitrices.getSelectedItems();
		if (monitrices.size() == 1){
			supprimerMonitrice(monitrices.get(0));
		}
	}

	public void handleNouvelleMonitrice(ActionEvent e){
		//fireEvent( new NouvellePageEvent(new CreerMonitrice()) );
		Dialog<String> dialog = initDialog();
		dialog.setTitle("ajout d'une nouvelle monitrice");
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			Monitrice monitrice = new Monitrice(nomTF.getText());
			try {
				QueryManager.ajoutMonitrice(monitrice);
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.out.println("erreur lors de creation de la monitrice");
			}
			listeMonitrices.add(monitrice);
		}
	}

	public void supprimerMonitrice(Monitrice monitrice){
		//message de mise en garde pour la suppression
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Attention !");
		alert.setHeaderText("Etes vous certain de vouloir supprimer cette monitrice?");
		alert.setContentText("Attention, la suppression sera définitive !");
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				try{
					QueryManager.suppressionMonitrice(monitrice, 0);
				}catch (Exception e){
					System.out.println("echec de la suppression de la monitrice");
					System.exit(0);
				}
				System.out.println("La monitrice a bien été supprimée");
				listeMonitrices.remove(monitrice);
			}
		});
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
		
		grid.add(new Label("nom :"), 0, 1);
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
}
