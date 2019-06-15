package agenda.ihm.controller.widget;

import java.util.ArrayList;

import agenda.process.object.Reprise;
import javafx.scene.control.Label;

public class RepriseYearCell extends YearCell{
	
	ArrayList<Reprise> reprises = new ArrayList<Reprise>();

	Label repriseT = new Label();
	
	public RepriseYearCell(){
	}
	
	public void addReprise(ArrayList<Reprise> reprises){
		reprises.addAll(reprises);
		updateLabel();
	}
	
	public void addReprise(Reprise reprise){
		reprises.add(reprise);
		updateLabel();
	}
	
	private void updateLabel(){
		if (reprises.size() == 1){
			int heureDebut = reprises.get(0).getHeureDebut();
			int heureFin= reprises.get(0).getHeureFin();
			repriseT.setText(((heureDebut-heureDebut%60)/60) + "h" + heureDebut%60 + "-" 
						+ ((heureFin-heureFin%60)/60) + "h" + heureFin%60);
		}else if(reprises.size() > 1){
			repriseT.setText(reprises.size() + " reprises");
		}
	}
	

}
