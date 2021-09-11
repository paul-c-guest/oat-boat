package oatboat.model.food;

/**
 * The collection of strings which form the common text which will appear on a
 * printed label which has been exported to adobe indesign. This class is
 * utilised by the Recipe so there should be no need to instantiate a LabelSet
 * by itself.
 *
 */
public class LabelSet implements Comparable<LabelSet> {

	public String setName, mainTitle, subTitle, contentsPrefix, nutritionPrefix, additionalInfo;
	public Integer expiry, unitWeight;
	public boolean clientSuppliedWeight;

	/**
	 * Total constructor. all variables are exposed - although some can be null.
	 * 
	 * @param setName    the internal name for the set
	 * @param mainTitle  the main title to appear on labelling
	 * @param unitWeight when given, overrides an automatically generated unit
	 *                   weight
	 */
	public LabelSet(String setName, String mainTitle, String subTitle, String contentsPrefix, String nutritionPrefix,
			String additionalInfo, Integer daysToExpiry, boolean clientSuppliedWeight, Integer unitWeight) {
		this.setName = setName;
		this.mainTitle = mainTitle;
		this.subTitle = subTitle;
		this.contentsPrefix = contentsPrefix;
		this.nutritionPrefix = nutritionPrefix;
		this.additionalInfo = additionalInfo;
		this.expiry = daysToExpiry;
		this.clientSuppliedWeight = clientSuppliedWeight;
		this.unitWeight = unitWeight;
	}

	@Override
	public String toString() {
		return setName;
	}

	@Override
	public int compareTo(LabelSet that) {
		return this.setName.compareTo(that.setName);
	}
	
	@Override
	public int hashCode() {
		return setName.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		LabelSet other = (LabelSet) obj;
		return this.setName.equals(other.setName);
	}
	
}
