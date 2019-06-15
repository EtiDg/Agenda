package agenda.ihm.controller.page;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import agenda.ihm.event.NouvellePageEvent;
import agenda.ihm.event.ValiderModeleEvent;
import agenda.process.object.Groupe;
import agenda.process.object.JoursSpeciaux;
import agenda.process.object.Reprise;
import agenda.process.sql.QueryManager;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class CreerGroupe extends CreerMRParticulier{
	    

	public CreerGroupe(){
		super();
		removeEventHandler(ValiderModeleEvent.VALIDER_MODELE, eventHandler);
		eventHandler = new EventHandler<ValiderModeleEvent>() {
					@Override
					public void handle(ValiderModeleEvent event) {
						ArrayList<Reprise> reprises = calendrierValidationModele.getReprises();
						validerModele(reprises);
						
					}
		};
		addEventHandler(ValiderModeleEvent.VALIDER_MODELE, eventHandler);
	}
	
	private void validerModele(ArrayList<Reprise> reprises){
		
		boolean validation = verifierJoursSpeciaux(reprises);

		if (validation){
			boolean testConflitLieu = testConflitLieu(reprises);
			boolean testConflitMonitrice = testConflitMonitrice(reprises);

			if (!testConflitLieu && !testConflitMonitrice){
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
					QueryManager.ajoutReprises(reprises);
				} catch (SQLException e) {
					System.out.println("erreur durant la création des reprises");
					e.printStackTrace();
					System.exit(0);
				}
				
				//affichage des modèles de reprises particuliers
				fireEvent(new NouvellePageEvent(new GestionGroupes()));
			}
		}
	}
	
	private boolean verifierJoursSpeciaux(ArrayList<Reprise> reprises){
		if (isTreveHivernale.selectedProperty().get()  || isVacances.selectedProperty().get()){
			for (int i=0; i<reprises.size();i++){
				if ( (isTreveHivernale.selectedProperty().get() && JoursSpeciaux.isTreveHivernale(reprises.get(i).getDate())) || 
						isVacances.selectedProperty().get() && JoursSpeciaux.isVacances(reprises.get(i).getDate())){
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation de la validation");
					alert.setHeaderText("Attention, certaines des reprises créées se situent pendant la trève hivernale ou pendant les vacances");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() != ButtonType.OK){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private boolean testConflitLieu(ArrayList<Reprise> reprises){
		boolean testConflitLieu = false;
		for (int i=0; i<reprises.size();i++){
			try{
				testConflitLieu = QueryManager.testConflitLieu(reprises.get(i).getLieu().getId(), reprises.get(i).getSQLDate(), reprises.get(i).getHeureDebut(), reprises.get(i).getHeureFin());
			} catch (SQLException e) {
				System.out.println("erreur durant le test des lieux");
				e.printStackTrace();
				System.exit(0);
			}
			if (testConflitLieu){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Attention !");
				alert.setHeaderText("Le lieu est déjà occupé pour certaines reprises");
				alert.showAndWait();
				break;
			}
		}
		return testConflitLieu;
	}
		
	private boolean testConflitMonitrice(ArrayList<Reprise> reprises){
		boolean testConflitMonitrice = false;
		for (int i=0; i<reprises.size() && !testConflitMonitrice;i++){
			for(int j=0; j < reprises.get(i).getMonitrices().size();j++){
				try{
					testConflitMonitrice = QueryManager.testConflitMonitrice(reprises.get(i).getMonitrices().get(j).getId(), reprises.get(i).getSQLDate(), reprises.get(i).getHeureDebut(), reprises.get(i).getHeureFin());
				} catch (SQLException e) {
					System.out.println("erreur durant le test des monitrices");
					e.printStackTrace();
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
		}
		return testConflitMonitrice;
	}
}