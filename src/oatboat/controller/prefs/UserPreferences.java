package oatboat.controller.prefs;

import java.util.prefs.Preferences;

public class UserPreferences {
	
	private Preferences prefs;
	private String decimal = "decimalFormatComboBoxIndex";
	private String tab = "tabSelectedOnStartup";
	private String dbFile = "localDatabaseFilename";
//	private String dbLocation = localDatabaseDirectoryLocation";
	
	public UserPreferences() {
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}
	
	public void setDecimalFormat(int comboBoxIndex) {
		prefs.putInt(decimal, comboBoxIndex);
	}
	
	public int getDecimalFormat() {
		return prefs.getInt(decimal, -1);
	}
	
	public void setStartupTab(String tabName) {
		prefs.put(tab, tabName);
	}
	
	public String getStartupTab() {
		return prefs.get(tab, "not selected");
	}
	
	public void setDatabaseFilename(String filename) {
		if (filename.isEmpty() || filename == null || filename.length() == 0) 
			prefs.put(dbFile, "default.db");
		prefs.put(dbFile, filename);
	}
	
	public String getDatabaseFilename() {
		return prefs.get(dbFile, "default.db");
	}
	
}
