package oatboat.model.logic.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Library of static methods offering a compact range of preconfigured JavaFX
 * {@link Alert} objects
 * 
 * @author ballsies
 *
 */
public class AlertFactory {

	/**
	 * returns a JavaFX {@link Alert} preconfigured with the {@link AlertType} of
	 * INFORMATION. The graphic is set to null. This alert type has a single 'OK'
	 * button to dismiss the alert.
	 * 
	 * @param header  title text
	 * @param content body text
	 * @return {@link Alert}
	 */
	public static Alert inform(String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setGraphic(null);
		alert.setHeaderText(header);
		alert.setContentText(content);
		return alert;
	}

	/**
	 * returns a JavaFX {@link Alert} preconfigured with the {@link AlertType} of
	 * ERROR. The Alert appears mostly as default, aside from the provided text.
	 * This alert type has a single 'OK' button to dismiss the alert.
	 * 
	 * @param content body text
	 * @return {@link Alert}
	 */
	public static Alert error(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(header);
		alert.setContentText(content);
		return alert;
	}

	/**
	 * returns a JavaFX {@link Alert} configured with the {@link AlertType} NONE,
	 * and a null graphic.
	 * <p>
	 * If no buttons are supplied at construction, the header and content strings
	 * are passed to the inform() method.
	 * 
	 * @param header  title text
	 * @param content body text
	 * @param buttons vararg of {@link ButtonType}
	 * @return {@link Alert}
	 */
	public static Alert blank(String header, String content, ButtonType... buttons) {
		if (buttons.length == 0)
			return inform(header, content);

		Alert alert = new Alert(AlertType.NONE, content, buttons);
		alert.setHeaderText(header);
		alert.setGraphic(null);
		return alert;
	}

}
