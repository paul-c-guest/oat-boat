package oatboat.model.food;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Object to represent each food ingredient, and store that ingredient's
 * information regarding: FDC ID (the American FDC unique identifier), cost per
 * 100 grams, and a NutrientSet Object which contains the nutritional values
 * required for formulation of a standard nutritional facts table.
 * 
 * @author ballsies
 *
 */
public class Ingredient implements Comparable<Ingredient> {

	private final StringProperty name, id, label, info;
	private final DoubleProperty cost;
	private final ListProperty<BooleanProperty> customFlags;
	private List<BooleanProperty> customFlagsBase;
	private ObservableList<BooleanProperty> customFlagsObservable;

	// unsure whether this should be in an ObjectProperty wrapper
	private NutrientSet nutrients;

	public Ingredient(String ingredientName, String labelText, String ID, String additionalInfo, double costPerKilo,
			NutrientSet nutrientSet, Boolean... checkboxStates) {
		name = new SimpleStringProperty(ingredientName);
		label = new SimpleStringProperty(labelText);
		id = new SimpleStringProperty(ID);
		info = new SimpleStringProperty(additionalInfo);
		cost = new SimpleDoubleProperty(costPerKilo);

		nutrients = nutrientSet;
		
		customFlagsBase = new ArrayList<BooleanProperty>();
		customFlagsObservable = FXCollections.observableList(customFlagsBase);
		customFlags = new SimpleListProperty<BooleanProperty>(customFlagsObservable);
		for (Boolean state : checkboxStates)
			customFlags.add(new SimpleBooleanProperty(state));
	}

	public final String getName() {
		return name.get();
	}

	public final void setName(String name) {
		this.name.set(name);
	}

	public StringProperty getNameProperty() {
		return name;
	}

	/**
	 * The FDC ID is a unique identifier, usually a string of 6 digits. The IDs are
	 * maintained by an American federal department - the FDC
	 * 
	 * @return this food item's FDC ID
	 */
	public String getId() {
		return id.get();
	}

	public StringProperty getIdProperty() {
		return id;
	}

	/**
	 * Defines the text that will be exported to labelling strings when the user
	 * copies the label text to the clipboard, or exports an ICML file for InDesign.
	 * 
	 * @return the labelling text
	 */
	public String getLabel() {
		return label.get();
	}

	/**
	 * Defines the text that will be exported to labelling strings when the user
	 * copies the label text to the clipboard, or exports an ICML file for InDesign.
	 */
	public void setLabel(String labelText) {
		this.label.set(labelText);
	}

	public StringProperty getLabelProperty() {
		return label;
	}

	public String getInfo() {
		return info.get();
	}

	public void setInfo(String description) {
		info.set(description);
	}

	public StringProperty getInfoProperty() {
		return info;
	}

	/**
	 * The cost per kilogram (1000g) for this ingredient
	 * 
	 * @return cost per kg (1000g)
	 */
	public double getCost() {
		return cost.get();
	}

	/**
	 * Set the cost per kilogram (1000g) for this ingredient
	 * 
	 * @param cost per kg (1000g)
	 */
	public void setCost(double cost) {
		this.cost.set(cost);
	}

	public DoubleProperty getCostProperty() {
		return cost;
	}

	public void setCustomFlag(int index, boolean state) {
		customFlags.add(index, new SimpleBooleanProperty(state));
	}

	public boolean getCustomFlag(int index) {
		return customFlags.get(index).get();
	}
	
	public BooleanProperty getCustomFlagProperty(int index) {
		return customFlags.get(index);
	}
	
	public List<BooleanProperty> getCustomFlagProperties() {
		return customFlags;
	}
	
	public NutrientSet getNutrientSet() {
		return nutrients;
	}

	@Override
	public String toString() {
		return name.get() + ", ID: " + id.get();
	}

	@Override
	public int compareTo(Ingredient other) {
		return this.name.get().toUpperCase().compareTo(other.name.get().toUpperCase());
	}

}
