package oatboat.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import oatboat.Main;
import oatboat.model.database.Database;
import oatboat.model.database.DatabaseUtils;
import oatboat.model.database.consumable.Consumable;
import oatboat.model.database.grouping.Grouping;
import oatboat.model.export.ExportToICML;
import oatboat.model.export.ICMLBuilder;
import oatboat.model.food.Ingredient;
import oatboat.model.food.LabelSet;
import oatboat.model.food.NutrientSet;
import oatboat.model.food.Recipe;
import oatboat.model.logic.FDCGenerator;
import oatboat.model.logic.NutritionalFactsBuilder;
import oatboat.model.logic.Selection;
import oatboat.model.logic.alerts.AlertFactory;
import oatboat.model.logic.textfield.TextFieldUtils;
import oatboat.model.unit.UnitCalc;

/**
 * FXML controller for all main application functions.
 * <p>
 * Sections in the class (which generally correspond to tab views in the
 * application) are named according to a convention: e.g. the prefix rcp refers
 * to some method or content that is most relevant to the Recipe tab (rcp). The
 * first section deals with initialisation of the database and its presentation
 * for the user; populating the columns and setting FXML action listeners.
 * <p>
 * The nutr prefix refers to the Generator tab, which was previously named
 * Nutrition.
 * <p>
 * The Consumables and Settings tabs are currently implemented where necessary
 * for the function of other more critical operations. In places they present
 * limited functionality for the user; in others are merely placeholders.
 * 
 * @author paul guest
 */
public class MainController {

	@FXML
	private TabPane tabPane;
	@FXML
	private Tab tabNutr;
	@FXML
	private Tab tabDB;
	@FXML
	private Tab tabRcp;
	@FXML
	private Tab tabCost;
	@FXML
	private Tab tabSettings;

	// fxml elements for nutritional info tab

	@FXML
	private TableView<Ingredient> nutrIngredientsTable;

	@FXML
	private TableColumn<Ingredient, String> nutrIngredientsColumn;

	@FXML
	private TableView<Selection> nutrSelectionTable;

	@FXML
	private TableColumn<Selection, String> nutrSelectionNameColumn;

	@FXML
	private TableColumn<Selection, String> nutrSelectionWeightColumn;

	@FXML
	private Button nutrAddButton;

	@FXML
	private Button nutrRemoveButton;

	@FXML
	private Button nutrRemoveAllButton;

	@FXML
	private Button nutrExportICML;

	@FXML
	private Button nutrCopy;

	@FXML
	private CheckBox nutrIncludePercentage;

	@FXML
	private TextField nutrGramsTextField;

	@FXML
	private TextArea nutrNutritionalFactsTextArea;

	@FXML
	private TextField nutrUnitCount;

	@FXML
	private TextField nutrUnitCost;

	@FXML
	private Label nutrAlertText;

	// --- fxml elements for database tab ---//

	@FXML
	private TableView<Ingredient> dbIngredientsTable;

	@FXML
	private TableColumn<Ingredient, String> dbIngredientsColumn;

	@FXML
	private Button dbButtonDelete;

	@FXML
	private Button dbButtonAdd;

	@FXML
	private Button dbButtonClear;

	@FXML
	private Button dbButtonSave;

	@FXML
	private TextField dbNameText;

	@FXML
	private Label dbIDnumberLabel;

	@FXML
	private Label dbIDtextLabel;

	@FXML
	private TextField dbLabelTextText;

	@FXML
	private Tooltip dbLabelTextTooltip;

	@FXML
	private TextField dbEnergyKJText;

	@FXML
	private TextField dbEnergyKCalText;

	@FXML
	private TextField dbFatsText;

	@FXML
	private TextField dbSaturatesText;

	@FXML
	private TextField dbCarbohydratesText;

	@FXML
	private TextField dbSugarsText;

	@FXML
	private TextField dbProteinText;

	@FXML
	private TextField dbSaltText;

	@FXML
	private TextField dbDescText;

	@FXML
	private TextField dbCostPerKiloText;

	@FXML
	private CheckBox dbGrouping0CheckBox;

	@FXML
	private CheckBox dbGrouping1CheckBox;

	@FXML
	private Label dbGrouping0Description;

	@FXML
	private Label dbGrouping1Description;

	// recipe tab components

	@FXML
	private ComboBox<Recipe> rcpSelectionCombo;

	@FXML
	private TableView<Selection> rcpSelectionsTable;

	@FXML
	private TableColumn<Selection, String> rcpIngredientColumn;

	@FXML
	private TableColumn<Selection, String> rcpRatioColumn;

	@FXML
	private TableColumn<Selection, String> rcpAmountColumn;

	@FXML
	private Label rcpKeystoneTitleLabel;

	@FXML
	private TextField rcpSelectedIngNameField;

	@FXML
	private Spinner<Double> rcpSelectedIngRatioSpinner;

	@FXML
	private TextField rcpSelectedIngAmountField;

	@FXML
	private TextField rcpBatchCountField;

	@FXML
	private ComboBox<LabelSet> rcpLabelSetComboBox;

	@FXML
	private Button rcpEditLabelSetsButton;

	@FXML
	private Spinner<Integer> rcpExpiryDateSpinner;

	@FXML
	private Label rcpExpiryDateLabel;

	@FXML
	private Button rcpUpdateSelectionButton;

	@FXML
	private Button rcpDeleteSelectionButton;

	@FXML
	private Button rcpNewRecipeButton;

	@FXML
	private Button rcpDeleteRecipeButton;

	@FXML
	private Button rcpSaveRecipeButton;

	@FXML
	private Button rcpGenerateFromRecipeButton;

	// fxml elements for costing tab

	@FXML
	private TextField costItemNameTextField;
	@FXML
	private TextField costSampleSizeTextField;
	@FXML
	private TextField costSampleCostTextField;
	@FXML
	private TextField costItemDescriptionTextField;
	@FXML
	private TextField costTotalUnitCost;

	@FXML
	private TableView<Consumable> costConsumableTableView;

	@FXML
	private TableColumn<Consumable, String> costConsumableItemTableColumn;

	// fxml fields for settings tab

	@FXML
	private TextField settingGrouping1DisplayText;

	@FXML
	private TextField settingGrouping1Description;

	@FXML
	private TextField settingFilename;

	@FXML
	private ToggleButton settingFilenameTextLock;

	@FXML
	private ComboBox<String> settingDecimalFormatComboBox;

	@FXML
	private Button settingTesting;

	// non-fxml objects
	private Main main;
	private Database db;
	private Stage parentStage, results, labelSetWindow;
	private List<Ingredient> nutrIngrdList, dbIngrdList;
	private ObservableList<Selection> nutrSelections;
	private Recipe selectedRecipe;
	private Ingredient selectedNutrIngredient;
	private Selection selectedNutrSelection, selectedRcpSelection;
	private StringConverter<Number> doubleConv, integerConv;

	/**
	 * methods intended to be called from Main.class to set self references for
	 * proper operation of application: setMain, setDatabase, setParentStage; and
	 * for initialisation of the main controller environment:
	 * preDisplayInitialisation.
	 */

	public void setMain(Main main) {
		this.main = main;
	}

	public void setDatabase() {
		db = main.getDatabase();
		createLists();
	}

	private void createLists() {
		nutrIngrdList = db.getIngredientList();
		dbIngrdList = db.getIngredientList();
		nutrSelections = FXCollections.observableArrayList();
	}

	public void setParentStage(Stage parent) {
		parentStage = parent;
	}

	// called BEFORE main stage is shown
	public void preDisplayInitialisation() {
		setDatabase();

		prepareStringConverters();
		prepareTextFieldFilters();

		prepareNutritionTabItems();
		prepareDatabaseTabItems();
		prepareRecipeTabItems();
		prepareConsumablesTabItems();
		prepareSettingsTabItems();

		setAllDatabaseItems();
	}

	private void setAllDatabaseItems() {
		updateNutritionColumns();
		updateDatabaseColumn();
		updateRecipesComboBox();
		updateLabelSetComboBox();
		settingLoadGroupings();
		rcpSetExpiryLabel();
		costConsumableTableView.setItems(db.getConsumables());
	}

	// called AFTER main stage is shown
	public void postDisplayInitialisation() {
		alertIfDecimalFormatNotSet();
	}

	private void alertIfDecimalFormatNotSet() {
//		settingDecimalFormatComboBox.getSelectionModel().select(-1); // uncomment for testing
		if (settingDecimalFormatComboBox.getSelectionModel().isSelected(-1)) {
			AlertFactory
					.inform("Settings need attention",
							"The decimal format needs to be selected, to ensure numbers appear in the required format.")
					.showAndWait();
			tabPane.getSelectionModel().select(tabSettings);
			settingDecimalFormatComboBox.requestFocus();
		}
	}

	private void prepareStringConverters() {
		doubleConv = TextFieldUtils.doubleConverter();
		integerConv = TextFieldUtils.integerConverter();
	}

	private void prepareTextFieldFilters() {
		for (TextField tf : new TextField[] { nutrUnitCount, costSampleSizeTextField, rcpBatchCountField })
			tf.setTextFormatter(TextFieldUtils.integerFormatter());

		for (TextField tf : new TextField[] { nutrGramsTextField, dbEnergyKJText, dbEnergyKCalText, dbFatsText,
				dbSaturatesText, dbCarbohydratesText, dbSugarsText, dbProteinText, dbSaltText, dbCostPerKiloText,
				rcpSelectedIngAmountField, costSampleCostTextField })
			tf.setTextFormatter(TextFieldUtils.doubleFormatter());
	}

//	some obscenely long javafx-related lines follow, disabling the Eclipse auto-formatter for a while so they stay on single lines
//	@formatter:off

	private void prepareNutritionTabItems() {
		nutrIngredientsColumn.setCellValueFactory(data -> data.getValue().getNameProperty());
		nutrIngredientsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSelectedNutrIngredient(newValue));

		nutrIngredientsTable.setOnKeyTyped(event -> {
			if (nutrIngredientsTable.getSelectionModel().getSelectedItem() == null) 
				nutrIngredientsTable.getSelectionModel().select(0);
			
			String keyPress = event.getCharacter().toLowerCase();
						
			// enterind a digit shifts typing to the grams entry field
			if (keyPress.matches("[0-9]")) {
				nutrGramsTextField.setText(keyPress);
				nutrGramsTextField.requestFocus();
				nutrGramsTextField.positionCaret(1);
			}
			
			// entering a letter attempts to select a matching ingredient by first letter
			else if (keyPress.matches("[a-z]")) {
				int start = nutrIngredientsTable.getSelectionModel().getSelectedIndex();
				int position = start;
				int entries = nutrIngrdList.size();
				char pressed = keyPress.charAt(0);
				
				// start at the current selected ingredient, and loop iteration back to selection.
				// resulting behaviour is to select next match if user presses same letter
				for (int i = start; i < entries + start; i++) {

					position = i % entries;
					Ingredient current = nutrIngredientsTable.getItems().get(position);
					
					if (current.getName().toLowerCase().charAt(0) == pressed
							&& nutrIngredientsTable.getSelectionModel().getSelectedItem() != current) {
						nutrIngredientsTable.getSelectionModel().clearAndSelect(position);
						nutrIngredientsTable.scrollTo(position);
						break;
					}
				}
				
			} else event.consume();
		});

		// selections table, name column
		nutrSelectionNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nutrSelectionNameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAmendedName()));

		// selections table, amount column
		nutrSelectionWeightColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nutrSelectionWeightColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(rcpAmountFormatter(data.getValue().getAmount())));

		// table listener
		nutrSelectionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSelectedNutrSelection(newValue));
		
		nutrRemoveButton.disableProperty().bind(nutrSelectionTable.getSelectionModel().selectedItemProperty().isNull());
		
		nutrSelections.addListener(new ListChangeListener<Selection>() {
			@Override
			public void onChanged(Change<? extends Selection> change) {
				while (change.next()) {
					nutrRemoveAllButton.setDisable(nutrSelections.isEmpty());
				}
			}
		});
		nutrRemoveAllButton.disabledProperty().addListener((obs, previous, current) -> rcpNewRecipeButton.setDisable(current.booleanValue()));
		
		nutrUnitCount.setOnKeyTyped((event) -> nutrUpdateNutritionalFactsText());
	}

	private void prepareDatabaseTabItems() {
		dbIngredientsColumn.setCellValueFactory(data -> data.getValue().getNameProperty());
		
		dbIngredientsTable.getSelectionModel().selectedItemProperty().addListener((obs, previous, current) -> dbBindIngredientTextFields(current, previous));
		dbIngredientsTable.getSelectionModel().selectedIndexProperty().addListener((obs, previous, current) -> dbButtonDelete.setDisable(current.intValue() == -1));
		
		dbLabelTextText.textProperty().addListener((obs, previous, current) -> dbLabelTextTooltip.setText(current));
		
		// following complicated mess untimately controls disabled state of save button
		BooleanProperty ingredientExists = new SimpleBooleanProperty();
		db.getIngredients().addListener(new MapChangeListener<String, Ingredient>() {
			@Override
			public void onChanged(Change<? extends String, ? extends Ingredient> change) {
				if (change.wasAdded())
					ingredientExists.setValue(db.hasIngredient(dbIDnumberLabel.getText()));
			}
		});
		dbIDnumberLabel.textProperty().addListener((obs, previous, current) -> ingredientExists.set(db.hasIngredient(current)));
		dbButtonSave.disableProperty().bind(
				dbNameText.textProperty().isEmpty()
				.or(ingredientExists)
				.or(dbEnergyKJText.textProperty().isEmpty())
				.or(dbEnergyKCalText.textProperty().isEmpty())
				.or(dbCarbohydratesText.textProperty().isEmpty())
				.or(dbSugarsText.textProperty().isEmpty())
				.or(dbFatsText.textProperty().isEmpty())
				.or(dbSaturatesText.textProperty().isEmpty())
				.or(dbProteinText.textProperty().isEmpty())
				.or(dbSaltText.textProperty().isEmpty())
				);
	}
	
	private void prepareRecipeTabItems() {
		rcpSelectionCombo.setCellFactory(ComboBoxListCell.forListView());
		rcpSelectionCombo.getSelectionModel().selectedItemProperty().addListener((obs, previous, current) -> setSelectedRecipeSelection(current));
		rcpSelectionCombo.getSelectionModel().selectedItemProperty().addListener((obs, previous, current) -> rcpGenerateFromRecipeButton.setDisable(current == null));

		rcpIngredientColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		rcpIngredientColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getIngredient().getName()));
		
		rcpRatioColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		rcpRatioColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(rcpRatioFormatter(data.getValue().getAmount())));
		
		rcpAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		rcpAmountColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(rcpAmountFormatter(data.getValue().getAmount() * selectedRecipe.getKeystone().getAmount())));
		
		rcpSelectionsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> rcpFillSelectionFields(newVal));

		rcpSelectedIngRatioSpinner.getEditor().setAlignment(Pos.CENTER);
		rcpSelectedIngRatioSpinner.setOnScroll(event -> {
			if (event.getDeltaY() > 0) rcpSelectedIngRatioSpinner.increment();
			else if (event.getDeltaY() < 0) rcpSelectedIngRatioSpinner.decrement();
		});
		
		rcpExpiryDateSpinner.getEditor().setAlignment(Pos.CENTER);
		rcpExpiryDateSpinner.setOnScroll(event -> {
			if (event.getDeltaY() > 0) rcpExpiryDateSpinner.increment();
			else if (event.getDeltaY() < 0) rcpExpiryDateSpinner.decrement();
		});
		rcpExpiryDateSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 3) {

			@Override
			public void increment(int steps) {
				super.increment(steps);
				rcpSetExpiryLabel();
			}

			@Override
			public void decrement(int steps) {
				super.decrement(steps);
				rcpSetExpiryLabel();
			}
		});
		// disable icml export button (nutr tab) if there is no label set is selected
		rcpLabelSetComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, previous, current) -> nutrExportICML.setDisable(current.intValue() == -1));
	}
	
	private void prepareConsumablesTabItems() {
		costConsumableItemTableColumn.setCellValueFactory(data -> data.getValue().getItemNameProperty());
		costConsumableTableView.getSelectionModel().selectedItemProperty().addListener((obs, previous, current) -> costBindAllFieldsToSelection(current, previous));
		
		updateTotalUnitCost();
		
		db.getConsumables().addListener(new ListChangeListener<Consumable>() {
			@Override
			public void onChanged(Change<? extends Consumable> changes) {
				while (changes.next()) {
				}
			}
		});
	}
	
	private Double getCombinedConsumablesUnitCost() {
		double total = 0.0;
		for (Consumable c : db.getConsumables())
			total += c.getCostPerSample() / c.getSampleSize();
		return total;
	}
	
	private String getCombinedConsumablesUnitCostAsString() {
		return String.format("%.2f", getCombinedConsumablesUnitCost());
	}
	
	private void updateTotalUnitCost() {
		costTotalUnitCost.setText(getCombinedConsumablesUnitCostAsString());		
	}
	
	private void prepareSettingsTabItems() {
		settingDecimalFormatComboBox.setCellFactory(ComboBoxListCell.forListView());
		settingDecimalFormatComboBox.getItems().addAll("Point: 0.0", "Comma: 0,0");
		settingDecimalFormatComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, previous, current) -> settingChangeDecimalSeparator(current.intValue(), previous.intValue()));
		settingDecimalFormatComboBox.getSelectionModel().select(main.getUserPrefs().getDecimalFormat());
		
		settingFilename.textFormatterProperty().set(TextFieldUtils.filenameFormatter());
		settingFilename.setText(main.getUserPrefs().getDatabaseFilename());
		
		settingFilename.setDisable(true);
		settingFilenameTextLock.selectedProperty().setValue(true);

		settingFilenameTextLock.selectedProperty().addListener((obs, previous, state) -> {
			settingFilename.setDisable(state);
			if (state) {
				settingSetDatabaseFilename();
				settingFilenameTextLock.setText("edit");
			}
			else settingFilenameTextLock.setText("lock");
		});	
	}
	
	//	@formatter:on

	private void rcpSetSelectionFields(double initialValue) {
		// reset the spinner for the new selection
		rcpSelectedIngRatioSpinner.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, initialValue, 0.01) {

					@Override
					public void increment(int steps) {
						super.increment(steps);
						rcpCalcAmountField();
					}

					@Override
					public void decrement(int steps) {
						super.decrement(steps);
						rcpCalcAmountField();
					}
				});

		rcpCalcAmountField();
	}

	private void rcpCalcAmountField() {
		rcpSelectedIngAmountField.setText(
				rcpAmountFormatter(selectedRecipe.getKeystone().getAmount() * rcpSelectedIngRatioSpinner.getValue()));
	}

	@FXML
	private void rcpSetRatioSpinnerNewValue() {
		Double newAmount;
		try {
			newAmount = parseDouble(rcpSelectedIngAmountField.getText());
		} catch (Exception e) {
			newAmount = 0.0;
		}

		rcpSetSelectionFields(newAmount / selectedRecipe.getKeystone().getAmount());

	}

	private double rcpGetSelectionRatio() {
		if (selectedRcpSelection != null)
			return selectedRcpSelection.getAmount();
		return 0.0;
	}

	private String rcpAmountFormatter(double amount) {
		if (amount % 1 == 0)
			return String.valueOf((int) amount);
		else
			return String.format("%.2f", amount);
	}

	private String rcpRatioFormatter(double ratio) {
		return String.format("%.3f", ratio);
	}

	private void setSelectedNutrIngredient(Ingredient i) {
		selectedNutrIngredient = i;
	}

	private void setSelectedNutrSelection(Selection s) {
		selectedNutrSelection = s;
	}

	private void setSelectedRecipeSelection(Recipe rcp) {
		selectedRecipe = rcp;

		// clear fields then repopulate where relevant
		rcpFillSelectionFields(null);
		rcpKeystoneTitleLabel.setText("Keystone ingredient: " + selectedRecipe.getKeystone().getIngredient().getName()
				+ " (" + selectedRecipe.getKeystone().getAmount() + "g)");

		// update expiry date spinner for chosen recipe
		int expiryDays = selectedRecipe.getLabelSet() != null ? db.getLabelSet(selectedRecipe.getLabelSet()).expiry : 1;
		rcpExpiryDateSpinner.getValueFactory().setValue(expiryDays);
		rcpSetExpiryLabel();

		rcpSetLabelSetComboSelection();

		updateRecipeColumns();

	}

	private void rcpSetLabelSetComboSelection() {
		if (selectedRecipe.getLabelSet() != null) {
			rcpLabelSetComboBox.getSelectionModel().select(db.getLabelSet(selectedRecipe.getLabelSet()));
		} else {
			rcpLabelSetComboBox.getSelectionModel().clearSelection();
			rcpLabelSetComboBox.setPromptText("no label set assigned");
		}
	}

	private void updateNutritionColumns() {
		Collections.sort(nutrIngrdList);
		nutrIngredientsTable.getItems().setAll(nutrIngrdList);

		Collections.sort(nutrSelections);
		nutrSelectionTable.getItems().setAll(nutrSelections);

	}

	private void updateDatabaseColumn() {
		dbIngredientsTable.getItems().setAll(dbIngrdList);
	}

	private void updateRecipeColumns() {
		rcpSelectionsTable.getItems().setAll(selectedRecipe.getSelections());
	}

	private void updateRecipesComboBox() {
		rcpSelectionCombo.getItems().setAll(db.getRecipes());
	}

	/**
	 * gets strings for nutritional facts table text area, and unit cost text field,
	 * where relevant. will also clear fields when there is no data so can be used
	 * in any situation where data and lists may have changed.
	 */
	@FXML
	private void nutrUpdateNutritionalFactsText() {
		// test whether string currently in the unit count field will be useful
		boolean validUnitCountEntered = false;
		int units = 0;
		try {
			units = Integer.parseInt(nutrUnitCount.getText());
			if (units > 0)
				validUnitCountEntered = true;
		} catch (Exception e) {
		}

		// set the text for both strings
		if (validUnitCountEntered && !nutrSelections.isEmpty()) {
			nutrNutritionalFactsTextArea.setText(
					removeCustomTags(UnitCalc.getUnitContents(nutrSelections, settingGrouping1DisplayText.getText())
							+ "\n" + new NutritionalFactsBuilder().buildWith(nutrSelections, units)));

			// sum all available unit costs for display
			Double totalCost = UnitCalc.getUnitCost(nutrSelections, units) + getCombinedConsumablesUnitCost();
			nutrUnitCost.setText(String.format("%.1f", totalCost));

			// assume unit count is not valid, so just get the nutritional facts string
		} else if (nutrSelections != null && !nutrSelections.isEmpty()) {
			nutrNutritionalFactsTextArea
					.setText(removeCustomTags(new NutritionalFactsBuilder().buildWith(nutrSelections)));
			nutrUnitCost.clear();

			// assume there is no data to utilise, clear both fields
		} else {
			nutrNutritionalFactsTextArea.clear();
			nutrUnitCost.clear();
		}
	}

	@FXML
	private void nutrAddIngredientToSelections() {
		int previous = nutrIngredientsTable.getSelectionModel().getSelectedIndex();

		if (selectedNutrIngredient != null && !nutrGramsTextField.getText().isEmpty()) {
			try {
				Selection sel = new Selection(selectedNutrIngredient, parseDouble(nutrGramsTextField.getText()),
						nutrIncludePercentage.isSelected());

				nutrSelections.add(sel);
				nutrIngrdList.remove(selectedNutrIngredient);

				selectedNutrIngredient = null;
				nutrGramsTextField.clear();
				nutrIncludePercentage.setSelected(false);

				updateNutritionColumns();
				nutrUpdateNutritionalFactsText();

			} catch (Exception e) {
				System.out.println(
						"something went wrong while adding a new selection. check that it hasn't impaired functionality");
			}

			nutrIngredientsTable.getSelectionModel().clearAndSelect(previous);
			nutrIngredientsTable.requestFocus();
		}
	}

	@FXML
	private void nutrRemoveIngredientFromSelections() {
		try {
			nutrIngrdList.add(selectedNutrSelection.getIngredient());
			nutrSelections.remove(selectedNutrSelection);
			updateNutritionColumns();
			nutrUpdateNutritionalFactsText();

		} catch (Exception e) {
			System.out.println(
					"did not fully complete operation to remove an ingredient from the selections table - functionality might be impaired");
		}
	}

	@FXML
	private void nutrRemoveAllIngredientsFromSelections() {
		if (nutrSelections.isEmpty())
			return;

		if (AlertFactory.blank(null, "Clear all the selections?", ButtonType.YES, ButtonType.CANCEL).showAndWait()
				.get() == ButtonType.YES)
			nutrClearSelections();
//			nutrRemoveButton.setDisable(true);
//			nutrRemoveAllButton.setDisable(true);
	}

	// clears selections without question or testing remove button states, as above
	private void nutrClearSelections() {

		for (Selection sel : nutrSelections) {
			nutrIngrdList.add(sel.getIngredient());
		}
		nutrSelections.clear();

		// update content
		updateNutritionColumns();
		nutrUpdateNutritionalFactsText();
	}

	/**
	 * removes custom formatting tags (i.e, double underscores and double asterisks)
	 * which are relevant only when exporting ICML file for indesign use
	 */
	private String removeCustomTags(String formatted) {
		StringBuilder clean = new StringBuilder();
		for (int i = 0; i < formatted.length(); i++) {
			if (i < formatted.length() - 2
					&& (formatted.substring(i, i + 2).equals("**") || formatted.substring(i, i + 2).equals("__"))) {
				i++;
			} else {
				clean.append(formatted.charAt(i));
			}
		}
		return clean.toString();
	}

	@FXML
	private void exportICML() {
		if (!isNutrSelectionsValid())
			return;

		LabelSet currentSet = db.getLabelSet(rcpLabelSetComboBox.getSelectionModel().getSelectedItem().setName);

		StringBuilder contentsText = new StringBuilder(currentSet.contentsPrefix);
		contentsText.append(UnitCalc.getUnitContents(nutrSelections, settingGrouping1DisplayText.getText()));

		StringBuilder nutritionText = new StringBuilder(currentSet.nutritionPrefix);
		nutritionText.append(new NutritionalFactsBuilder().buildWith(nutrSelections));

		// test for keyword %WEIGHT% while constructing footer text
		StringBuilder footerText = new StringBuilder();
		char[] chars = currentSet.additionalInfo.toCharArray();
		for (int i = 0; i < chars.length;) {
			if (chars[i] == '%' && currentSet.additionalInfo.substring(i + 1, i + 8).equals("WEIGHT%")) {
				if (currentSet.clientSuppliedWeight)
					footerText.append(currentSet.unitWeight);
				else {
					int units;
					try {
						units = Integer.parseInt(nutrUnitCount.getText());
					} catch (Exception e) {
						units = 1;
					}
					footerText.append(UnitCalc.getUnitWeight(nutrSelections, units));
				}
				i += 8;

			} else {
				footerText.append(chars[i++]);
			}
		}

		String headingBlock, subheadingBlock, contentsBlock, nutritionBlock, footerBlock, expiryBlock;
		ICMLBuilder builder = new ICMLBuilder();

		headingBlock = builder.makeHeadingBlock(currentSet.mainTitle);
		subheadingBlock = builder.makeSubheadingBlock(currentSet.subTitle);
		contentsBlock = builder.makeContentsBlock(contentsText.toString());
		nutritionBlock = builder.makeNutritionBlock(nutritionText.toString());
		footerBlock = builder.makeFooterBlock(footerText.toString());
		expiryBlock = builder.makeExpiryDate(getFormattedDate(rcpExpiryDateSpinner.getValue()));

		String[] storyBlockContents = { headingBlock, subheadingBlock, contentsBlock, nutritionBlock, footerBlock,
				expiryBlock };

		try {
			if (!ExportToICML.makeFile(rcpLabelSetComboBox.getSelectionModel().getSelectedItem().setName + ".icml",
					storyBlockContents)) {
				AlertFactory.error("Uh oh...",
						"Something went wrong making the ICML file. Check the console output for some cunfusing clues.")
						.showAndWait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void nutrCopyToClipboard() {
		if (!isNutrSelectionsValid())
			return;

		Clipboard board = Clipboard.getSystemClipboard();
		ClipboardContent boardContent = new ClipboardContent();

		boardContent.putString(nutrNutritionalFactsTextArea.getText());
		board.setContent(boardContent);
	}

	private boolean isNutrSelectionsValid() {
		for (Selection sel : nutrSelections) {
			if (sel.getIngredient().getLabel().isEmpty() && !isMemberOfAnyGrouping(sel.getIngredient())) {

				ButtonType goAhead = new ButtonType("Go Ahead Anyway", ButtonData.YES);
				ButtonType fixit = new ButtonType("Fix It Now", ButtonData.NO);

				if (AlertFactory.blank("Found a problem:", "The ingredient \"" + sel.getIngredient().getName()
						+ "\" has no label text entered but is currently configured to appear as a listed content. The label's contents will not be exported as expected.",
						goAhead, fixit).showAndWait().get().getButtonData() == ButtonData.NO) {
					tabPane.getSelectionModel().select(tabDB);
					dbIngredientsTable.getSelectionModel().select(sel.getIngredient());
					dbLabelTextText.requestFocus();
					return false;
				}
			}
		}
		return true;
	}

	private boolean isMemberOfAnyGrouping(Ingredient in) {
		for (int i = 1; i < in.getCustomFlagProperties().size(); i++) {
			if (in.getCustomFlag(i))
				return true;
		}
		return false;
	}

	// gets relevant data from given ingredient for text fields in DB tab
	private void dbBindIngredientTextFields(Ingredient current, Ingredient previous) {

		unbindDBTextFields(previous);

		if (current == null)
			return;
		dbNameText.textProperty().bindBidirectional(current.getNameProperty());
		setDBIDLabelText(current.getId());
		dbLabelTextText.textProperty().bindBidirectional(current.getLabelProperty());
		dbDescText.textProperty().bindBidirectional(current.getInfoProperty());
		dbCostPerKiloText.textProperty().bindBidirectional(current.getCostProperty(), doubleConv);

		dbGrouping0CheckBox.selectedProperty().bindBidirectional(current.getCustomFlagProperty(0));
		dbGrouping1CheckBox.selectedProperty().bindBidirectional(current.getCustomFlagProperty(1));

		dbBindNutrientTextFields(current.getNutrientSet());
	}

	private void dbBindNutrientTextFields(NutrientSet nset) {
		dbEnergyKJText.textProperty().bindBidirectional(nset.getEnergyKJProperty(), doubleConv);
		dbEnergyKCalText.textProperty().bindBidirectional(nset.getEnergyKcalProperty(), doubleConv);
		dbFatsText.textProperty().bindBidirectional(nset.getFatProperty(), doubleConv);
		dbSaturatesText.textProperty().bindBidirectional(nset.getSaturatesProperty(), doubleConv);
		dbCarbohydratesText.textProperty().bindBidirectional(nset.getCarbohydratesProperty(), doubleConv);
		dbSugarsText.textProperty().bindBidirectional(nset.getSugarsProperty(), doubleConv);
		dbProteinText.textProperty().bindBidirectional(nset.getProteinProperty(), doubleConv);
		dbSaltText.textProperty().bindBidirectional(nset.getSaltProperty(), doubleConv);
	}

	private void unbindDBTextFields(Ingredient bound) {
		if (bound != null) {
			dbNameText.textProperty().unbindBidirectional(bound.getNameProperty());
			dbLabelTextText.textProperty().unbindBidirectional(bound.getLabelProperty());
			dbDescText.textProperty().unbindBidirectional(bound.getInfoProperty());
			dbCostPerKiloText.textProperty().unbindBidirectional(bound.getCostProperty());
			dbGrouping0CheckBox.selectedProperty().unbindBidirectional(bound.getCustomFlagProperty(0));
			dbGrouping1CheckBox.selectedProperty().unbindBidirectional(bound.getCustomFlagProperty(1));

			NutrientSet nset = bound.getNutrientSet();
			dbEnergyKJText.textProperty().unbindBidirectional(nset.getEnergyKJProperty());
			dbEnergyKCalText.textProperty().unbindBidirectional(nset.getEnergyKcalProperty());
			dbFatsText.textProperty().unbindBidirectional(nset.getFatProperty());
			dbSaturatesText.textProperty().unbindBidirectional(nset.getSaturatesProperty());
			dbCarbohydratesText.textProperty().unbindBidirectional(nset.getCarbohydratesProperty());
			dbSugarsText.textProperty().unbindBidirectional(nset.getSugarsProperty());
			dbProteinText.textProperty().unbindBidirectional(nset.getProteinProperty());
			dbSaltText.textProperty().unbindBidirectional(nset.getSaltProperty());
		}
	}

	/**
	 * launch the results search from here so the results can be passed back to
	 * method for enterIngredientDetails() if OK button is pressed there
	 * 
	 */
	@FXML
	private void dbLaunchSearchWindow() throws Exception {

		results = new Stage();
		results.initModality(Modality.APPLICATION_MODAL);

		FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Results.fxml"));
		GridPane searchPane = (GridPane) loader.load();

		ResultsController controller = loader.getController();
		controller.setParentController(this);
		controller.setSelfReference(results);

		Scene scene = new Scene(searchPane);

		results.setTitle("FDC Database Search");
		results.setScene(scene);
		results.setResizable(false);
		results.setX(parentStage.getX() + 100);
		results.setY(parentStage.getY() + 20);

		controller.preDisplayTasks();

		results.show();

		controller.postDisplayTasks();
	}

	// receives data passed from search window, populates db tab text fields
	public void enterIngredientDetails(String desc, String id, NutrientSet nutr) {
		if (db.hasIngredient(id)) {
			AlertFactory
					.inform("Oh snap!", "That ingredient is already present in the database, with the title "
							+ db.getIngredient(id).getName() + ". (Delete it first if you're trying to replace it)")
					.showAndWait();
			dbIngredientsTable.getSelectionModel().select(db.getIngredient(id));
			dbIngredientsTable.requestFocus();
		} else {
			clearDBTextFields();
			dbDescText.setText(desc);
			setDBIDLabelText(id);
			dbBindNutrientTextFields(nutr);
			dbNameText.requestFocus();
		}
		results.close();
	}

	/**
	 * Saves the details currently entered in the relevant text fields from the DB
	 * tab, as an Ingredient in the Database. Creates a new Ingredient to pass to
	 * database; rather than update an old entry.
	 * <p>
	 * If the new ingredient's FDC ID already exists for an ingredient in the
	 * database, the old ingredient will in effect be 'updated'.
	 * <p>
	 * All lists which serve the tables in the application will then also be
	 * updated; and when the application exits the user will be prompted to save the
	 * database.
	 * 
	 */
	@FXML
	private void dbSaveIngredient() {

		NutrientSet newNutr = null;
		Ingredient newIng = null;

		// get the nutrient set before creating the ingredient
		try {
			newNutr = new NutrientSet(parseDouble(dbEnergyKJText.getText()), parseDouble(dbEnergyKCalText.getText()),
					parseDouble(dbFatsText.getText()), parseDouble(dbSaturatesText.getText()),
					parseDouble(dbCarbohydratesText.getText()), parseDouble(dbSugarsText.getText()),
					parseDouble(dbProteinText.getText()), parseDouble(dbSaltText.getText()));
		} catch (Exception e) {
			System.out.println("something is wrong with the nutritional values");
			return;
		}

		// make the ingredient and put to database
		try {
			Double cost;
			if (dbCostPerKiloText.getText().equals(""))
				cost = 0.0;
			else
				cost = parseDouble(dbCostPerKiloText.getText());

			// construct the new ingredient
			newIng = new Ingredient(dbNameText.getText(), dbLabelTextText.getText(), dbIDnumberLabel.getText(),
					dbDescText.getText(), cost, newNutr, dbGrouping0CheckBox.isSelected(),
					dbGrouping1CheckBox.isSelected());

			db.addIngredient(newIng);

		} catch (Exception e) {
			System.out.println("could not create an ingredient from the given information");
			return;
		}

		// ***if method gets here, assume ingredient was added, so continue***

		// update db list & column
		dbIngrdList = db.getIngredientList();
		updateDatabaseColumn();

		// in the possibility that the new ingredient was saved based upon an existing
		// ingredient, test for the old entry in the nutrition column lists and update
		// it, otherwise add the new ingredient to the available list

		while (true) {
			// test for then update selections column
			Selection testSel = nutrSelectionsContains(newIng);
			if (testSel != null) {
				Selection temp = new Selection(newIng, testSel.getAmount());
				temp.setAppendPercent(testSel.getAppendPercent());
				nutrSelections.add(temp);
				nutrSelections.remove(testSel);
				break;
			}

			// test for then update available ingredients column
			Ingredient testIng = nutrIngrdListContains(newIng);
			if (testIng != null) {
				nutrIngrdList.remove(testIng);
				nutrIngrdList.add(newIng);
				break;
			}

			// assume ingredient was not already present in either list
			nutrIngrdList.add(newIng);
			break;
		}

		updateNutritionColumns();
		nutrUpdateNutritionalFactsText();
	}

	@FXML
	private void dbDeleteIngredient() throws Exception {

		Ingredient selected = dbIngredientsTable.getSelectionModel().getSelectedItem();

		if (AlertFactory.blank("Delete Ingredient", "Really delete " + selected.getName() + "?", ButtonType.CANCEL,
				ButtonType.OK).showAndWait().get() == ButtonType.OK) {

			clearDBTextFields();

			// check current selections list for this entry and remove it
			Selection sel = nutrSelectionsContains(selected);
			if (sel != null) {
				nutrSelections.remove(sel);
				nutrUpdateNutritionalFactsText();

			} else
				nutrIngrdList.remove(selected);

			updateNutritionColumns();

			// finally properly remove the ingredient from the Database
			db.deleteIngredient(selected.getId());

			// update displayed lists
			dbIngrdList = db.getIngredientList();
			updateDatabaseColumn();
		}
	}

	private void setDBIDLabelText(String id) {
		dbIDnumberLabel.setText(id);
		if (id.matches("[0-9]{6}"))
			dbIDtextLabel.setText("FDC ID: ");
		else
			dbIDtextLabel.setText("Internal ID: ");
	}

	@FXML
	private void clearDBTextFields() {
		dbIngredientsTable.getSelectionModel().clearSelection();

		TextField[] fields = { dbNameText, dbDescText, dbEnergyKJText, dbEnergyKCalText, dbFatsText, dbSaturatesText,
				dbCarbohydratesText, dbSugarsText, dbProteinText, dbSaltText, dbCostPerKiloText, dbLabelTextText };

		for (TextField tf : fields)
			tf.setText("");

		dbIDtextLabel.setText("New Internal ID: ");
		dbIDnumberLabel.setText(FDCGenerator.getNextAvailableFDC(dbIngrdList));

		dbGrouping0CheckBox.setSelected(false);
		dbGrouping1CheckBox.setSelected(false);
	}

	@FXML
	private void rcpGetRecipeFromSelections() {

		// get a name for the recipe - check user's intentions if it already exists
		String title = null;
		boolean titleIsUnique = true;
		do {
			TextInputDialog getTitle = new TextInputDialog(title);
			getTitle.setTitle("New Recipe");
			getTitle.setHeaderText("Give the new recipe a good name");
			getTitle.setGraphic(null);

			// receive the user input
			Optional<String> result = getTitle.showAndWait();

			title = result.get().trim();

			if (title.length() == 0)
				return;

			if (db.hasRecipe(title)) {
				titleIsUnique = false;

				if (AlertFactory.blank("Title already in use",
						"Do you want to replace the existing recipe with the new selections?", ButtonType.YES,
						ButtonType.NO).showAndWait().get() == ButtonType.YES)
					titleIsUnique = true;
			}
		} while (!titleIsUnique);

		// check whether there is a batch number set
		Integer divisor = null;
		try {
			divisor = Integer.parseInt(nutrUnitCount.getText());
		} catch (Exception e) {
			// necessary to ask for a batch number for the selections?
		}

		// preselect a keystone ingredient if any have been marked for percentage
		Selection priority = null;
		for (Selection sel : nutrSelections) {
			if (sel.getAppendPercent()) {
				priority = sel;
				break;
			}
		}

		// prepare a choice dialog for the keystone selection
		ChoiceDialog<Selection> getKeystone = new ChoiceDialog<Selection>(priority, nutrSelections);
		getKeystone.setContentText("choose the keystone ingredient");
		Optional<Selection> key = getKeystone.showAndWait();

		if (!key.isPresent())
			return;

		// get a copy of the nutritional tab selections to avoid disturbing that
		// collection
		List<Selection> temp = new ArrayList<Selection>(nutrSelections);

		// prepare temporary collection for recipe creation, excluding keystone
		Selection[] sels = new Selection[nutrSelections.size() - 1];
		int i = 0;

		// if there is a batch count specified, use it as the divisor
		if (divisor != null) {
			// divide selection amounts if a batch count was specified
			key.get().setAmount(key.get().getAmount() / divisor);
//			key.get().amount = key.get().amount / divisor;

			// for every other selection change the amount values
			for (Selection sel : temp) {
				if (!sel.getIngredient().getId().equals(key.get().getIngredient().getId()))
					sels[i++] = new Selection(sel.getIngredient(), (sel.getAmount() / divisor) / key.get().getAmount());
			}

			// when no divisor / batch count was set, transfer every other selection with an
			// amount field calculated as the ratio of keystone amount to selection's amount
		} else {
			for (Selection sel : temp) {
				if (!sel.getIngredient().getId().equals(key.get().getIngredient().getId()))
					sels[i++] = new Selection(sel.getIngredient(), sel.getAmount() / key.get().getAmount());
			}

		}

		// add the new recipe and update views
		db.addRecipe(new Recipe(title, key.get(), sels));
		updateRecipesComboBox();
		rcpSelectionCombo.getSelectionModel().select(db.getRecipe(title));

	}

	@FXML
	private void rcpSetNutrSelectionsFromSelectedRecipe() {

		int batch;

		try {
			batch = Integer.parseInt(rcpBatchCountField.getText());
		} catch (Exception e) {
			AlertFactory.error("Enter a valid batch count", null).showAndWait();
			return;
		}

		if (selectedRecipe == null) {
			AlertFactory.error("No recipe selected", null).showAndWait();
			return;

		} else if (!nutrSelections.isEmpty()) {
			if (AlertFactory.blank("The generator selection column is not empty",
					"Do you want to replace the selections in the generator's "
							+ "selections column with the ingredients from this recipe?",
					ButtonType.OK, ButtonType.CANCEL).showAndWait().get() != ButtonType.OK)
				return;
		}

		nutrClearSelections();

		// make a copy of the keystone
		Selection key = new Selection(selectedRecipe.getKeystone().getIngredient(),
				selectedRecipe.getKeystone().getAmount(), selectedRecipe.getKeystone().getAppendPercent());

		// first put the other ingredients as new selections
		for (Selection sel : selectedRecipe.getSelections()) {
			// these selections hold a ratio which needs to be converted back to an amount
			Selection temp = new Selection(sel.getIngredient(), (key.getAmount() * sel.getAmount()) * batch,
					sel.getAppendPercent());
			nutrSelections.add(temp);
			nutrIngrdList.remove(temp.getIngredient());
		}

		// put the keystone
		key.setAmount(key.getAmount() * batch);
		nutrSelections.add(key);
		nutrIngrdList.remove(key.getIngredient());

		// update views
		nutrUnitCount.setText(String.valueOf(batch));
		updateNutritionColumns();
		nutrUpdateNutritionalFactsText();
		tabPane.getSelectionModel().select(tabNutr);
	}

	@FXML
	private void rcpUpdateSelection() {
		selectedRecipe.getSelection(selectedRcpSelection).setAmount(rcpSelectedIngRatioSpinner.getValue());
		updateRecipeColumns();
	}

	@FXML
	private void rcpDeleteRecipe() {
		// do nothing if there is no recipe selected
		if (selectedRecipe == null)
			return;

		if (AlertFactory.blank("Delete Recipe", "Delete the recipe: " + selectedRecipe.getTitle() + "?",
				ButtonType.CANCEL, ButtonType.OK).showAndWait().get() == ButtonType.OK) {

			// clear the fields so the details do not linger
			rcpKeystoneTitleLabel.setText(null);
			rcpSelectedIngNameField.clear();

			// remove the recipe from the database
			db.deleteRecipe(selectedRecipe.getTitle());
			selectedRecipe = null;

			// update combo box and clear the columns
			updateRecipesComboBox();
			rcpSelectionsTable.getItems().clear();
		}
	}

	@FXML
	private void rcpSaveRecipeChanges() {
		// update existing recipe
		if (selectedRecipe != null) {
			// construct an array of updated ingredient selections
			Selection[] sels = new Selection[rcpSelectionsTable.getItems().size()];
			int i = 0;
			for (Selection cell : rcpSelectionsTable.getItems()) {
				sels[i++] = new Selection(cell.getIngredient(), cell.getAmount());
			}

			// construct new recipe from fields/columns
			Recipe updated = new Recipe(selectedRecipe.getTitle(), selectedRecipe.getKeystone(), sels);

			updated.setLabelSet(rcpLabelSetComboBox.getSelectionModel().getSelectedItem().setName);

			// replace the recipe - add() method will replace when title is same
			db.addRecipe(updated);
			int current = rcpSelectionCombo.getSelectionModel().getSelectedIndex();
			updateRecipesComboBox();
			rcpSelectionCombo.getSelectionModel().select(current);
		}
	}

	@FXML
	private void rcpDeleteSelectedIngredient() {

		if (AlertFactory
				.blank("Remove ingredient...",
						"Do you want to remove the ingredient " + selectedRcpSelection.getIngredient().getName()
								+ " from the recipe?",
						ButtonType.OK, ButtonType.CANCEL)
				.showAndWait().get() == ButtonType.OK) {
			selectedRecipe.removeSelection(selectedRcpSelection.getIngredient().getId());
			updateRecipeColumns();
		}
	}

	private void rcpFillSelectionFields(Selection sel) {
		selectedRcpSelection = sel;
		rcpSetSelectionFields(rcpGetSelectionRatio());

		if (sel != null)
			rcpSelectedIngNameField.setText(selectedRcpSelection.getIngredient().getName());
		else
			rcpSelectedIngNameField.clear();
	}

	private void rcpSetExpiryLabel() {
		int daysFromNow;
		try {
			daysFromNow = rcpExpiryDateSpinner.getValue();
		} catch (Exception e) {
			daysFromNow = 0;
		}

		rcpExpiryDateLabel.setText("label expiry: " + getFormattedDate(daysFromNow));
	}

	@FXML
	private void rcpLaunchLabelSetEditor() throws Exception {

		LabelSet previous = rcpLabelSetComboBox.getSelectionModel().getSelectedItem();

		labelSetWindow = new Stage();
		labelSetWindow.initModality(Modality.APPLICATION_MODAL);

		FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/LabelSet.fxml"));
		AnchorPane labelSetPane = (AnchorPane) loader.load();
		Scene scene = new Scene(labelSetPane);
		LabelSetController controller = loader.getController();

		controller.setParentController(this);
		controller.setSelfReference(labelSetWindow);
		controller.setDatabase(db);
		controller.initialise();

		labelSetWindow.setScene(scene);
		labelSetWindow.setTitle("Label Set Editor");
		labelSetWindow.setResizable(false);
		labelSetWindow.setX(parentStage.getX() + 100);
		labelSetWindow.setY(parentStage.getY() + 50);
		labelSetWindow.showAndWait();

		updateLabelSetComboBox();
		rcpLabelSetComboBox.getSelectionModel().select(previous);
	}

	public void updateLabelSetComboBox() {
		rcpLabelSetComboBox.getItems().setAll(db.getLabelSets());
	}

	private String getFormattedDate(int daysFromNow) {
		return LocalDate.parse(LocalDate.now().toString()).plusDays(daysFromNow)
				.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
	}

	private void costBindAllFieldsToSelection(Consumable current, Consumable previous) {
		costUnbindFields(previous);
		costBindFields(current);
	}

	private void costBindFields(Consumable cons) {
		if (cons == null)
			return;
		costItemNameTextField.textProperty().bindBidirectional(cons.getItemNameProperty());
		costItemDescriptionTextField.textProperty().bindBidirectional(cons.getDescriptionProperty());
		costSampleCostTextField.textProperty().bindBidirectional(cons.getCostPerSampleProperty(), doubleConv);
		costSampleSizeTextField.textProperty().bindBidirectional(cons.getSampleSizeProperty(), integerConv);

		// weak listeners can be garbage collected
		costSampleCostTextField.textProperty().addListener(new WeakChangeListener<String>(
				(obs, old, current) -> costTotalUnitCost.setText(getCombinedConsumablesUnitCostAsString())));
		costSampleSizeTextField.textProperty().addListener(new WeakChangeListener<String>(
				(obs, old, current) -> costTotalUnitCost.setText(getCombinedConsumablesUnitCostAsString())));
	}

	private void costUnbindFields(Consumable cons) {
		if (cons == null)
			return;
		costItemNameTextField.textProperty().unbindBidirectional(cons.getItemNameProperty());
		costSampleCostTextField.textProperty().unbindBidirectional(cons.getCostPerSampleProperty());
		costSampleSizeTextField.textProperty().unbindBidirectional(cons.getSampleSizeProperty());
		costItemDescriptionTextField.textProperty().unbindBidirectional(cons.getDescriptionProperty());
	}

	@FXML
	private void costAddConsumable() {
		try {
			db.addConsumable(new Consumable(costItemNameTextField.getText(), costItemDescriptionTextField.getText(),
					parseDouble(costSampleCostTextField.getText()), parseInteger(costSampleSizeTextField.getText())));
		} catch (Exception e) {
		}
	}

	@FXML
	private void costDeleteConsumable() {
		Consumable selected = costConsumableTableView.getSelectionModel().getSelectedItem();
		costUnbindFields(selected);
		db.deleteConsumable(selected);
		costClearTextFields();
	}

	@FXML
	private void costClearTextFields() {
		costConsumableTableView.getSelectionModel().clearSelection();
		for (TextField tf : getCostTextFields())
			tf.clear();
	}

	private TextField[] getCostTextFields() {
		return new TextField[] { costItemNameTextField, costSampleCostTextField, costSampleSizeTextField,
				costItemDescriptionTextField };
	}

	@FXML
	private void settingUpdateGroupings() {
		// there is display text entered without a description - will not be allowed
		if (settingGrouping1Description.getLength() == 0 && settingGrouping1DisplayText.getLength() > 0) {
			settingGrouping1Description.requestFocus();
		}

		// success case: description has an entry (display text is not compulsory)
		else if (settingGrouping1Description.getLength() > 0) {
			dbGrouping1Description.setText(settingGrouping1Description.getText());
			db.addGrouping(
					new Grouping(1, settingGrouping1Description.getText(), settingGrouping1DisplayText.getText()));

		} else {
			dbGrouping1Description.setText("(grouping not assigned, check settings)");
		}
	}

	// called during initialization
	private void settingLoadGroupings() {
		try {
			settingGrouping1Description.setText(db.getGrouping(1).description);
			settingGrouping1DisplayText.setText(db.getGrouping(1).displayText);
			dbGrouping1Description.setText(settingGrouping1Description.getText());
		} catch (Exception e) {
		}
	}

	@FXML
	private void settingSetDatabaseFilename() {
		String proposed = settingFilename.getText();
		main.getUserPrefs().setDatabaseFilename(proposed);

		if (DatabaseUtils.isDatabase(proposed)) {
			main.loadDatabase(proposed);
			setDatabase();
			setAllDatabaseItems();
		}
	}

	private void settingChangeDecimalSeparator(int current, int previous) {
		main.getUserPrefs().setDecimalFormat(current);

		if (current != previous) {
			main.setInternalLocaleFormat(current);
			doubleConv = TextFieldUtils.doubleConverter(); // replaces previous converter

			updateNutritionColumns();

			int dbSelection = dbIngredientsTable.getSelectionModel().getSelectedIndex();
			dbIngredientsTable.getSelectionModel().clearSelection();
			dbIngredientsTable.getSelectionModel().select(dbSelection);

			if (!rcpLabelSetComboBox.getSelectionModel().isEmpty())
				updateRecipeColumns();

			int consumableSelection = costConsumableTableView.getSelectionModel().getSelectedIndex();
			costConsumableTableView.getSelectionModel().clearSelection();
			costConsumableTableView.getSelectionModel().select(consumableSelection);

			updateTotalUnitCost();
		}
	}

	// helper method to check if nutrSelections list holds a particular ingredient,
	// returns the appropriate Selection if present
	private Selection nutrSelectionsContains(Ingredient ing) {
		// iterate through list and test for an ingredient match by FDC ID
		for (Selection selected : nutrSelections) {
			if (selected.getIngredient().getId().equals(ing.getId()))
				return selected;
		}
		return null;
	}

	// similar to nutrSelectionsContains()
	private Ingredient nutrIngrdListContains(Ingredient ing) {
		for (Ingredient entry : nutrIngrdList) {
			if (entry.getId().equals(ing.getId())) {
				return entry;
			}
		}
		return null;
	}

	public void closeThisWindow(Stage windowToClose) {
		windowToClose.close();
	}

	private double parseDouble(String in) {
		return Double.parseDouble(in.replace(',', '.'));
	}

	private int parseInteger(String input) {
		return Integer.parseInt(input);
	}

// junk button for testing novel ideas
	@FXML
	private void settingsRandomTestingButton() {

		try {
			// code here. use test button in settings tab

//			ChoiceDialog<String> font = new ChoiceDialog<>("select a font...", Font.getFontNames());
//			font.showAndWait();
//			FileChooser save = new FileChooser();
//			save.showSaveDialog(parentStage);

		} catch (Exception e) {
			AlertFactory.error("That's not behaving as expected", "check the console output for a detailed stack trace")
					.showAndWait();
			e.printStackTrace();
		}
	}

	// basic proof of concept for font picking
//		List<String> fonts = Font.getFontNames();

}
