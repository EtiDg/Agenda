package agenda.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import agenda.CalendarMaps;
import agenda.process.object.Reprise;
import agenda.process.sql.QueryManager;

public class GetExcel {
	private static final String MINUTES[] = { "00", "15", "30", "45" };
	private static final String FILE_NAME = "ExcelTest.xlsx";
	private static final int HEURE_MIN = 8;
	private static final int HEURE_MAX = 19;
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private static ArrayList<Reprise> listeReprises = new ArrayList<Reprise>();
	private static LocalDate date;
	
	private static XSSFWorkbook workbook;
	private static XSSFSheet sheet;
	private static ArrayList<ArrayList<Reprise>> reprises;
	private static ArrayList<ArrayList<String>> lieux; 
	private static Row row0;
	private static Row row1;
	private static CellStyle centerStyle;

	public static void get(LocalDate dateDebut, LocalDate dateFin) {

		// calcul de la date de début de semaine
		date = dateDebut.minusDays(dateDebut.getDayOfWeek().getValue() - 1);
		
		

		// creation du fichier excel
		workbook = new XSSFWorkbook();
		
		while (date.isBefore(dateFin)){
			
			importReprises();
			
			//creation de l'onglet pour la semaine
			sheet = workbook.createSheet(formatter.format(date));
	
			// affichage de la date
			row0 = sheet.createRow(0);
			row0.createCell(0).setCellValue(formatter.format(date));
	
			initialisationListes();
			initialisationStyles();
	
			triListes(listeReprises);
	
			// affiche les heures
			afficherHeures();
	
			System.out.println(reprises);
			System.out.println(lieux);
	
			int col = 1;
			row1 = sheet.createRow(1);
			for (int jour = 0; jour < 6; jour++) {
				afficherJour(jour, col);
	
				for (String lieu : lieux.get(jour)) {
					afficherLieu(col, lieu);
					afficherReprises(jour, col, lieu);
					col++;
				}
			}
	
			redimensionnerColonnes(col);
			
			//passage à la semaine suivante
			date = date.plusDays(7);
		}
		
		try {
			FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
	}

	private static void initialisationListes() {
		// réinitialisation des listes
		reprises = new ArrayList<ArrayList<Reprise>>();
		lieux = new ArrayList<ArrayList<String>>();
		
		for (int i = 0; i < 7; i++) {
			reprises.add(new ArrayList<Reprise>());
			lieux.add(new ArrayList<String>());
		}
	}

	private static void initialisationStyles() {
		centerStyle = workbook.createCellStyle();
		centerStyle.setAlignment(HorizontalAlignment.CENTER);
		centerStyle.setVerticalAlignment(VerticalAlignment.TOP);
		centerStyle.setWrapText(true);
	}

	private static void afficherHeures() {
		int rowNum = 2;
		for (int i = HEURE_MIN; i < HEURE_MAX; i++) {
			for (int j = 0; j < 4; j++) {
				Row row = sheet.createRow(rowNum++);
				Cell cell = row.createCell(0);
				cell.setCellValue(String.valueOf(i) + "h" + MINUTES[j]);
			}
		}
		sheet.autoSizeColumn(0);
	}

	private static void triListes(ArrayList<Reprise> listeReprises) {
		// tri des reprise par jours
		for (Reprise reprise : listeReprises) {
			int day = reprise.getDate().getDayOfWeek().getValue() - 1;
			reprises.get(day).add(reprise);
		}

		// recherche des lieux par jours
		int j = 0;
		for (ArrayList<Reprise> jour : reprises) {
			for (Reprise reprise : jour) {
				if (!lieux.get(j).contains(reprise.getLieu().getNom()))
					lieux.get(j).add(reprise.getLieu().getNom());
			}
			j++;
		}
	}

	private static void afficherJour(int jour, int col) {
		Cell cell = row0.createCell(col);
		if (reprises.get(jour).size() > 0) {
			cell.setCellValue(CalendarMaps.JOURS.get(jour));
			if (lieux.get(jour).size() >= 2) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, col, col + lieux.get(jour).size() - 1));
			}
		}

		cell.setCellStyle(centerStyle);
	}

	private static void afficherLieu(int col, String lieu) {
		Cell cell = row1.createCell(col);
		cell.setCellValue(lieu);
		cell.setCellStyle(centerStyle);
	}

	private static void afficherReprises(int jour, int col, String lieu) {
		for (Reprise reprise : reprises.get(jour)) {

			if (reprise.getLieu().getNom().equals(lieu) && reprise.getHeureDebut() >= HEURE_MIN * 60
					&& reprise.getHeureFin() < HEURE_MAX * 60) {
				int numRowHeureDebut = 4 * reprise.getHeureDebut() / 60 - 4 * HEURE_MIN + 2;
				int numRowHeureFin = (4 * reprise.getHeureFin() - 1) / 60 - 4 * HEURE_MIN + 2;
				Row row = sheet.getRow(numRowHeureDebut);
				Cell cell = row.createCell(col);
				cell.setCellValue(reprise.toString());
				cell.setCellStyle(centerStyle);
				if (numRowHeureFin > numRowHeureDebut) {
					sheet.addMergedRegion(new CellRangeAddress(numRowHeureDebut, numRowHeureFin, col, col));
				}
			}
		}

	}

	private static void redimensionnerColonnes(int col) {
		int size;
		if (col > 1) {
			size = 44000 / (col - 1);
		} else {
			size = 44000;
		}
		for (int i = 1; i < col; i++) {
			sheet.setColumnWidth(i, size);
		}
	}
	
	private static void importReprises(){
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		try {
			listeReprises = QueryManager.selectReprisesDeSemaine(date.getYear(),
					date.get(weekFields.weekOfWeekBasedYear()) - 1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur lors du chargement des reprises du planning");
			System.exit(0);
		}
	}
}