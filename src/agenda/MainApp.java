package agenda;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Agenda");

        initRootLayout();

        showAccueil();
	}

	/**
     * Initialise la page principale
     */
    public void initRootLayout() {
        try {
            // charge le menu
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ihm/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // affiche la fenêtre principale avec le menu
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true); 
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche la page d'accueil
     */
    public void showAccueil() {
        try {
            // charge la page d'accueil.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ihm/view/AccueilView.fxml"));
            AnchorPane accueilView = (AnchorPane) loader.load();
            
            // place la page d'accueil dans la fenêtre principale
            rootLayout.setCenter(accueilView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Retourne la fenêtre principale
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
