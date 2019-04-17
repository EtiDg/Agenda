package agenda.ihm.controller.widget;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class WeekCell extends Cell{

	protected LocalDate date;
	protected int heure;
	protected HBox hBox = new HBox();
	protected Label heureLabel = new Label();
	
	public WeekCell(int id, LocalDate date, int heure){
		super(id);
		this.date = date;
		setHeure(heure);
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
	
	private void setHeure(int heure){
		this.heure = heure;
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
	
	public int getHeure(){
		return heure;
	}
	
}
