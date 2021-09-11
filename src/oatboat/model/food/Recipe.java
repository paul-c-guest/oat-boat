package oatboat.model.food;

import java.util.ArrayList;
import java.util.List;

import oatboat.model.logic.Selection;

/**
 * A Recipe is fundamentally a set of Selections, however with the important
 * addition of one extra Selection which will be be referred to as the
 * <b>keystone</b>.
 * <p>
 * The keystone's <b>Selection.amount</b> field should be supplied as the
 * <u>amount in grams</u> that <u>one</u> unit will contain. For each other
 * Selection in the main list, it is necessary to give the amount as a
 * multiplier -- i.e. a multiple of the keystone amount (calculations for any
 * number of units can thus be scaled without error).
 * <p>
 * Example: The keystone is set with an amount of <b>60</b>. A Selection has the
 * amount given as 0.5, indicating that the amount of this ingredient to add is
 * 0.5 of the keystone (60 * 0.5 = <b>30</b>). Likewise if the multiplier given
 * were 2.3, the amount of that ingredient to add would be <b>138</b>.
 * 
 * @see oatboat.model.logic.Selection
 * @author ballsies
 *
 */
public class Recipe implements Comparable<Recipe> {

	private final String title;
	private String labelSet;
	private Selection keystone;
	private List<Selection> selections = new ArrayList<Selection>();

	/**
	 * Constructor for a new Recipe. Only the title and keystone are compulsory for
	 * successful construction.
	 * 
	 * @param keystone   the unique Selection
	 * @param selections accepts any number of Selections as vararg
	 */
	public Recipe(String title, Selection keystone, Selection... selections) {
		this.title = title;
		this.keystone = keystone;
		for (Selection ing : selections)
			this.selections.add(ing);
	}

	public String toString() {
		return title;
//		return title + "\n[key: " + keystone.ingredient.getName().toLowerCase() + "]";
	}

	/**
	 * used as key for database access, compares etc.
	 * 
	 * @return the recipe title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * the Selection upon which all other Selection/Ingredients in this Recipe will
	 * calculate their amounts.
	 * 
	 * @return the keystone Ingredient wrapped as a <b>Selection</b>
	 */
	public Selection getKeystone() {
		return keystone;
	}

	/**
	 * Be Aware: In a Recipe, each selection's 'amount' field is used as a
	 * multiplier of the keystone's amount -- not as an amount in grams.
	 * 
	 * @return
	 */
	public List<Selection> getSelections() {
		return selections.subList(0, selections.size());
	}

	/**
	 * actually just a complete replacement (removal and addition) - needs to be
	 * passed a Selection which holds an ingredient with a matching ID to an
	 * ingredient already held in the Selections list, or will do nothing.
	 * 
	 * @param sel
	 */
	public boolean editSelection(Selection sel) {
		if (removeSelection(sel.getIngredient().getId())) {
			addSelection(sel);
			return true;
		} else {
			System.out.println("failed editing: " + sel.getIngredient().getName() + " - no matching ID");
			return false;
		}
	}

	public Selection getSelection(Selection sel) {
		for (int i = 0; i < selections.size(); i++) {
			if (selections.get(i).getIngredient() == sel.getIngredient()) {
				return selections.get(i);
			}
		}
		return null;
	}

	public void addSelection(Selection sel) {
		selections.add(sel);
	}

	public boolean removeSelection(String IDToRemove) {
		int surplus = -1;
		for (int i = 0; i < selections.size(); i++) {
			if (selections.get(i).getIngredient().getId().equals(IDToRemove)) {
				surplus = i;
				break;
			}
		}
		if (surplus == -1)
			return false;
		else
			selections.remove(surplus);
		return true;
	}
	
	public void setLabelSet(String set) {
		this.labelSet = set;
	}
	
	public String getLabelSet() {
		return labelSet;
	}

	@Override
	public int compareTo(Recipe other) {
		return this.title.compareTo(other.title);
	}
	
	@Override
	public int hashCode() {
		return 17 * title.hashCode() + keystone.getIngredient().getId().hashCode();
	}
}
