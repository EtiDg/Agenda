package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import agenda.MainApp;
import agenda.process.object.TreveHivernale;
import agenda.process.sql.QueryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class GestionTreveHivernale extends AnchorPane{

	@FXML private Text treveHivernaleT;
	TreveHivernale treveHivernale;
	private DatePicker dateDebutDP;
	private DatePicker dateFinDP;

	public GestionTreveHivernale(){
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/GestionTreveHivernale.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		// charge la trève hivernale
		reloadTreveHivernale();
		treveHivernaleT.setText(treveHivernale.toString());
	}
	
	private void reloadTreveHivernale(){
		try {
			treveHivernale = QueryManager.selectTreveHivernale();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement de la treve hivernale");
			System.exit(0);
		}
	}
	
	
	public void handleModifier(ActionEvent e){
		Dialog<Boolean> dialog = initDialog();
		dialog.setTitle("modification de la treve hivernale");
		
		Optional<Boolean> result = waitDialog(dialog);
		if (result.isPresent()){
			treveHivernale = new TreveHivernale(dateDebutDP.getValue(), dateFinDP.getValue());
			try {
				QueryManager.modificationTreveHivernale(treveHivernale);
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.out.println("erreur lors de la modification de la trève hivernale");
			}
			;
		}
		treveHivernaleT.setText(treveHivernale.toString());
	}
	
	private Dialog<Boolean> initDialog(){
		//affichage de la boite de dialogue
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		dateDebutDP = new DatePicker();
		dateFinDP = new DatePicker();
		
		grid.add(new Label("date de début:"), 0, 0);
		grid.add(dateDebutDP, 1, 0);
		grid.add(new Label("date de fin:"), 0, 1);
		grid.add(dateFinDP, 1, 1);

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
				if(dateDebutDP.getValue()==null || dateFinDP.getValue()==null){
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

