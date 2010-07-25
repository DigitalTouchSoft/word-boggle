package dtsoft.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class SplashScreen extends Activity {
	
	public static final long TIME_TO_DISPLAY = 2000;	// Display for 1500 milliseconds 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.splashscreen);
	
		ProgressBar pb = (ProgressBar)findViewById(R.id.WordProgressBar);
		pb.setMax(100);
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				finish();
				startActivity(new Intent("dtsoft.main.WordBoggle"));
			}
		}, TIME_TO_DISPLAY);
		
		
	} 
}
