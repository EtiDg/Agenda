package agenda.ihm.controller.widget;

import java.time.LocalDate;
import java.util.ArrayList;

import agenda.ihm.event.CellClickedEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

abstract class CellCollection<T extends Cell> extends AnchorPane{

	protected ArrayList<T> collection = new ArrayList<T>();
	protected ArrayList<Integer> selectedCells = new ArrayList<Integer>();
	protected boolean isMultiple;

	public CellCollection(boolean isMultiple){

		this.isMultiple = isMultiple;

		addEventHandler(CellClickedEvent.CELL_SELECTED,
				new EventHandler<CellClickedEvent>() {
			@Override
			public void handle(CellClickedEvent event) {
				MouseEvent e = event.getMouseClick();
				if (!isMultiple){
					if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount()==1){
						if (!e.isControlDown()){
							clearCells();
							selectCell(event.getCell());
						}else{
							if (selectedCells.contains(event.getCell())){
								deselectCell(event.getCell());
							}else{
								selectCell(event.getCell());
							}
						}
					}
				}else{
					if (e.getButton().equals(MouseButton.PRIMARY)){
						if (selectedCells.contains(event.getCell())){
							deselectCell(event.getCell());
						}else{
							selectCell(event.getCell());
						}
					}
				}	
			}

		}
				);

	}

	protected void selectCell(Integer cell){
		collection.get(cell).selectCell();
		selectedCells.add(cell);
	}

	protected void deselectCell(Integer cell){
		collection.get(cell).deselectCell();
		selectedCells.remove(cell);
	}

	protected void clearCells(){
		while(!selectedCells.isEmpty()){
			deselectCell(selectedCells.get(0));
		}
	}
	
	public ArrayList<T> getSelectedCells(){
		ArrayList<T> selectedCells = new ArrayList<T>();
		for (int i = 0; i<this.selectedCells.size(); i++){
			selectedCells.add(collection.get(this.selectedCells.get(i)));
		}
		return selectedCells;
	}

}
