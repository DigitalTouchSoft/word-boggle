package dtsoft.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import dtsoft.main.data.WordsDataHelper;
import dtsoft.main.data.cache.SettingsCache;

public class SplashScreen extends Activity {
	
	public static final long TIME_TO_DISPLAY = 2000;	// in milli 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.splashscreen);

		((ProgressBar)findViewById(R.id.WordProgressBar)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.WordProgressBarText)).setVisibility(View.INVISIBLE);
		// Otherwise show for the usual amount of time
		new Handler().postDelayed(new Runnable() {
			public void run() {
				startNextActivity();
			}
		}, TIME_TO_DISPLAY);
	} 
	
	private void startNextActivity() {
		finish();
		startActivity(new Intent("dtsoft.main.WordBoggle"));
	}
}
