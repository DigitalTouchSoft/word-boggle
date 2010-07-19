package dtsoft.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class GameSettings extends Activity {

	private static final String SETTINGS_FILE = "settings.conf";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Load the settings window
		super.setContentView(R.layout.settings);

	}
	

}
