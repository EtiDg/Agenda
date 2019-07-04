package agenda.ihm.controller.widget;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import agenda.process.object.Creneau;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CreneauCell extends Cell{

	protected int heureDebut;
	protected int minutesDebut;
	protected HBox hBox = new HBox();
	protected Label heureLabel = new Label();
	
	public CreneauCell(int id, int heureDebut, int minutesDebut){
		super(id);
		
		//affichage de l'heure
		this.heureDebut = heureDebut;
		this.minutesDebut = minutesDebut;
		heureLabel.setText(heureDebut + "h" + minutesDebut);
		
		GridPane.setHgrow(this, Priority.ALWAYS);
		GridPane.setVgrow(this, Priority.ALWAYS);
		AnchorPane.setTopAnchor(hBox, 0.0);
		AnchorPane.setLeftAnchor(hBox, 0.0);
		AnchorPane.setRightAnchor(hBox, 0.0);
		AnchorPane.setBottomAnchor(hBox, 0.0);
		heureLabel.setPrefWidth(20);
		heureLabel.getStyleClass().add("right_border");
		hBox.getChildren().add(heureLabel);
	    hBox.setSpacing(1);

		getChildren().add(hBox);


	}
	
	public void selectCell(){
		getStyleClass().remove("white_background");
		getStyleClass().add("blue_background");
	}
	
	public void deselectCell(){
		getStyleClass().remove("blue_background");
		getStyleClass().add("white_background");
	}

	
}
