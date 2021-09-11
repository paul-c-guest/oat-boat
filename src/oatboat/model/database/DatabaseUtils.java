package oatboat.model.database;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import oatboat.model.database.consumable.Consumable;
import oatboat.model.database.grouping.Grouping;
import oatboat.model.database.protobuf.ProtoDB;
import oatboat.model.food.Ingredient;
import oatboat.model.food.LabelSet;
import oatboat.model.food.NutrientSet;
import oatboat.model.food.Recipe;
import oatboat.model.logic.Selection;

/**
 * Handles saving and loading the Database object as a local file which is read
 * and written using the com.google.protobuf 'Protocol Buffer'
 * 
 * @author ballsies
 *
 */
public class DatabaseUtils {

//	private static final String fileName = "oatboat.db";

	/**
	 * Looks for an existing .db file in local storage, to load as the working
	 * Database object while the program runs.
	 * 
	 * @return an existing Database object, or null.
	 */
	public static Database load(String filename) {

		Database db = null;
		ProtoDB.ProtoDatabase savedDBFile = null;

		try {
			savedDBFile = ProtoDB.ProtoDatabase.parseFrom(new FileInputStream(filename));
			if (savedDBFile != null)
				db = new Database();
		} catch (Exception e) {
			System.out.println("could not find a valid database to load at " + filename);
			return db;
		}

		// load ingredients to local objects
		for (ProtoDB.ProtoIngredient pIng : savedDBFile.getProtoIngredients().getIngredientsList()) {

			// make a nutrient set
			double[] nutset = { pIng.getNutrients().getEnergyKj(), pIng.getNutrients().getEnergyCal(),
					pIng.getNutrients().getFats(), pIng.getNutrients().getSaturates(), pIng.getNutrients().getCarbs(),
					pIng.getNutrients().getSugars(), pIng.getNutrients().getProtein(), pIng.getNutrients().getSalt() };

			// make an array of flags to pass to constructor as vararg
			Boolean[] bools = new Boolean[pIng.getGroupingFlags().getFlagCount()];
			for (int i = 0; i < bools.length; i++) {
				bools[i] = pIng.getGroupingFlags().getFlag(i);
			}

			// pass it all to new ingredient and add to local DB
			db.addIngredient(new Ingredient(pIng.getName(), pIng.getDescCZ(), pIng.getId(), pIng.getDescEN(),
					pIng.getCostPerKilo(), new NutrientSet(nutset), bools));
		}

		// load consumables to local format
		for (ProtoDB.ProtoConsumable pCon : savedDBFile.getProtoConsumables().getConsumablesList()) {
			db.addConsumable(
					new Consumable(pCon.getName(), pCon.getDescription(), pCon.getSampleCost(), pCon.getSampleSize()));
		}

		// load grouping information
		for (ProtoDB.ProtoGrouping pGrp : savedDBFile.getProtoGroupings().getGroupingsList()) {
			db.addGrouping(new Grouping(pGrp.getIndex(), pGrp.getDescription(), pGrp.getDisplayText()));
		}

		for (ProtoDB.ProtoLabelSet pSet : savedDBFile.getProtoLabelSets().getLabelSetsList()) {

			String setName = pSet.getSetName(), mainTitle = pSet.getMainTitle(), subTitle = pSet.getSubTitle(),
					contentsPrefix = pSet.getContentsPrefix(), nutritionPrefix = pSet.getNutritionPrefix(),
					additionalInfo = pSet.getAdditionalInfo();
			boolean clientSuppliedWeight = pSet.getClientSuppliedWeight();
			Integer unitWeight = pSet.getUnitWeight(), daysToExpiry = pSet.getDaysToExpiry();

			db.addLabelSet(new LabelSet(setName, mainTitle, subTitle, contentsPrefix, nutritionPrefix, additionalInfo,
					daysToExpiry, clientSuppliedWeight, unitWeight));
		}

		// load recipes into local format
		for (ProtoDB.ProtoRecipe pRcp : savedDBFile.getProtoRecipes().getRecipesList()) {

			// make the keystone Selection
			Selection keystone = new Selection(db.getIngredient(pRcp.getKeystone().getFDCID()),
					pRcp.getKeystone().getMultiplier(), pRcp.getKeystone().getAppendPercentage());

			// make the selection array
			Selection[] sels = new Selection[pRcp.getSelections().getSelectionSetList().size()];
			for (int i = 0; i < sels.length; i++) {
				Selection tempSel = new Selection(db.getIngredient(pRcp.getSelections().getSelectionSet(i).getFDCID()),
						pRcp.getSelections().getSelectionSet(i).getMultiplier(),
						pRcp.getSelections().getSelectionSet(i).getAppendPercentage());
				sels[i] = tempSel;
			}

			// build the recipe object
			Recipe tempRcp = new Recipe(pRcp.getTitle(), keystone, sels);

			// set reference to a labelset if present
			String labelSetName = pRcp.getLabelSetName();
			if (labelSetName.length() > 0 && db.hasLabelSet(labelSetName)) {
				tempRcp.setLabelSet(labelSetName);
			}

			// add the recipe to local db
			db.addRecipe(tempRcp);
		}

		return db;

	}

	/**
	 * Writes the database object to the hard drive in whichever location the
	 * application is being executed. Uses the com.google.protobuf Protocol Buffer
	 * as the format for storing fields from each Ingredient and Consumable
	 * 
	 * @see com.google.protobuf
	 */
	public static void save(Database db, String filename) {
		// @formatter:off
		// construct the ingredient and consumable databases
		ProtoDB.IngredientDatabase.Builder protoIngredientDB = ProtoDB.IngredientDatabase.newBuilder();
		ProtoDB.ConsumableDatabase.Builder protoConsumableDB = ProtoDB.ConsumableDatabase.newBuilder();
		ProtoDB.GroupingDatabase.Builder protoGroupingDB = ProtoDB.GroupingDatabase.newBuilder();
		ProtoDB.RecipeDatabase.Builder protoRecipeDB = ProtoDB.RecipeDatabase.newBuilder();
		ProtoDB.LabelSetDatabase.Builder protoLabelSetDB = ProtoDB.LabelSetDatabase.newBuilder();

		// for each local Ingredient, transfer all possible fields to a protoIngredient and add it to the proto-db
		for (Ingredient ing : db.getIngredientList()) {

			// construct a new protoIngredient
			ProtoDB.ProtoIngredient.Builder proto = ProtoDB.ProtoIngredient.newBuilder(); 

			// get & set for all known fields. proto buffer can handle bad/null values internally so it should be safe (?) to attempt without a long repetition of try/catch blocks
			proto
				.setName(ing.getName())
				.setId(ing.getId())
				.setDescEN(ing.getInfo())
				.setDescCZ(ing.getLabel())
				.setCostPerKilo(ing.getCost());
			
			ProtoDB.ProtoIngredient.Flags.Builder flags = ProtoDB.ProtoIngredient.Flags.newBuilder();
			for (int i = 0; i < ing.getCustomFlagProperties().size(); i++) 
				flags.addFlag(ing.getCustomFlag(i));
			
			ProtoDB.ProtoIngredient.ProtoNutrientSet.Builder nutset = ProtoDB.ProtoIngredient.ProtoNutrientSet.newBuilder();
			double[] nutr = ing.getNutrientSet().getAllValues();
			
			nutset
				.setEnergyKj(nutr[0])
				.setEnergyCal(nutr[1])
				.setFats(nutr[2])
				.setSaturates(nutr[3])
				.setCarbs(nutr[4])
				.setSugars(nutr[5])
				.setProtein(nutr[6])
				.setSalt(nutr[7])
				.build();
			
			// finally build the protoingredient with the nutrient set, adding it to the proto-ingredient database 
			protoIngredientDB.addIngredients(proto.setNutrients(nutset).setGroupingFlags(flags).build());
		}
		
		// similarly; transfer the consumables to proto-form
		for (Consumable item : db.getConsumables()) {
			ProtoDB.ProtoConsumable.Builder protoConsumable = ProtoDB.ProtoConsumable.newBuilder();
			
			try {
				protoConsumable
				.setName(item.getItemName())
				.setSampleCost(item.getCostPerSample())
				.setSampleSize(item.getSampleSize())
				.setDescription(item.getDescription())
				;
				
				protoConsumableDB.addConsumables(protoConsumable).build();
				
			} catch (Exception e) {
				System.out.println("there was a problem adding " + item.getItemName() + " to the consumables proto-db");
			}
		}
		
		// transfer the groupings to proto-form
		
		// to make sure the zero position grouping is always hardcoded:
		db.addGrouping(new Grouping(0, "Do not include in contents list", ""));
		
		// iterate through the groupings. index zero is always the 'non-printing' grouping flag 
		for (int i = 0; db.getGrouping(i) != null; i++) {
			ProtoDB.ProtoGrouping.Builder protoGrouping = ProtoDB.ProtoGrouping.newBuilder();
			
			protoGrouping
				.setIndex(i)
				.setDescription(db.getGrouping(i).description)
				.setDisplayText(db.getGrouping(i).displayText);
			
			protoGroupingDB.addGroupings(protoGrouping).build();
			
		}
		
		// and transfer the recipes to proto-form
		for (Recipe rcp : db.getRecipes()) {
			
			// build the keystone selection first 
			ProtoDB.ProtoSelection.Builder pKey = ProtoDB.ProtoSelection.newBuilder();
			
			pKey.setFDCID(rcp.getKeystone().getIngredient().getId())
				.setMultiplier(rcp.getKeystone().getAmount())
				.setAppendPercentage(rcp.getKeystone().getAppendPercent());
			
			// build the selection set of other ingredients
			ProtoDB.ProtoRecipe.ProtoSelectionSet.Builder pSelSet = ProtoDB.ProtoRecipe.ProtoSelectionSet.newBuilder();
			for (Selection sel : rcp.getSelections()) {
				
				ProtoDB.ProtoSelection.Builder pSel = ProtoDB.ProtoSelection.newBuilder();
				
				pSel.setFDCID(sel.getIngredient().getId())
					.setMultiplier(sel.getAmount())
					.setAppendPercentage(sel.getAppendPercent());
				
				pSelSet.addSelectionSet(pSel.build());
			}
			
			// build the recipe 
			ProtoDB.ProtoRecipe.Builder pRcp = ProtoDB.ProtoRecipe.newBuilder();
			
			pRcp.setTitle(rcp.getTitle())
				.setKeystone(pKey.build())
				.setSelections(pSelSet.build());
			
			if (rcp.getLabelSet() != null) 
				pRcp.setLabelSetName(rcp.getLabelSet());
			
			protoRecipeDB.addRecipes(pRcp.build());
		}
		
		// label sets
		for (LabelSet set : db.getLabelSets()) {
			ProtoDB.ProtoLabelSet.Builder pSet = ProtoDB.ProtoLabelSet.newBuilder();
			
			pSet.setSetName(set.setName)
			.setMainTitle(set.mainTitle)
			.setSubTitle(set.subTitle)
			.setContentsPrefix(set.contentsPrefix)
			.setNutritionPrefix(set.nutritionPrefix)
			.setAdditionalInfo(set.additionalInfo)
			.setDaysToExpiry(set.expiry)
			.setClientSuppliedWeight(set.clientSuppliedWeight);
			
			if (set.clientSuppliedWeight) {
				pSet.setUnitWeight(set.unitWeight);
			}
			
			protoLabelSetDB.addLabelSets(pSet.build());
		}
				
		// construct the main proto-database wrapper and add the ingredient and consumable databases
		ProtoDB.ProtoDatabase.Builder protoDB = ProtoDB.ProtoDatabase.newBuilder()
				.setProtoIngredients(protoIngredientDB)
				.setProtoConsumables(protoConsumableDB)
				.setProtoGroupings(protoGroupingDB)
				.setProtoRecipes(protoRecipeDB)
				.setProtoLabelSets(protoLabelSetDB);
		
		// @formatter:on

		// build the main proto-database and write it as a file
		try {
			FileOutputStream stream = new FileOutputStream(filename);
			protoDB.build().writeTo(stream);
			stream.close();

		} catch (Exception e) {
			System.out.println("writing db didn't work");
		}
	}

	public static boolean isDatabase(String filename) {
		try {
			ProtoDB.ProtoDatabase.parseFrom(new FileInputStream(filename));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
