package agenda.ihm.controller.page;

import agenda.process.sql.QueryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Locale;

import agenda.MainApp;
import agenda.ihm.event.NouvellePageEvent;
import agenda.process.object.*;

public class InfoReprise extends AnchorPane {
	
	// TODO:
	// ajout Lieu
	// ajout des Monitrices
	// ajout annuler
    
    private Reprise reprise;
    private boolean isGroupe;
	@FXML private SplitPane splitPane;
	@FXML private TextField nomTF;
	@FXML private DatePicker dateDP;
	@FXML private TextField heureDebutTF;
	@FXML private TextField heureFinTF;

	public InfoReprise(Reprise reprise, boolean isGroupe){
		//import du fxml
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/InfoReprise.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		this.reprise = reprise;
		this.isGroupe = isGroupe;
		
		// initialisation des champs de formulaire
		nomTF.setText(reprise.getNom());
		dateDP.setValue(reprise.getDate());

		// ajout du formattage pour les dates
		heureDebutTF.setTextFormatter(new TextFormatter<>(
				new LocalTimeStringConverter(FormatStyle.SHORT, Locale.FRANCE), 
				LocalTime.ofSecondOfDay(reprise.getHeureDebut()*60))
		);
		heureFinTF.setTextFormatter(new TextFormatter<>(
				new LocalTimeStringConverter(FormatStyle.SHORT, Locale.FRANCE),
				LocalTime.ofSecondOfDay(reprise.getHeureFin()*60))
		);
		
	}
	
	public void handleValider(ActionEvent e){
		boolean formulaireOk = true;
		LocalDate date = null;
		// vérification des champs de formulaire et import des dates
		if(dateDP.getValue()!=null && nomTF.getText() != null){
			date = dateDP.getValue();
		}else{
			formulaireOk = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention !");
			alert.setHeaderText("Certains champs n'ont pas été remplis");
			alert.showAndWait();
		}
			
		// import et conversion des heures
		String heureDebutString = heureDebutTF.getText();
		String heureFinString = heureFinTF.getText();
		int heureDebut;
		int heureFin;
		heureDebut = Integer.parseInt(heureDebutString.substring(0,2))*60 + Integer.parseInt(heureDebutString.substring(3,5));
		heureFin = Integer.parseInt(heureFinString.substring(0,2))*60 + Integer.parseInt(heureFinString.substring(3,5));
		if (heureFin <= heureDebut){
			formulaireOk = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention !");
			alert.setHeaderText("L'heure de début doit être supérieure à celle de fin");
			alert.showAndWait();
		}
		
		// creation de la reprise.
		if (formulaireOk){
			reprise = new Reprise(reprise.getId(), nomTF.getText(), date, heureDebut, heureFin, reprise.getIdMR(), new Lieu(0,"salut"));
		}
		
		// mise à jour des modifications
		try {
			QueryManager.modificationReprise(reprise);
		} catch (SQLException e2) {
			System.out.println("erreur durant la création des reprises");
			e2.printStackTrace();
			System.exit(0);
		}
		
		// retour à la page de gestion des modèles de reprises (particuliers ou groupes)
		if (isGroupe){
			fireEvent(new NouvellePageEvent(new GestionGroupes()));
		}else{
			fireEvent(new NouvellePageEvent(new GestionParticuliers()));
		}
	}
	
	public void annuler(){
		
	}
	
}
