package dtsoft.main.data.cache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.Log;

/**
 * This class is used for reading / writing the settings
 * 
 * @author Michael
 *
 */
public class SettingsCache {
	private File mSettingsFile;
	private Settings mSettings;
	
	public SettingsCache(String settingsFile) {
		try {
			mSettingsFile = getSettingsFile(settingsFile);
			mSettings = readSettings();
		} catch (IOException e) {
			Log.e("Settings", "Unable to load/create the settings file");
			throw new RuntimeException(e);
		}
	}
	
	public Settings getSettings() {
		return mSettings;
	}
	
	private Settings readSettings() throws IOException {
		BufferedReader settings = new BufferedReader(new InputStreamReader(new FileInputStream(mSettingsFile)));
		String setting;
		Settings result = new Settings();
		while ((setting = settings.readLine()) != null) {
			String[] keyValue = setting.split("=");
			
			// Skip comments
			if (setting.startsWith("@") || setting.startsWith("#"))
				continue;
			
			// Skip if this setting looks invalid
			if (keyValue.length != 2)
				continue;
		
			if (keyValue[0].trim().equalsIgnoreCase("gameColumns")) {
				result.gameColumns = Integer.parseInt(keyValue[1].trim());
				continue;
			}
			
			if (keyValue[0].trim().equalsIgnoreCase("freeMode")) {
				result.freeMode = Boolean.getBoolean(keyValue[1].trim());
				continue;
			}
			
			if (keyValue[0].trim().equalsIgnoreCase("diagonalsEnabled")) {
				result.diagonalsEnabled = Boolean.getBoolean(keyValue[1].trim());
				continue;
			}
			
		}
		
		// Cleanup
		settings.close();
		settings = null;
		
		return result;
	}
	
	/**
	 * Configures the settings file for first time use then closes / cleans up the stream
	 * @param file
	 * @throws IOException
	 */
	private void configureSettingsForFirstTimeUse(File file) throws IOException {
		BufferedWriter settingsFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		settingsFile.write("@CONFIGURATION FILE");
		settingsFile.newLine();
		settingsFile.write("gameColumns=5");
		settingsFile.newLine();
		settingsFile.write("freeMode=false");
		settingsFile.newLine();
		settingsFile.write("diagonalsEnabled=false");
		settingsFile.newLine();
		settingsFile.close();
	}
	
	
	private File getSettingsFile(String cacheFile) throws IOException {
		File f = new File(cacheFile);
		if (!f.exists()) {
			if (f.createNewFile()) {
				// If it doesn't exist set it up for first time use then
				configureSettingsForFirstTimeUse(f);
			} else {
				throw new IOException("Unable to setup the file for first time use");
			}
		}
		return f;
	}
	
	/***
	 * Settings are delimited like this
	 * SettingName1=SettingValue
	 * SettingName2=SettingValue
	 * 
	 * Comments start with: #, @
	 * @throws IOException 
	 */
	
	public void updateSettings(Settings settings) {
		BufferedWriter settingsFile;
		try {
			settingsFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mSettingsFile)));
			settingsFile.write("@CONFIGURATION FILE");
			settingsFile.newLine();
			settingsFile.write("gameColumns="+settings.gameColumns);
			settingsFile.newLine();
			settingsFile.write("freeMode="+settings.freeMode);
			settingsFile.newLine();
			settingsFile.write("diagonalsEnabled="+settings.diagonalsEnabled);
			settingsFile.newLine();
			settingsFile.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			settingsFile = null;
		}
	}
	
	/**
	 * Default settings:
	 * gameColumns=5
	 * freeMode=false
	 * diagonalsEnabled=false
	 * 
	 * @author Michael
	 *
	 */
	public class Settings {
		public int gameColumns;
		public boolean freeMode;
		public boolean diagonalsEnabled;
	}
}
