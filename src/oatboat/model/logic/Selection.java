package oatboat.model.logic;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import oatboat.model.food.Ingredient;

/**
 * A Selection is used internally to pass Ingredient objects around wrapped with
 * extra temporary information i.e. specific weights or ratios, and boolean
 * values regarding how to display the name of the ingredient
 * 
 * @author ballsies
 * @see oatboat.model.food.Ingredient
 */
public class Selection implements Comparable<Selection> {

	private Ingredient ingredient;
	private DoubleProperty amount; // the weight in grams / or a multiplier when used in a Recipe.class
	private BooleanProperty appendPercent; // whether to append output with a suffix

	public Selection(Ingredient in, Double amount, boolean appendPercentage) {
		this.ingredient = in;
		this.amount = new SimpleDoubleProperty(amount);
		appendPercent = new SimpleBooleanProperty(appendPercentage);
	}

	public Selection(Ingredient in, Double amount) {
		this(in, amount, false);
	}
	
	public Ingredient getIngredient() {
		return ingredient;
	}
	
	public double getAmount() {
		return amount.get();
	}
	
	public void setAmount(double amount) {
		this.amount.set(amount);
	}
	
	public DoubleProperty getAmountProperty() {
		return amount;
	}

	/**
	 * 
	 * @return the boolean value specifying whether this selection will also be
	 *         suffixed with an ingredient percentage when displayed in a contents
	 *         list.
	 */
	public boolean getAppendPercent() {
		return appendPercent.get();
	}

	/**
	 * the suffix state helps other methods determine whether to also include a
	 * percentage of total ingredient weight as part of the output string for the
	 * ingredient, when constructing contents lists. e.g. When set to true,
	 * <i>Oats</i> would display as <i>Oats 20%</i> (assuming that oats made up 20%
	 * of the weight of the contents).
	 * <p>
	 * the calculation of the percentage is done in methods elsewhere.
	 * 
	 * @param appendPercentage
	 */
	public void setAppendPercent(boolean appendPercentage) {
		appendPercent.set(appendPercentage);
	}

	public BooleanProperty getAppendPercentProperty() {
		return appendPercent;
	}

	/**
	 * Gives the Ingredient name amended with other elements for visual aid: 1.
	 * Whether the Selection is to have a percentage value associated on any output
	 * text for labelling or otherwise; 2. Whether the Ingredient does not have a
	 * cost set (i.e. cost is 0.0)
	 * 
	 * @return the amended Ingredient name
	 */
	public String getAmendedName() {
		StringBuilder str = new StringBuilder(ingredient.getName());
		if (appendPercent.get())
			str.append(" [%]");
		if (ingredient.getCost() == 0.0) {
			str.insert(0, "\u2A3B ");
		}
		return str.toString();
	}

	public String toString() {
		return ingredient.getName();
	}

	/**
	 * selections will first be compared by their amount, otherwise alphabetically
	 */
	public int compareTo(Selection other) {
		if (this.amount.get() > other.amount.get())
			return -1;
		else if (this.amount.get() < other.amount.get())
			return 1;
		else
			return this.ingredient.compareTo(other.ingredient);
	}

}
