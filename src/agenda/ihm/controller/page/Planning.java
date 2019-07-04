package agenda.ihm.controller.page;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import agenda.MainApp;
import agenda.excel.GetExcel;
import agenda.ihm.controller.widget.CalendarWeek;
import agenda.ihm.controller.widget.DateManagement;
import agenda.ihm.event.ValiderModeleEvent;
import agenda.process.object.Reprise;
import agenda.process.sql.QueryManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class Planning extends AnchorPane {

	LocalDate date;
	@FXML private DatePicker dateDebutDP;
	@FXML private DatePicker dateFinDP;
	@FXML private CalendarWeek calendarWeek;

	public Planning() {
		// import du fxml
		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ihm/view/page/Planning.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		// iniitialisation des dates
		dateDebutDP.setValue(NOW_LOCAL_DATE());
		dateFinDP.setValue(NOW_LOCAL_DATE());

		// chargement du planning
		date = LocalDate.now();
		updateCalendrier();

	}

	public void handleAjouterSemaine(ActionEvent e) {
		date = date.plusDays(7);
		updateCalendrier();
	}

	public void handleRetirerSemaine(ActionEvent e) {
		date = date.minusDays(7);
		updateCalendrier();
	}

	public void handleAjouterMois(ActionEvent e) {
		date = date.plusMonths(1);
		updateCalendrier();
	}

	public void handleRetirerMois(ActionEvent e) {
		date = date.minusMonths(1);
		updateCalendrier();
	}
	
	public void handleGenererPlanningExcel(ActionEvent e){
		if (dateDebutDP.getValue().isBefore(dateFinDP.getValue()) || dateDebutDP.getValue().isEqual(dateFinDP.getValue()));
		GetExcel.get(dateDebutDP.getValue(), dateFinDP.getValue());
	}
	
	private void updateCalendrier(){
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		try {
			calendarWeek.updateCalendrier(QueryManager.selectReprisesDeSemaine(date.getYear(),
					date.get(weekFields.weekOfWeekBasedYear()) - 1), date);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des reprises du planning");
			System.exit(0);
		}
	}
	
	public static final LocalDate NOW_LOCAL_DATE (){
        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date , formatter);
        return localDate;
    }
}
