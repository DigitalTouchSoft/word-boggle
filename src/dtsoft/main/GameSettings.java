package dtsoft.main;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import dtsoft.main.data.cache.SettingsCache;

public class GameSettings extends Activity {
	
	private SettingsCache mGameSettings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.settings);
		
		// Get a copy of the settings
		mGameSettings = new SettingsCache(super.getCacheDir().getPath() + File.pathSeparator + SettingsCache.SETTINGS_FILE);
		
		
		// Read the settings and configure the display 
		if (mGameSettings.getSettings().freeMode)
			((ImageView)findViewById(R.id.ClassicGameMode)).setAlpha(0x4B);
		else 
			((ImageView)findViewById(R.id.FreeStyleGameMode)).setAlpha(0x4B);
		
		if (mGameSettings.getSettings().gameColumns == 4)
			((ImageView)findViewById(R.id.FiveByFive)).setAlpha(0x4B);
		else 
			((ImageView)findViewById(R.id.FourByFour)).setAlpha(0x4B);
		
		
		// Setup the Apply / Cancel buttons
		((Button)findViewById(R.id.SettingsApply)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveSettingsAndFinish();
			}
		});
		((Button)findViewById(R.id.SettingsCancel)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancelAndFinish();
			}
		});		
		
		// Handlers for the 4x4 and 5x5 game boards
		((ImageView)findViewById(R.id.FourByFour)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ImageView)v).setAlpha(0xFF);
				((ImageView)findViewById(R.id.FiveByFive)).setAlpha(0x4B);
				
				mGameSettings.getSettings().gameColumns = 4;
			}
		});
		((ImageView)findViewById(R.id.FiveByFive)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ImageView)v).setAlpha(0xFF);
				((ImageView)findViewById(R.id.FourByFour)).setAlpha(0x4B);
				
				mGameSettings.getSettings().gameColumns = 5;
			}
		});		

		// Handlers for the classic / free mode game modes. 
		((ImageView)findViewById(R.id.ClassicGameMode)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ImageView)v).setAlpha(0xFF);
				((ImageView)findViewById(R.id.FreeStyleGameMode)).setAlpha(0x4B);
				
				mGameSettings.getSettings().freeMode = false;
			}
		});
		((ImageView)findViewById(R.id.FreeStyleGameMode)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ImageView)v).setAlpha(0xFF);
				((ImageView)findViewById(R.id.ClassicGameMode)).setAlpha(0x4B);
				
				mGameSettings.getSettings().freeMode = true;
			}
		});
	}
	
	private void saveSettingsAndFinish() {
		// Alert the user that the settings will take effect on next restart
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setCancelable(false);
		d.setMessage("Changes will take when you start a new game or restart the application!");
		d.setPositiveButton("Okay", new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mGameSettings.updateSettings(mGameSettings.getSettings());
				mGameSettings = null;
				finish();
			}
		});
		d.show();
		d = null;
	}
	private void cancelAndFinish() {
		mGameSettings = null;
		finish();
	}

	@Override
	public void onBackPressed() {
		// Prompt the user to save and or close
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setMessage("Save your changes? Any unsaved changes will be lost!");
		d.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Save the settings and destroy the resource
				saveSettingsAndFinish();
			}
		});
		
		// Doesn't save a dang thing
		d.setNegativeButton("No", new AlertDialog.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancelAndFinish();
			}	
		});
		d.show();
		d = null;
	}

}
