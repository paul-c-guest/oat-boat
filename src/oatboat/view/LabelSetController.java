package oatboat.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.stage.Stage;
import oatboat.model.database.Database;
import oatboat.model.food.LabelSet;
import oatboat.model.food.Recipe;

public class LabelSetController {

	@FXML
	private ComboBox<LabelSet> labelSets;

	@FXML
	private TextField title;

	@FXML
	private TextField subtitle;

	@FXML
	private TextField contentsPrefix;

	@FXML
	private TextField nutritionPrefix;

	@FXML
	private TextField additionalInfo;

	@FXML
	private Spinner<Integer> defaultExpirySpinner;

	@FXML
	private Spinner<Integer> manualWeightEntrySpinner;

	@FXML
	private CheckBox manualWeightEntryCheckBox;

	@FXML
	private Label manualWeightEntryLabel;

	@FXML
	private Button clearFields;

	@FXML
	private Button update;

	@FXML
	private Button saveNew;

	@FXML
	private Button delete;

	@FXML
	private Button exit;

	private MainController mainApp;
	private Stage self;
	private Database db;
	private LabelSet selectedSet;

	public void setParentController(MainController controller) {
		this.mainApp = controller;
	}

	public void setSelfReference(Stage stage) {
		this.self = stage;
	}

	public void setDatabase(Database db) {
		this.db = db;
	}

	public void initialise() {
		labelSets.setCellFactory(ComboBoxListCell.forListView());

		labelSets.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSet, newSet) -> setSelectedLabelSet(newSet));

		labelSets.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				boolean comboBoxHasSelection = newValue.intValue() == -1;
				delete.setDisable(comboBoxHasSelection);
				update.setDisable(comboBoxHasSelection);
			}
		});

		defaultExpirySpinner
				.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1));

		defaultExpirySpinner.setOnScroll(event -> {
			if (event.getDeltaY() > 0)
				defaultExpirySpinner.increment();
			if (event.getDeltaY() < 0)
				defaultExpirySpinner.decrement();
		});

		manualWeightEntrySpinner
				.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

		manualWeightEntrySpinner.setOnScroll(event -> {
			if (event.getDeltaY() > 0)
				manualWeightEntrySpinner.increment();
			if (event.getDeltaY() < 0)
				manualWeightEntrySpinner.decrement();
		});

		manualWeightEntryCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				manualWeightEntrySpinner.setDisable(!newValue);
			}
		});

		manualWeightEntryLabel.setOnMouseClicked(event -> {
			if (manualWeightEntryCheckBox.isSelected())
				manualWeightEntryCheckBox.setSelected(false);
			else
				manualWeightEntryCheckBox.setSelected(true);
		});

		self.setOnCloseRequest(event -> exit());

		resetComboBoxItems();
	}

	private void setSelectedLabelSet(LabelSet set) {
		this.selectedSet = set;

		if (set == null) {
			TextField[] fields = { title, subtitle, contentsPrefix, nutritionPrefix, additionalInfo };
			for (TextField tf : fields)
				tf.clear();
			defaultExpirySpinner.getValueFactory().setValue(1);
			manualWeightEntryCheckBox.setSelected(false);
			return;

		} else {
			title.setText(set.mainTitle);
			subtitle.setText(set.subTitle);
			contentsPrefix.setText(set.contentsPrefix);
			nutritionPrefix.setText(set.nutritionPrefix);
			additionalInfo.setText(set.additionalInfo);
			defaultExpirySpinner.getValueFactory().setValue(set.expiry);
			manualWeightEntryCheckBox.setSelected(set.clientSuppliedWeight);
			if (manualWeightEntryCheckBox.isSelected())
				manualWeightEntrySpinner.getValueFactory().setValue(set.unitWeight);
		}
	}

//@formatter:off
	private LabelSet makeLabelSetFromFields(String setName) {
		Integer userSpecifiedWeight = null;
		if (manualWeightEntryCheckBox.isSelected()) 
			userSpecifiedWeight = manualWeightEntrySpinner.getValue();
		
		return new LabelSet(
			setName,
			title.getText(), 
			subtitle.getText(), 
			contentsPrefix.getText(), 
			nutritionPrefix.getText(), 
			additionalInfo.getText(), 
			defaultExpirySpinner.getValue(), 
			manualWeightEntryCheckBox.isSelected(),
			userSpecifiedWeight
		);
	}
//@formatter:on

	private void resetComboBoxItems() {
		labelSets.getItems().setAll(db.getLabelSets());
	}

	// --- Button methods ---//

	@FXML
	private void clearFields() {
		setSelectedLabelSet(null);
		labelSets.getSelectionModel().clearSelection();
	}

	@FXML
	private void delete() {
		List<String> linkedRecipeUsages = new ArrayList<String>();
		for (Recipe rcp : db.getRecipes())
			if (rcp.getLabelSet() != null && rcp.getLabelSet().equals(selectedSet.setName))
				linkedRecipeUsages.add(rcp.getTitle());

		Alert deleteAlert = new Alert(AlertType.CONFIRMATION);
		deleteAlert.setHeaderText("Delete the Label Set " + selectedSet.setName + "?");

		if (!linkedRecipeUsages.isEmpty()) {
			StringBuilder content = new StringBuilder("There are recipes currently associated with this set:\n\n");
			for (String recipeName : linkedRecipeUsages)
				content.append("\u2022 " + recipeName + "\n");
			deleteAlert.setContentText(content.toString());

		} else {
			deleteAlert.setContentText("The set is not used by any recipes");
			deleteAlert.setGraphic(null);
		}

		Optional<ButtonType> response = deleteAlert.showAndWait();

		if (response.get() == ButtonType.OK) {
			db.deleteLabelSet(selectedSet.setName);
			resetComboBoxItems();
			for (String rcp : linkedRecipeUsages)
				db.getRecipe(rcp).setLabelSet(null);
		}
	}

	@FXML
	private void update() {
		int current = labelSets.getSelectionModel().getSelectedIndex();
		db.addLabelSet(makeLabelSetFromFields(selectedSet.setName));
		resetComboBoxItems();
		labelSets.getSelectionModel().select(current);
	}

	@FXML
	private void saveNew() {
		LabelSet newSet = null;
		TextInputDialog getName = new TextInputDialog();
		getName.setTitle("New Label Set");
		getName.setHeaderText("Give the new set a good name:");
		getName.setContentText(null);

		String newSetName = getName.showAndWait().get().trim();
		if (newSetName.length() > 0) {
			newSet = makeLabelSetFromFields(newSetName);
			this.selectedSet = newSet;
			db.addLabelSet(newSet);
		}

		resetComboBoxItems();
		labelSets.getSelectionModel().select(newSet);
	}

	@FXML
	private void exit() {
		mainApp.updateLabelSetComboBox();
		mainApp.closeThisWindow(self);
	}

}
