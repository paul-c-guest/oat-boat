package oatboat.model.database.consumable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a single item which each product will require, for example a cup
 * or a label.
 * <p>
 * Consumable is intended to assist with tracking of unit costs. The best
 * practice would be to provide large sample sizes and prices so an individual
 * cost can be more precisely calculated, i.e. be supplying the cost for 1000
 * consumables instead of an estimate for one.
 * <p>
 * The Consumable class is also implemented as an observable javaFX Bean - thus
 * contains the expected getters & setters as well as Property getters for the
 * observation of field changes.
 * 
 * @author ballsies
 *
 */
public class Consumable implements Comparable<Consumable> {

	private StringProperty itemName;
	private StringProperty description;
	private DoubleProperty costPerSample;
	private IntegerProperty sampleSize;

	public Consumable(String item, String description, double costPerSample, int sampleSize) {
		this.itemName = new SimpleStringProperty(item);
		this.description = new SimpleStringProperty(description);
		this.costPerSample = new SimpleDoubleProperty(costPerSample);
		this.sampleSize = new SimpleIntegerProperty(sampleSize);
	}

	public String getItemName() {
		return itemName.get();
	}

	public void setItemName(String name) {
		this.itemName.set(name);
	}

	public StringProperty getItemNameProperty() {
		return itemName;
	}
	
	public String getDescription() {
		return description.get();
	}

	public void setDescription(String description) {
		this.description.set(description);
	}

	public StringProperty getDescriptionProperty() {
		return description;
	}
	
	public double getCostPerSample() {
		return costPerSample.get();
	}

	public void setCostPerSample(double cost) {
		this.costPerSample.set(cost);
	}

	public DoubleProperty getCostPerSampleProperty() {
		return costPerSample;
	}
	
	public int getSampleSize() {
		return sampleSize.get();
	}

	public void setSampleSize(int count) {
		this.sampleSize.set(count);
	}

	public IntegerProperty getSampleSizeProperty() {
		return sampleSize;
	}

	@Override
	public int compareTo(Consumable other) {
		return this.itemName.get().toUpperCase().compareTo(other.itemName.get().toUpperCase());
	}
}
