package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import agenda.CalendarMaps;
import agenda.MainApp;
import agenda.ihm.event.NouvellePageEvent;
import agenda.ihm.event.ValiderModeleEvent;
import agenda.process.object.Groupe;
import agenda.process.object.Lieu;
import agenda.process.object.ModeleDeReprise;
import agenda.process.object.Reprise;
import agenda.process.sql.QueryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DateTimeStringConverter;

public class CreerGroupe extends AnchorPane{

	//TODO:
		// ajout des Monitrices
		// ajout Lieu
		// ajout option vacances / TrèveHivernale
		// validation des reprises (avec calendrier)
		// ajout annuler
		// interdire la création d'un modele sans reprise
	    
	    private ModeleDeReprise modeleDeReprise;
	    private ListeValidationModele listeValidationModele;
		@FXML private SplitPane splitPane;
		@FXML private TextField nomTF;
		@FXML private DatePicker dateDebutDP;
		@FXML private DatePicker dateFinDP;
		@FXML private ComboBox<String> jourCB;
		@FXML private TextField heureDebutTF;
		@FXML private TextField heureFinTF;

		public CreerGroupe(){
			//import du fxml
			FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/CreerGroupe.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			try {
				fxmlLoader.load();
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
			
			// ajout du formattage pour les dates
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			try {
				heureDebutTF.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format), format.parse("00:00")));
				heureFinTF.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format), format.parse("00:00")));
			} catch (ParseException e) {
				e.printStackTrace();
				System.exit(0);
			}
			jourCB.setItems(CalendarMaps.JOURS);
			
			
			// validation du modele de reprise
			addEventHandler(ValiderModeleEvent.VALIDER_MODELE,
	                new EventHandler<ValiderModeleEvent>() {
						@Override
						public void handle(ValiderModeleEvent event) {
							// ajout du modele de reprise
							try {
								QueryManager.ajoutModeleDeReprise(modeleDeReprise);
							} catch (SQLException e) {
								System.out.println("erreur durant l'ajout du modèle de reprise");
								e.printStackTrace();
								System.exit(0);
							}
							
							//ajout du groupe
							try {
								QueryManager.ajoutGroupe(new Groupe(modeleDeReprise.getNom(), true, true, modeleDeReprise));
							} catch (SQLException e) {
								System.out.println("erreur durant l'ajout du groupe");
								e.printStackTrace();
								System.exit(0);
							}
							
							// ajout des reprises
							try {
								QueryManager.ajoutReprises(listeValidationModele.getReprises());
							} catch (SQLException e) {
								System.out.println("erreur durant la création des reprises");
								e.printStackTrace();
								System.exit(0);
							}
							
							//affichage des modèles de reprises particuliers
							fireEvent(new NouvellePageEvent(new GestionGroupes()));
						}
					}
			);
		}
		
		public void handleGenerer(ActionEvent e){
			boolean formulaireOk = true;
			LocalDate dateDebut = null;
			LocalDate dateFin = null; 
			// vérification des champs de formulaire et import des dates
			if(dateDebutDP.getValue()!=null && dateFinDP.getValue()!=null && jourCB.getValue()!=null && nomTF.getText() != null){
				dateDebut = dateDebutDP.getValue();
				dateFin = dateFinDP.getValue();
			}else{
				formulaireOk = false;
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Attention !");
				alert.setHeaderText("Certains champs n'ont pas été remplis");
				alert.showAndWait();
			}
			if (dateDebut.isAfter(dateFin) ){
				formulaireOk = false;
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Attention !");
				alert.setHeaderText("Le jour de fin doit être supérieur au jour de début");
				alert.showAndWait();
			}
			if (CalendarMaps.JOURS_SEMAINE.get(jourCB.getValue()) == null){
				formulaireOk = false;
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Attention !");
				alert.setHeaderText("Ce jour de la semaine n'existe pas");
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
			// creation du modele de reprise et de ses reprises.
			if (formulaireOk){
				modeleDeReprise = new ModeleDeReprise(nomTF.getText());

				// calcul des reprises
				DayOfWeek jour = CalendarMaps.JOURS_SEMAINE.get(jourCB.getValue());
				DayOfWeek jourDebut = dateDebut.getDayOfWeek();
				dateDebut = dateDebut.plusDays(( (jour.getValue()-jourDebut.getValue())%7 + 7)%7);
				ArrayList<Reprise> reprises = new ArrayList<Reprise>();
				while (dateDebut.compareTo(dateFin) <= 0){
					reprises.add(new Reprise(modeleDeReprise.getNom(), dateDebut, heureDebut, heureFin, modeleDeReprise.getId(), new Lieu(0,"salut")));
					dateDebut = dateDebut.plusDays(7);
				}
				
				// affichage des reprises calculées
				splitPane.getItems().clear();
		        listeValidationModele = new ListeValidationModele(reprises);
		        AnchorPane.setBottomAnchor(listeValidationModele , 5.0);
		        AnchorPane.setTopAnchor(listeValidationModele , 5.0);
		        AnchorPane.setRightAnchor(listeValidationModele , 5.0);
		        AnchorPane.setLeftAnchor(listeValidationModele , 5.0);
		        splitPane.getItems().addAll(listeValidationModele );
			}
			
		}
		
		public void annuler(){
			
		}
		
		public void afficherCalendrier(){
			
		}
	}