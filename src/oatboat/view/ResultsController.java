package oatboat.view;

import java.util.Map;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import oatboat.model.logic.FDCDatabase;
import oatboat.model.logic.FDCSearch;

public class ResultsController {

	@FXML
	private TableView<String> resultsTable;

	@FXML
	private TableColumn<String, String> resultsColumn;

	@FXML
	private TextField keyword;

	@FXML
	private Button confirm;

	@FXML
	private Button cancel;

	@FXML
	private Button search;

	private MainController mainApp;
	private Map<String, Integer> results;
	private String selection = null;
	private Stage self;
	private FDCDatabase fdcDB;

	public void setParentController(MainController maincontrol) {
		this.mainApp = maincontrol;
	}

	public void setSelfReference(Stage stage) {
		this.self = stage;
	}

	public void preDisplayTasks() {
		resultsColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		resultsColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()));

		resultsTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selection = newValue);
		
		keyword.setOnKeyTyped((event) -> {
			getAndDisplayResults();
		});
	}

	public void postDisplayTasks() {
		keyword.requestFocus();
		initDB();
	}

	private void initDB() {
		fdcDB = new FDCDatabase();
		fdcDB.isReady().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				confirm.setDisable(!newValue);
			}
		});
		try {
			Thread dbThread = new Thread(fdcDB);
			dbThread.start();

		} catch (Exception e) {
			System.out.println("fdc treemap thread failed");
//			e.printStackTrace();
		}
	}

	@FXML
	private void getAndDisplayResults() {
		results = FDCSearch.getResults(keyword.getText());
		resultsTable.getItems().setAll(results.keySet());
	}

	// OK button action
	@FXML
	private void passbackIngredient() throws Exception {
		if (selection != null) {
			Integer fdcID = results.get(selection);
			mainApp.enterIngredientDetails(selection, fdcID.toString(), fdcDB.getNutrientSet(fdcID));
		}
	}

	@FXML
	private void close() {
		mainApp.closeThisWindow(self);
	}

}