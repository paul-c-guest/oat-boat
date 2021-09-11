package oatboat.model.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import oatboat.model.food.NutrientSet;

/**
 * reads data from CSV files retrieved from fdc.nal.usda.gov
 * <p>
 * when instantiated, implements Runnable to facilitate background/concurrent
 * construction of internal treemap for fast retrieval of nutrient sets
 * <p>
 * static methods :
 * 
 * @author ballsies
 */
public class FDCDatabase implements Runnable {

	private Map<Integer, NutrientSet> fdcDB;
	private ReadOnlyBooleanWrapper dbReady = new ReadOnlyBooleanWrapper(false);
	final int energy_kJ = 1062;
	final int energy_kcal = 1008;
	final int fat = 1004;
	final int saturates = 1258;
	final int carbohydrates = 1005;
	final int sugars = 2000;
	final int protein = 1003;
	final int salt = 1093;

	/**
	 * creates an internal map of a local FDC nutrients CSV file.
	 * <p>
	 * intended use is to facilitate instant retrieval of a nutrient set upon user
	 * selection of a search result: execution of this method in a concurrent thread
	 * constructs the nutrient sets while the user manually searches for their
	 * desired ingredient.
	 */
	@Override
	public void run() {
		try {
			File fdcNutrientsCSV = new File("fdc/food_nutrient.csv");

			if (fdcNutrientsCSV.isFile()) {
				BufferedReader reader = new BufferedReader(new FileReader(fdcNutrientsCSV));
				NutrientSet nutrients = new NutrientSet();

				fdcDB = new TreeMap<Integer, NutrientSet>();

				int fdcID = 0;
				int nutrientID;
				double nutrientValue;

				String row = reader.readLine();
				do {
					String[] lineData = row.split(",");
					int nextFdcID = parseInt(lineData[1]);
					if (nextFdcID != fdcID) {
						fdcDB.put(fdcID, nutrients);
						nutrients = new NutrientSet();
						fdcID = nextFdcID;
					}
					nutrientID = parseInt(lineData[2]);
					nutrientValue = parseDouble(lineData[3]);

					switch (nutrientID) {
					case energy_kJ:
						nutrients.setEnergyKJ(nutrientValue);
						break;

					case energy_kcal:
						nutrients.setEnergyKcal(nutrientValue);
						break;

					case fat:
						nutrients.setFat(nutrientValue);
						break;

					case saturates:
						nutrients.setSaturates(nutrientValue);
						break;

					case carbohydrates:
						nutrients.setCarbohydrates(nutrientValue);
						break;

					case sugars:
						nutrients.setSugars(nutrientValue);
						break;

					case protein:
						nutrients.setProtein(nutrientValue);
						break;

					case salt:
						nutrients.setSalt(nutrientValue);
						break;
					}

				} while ((row = reader.readLine()) != null);

				reader.close();
				dbReady.setValue(true);
			}

		} catch (Exception e) {
		}

	}

	public ReadOnlyBooleanWrapper isReady() {
		return dbReady;
	}

	public NutrientSet getNutrientSet(int fdcID) {
		return fdcDB.get(fdcID);
	}

	/**
	 * read a local copy of the FDC food items database to produce a hashmap of
	 * fdc_id & food descriptions
	 * 
	 * @return a map of Integers and Strings
	 */
	public static Map<Integer, String> getFoodList() {

		Map<Integer, String> foodsMap = new HashMap<Integer, String>();

		try {
			File csv = new File("fdc/food.csv");

			if (csv.isFile()) {
				BufferedReader csvReader = new BufferedReader(new FileReader(csv));
				String row = "";

				while ((row = csvReader.readLine()) != null) {
					try {
						String[] lineData = row.split("\",\"");

						int fdcID = Integer.parseInt(lineData[0].substring(1));
						String foodDesc = lineData[2];

						// place a food item to the local map with key: fdc_id and value: the food's
						// description
						foodsMap.put(fdcID, foodDesc);
					} catch (Exception e) {
					}
				}

				csvReader.close();
				return foodsMap;
			}

		} catch (Exception e) {
			System.out.println("Is food.csv present in the folder '/fdc'?");
		}
		return null;

	}

	private static int parseInt(String input) {
		try {
			return Integer.parseInt(input.substring(1, input.length() - 1));
		} catch (Exception e) {
			return -1;
		}
	}

	private static double parseDouble(String input) {
		try {
			return Double.parseDouble(input.substring(1, input.length() - 1));
		} catch (Exception e) {
			return -1.0;
		}
	}

}
