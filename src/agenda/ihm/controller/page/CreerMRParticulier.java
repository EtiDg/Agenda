package agenda.ihm.controller.page;

import agenda.process.object.ModeleDeReprise;
import agenda.process.sql.QueryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import agenda.CalendarMaps;
import agenda.MainApp;
import agenda.ihm.controller.widget.Liste;
import agenda.ihm.event.NouvellePageEvent;
import agenda.ihm.event.ValiderModeleEvent;
import agenda.process.object.*;

public class CreerMRParticulier extends AnchorPane {
	
	//TODO:
	// ajout annuler
	// interdire la création d'un modele sans reprise
	
    protected ModeleDeReprise modeleDeReprise;
    protected CalendrierValidationModele calendrierValidationModele;
	@FXML protected TextField nomTF;
	@FXML protected DatePicker dateDebutDP;
	@FXML protected DatePicker dateFinDP;
	@FXML protected ComboBox<String> jourCB;
	@FXML protected TextField heureDebutTF;
	@FXML protected TextField heureFinTF;
	@FXML protected CheckBox isTreveHivernale;
	@FXML protected CheckBox isVacances;
	@FXML protected Liste<Monitrice> listeMonitrices;
	@FXML protected ListeCavaliers listeCavaliers;
	@FXML protected ComboBox<String> lieuCB;
	protected Map<String,Lieu> lieux = new HashMap<String,Lieu>();
	protected EventHandler<ValiderModeleEvent> eventHandler;

	public CreerMRParticulier() {
		// import du fxml
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/CreerMRParticulier.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		// ajout du formattage pour les dates
		heureDebutTF.setTextFormatter(new TextFormatter<>(
				new LocalTimeStringConverter(FormatStyle.SHORT, Locale.FRANCE), LocalTime.of(0, 0)));
		heureFinTF.setTextFormatter(new TextFormatter<>(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.FRANCE),
				LocalTime.of(0, 0)));

		// ajout de la liste de jours dans la selection
		jourCB.setItems(CalendarMaps.JOURS);

		// import des lieux
		loadLieux();

		// import des monitrices
		loadMonitrices();

		// validation du modele de reprise
		eventHandler = new EventHandler<ValiderModeleEvent>() {
			@Override
			public void handle(ValiderModeleEvent event) {
				ArrayList<Reprise> reprises = calendrierValidationModele.getReprises();
				validerModele(reprises);
			}
		};
		addEventHandler(ValiderModeleEvent.VALIDER_MODELE, eventHandler);
	}

	public void handleGenerer(ActionEvent e) {
		boolean formulaireOk = verifierFormulaire();
		LocalDate dateDebut = dateDebutDP.getValue();
		LocalDate dateFin = dateFinDP.getValue();

		// import et conversion des heures
		String heureDebutString = heureDebutTF.getText();
		String heureFinString = heureFinTF.getText();
		int heureDebut;
		int heureFin;
		heureDebut = Integer.parseInt(heureDebutString.substring(0, 2)) * 60
				+ Integer.parseInt(heureDebutString.substring(3, 5));
		heureFin = Integer.parseInt(heureFinString.substring(0, 2)) * 60
				+ Integer.parseInt(heureFinString.substring(3, 5));
		if (heureFin <= heureDebut) {
			formulaireOk = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention !");
			alert.setHeaderText("L'heure de début doit être supérieure à celle de fin");
			alert.showAndWait();
		}

		// creation du modele de reprise et de ses reprises.
		if (formulaireOk) {
			genererReprises(dateDebut, dateFin, heureDebut, heureFin);
		}

	}

	private void loadLieux() {
		try {
			ArrayList<Lieu> listeLieux = QueryManager.selectListeLieu();
			ObservableList<String> nomLieux = FXCollections.observableArrayList();
			for (Lieu lieu : listeLieux) {
				lieux.put(lieu.getNom(), lieu);
				nomLieux.addAll(lieu.getNom());
			}
			lieuCB.setItems(nomLieux);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des lieux");
			System.exit(0);
		}

	}

	private void loadMonitrices() {
		try {
			listeMonitrices.loadListe(QueryManager.selectListeMonitrice());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des monitrices");
			System.exit(0);
		}
	}

	public void annuler() {

	}

	private boolean verifierFormulaire() {
		boolean formulaireOk = true;
		LocalDate dateDebut = null;
		LocalDate dateFin = null;
		// vérification des champs de formulaire et import des dates
		if (dateDebutDP.getValue() != null && dateFinDP.getValue() != null && jourCB.getValue() != null
				&& !nomTF.getText().isEmpty() && lieuCB.getValue() != null) {
			dateDebut = dateDebutDP.getValue();
			dateFin = dateFinDP.getValue();
		} else {
			formulaireOk = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention !");
			alert.setHeaderText("Certains champs n'ont pas été remplis");
			alert.showAndWait();
		}
		if (formulaireOk && dateDebut.isAfter(dateFin)) {
			formulaireOk = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention !");
			alert.setHeaderText("Le jour de fin doit être supérieur au jour de début");
			alert.showAndWait();
		}
		if (CalendarMaps.JOURS_SEMAINE.get(jourCB.getValue()) == null) {
			formulaireOk = false;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Attention !");
			alert.setHeaderText("Ce jour de la semaine n'existe pas");
			alert.showAndWait();
		}
		return formulaireOk;
	}

	private void genererReprises(LocalDate dateDebut, LocalDate dateFin, int heureDebut, int heureFin) {
		modeleDeReprise = new ModeleDeReprise(nomTF.getText());

		// calcul des reprises
		DayOfWeek jour = CalendarMaps.JOURS_SEMAINE.get(jourCB.getValue());
		DayOfWeek jourDebut = dateDebut.getDayOfWeek();
		dateDebut = dateDebut.plusDays(((jour.getValue() - jourDebut.getValue()) % 7 + 7) % 7);
		ArrayList<Reprise> reprises = new ArrayList<Reprise>();
		while (dateDebut.compareTo(dateFin) <= 0) {
			// on verifie si la reprise ne tombe pas pendant les vacances ou la
			// treve hivernale
			if (!((isTreveHivernale.selectedProperty().get() && JoursSpeciaux.isTreveHivernale(dateDebut))
					|| (isVacances.selectedProperty().get() && JoursSpeciaux.isVacances(dateDebut))
					|| JoursSpeciaux.isFerie(dateDebut))) {
				reprises.add(new Reprise(modeleDeReprise.getNom(), dateDebut, heureDebut, heureFin,
						modeleDeReprise.getId(), lieux.get(lieuCB.getValue()), listeCavaliers.getCavaliers(),
						listeMonitrices.getSelectedItems()));
			}
			dateDebut = dateDebut.plusDays(7);
		}

		// affichage des reprises calculées
		getChildren().clear();
		calendrierValidationModele = new CalendrierValidationModele(reprises, isTreveHivernale.selectedProperty().get(),
				true);
		AnchorPane.setBottomAnchor(calendrierValidationModele, 5.0);
		AnchorPane.setTopAnchor(calendrierValidationModele, 5.0);
		AnchorPane.setRightAnchor(calendrierValidationModele, 5.0);
		AnchorPane.setLeftAnchor(calendrierValidationModele, 5.0);
		getChildren().addAll(calendrierValidationModele);
	}

	private void validerModele(ArrayList<Reprise> reprises) {

		boolean validation = verifierJoursSpeciaux(reprises);

		if (validation) {
			boolean testConflitLieu = testConflitLieu(reprises);
			boolean testConflitMonitrice = testConflitMonitrice(reprises);

			if (!testConflitLieu && !testConflitMonitrice) {
				// ajout du modele de reprise
				try {
					QueryManager.ajoutModeleDeReprise(modeleDeReprise);
				} catch (SQLException e) {
					System.out.println("erreur durant l'ajout du modèle de reprise");
					e.printStackTrace();
					System.exit(0);
				}

				// ajout des reprises
				try {
					QueryManager.ajoutReprises(reprises);
				} catch (SQLException e) {
					System.out.println("erreur durant la création des reprises");
					e.printStackTrace();
					System.exit(0);
				}

				// affichage des modèles de reprises particuliers
				fireEvent(new NouvellePageEvent(new GestionParticuliers()));
			}
		}
	}

	private boolean verifierJoursSpeciaux(ArrayList<Reprise> reprises) {
		if (isTreveHivernale.selectedProperty().get() || isVacances.selectedProperty().get()) {
			for (Reprise reprise : reprises) {
				if ((isTreveHivernale.selectedProperty().get() && JoursSpeciaux.isTreveHivernale(reprise.getDate()))
						|| isVacances.selectedProperty().get() && JoursSpeciaux.isVacances(reprise.getDate())) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation de la validation");
					alert.setHeaderText(
							"Attention, certaines des reprises créées se situent pendant la trève hivernale ou pendant les vacances");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() != ButtonType.OK) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean testConflitLieu(ArrayList<Reprise> reprises) {
		boolean testConflitLieu = false;
		for (Reprise reprise : reprises) {
			try {
				testConflitLieu = QueryManager.testConflitLieu(reprise.getLieu().getId(), reprise.getSQLDate(),
						reprise.getHeureDebut(), reprise.getHeureFin());
			} catch (SQLException e) {
				System.out.println("erreur durant le test des lieux");
				e.printStackTrace();
				System.exit(0);
			}
			if (testConflitLieu) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Attention !");
				alert.setHeaderText("Le lieu est déjà occupé pour certaines reprises");
				alert.showAndWait();
				break;
			}
		}
		return testConflitLieu;
	}

	private boolean testConflitMonitrice(ArrayList<Reprise> reprises) {
		boolean testConflitMonitrice = false;
		for (int i = 0; i < reprises.size() && !testConflitMonitrice; i++) {
			for (Monitrice monitrice : reprises.get(i).getMonitrices()) {
				try {
					testConflitMonitrice = QueryManager.testConflitMonitrice(
							monitrice.getId(), reprises.get(i).getSQLDate(),
							reprises.get(i).getHeureDebut(), reprises.get(i).getHeureFin());
				} catch (SQLException e) {
					System.out.println("erreur durant le test des monitrices");
					e.printStackTrace();
					System.exit(0);
				}
				if (testConflitMonitrice) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Attention !");
					alert.setHeaderText("La ou les monitrices sont déjà affectées à une  reprise");
					alert.showAndWait();
					break;
				}
			}
		}
		return testConflitMonitrice;
	}

}
