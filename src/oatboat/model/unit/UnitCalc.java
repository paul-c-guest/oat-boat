package oatboat.model.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oatboat.model.logic.Selection;

/**
 * calculator takes a List of Selections (ideally that which the nutritional
 * table creator compiles during creation of nutritional information) and an
 * integer to stand as the number of units, returning relevant unit
 * calculations.
 * 
 * 
 * @author ballsies
 *
 */
public class UnitCalc {

	/**
	 * 
	 * @return the weight of one unit
	 */
	public static int getUnitWeight(List<Selection> sels, int count) {

		double gross = 0;
		for (Selection sel : sels) {
			gross += sel.getAmount();
		}
		return (int) (gross / count);
	}

	/**
	 * static method to calculate the total cost for each unit from the given list
	 * and unit count.
	 * <p>
	 * It is important to make sure each Ingredient in the Selections has a value
	 * set for Cost per Kilo (at least zero, but not null), or this library will not
	 * return usable results.
	 * 
	 * 
	 * @param sels  a java.util.List of Selection objects
	 * @param count the number of units produced from the given list
	 * @return the cost for one unit
	 */
	public static double getUnitCost(List<Selection> sels, int count) {

		double costSum = 0.0;
		boolean success = true;

		for (Selection s : sels) {
			try {
				costSum += (s.getAmount() / 1000) * s.getIngredient().getCost();

			} catch (Exception e) {
				System.out.println("missing a cost value for: " + s.getIngredient().getName());
				success = false;
			}
		}

		if (!success)
			return -1.0;
		else
			return costSum / count;
	}

	/**
	 * Calculator to return the contents list for a List of Selections, ordered from
	 * most to least weight. Uses flags set in individual Ingredients to skip over
	 * unneccessary additions, and to create Common Groupings such as for fruits
	 * 
	 * @param sels
	 * @return a String for use on packaging to display contents
	 */
	public static String getUnitContents(List<Selection> sels, String... groupings) {
		/**
		 * compile a list of Contents so requirements about grouping and visibility will
		 * be taken into account when constructing output string, and contents and
		 * groupings can be correctly ordered by weight
		 */
		List<Content> contents = new ArrayList<Content>();
		double grouping1weight = 0.0, totalWeight = 0.0;

		for (Selection sel : sels) {
			// add the weight to the total
			totalWeight += sel.getAmount();

			// index 0 indicates this ingredient is not to be shown on labelling
			if (sel.getIngredient().getCustomFlag(0))
				continue;

			// currently there is only one grouping flag: index 1
			else if (sel.getIngredient().getCustomFlag(1)) {
				grouping1weight += sel.getAmount();

				// otherwise it is a normal addition to the output contents list
			} else {
				contents.add(new Content(sel.getIngredient().getLabel(), sel.getAmount(), sel.getAppendPercent()));
			}
		}

		// include common grouping 1 as a Content only if present
		if (grouping1weight > 0.0) {
			contents.add(new Content(groupings[0], grouping1weight, false));
		}

		Collections.sort(contents);

		StringBuilder contentsString = new StringBuilder();

		for (int i = 0; i < contents.size(); i++) {
			contentsString.append(contents.get(i).text);

			if (contents.get(i).percent) {
				double percentage = 100 * contents.get(i).amount / totalWeight;
				contentsString.append(" " + (int) percentage + "%");
			}

			if (i < contents.size() - 1)
				contentsString.append(", ");
		}

		contentsString.append(".");
		return contentsString.toString();
	}

	// internal help class
	private static class Content implements Comparable<Content> {

		String text;
		double amount;
		boolean percent;

		public Content(String text, double amount, boolean includePercent) {
			this.text = text;
			this.amount = amount;
			this.percent = includePercent;
		}

		@Override
		public int compareTo(Content other) {
			if (this.amount > other.amount)
				return -1;
			if (this.amount < other.amount)
				return 1;
			return 0;
		}

	}

}
