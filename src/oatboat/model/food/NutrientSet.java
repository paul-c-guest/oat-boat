
package oatboat.model.food;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * A Set of the seven fundamental nutritional information types which are
 * legally required on food labelling in the EU, i.e.: energy (provided in both
 * kcal and kJoules); fat, and the saturated fats contained therein;
 * carbohydrates, and the sugars contained therein; protein; and salt.
 * <p>
 * This object is utilised by the generic <b>Ingredient</b> object so there is
 * should be no need to instantiate this object directly.
 * <p>
 * <b>Important:</b> attention must be given to the value for salt. it must be
 * set here in <b>grams</b> -- in other relevant sources such as the FDC
 * databases it may be listed in <b>milligrams</b>.
 *
 * @author ballsies
 */
public class NutrientSet {

	// the number of elements stored in this version
	public static final int NUT_INDEX = 8;

	private DoubleProperty energyKJ;
	private DoubleProperty energyKcal;
	private DoubleProperty fat;
	private DoubleProperty saturates;
	private DoubleProperty carbohydrates;
	private DoubleProperty sugars;
	private DoubleProperty protein;
	private DoubleProperty salt;

	/**
	 * initialise a nutrient set and specify each double value individually
	 */
	public NutrientSet(double energy_kJ, double energy_kcal, double fat, double saturates, double carbohydrates,
			double sugars, double protein, double salt) {

		this.energyKJ = new SimpleDoubleProperty(energy_kJ);
		this.energyKcal = new SimpleDoubleProperty(energy_kcal);
		this.fat = new SimpleDoubleProperty(fat);
		this.saturates = new SimpleDoubleProperty(saturates);
		this.carbohydrates = new SimpleDoubleProperty(carbohydrates);
		this.sugars = new SimpleDoubleProperty(sugars);
		this.protein = new SimpleDoubleProperty(protein);
		this.salt = new SimpleDoubleProperty(salt);
	}

	/**
	 * initialises a nutrient set with all values set to 0.0
	 */
	public NutrientSet() {
		this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	}

	/**
	 * initialises a nutrient set with the values from a vararg stream of doubles.
	 * the values are set from the first 8 indices of the array; and must match the
	 * order of nutrients for the complete constructor, i.e: { energy_kJ,
	 * energy_kcal, fat, saturates, carbohydrates, sugars, protein, salt }
	 * 
	 */
	public NutrientSet(double... inf) {
		this(inf[0], inf[1], inf[2], inf[3], inf[4], inf[5], inf[6], inf[7]);
	}

	public void setEnergyKJ(double energy) {
		energyKJ.set(energy);
	}

	public double getEnergyKJ() {
		return energyKJ.get();
	}

	public DoubleProperty getEnergyKJProperty() {
		return energyKJ;
	}

	public void setEnergyKcal(double energy) {
		energyKcal.set(energy);
	}

	public double getEnergyKcal() {
		return energyKcal.get();
	}

	public DoubleProperty getEnergyKcalProperty() {
		return energyKcal;
	}

	public void setFat(double fat) {
		this.fat.set(fat);
	}

	public double getFat() {
		return fat.get();
	}

	public DoubleProperty getFatProperty() {
		return fat;
	}

	public void setSaturates(double saturates) {
		this.saturates.set(saturates);
	}

	public double getSaturates() {
		return saturates.get();
	}

	public DoubleProperty getSaturatesProperty() {
		return saturates;
	}

	public void setCarbohydrates(double carbs) {
		carbohydrates.set(carbs);
	}

	public double getCarbohydrates() {
		return carbohydrates.get();
	}

	public DoubleProperty getCarbohydratesProperty() {
		return carbohydrates;
	}

	public void setSugars(double sugar) {
		sugars.set(sugar);
	}

	public double getSugars() {
		return sugars.get();
	}

	public DoubleProperty getSugarsProperty() {
		return sugars;
	}

	public void setProtein(double protein) {
		this.protein.set(protein);
	}

	public double getProtein() {
		return protein.get();
	}

	public DoubleProperty getProteinProperty() {
		return protein;
	}

	public void setSalt(double salt) {
		this.salt.set(salt);
	}

	public double getSalt() {
		return salt.get();
	}

	public DoubleProperty getSaltProperty() {
		return salt;
	}

	public double[] getAllValues() {
		return new double[] { energyKJ.get(), energyKcal.get(), fat.get(), saturates.get(), carbohydrates.get(),
				sugars.get(), protein.get(), salt.get() };
	}

	public DoubleProperty[] getAllValueProperties() {
		return new DoubleProperty[] { energyKJ, energyKcal, fat, saturates, carbohydrates, sugars, protein, salt };
	}

//	formatter:off
//	public String toString() {
//		return "\nEnergy (kJ):\t" + energy_kJ + "\nEnergy (kcal):\t" + energy_kcal + "\nFat:\t\t" + fat
//				+ "\nSaturates:\t" + saturates + "\nCarbohydrates:\t" + carbohydrates + "\nSugars:\t\t" + sugars
//				+ "\nProtein:\t" + protein + "\nSalt (mg):\t\t" + salt / 1000;
//	}

}
