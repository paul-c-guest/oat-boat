package oatboat.model.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FDCSearch {
	/**
	 * takes any number of keywords separated either by spaces or commas, and
	 * returns a map of matching results from the CSV file food.csv if it is present
	 * in the ~/fdc folder wherever the main application is being exectued from.
	 * <p>
	 * search is blind to upper and lower case usage, thus will return all results
	 * regardless of case.
	 * <p>
	 * 
	 * @param searchString raw input from the user
	 * @return a hashmap of FDC food descriptions and fdc ID numbers
	 */
	public static Map<String, Integer> getResults(String searchString) {

		Map<Integer, String> foodList = FDCDatabase.getFoodList();
		Map<String, Integer> results = new HashMap<String, Integer>();

		List<String> keywords = getSearchTerms(searchString);
		boolean found = false;

		for (Integer fdcID : foodList.keySet()) {
			found = true;
			for (String thisQuery : keywords) {
				if (foodList.get(fdcID).toLowerCase().contains(thisQuery.toLowerCase()) || thisQuery.equals(fdcID.toString()))
					continue;
				else
					found = false;
			}
			if (found)
				results.put(foodList.get(fdcID), fdcID);
		}
		return results;
	}

	private static List<String> getSearchTerms(String query) {
		// initialise the list to return
		List<String> queries = new ArrayList<>();

		// change some punctuation to spaces
		StringBuilder temp = new StringBuilder();
		for (char c : query.toCharArray()) {
			if (c == ',' || c == '.')
				temp.append(" ");
			else
				temp.append(c);
		}

		// sneak a space character onto the end to assist following block
		temp.append(" ");
		query = temp.toString();

		// iterate the query and add each distinguishable term to the list.
		for (int start = 0, end = 0; end < query.length(); end++) {

			// increment 'end' until a comma or space character is found
			if (query.charAt(end) == ' ') {

				// get the string between 'start' & 'end'
				String split = query.substring(start, end).trim();
				// if it still has length after trimming, add it to queries
				if (split.length() > 0)
					queries.add(split);

				// get new values for start & end
				for (int i = end + 1; i < query.length(); i++) {
					if (query.charAt(i) == ' ')
						continue;
					start = end = i;
					break;
				}
			}
		}

		// return stream of all distinct strings (remove duplicates) 
		return queries.stream().distinct().collect(Collectors.toList());
	}

//	passes a messy query for testing 
	public static void main(String[] args) {
		for (String s : getSearchTerms(", f horse, goat,, ")) {
			System.out.println("'" + s + "'");
		}
	}

}
