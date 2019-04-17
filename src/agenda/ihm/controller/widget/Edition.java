package agenda.ihm.controller.widget;

import java.io.IOException;
import agenda.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public abstract class Edition extends AnchorPane{
	
	private StringProperty intitule;
	@FXML private Text textIntitule;
	@FXML private Button buttonEditer;
	@FXML private HBox hBox;
	private Button buttonValider;
	
	public Edition(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/widget/Edition.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        intituleProperty();
        buttonValider = new Button("Valider");
        buttonValider.setOnMouseClicked(this::handleValider);
	}
	
	public abstract void handleEditer(MouseEvent e);
	
	public abstract void handleValider(MouseEvent e);
	
	public void editer(Node node){			
		hBox.getChildren().clear();
		hBox.getChildren().addAll(buttonValider, textIntitule, node);
	}
	
	public void valider(Node node){			
		hBox.getChildren().clear();
		hBox.getChildren().addAll(buttonEditer, textIntitule, node);
	}
	
	public final StringProperty intituleProperty() {
		if (intitule == null) {
			intitule = new SimpleStringProperty("Intitulé" + " :");
		}
		return intitule;
	}
	
	public String getIntitule(){
		return intitule.get();
	}
	
	public void setIntitule(String titre){
		this.intitule.set(titre);
		textIntitule.setText(titre + " :");
	}
	
}
