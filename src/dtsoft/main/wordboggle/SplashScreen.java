package dtsoft.main.wordboggle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
	
	public static final long TIME_TO_DISPLAY = 3000;	// in milli 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.splashscreen);

		// Otherwise show for the usual amount of time
		new Handler().postDelayed(new Runnable() {
			public void run() {
				startNextActivity();
			}
		}, TIME_TO_DISPLAY);
	} 
	
	private void startNextActivity() {
		finish();
		startActivity(new Intent("dtsoft.main.wordboggle.WordBoggle"));
	}
}
