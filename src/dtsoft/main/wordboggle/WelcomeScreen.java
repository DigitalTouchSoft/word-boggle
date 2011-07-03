package dtsoft.main.wordboggle;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

public class WelcomeScreen extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome); 
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
}

