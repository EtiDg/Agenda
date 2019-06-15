package agenda.ihm.controller.widget;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import agenda.process.object.JoursSpeciaux;
import agenda.CalendarMaps;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class YearCell extends Cell{
	
	protected LocalDate date;
	boolean isTreveHivernale;
	boolean isVacances;
	protected HBox hBox = new HBox();
	protected Label jourT = new Label();
	protected Label jourN = new Label();
	
	public YearCell(int id, LocalDate date, boolean isTreveHivernale, boolean isVacances){
		super(id);
		setDate(date);
		this.isTreveHivernale = isTreveHivernale;
		this.isVacances = isVacances;
		GridPane.setHgrow(this, Priority.ALWAYS);
		GridPane.setVgrow(this, Priority.ALWAYS);
		AnchorPane.setTopAnchor(hBox, 0.0);
		AnchorPane.setLeftAnchor(hBox, 0.0);
		AnchorPane.setRightAnchor(hBox, 0.0);
		AnchorPane.setBottomAnchor(hBox, 0.0);
		jourT.setPrefWidth(12);
		jourT.getStyleClass().add("right_border");
		jourN.setPrefWidth(15);
		jourN.getStyleClass().add("right_border");
		hBox.getChildren().addAll(jourT, jourN);
		if (isTreveHivernale && JoursSpeciaux.isTreveHivernale(date)){
			Pane pane = new Pane();
			pane.getStyleClass().add("treveHivernale_background");
			pane.setPrefWidth(20);
			hBox.getChildren().add(pane);
		}
		if (isVacances && JoursSpeciaux.isVacances(date)){
			Pane pane = new Pane();
			pane.getStyleClass().add("vacances_background");
			pane.setPrefWidth(20);
			hBox.getChildren().add(pane);
		}
		if (JoursSpeciaux.isFerie(date)){
			Pane pane = new Pane();
			pane.getStyleClass().add("jourFerie_background");
			pane.setPrefWidth(20);
			hBox.getChildren().add(pane);
		}
	    //hBox.setPadding(new Insets(0, 5, 0, 5));
	    hBox.setSpacing(1);

		//hBox.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		getChildren().add(hBox);


	}
	
	private void setDate(LocalDate date){
		this.date = date;
		jourT.setText(CalendarMaps.JOURS_ABREGES.get(date.getDayOfWeek()));
		jourN.setText(String.valueOf(date.getDayOfMonth()));
		if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
			getStyleClass().remove("white_background");
			getStyleClass().add("gray_background");
		}
	}
	
	public void selectCell(){
		if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
			getStyleClass().remove("gray_background");
			getStyleClass().add("bluegray_background");
		}else{
			getStyleClass().remove("white_background");
			getStyleClass().add("blue_background");
		}
	}
	
	public void deselectCell(){
		if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
			getStyleClass().remove("bluegray_background");
			getStyleClass().add("gray_background");
		}else{
			getStyleClass().remove("blue_background");
			getStyleClass().add("white_background");
		}
	}
	
	public LocalDate getDate(){
		return date;
	}
	
	
}
