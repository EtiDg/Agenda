package agenda.ihm.controller.page;

import agenda.process.sql.QueryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import agenda.MainApp;
import agenda.ihm.controller.widget.Liste;
import agenda.ihm.event.AfficherModeleDeRepriseEvent;
import agenda.ihm.event.NouvellePageEvent;
import agenda.ihm.event.ValiderModeleEvent;
import agenda.process.object.*;

public class InfoReprise extends AnchorPane {
	
	// TODO:
	// ajout annuler
    
	private ModeleDeReprise modeleDeReprise;
    private Reprise reprise;
    private boolean isGroupe;
	@FXML private SplitPane splitPane;
	@FXML private TextField nomTF;
	@FXML private DatePicker dateDP;
	@FXML private TextField heureDebutTF;
	@FXML private TextField heureFinTF;
	@FXML protected Liste<Monitrice> listeMonitrices;
	@FXML protected ComboBox<String> lieuCB;
	protected Map<String,Lieu> lieux = new HashMap<String,Lieu>();
	

	public InfoReprise(ModeleDeReprise modeleDeReprise, Reprise reprise, boolean isGroupe){
		//import du fxml
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/InfoReprise.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		this.modeleDeReprise = modeleDeReprise;
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
		
		//import des lieux
		loadLieux();
		
		
		//import des monitrices
		loadMonitrices();
		
		
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
			reprise = new Reprise(reprise.getId(), nomTF.getText(), date, heureDebut, heureFin, reprise.getIdMR(),lieux.get(lieuCB.getValue()), listeMonitrices.getSelectedItems() );
		}
		
		boolean testConflitLieu = testConflitLieu(reprise);
		boolean testConflitMonitrice = testConflitMonitrice(reprise);
		
		// mise à jour des modifications
		if (!testConflitLieu && !testConflitMonitrice){
			try {
				QueryManager.modificationReprise(reprise);
			} catch (SQLException e2) {
				System.out.println("erreur durant la création des reprises");
				e2.printStackTrace();
				System.exit(0);
			}
			
			// retour à la page de gestion des modèles de reprises (particuliers ou groupes)
			if (isGroupe){
				GestionGroupes gestionGroupes = new GestionGroupes();
				gestionGroupes.fireEvent(new AfficherModeleDeRepriseEvent(modeleDeReprise));
				fireEvent(new NouvellePageEvent(gestionGroupes));
			}else{
				GestionParticuliers gestionParticuliers = new GestionParticuliers();
				gestionParticuliers.fireEvent(new AfficherModeleDeRepriseEvent(modeleDeReprise));
				fireEvent(new NouvellePageEvent(gestionParticuliers));
			}
		}
	}
	
	public void annuler(){
		
	}
	
	private void loadLieux(){
		try {
			ArrayList<Lieu> listeLieux = QueryManager.selectListeLieu();
			ObservableList<String> nomLieux = FXCollections.observableArrayList(); 
			for(int i =0; i<listeLieux.size();i++){
				lieux.put(listeLieux.get(i).getNom(), listeLieux.get(i));
				nomLieux.addAll(listeLieux.get(i).getNom());
			}
			lieuCB.setItems(nomLieux);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des lieux");
			System.exit(0);
		}
		lieuCB.setValue(reprise.getLieu().getNom());
	}
	
	private void loadMonitrices(){
		try {
			listeMonitrices.loadListe(QueryManager.selectListeMonitrice());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des monitrices");
			System.exit(0);
		}
		// on selectionne les monitrices de la reprise
		ArrayList<Monitrice> monitrices = listeMonitrices.getListe();
		for (int i=0; i<reprise.getMonitrices().size();i++){
			for (int j=0; j<monitrices.size();j++){
				if (reprise.getMonitrices().get(i).getId() == monitrices.get(j).getId()){
					listeMonitrices.selectItem(monitrices.get(j));
				}
			}
		}
	}
	
	private boolean testConflitLieu(Reprise reprise){
		boolean testConflitLieu  = false;
		
		try{
			testConflitLieu = QueryManager.testConflitLieu(reprise.getLieu().getId(), reprise.getSQLDate(), reprise.getHeureDebut(), reprise.getHeureFin());
		} catch (SQLException e2) {
			System.out.println("erreur durant le test des lieux");
			e2.printStackTrace();
			System.exit(0);
		}
		if (testConflitLieu){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention !");
			alert.setHeaderText("Le lieu est déjà occupé pour certaines reprises");
			alert.showAndWait();
		}
		return testConflitLieu;
	}
	
	private boolean testConflitMonitrice(Reprise reprise){
		boolean testConflitMonitrice = false;
		for(int i=0; i < reprise.getMonitrices().size();i++){
			try{
				testConflitMonitrice = QueryManager.testConflitMonitrice(reprise.getMonitrices().get(i).getId(), reprise.getSQLDate(), reprise.getHeureDebut(), reprise.getHeureFin());
			} catch (SQLException e2) {
				System.out.println("erreur durant le test des monitrices");
				e2.printStackTrace();
				System.exit(0);
			}
			if (testConflitMonitrice){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Attention !");
				alert.setHeaderText("La ou les monitrices sont déjà affectées à une  reprise");
				alert.showAndWait();
				break;
			}
		}
		return testConflitMonitrice;
	}
	
}
