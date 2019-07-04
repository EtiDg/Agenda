package agenda.ihm.controller.widget;

import agenda.process.object.Reprise;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class RepriseCell extends Cell{
	
	Reprise reprise;
	Label label;
	
	public RepriseCell(Reprise reprise){
		super(0);
		this.reprise  = reprise;
		getStyleClass().add("repriceCell");
		label = new Label(reprise.toString2());
		label.setPadding(new Insets(0,0,0, 3));
		getChildren().add(label);
	}
}
