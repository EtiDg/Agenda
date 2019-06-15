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

	protected Creneau creneau;
	protected HBox hBox = new HBox();
	protected Label heureLabel = new Label();
	
	public CreneauCell(int id, Creneau creneau){
		super(id);
		setCreneau(creneau);
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
	
	private void setCreneau(Creneau creneau){
		this.creneau = creneau;
		int heure = creneau.getHeureDebut();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		heureLabel.setText(LocalTime.of(heure%60, heure%60-heure).format(dtf));
	}
	
	public void selectCell(){
		getStyleClass().remove("white_background");
		getStyleClass().add("blue_background");
	}
	
	public void deselectCell(){
		getStyleClass().remove("blue_background");
		getStyleClass().add("white_background");
	}
	public Creneau getCreneau(){
		return creneau;
	}
	
}
