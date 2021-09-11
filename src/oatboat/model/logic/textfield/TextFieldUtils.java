package oatboat.model.logic.textfield;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.function.UnaryOperator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

/**
 * static method library of helper objects for use in conjunction with the
 * JavaFX {@link TextField} class.
 * <p>
 * Included are methods to return a {@link NumberStringConverter} for the
 * conversion of either doubles or integers; and to return a
 * {@link TextFormatter} which effectively acts as an input filter such that any
 * input will be ignored if it cannot assist the construction of a valid double,
 * or a valid integer.
 * 
 * @author ballsies
 *
 */
public class TextFieldUtils {

	/**
	 * The decimal format separator character for the user's {@link Locale}.
	 * Returned as a {@link StringProperty} to offer the extra functionality - i.e.
	 * binding and observability.
	 * 
	 */
	public static StringProperty decimal = new SimpleStringProperty(
			String.valueOf(new DecimalFormatSymbols(Locale.getDefault()).getDecimalSeparator()));

	/**
	 * A hybrid {@link StringConverter} which is for the most part a regular
	 * {@link NumberStringConverter}. Text output however will always display with a
	 * decimal place -- overriding the {@link Number} default behaviour to output
	 * cleaner integer values from whole numbers (i.e. when x.0 is returned as x).
	 * 
	 * @return {@link NumberStringConverter}
	 */
	public static NumberStringConverter doubleConverter() {
		return new NumberStringConverter() {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault(Category.FORMAT));
			{
				nf.setGroupingUsed(false);
				nf.setMinimumFractionDigits(1);
			}

			@Override
			public String toString(Number value) {
				return nf.format(value.doubleValue());
			}
			
			@Override
			public Number fromString(String value) {
				try {
					return nf.parse(value);
				} catch (ParseException e) {
					return null;
				}
			}
		};
	}

	/**
	 * A {@link StringConverter} which will only operate upon and return integers.
	 * 
	 * @return {@link NumberStringConverter}
	 */
	public static NumberStringConverter integerConverter() {
		return new NumberStringConverter() {
			StringConverter<Integer> ic = new IntegerStringConverter();

			@Override
			public String toString(Number value) {
				return ic.toString(value.intValue());
			}
		};
	}

	/**
	 * A {@link TextFormatter} for Strings which will only allow for the
	 * construction of valid doubles in the JavaFX {@link TextField} to which it is
	 * set.
	 * 
	 * @return {@link TextFormatter}
	 */
	public static TextFormatter<String> doubleFormatter() {
		return new TextFormatter<String>(new UnaryOperator<TextFormatter.Change>() {
			@Override
			public TextFormatter.Change apply(TextFormatter.Change change) {
				if (!validate(change.getControlNewText())) {
					try {
						change.setText("");
						change.setCaretPosition(change.getCaretPosition() - 1);
						change.setAnchor(change.getAnchor() - 1);
					} catch (Exception e) {
					}
				}
				return change;
			}

			// only returns true for the empty string, or cleanly expressed integer and
			// double values
			private boolean validate(String entered) {
				return entered.matches("|0|[1-9][0-9]*[.|,]?[0-9]*|0[.|,][0-9]*");
			}
		});
	}

	/**
	 * A {@link TextFormatter} for Strings which will only allow for the
	 * construction of valid integers in the JavaFX {@link TextField} to which it is
	 * set.
	 * 
	 * @return {@link TextFormatter}
	 */
	public static TextFormatter<String> integerFormatter() {
		return new TextFormatter<String>(new UnaryOperator<TextFormatter.Change>() {
			@Override
			public TextFormatter.Change apply(TextFormatter.Change change) {
				if (!validate(change.getControlNewText())) {
					try {
						change.setText("");
						change.setCaretPosition(change.getCaretPosition() - 1);
						change.setAnchor(change.getAnchor() - 1);
					} catch (Exception e) {
					}
				}
				return change;
			}

			// only returns true for an empty string or cleanly expressed integer values
			private boolean validate(String entered) {
				return entered.matches("|[1-9][0-9]*");
			}
		});
	}

	public static TextFormatter<String> filenameFormatter() {
		return new TextFormatter<String>(new UnaryOperator<TextFormatter.Change>() {
			@Override
			public TextFormatter.Change apply(TextFormatter.Change change) {
				if (!validate(change.getControlNewText())) {
					try {
						change.setText("");
						change.setCaretPosition(change.getCaretPosition() - 1);
						change.setAnchor(change.getAnchor() - 1);
					} catch (Exception e) {
					}
				}
				return change;
			}

			private boolean validate(String ch) {
				return ch.matches("[^!@#$%^&*(){},]*");
			}
		});
	}

}
