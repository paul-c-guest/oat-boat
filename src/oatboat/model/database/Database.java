package oatboat.model.database;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import oatboat.model.database.consumable.Consumable;
import oatboat.model.database.grouping.Grouping;
import oatboat.model.food.Ingredient;
import oatboat.model.food.LabelSet;
import oatboat.model.food.Recipe;

/**
 * The database referenced by the main application, holding all the required
 * values to maintain the collections of ingredients, recipes, consumables,
 * label sets and grouping information.
 * <p>
 * The database object is created and populated fresh on each run of the app;
 * data is parsed from a local file saved in the format provided by
 * com.google.protobuf
 * <p>
 * 
 * @see oatboat.model.database.protobuf
 * @author ballsies
 */
public class Database {

	private ObservableMap<String, Ingredient> ingredients;
	private ObservableMap<String, Recipe> recipes;
	private ObservableMap<String, LabelSet> labelSets;
	private ObservableMap<Integer, Grouping> groups;
	private ObservableList<Consumable> consumables;

	/**
	 * Creates a new empty database.
	 */
	public Database() {
		ingredients = FXCollections.observableHashMap();
		recipes = FXCollections.observableHashMap();
		labelSets = FXCollections.observableHashMap();
		groups = FXCollections.observableHashMap();
		consumables = FXCollections.observableArrayList();
	}

	/**
	 * Simple method to put Ingredient to database.
	 * <p>
	 * <b>Note: </b>This method does not test for a preexisting record, any required
	 * checks should be done prior to using this method.
	 * 
	 * @param in the Ingredient to add
	 */
	public void addIngredient(Ingredient in) {
		ingredients.put(in.getId(), in);
	}

	/**
	 * Immediately remove the specified Ingredient from the database.
	 * 
	 * @param identifier the Ingredient name to delete
	 */
	public void deleteIngredient(String identifier) {
		ingredients.remove(identifier);
	}

	// get specific ingredient by fdc id, required by loader (recipes)
	public Ingredient getIngredient(String identifier) {
		if (hasIngredient(identifier))
			return ingredients.get(identifier);
		return null;
	}
	
	public boolean hasIngredient(String identifier) {
		return ingredients.containsKey(identifier);
	}

	/**
	 * get a fresh list of all currently held Ingredients. limits exposure of the
	 * underlying ingredient map.
	 * 
	 * @return an {@link ArrayList}
	 */
	public List<Ingredient> getIngredientList() {
		List<Ingredient> list = new ArrayList<Ingredient>();

		for (Ingredient in : ingredients.values()) {
			list.add(in);
		}

		Collections.sort(list);
		return list;
	}

	public ObservableMap<String, Ingredient> getIngredients() {
		return ingredients;
	}

	public void addConsumable(Consumable c) {
		try {
			consumables.add(c);
		} catch (Exception e) {
			System.out.println("couldn't add '" + c.getItemName() + "' at DB level");
			e.printStackTrace();
		}
	}

	public void deleteConsumable(Consumable c) {
		consumables.remove(c);
	}

	// TODO change reference to obs!!
	public void deleteConsumable(String s) {
		for (int i = 0; i < consumables.size(); i++) {
			if (consumables.get(i).getItemName().equals(s)) {
				consumables.remove(i);
				break;
			}
		}
	}

	public ObservableList<Consumable> getConsumables() {
		return consumables;
	}

	public void addGrouping(Grouping group) {
		groups.put(group.getIndex(), group);
	}

	public Grouping getGrouping(int index) {
		return groups.get(index);
	}

	/**
	 * adds the recipe or replaces an existing title match without question
	 * 
	 * @param rcp
	 */
	public void addRecipe(Recipe rcp) {
		recipes.put(rcp.getTitle(), rcp);
	}

	/**
	 * returns a recipe if it exists, otherwise returns null
	 * 
	 * @param title
	 * @return
	 */
	public Recipe getRecipe(String title) {
		if (hasRecipe(title))
			return recipes.get(title);
		else
			return null;
	}

	public boolean hasRecipe(String title) {
		return recipes.containsKey(title);
	}

	public List<Recipe> getRecipes() {
		List<Recipe> list = new ArrayList<>();
		for (Recipe rcp : recipes.values())
			list.add(rcp);
		Collections.sort(list);
		return list;
	}

	/*
	 * deletes a matching recipe without question
	 */
	public void deleteRecipe(String title) {
		if (hasRecipe(title))
			recipes.remove(title);
	}

	/**
	 * adds the labelset without question -- wil overwrite any existing set with the
	 * same name.
	 */
	public void addLabelSet(LabelSet set) {
		labelSets.put(set.setName, set);
	}

	public void deleteLabelSet(String name) {
		if (hasLabelSet(name))
			labelSets.remove(name);
	}

	public LabelSet getLabelSet(String name) {
		if (hasLabelSet(name))
			return labelSets.get(name);
		return null;
	}

	public List<LabelSet> getLabelSets() {
		List<LabelSet> sets = new ArrayList<LabelSet>();

		for (LabelSet set : labelSets.values())
			sets.add(set);

		Collections.sort(sets);

		return sets;
	}

	public boolean hasLabelSet(String name) {
		return labelSets.containsKey(name);
	}

	public boolean isEmpty() {
		return ingredients.isEmpty() && consumables.isEmpty() && recipes.isEmpty() && labelSets.isEmpty();
	}

	/**
	 * Compares this database against an existing file. A temporary file
	 * representing this database in its current state is created and 
	 * compared against the given argument file, at the byte level.
	 * 
	 * @return true if both databases are equal when compared as files
	 */
	public boolean equals(String filename) {
		byte[] tempBytes = null, existingBytes = null;

		String tempfile = ".temp";
		DatabaseUtils.save(this, tempfile);

		try {
			tempBytes = Files.readAllBytes(Paths.get(tempfile));
			existingBytes = Files.readAllBytes(Paths.get(filename));
		} catch (Exception e) {
			return false;
		}

		boolean result = Arrays.equals(tempBytes, existingBytes);

		try {
			Files.delete(Paths.get(tempfile));
		} catch (Exception e) {
		}

		return result;
	}

	/**
	 * returns only a list of the Ingredients fdcIDs and names
	 */
	public String toString() {
		String ret = "";

		for (Ingredient item : ingredients.values()) {
			ret += "\n" + item.getId() + ": " + item.getName();
		}

		return ret;
	}

}
