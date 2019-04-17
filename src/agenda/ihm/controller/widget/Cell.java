package agenda.ihm.controller.widget;

import agenda.ihm.event.CellClickedEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public abstract class Cell extends AnchorPane{
	
	int id;

	public Cell(int id){
		this.id = id;
		getStyleClass().add("white_background");
		setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
        	public void handle(MouseEvent e){
    			handleClick(e);
            }
        });
	}
	
	public void handleClick(MouseEvent e){
			fireEvent(new CellClickedEvent(id, e));
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
