package oatboat;

import java.util.Locale;
import java.util.Locale.Category;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import oatboat.controller.prefs.UserPreferences;
import oatboat.model.database.DatabaseUtils;
import oatboat.model.database.Database;
import oatboat.view.MainController;

public class Main extends Application {

	private Stage mainStage;
	private MainController mainSceneController;
	private Database database;
	private UserPreferences prefs;

	@Override
	public void start(Stage primaryStage) {
		mainStage = primaryStage;

		preloadTasks();
		getMainLayout();
		postloadTasks();
	}

	private void preloadTasks() {
		prefs = new UserPreferences();
		setInternalLocaleFormat(prefs.getDecimalFormat());
		loadDatabase(prefs.getDatabaseFilename());
	}

	public void loadDatabase(String filename) {
		database = DatabaseUtils.load(filename);
		if (database == null)
			database = new Database();
	}

	private void getMainLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Main.fxml"));
			BorderPane mainPane = (BorderPane) loader.load();

			mainSceneController = loader.getController();
			mainSceneController.setMain(this);

			Scene scene = new Scene(mainPane);

			mainStage.setTitle("Nutritional Labelling Streamliner");
			mainStage.getIcons().add(new Image(Main.class.getResourceAsStream("/oatboat/view/icon/ob-icon.png")));
			mainStage.setScene(scene);
			mainStage.setResizable(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void postloadTasks() {
		mainSceneController.setParentStage(mainStage);
		mainSceneController.preDisplayInitialisation();

		mainStage.setOnCloseRequest(event -> saveDatabaseChangesOnExit(event));
		mainStage.show();
		mainSceneController.postDisplayInitialisation();
	}

	public Database getDatabase() {
		return database;
	}

	public UserPreferences getUserPrefs() {
		return prefs;
	}

	/**
	 * determines the appearance of numbers, specifically the representation of the
	 * decimal separator. link the arg to the value from the decimal format setting
	 * tab for best use.
	 * 
	 * @param setting
	 */
	public void setInternalLocaleFormat(int setting) {
		switch (setting) {
		case 0:
			Locale.setDefault(Category.FORMAT, Locale.UK);
			break;
		case 1:
			Locale.setDefault(Category.FORMAT, Locale.GERMANY);
			break;
		default:
			break;
		}
	}

	private void saveDatabaseChangesOnExit(WindowEvent event) {

		if (database.equals(prefs.getDatabaseFilename()))
			return; // no need to alert user

		Alert exitDialog = new Alert(AlertType.NONE, "Changes were made to the database. You can:");

		ButtonType back = new ButtonType("Go Back", ButtonData.LEFT);
		ButtonType quit = new ButtonType("Exit", ButtonData.BACK_PREVIOUS);
		ButtonType save = new ButtonType("Save & Exit", ButtonData.OK_DONE);

		exitDialog.getButtonTypes().setAll(back, quit, save);
		exitDialog.setTitle("Save Database?");

		ButtonData decision = exitDialog.showAndWait().get().getButtonData();
		if (decision == ButtonData.OK_DONE)
			DatabaseUtils.save(database, prefs.getDatabaseFilename());
		else if (decision == ButtonData.LEFT)
			event.consume();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
