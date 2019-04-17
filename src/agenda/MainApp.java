package agenda;

import java.io.IOException;

import agenda.ihm.controller.page.*;
import agenda.ihm.controller.widget.CalendarYear;
import agenda.ihm.controller.widget.YearCell;
import agenda.ihm.event.NouvellePageEvent;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private Scene scene;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Agenda");
        initRootLayout();

        showAccueil();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ihm/view/page/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            // Show the scene containing the root layout.
            scene = new Scene(rootLayout);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ajouteEventListeners();
    }

    /**
     * Shows the accueil inside the root layout.
     */
    public void showAccueil() {
    	//CreerMRParticulier accueil = new CreerMRParticulier();  
    	//GestionParticuliers accueil = new GestionParticuliers();
    	//GestionLieux accueil = new GestionLieux();
    	//GestionGroupes accueil = new GestionGroupes();
    	//GestionVacances accueil = new GestionVacances();
    	//GestionJoursFeries accueil = new GestionJoursFeries();
    	GestionJoursSpeciaux accueil = new GestionJoursSpeciaux();
        //InfoModeleDeReprise accueil = new InfoModeleDeReprise();
    	//CalendarYear accueil = new CalendarYear(2019, true);
        // Set person overview into the center of root layout.
        rootLayout.setCenter(accueil);
    }
    
    /**
     * ajoute les events listeners pour detecter les changements de page
     */
    public void ajouteEventListeners(){
    	rootLayout.addEventHandler(NouvellePageEvent.NOUVELLE_PAGE,
                new EventHandler<NouvellePageEvent>() {
					@Override
					public void handle(NouvellePageEvent event) {
						rootLayout.getChildren().removeAll();
						rootLayout.setCenter(event.getPage());
					}
        		}
        );
    }
    
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}
