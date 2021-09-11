package oatboat.model.logic;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oatboat.model.food.Ingredient;
import oatboat.model.food.NutrientSet;

public class NutritionalFactsBuilder {

	private int nutIndex;

	// all overloaded methods eventually call this method
	/**
	 * Constructs and returns a formatted string of nutritional information, for the
	 * given collection of ingredients & amounts. Amounts should be given in grams
	 * and can have decimal positions; e.g. 2.4004kg must be entered in the
	 * corresponding Map's Value as 2400.4
	 * <p>
	 * If a number of units is also passed to the method, the returned string will
	 * be appended by a supplementary phrase specifying the weight of each unit.
	 * 
	 * @param ingredients A HashMap of Ingredient & Double
	 * @param units       The number of produced units
	 * @return A String of nutritional information
	 */
	public String buildWith(Map<Ingredient, Double> ingredients, int units) {

		// initialise helper variables
		nutIndex = NutrientSet.NUT_INDEX;
		double[] finalValues = new double[nutIndex];
		double totalWeight = 0.0;

		// determine total weight of all provided Ingredients
		for (Double weight : ingredients.values()) {
			totalWeight += weight;
		}

		// for each ingredient, increment proportion of each of the seven nutritional
		// componenents (e.g. salt, protein) to the array 'finalValues'
		for (Ingredient ing : ingredients.keySet()) {

			for (int i = 0; i < nutIndex; i++) {

				double[] ingValues = ing.getNutrientSet().getAllValues();

				// here be magic: calculate each nutrient's value based on their proportion of
				// the combined weight of all ingredients. ignores negative values but posts
				// warnings.
				if (ingValues[i] > 0.0) {
					finalValues[i] += (ingredients.get(ing) / totalWeight) * ingValues[i];
				} else if (ingValues[i] == -1.0) { // was likely set to -1.0 by nutrientset generator
					System.out.println("Caution - there is a nutritional value missing for: " + ing.getName());
				}
			}
		}

		// set the locale to CZ to ensure decimal numbers will be separated by a comma
		// (does not necessarily need to be CZ, many other european locales would output
		// the required decimal format)
//		Locale.setDefault(Locale.forLanguageTag("cs-CZ"));

		// construct and return the string from summed figures held in finalValues
		String table = getStringCzech(finalValues);

		// return the table without unit information
		if (units == 0)
			return table;

		// else return the full string
		return table + "\nUnit weight: " + (int) totalWeight / units + "g";
	}

	// overloaded createWith() methods follow
	public String buildWith(List<Selection> selections) {
		return buildWith(selections, 0);
	}

	public String buildWith(List<Selection> selections, int units) {
		Map<Ingredient, Double> converted = new HashMap<Ingredient, Double>();
		for (Selection sel : selections) {
			converted.put(sel.getIngredient(), sel.getAmount());
		}
		return buildWith(converted, units);
	}

	public String buildWith(Map<Ingredient, Double> ingredients) {
		return buildWith(ingredients, 0);
	}

//		@formatter:off
	
	// usused currently but could become useful again (21.02.2020)
//	private String getStringEnglish(double[] values) {
//		return "energy " + (int) values[0] + "kJ / " + (int) values[1] + "kcal "
//				+ "fat " + roundDouble(values[2]) + "g "
//				+ "saturates " + roundDouble(values[3]) + "g "
//				+ "carbohydrates " + roundDouble(values[4]) + "g "
//				+ "sugars " + roundDouble(values[5]) + "g "
//				+ "protein " + roundDouble(values[6]) + "g "
//				+ "salt " + roundDoubleMilligram(values[7] / 1000) + "mg"; // uhoh -- mg or g ??
//	}
	
	private String getStringCzech(double[] values) {
		return "energie " + (int) values[0] + " kJ / " + (int) values[1] 
				+ " kcal, tuky " + roundDouble(values[2]) + " g (z toho nasyc.mast.kys. " 
				+ roundDouble(values[3]) + " g), sacharidy " + roundDouble(values[4]) 
				+ " g (z toho cukry " + roundDouble(values[5]) + " g), bílkoviny " 
				+ roundDouble(values[6]) + " g, sůl " + roundDoubleMilligram(values[7] / 1000) + " g.";
	}
	
//	@formatter:on 

	// formatting helper
	private String roundDouble(double val) {
		DecimalFormat df = new DecimalFormat("#.#");
		return df.format(val);
	}

	// formatting helper
	private String roundDoubleMilligram(double val) {
		DecimalFormat df = new DecimalFormat("#.###");
		return df.format(val);
	}

}
