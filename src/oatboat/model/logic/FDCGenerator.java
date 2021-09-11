package oatboat.model.logic;

import java.util.ArrayList;
import java.util.List;

import oatboat.model.food.Ingredient;

/**
 * static method library for FDC related operations
 * 
 * @author ballsies
 *
 */
public class FDCGenerator {

	/**
	 * \ automatically generate and return a valid and unused FDCID, up to the value
	 * of 999.
	 * 
	 * @param list the list of current ingredients to test against
	 * @return the lowest next available number as a string
	 */
	public static String getNextAvailableFDC(List<Ingredient> list) {
		List<String> FDCList = new ArrayList<String>();

		for (Ingredient in : list) {
			if (in.getId().length() < 4)
				FDCList.add(in.getId());
		}

		for (int i = 1; i < 1000; i++) {
			String nextFDC = String.format("%03d", i);
			if (!FDCList.contains(nextFDC))
				return nextFDC;
		}
		return "--";
	}

}
