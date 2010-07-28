package dtsoft.main.wordboggle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import dtsoft.main.wordboggle.data.cache.SettingsCache;

public class GameSettings extends Activity {
	
	private SharedPreferences mGameSettings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.settings);
		
		mGameSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		// Setup the settings for the first time
		if (!mGameSettings .contains(SettingsCache.GAME_COLUMNS)) {
			mGameSettings.edit().putInt(SettingsCache.GAME_COLUMNS, SettingsCache.GameCols.fourbyfour).commit();
		}
		
		if (!mGameSettings .contains(SettingsCache.FREE_MODE)) {
			mGameSettings.edit().putBoolean(SettingsCache.FREE_MODE, SettingsCache.GameMode.classic).commit();
		}
		
		
		// Read the settings and configure the display 
		if (mGameSettings.getBoolean(SettingsCache.FREE_MODE, false))
			((ImageView)findViewById(R.id.ClassicGameMode)).setAlpha(0x4B);
		else 
			((ImageView)findViewById(R.id.FreeStyleGameMode)).setAlpha(0x4B);
		
		if (mGameSettings.getInt(SettingsCache.GAME_COLUMNS, 4) == 4)
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

		// Handlers for the 4x4 and 5x5 game boards
		((ImageView)findViewById(R.id.FourByFour)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ImageView)v).setAlpha(0xFF);
				((ImageView)findViewById(R.id.FiveByFive)).setAlpha(0x4B);
				
				mGameSettings.edit().putInt(SettingsCache.GAME_COLUMNS, SettingsCache.GameCols.fourbyfour).commit();
			}
		});
		((ImageView)findViewById(R.id.FiveByFive)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ImageView)v).setAlpha(0xFF);
				((ImageView)findViewById(R.id.FourByFour)).setAlpha(0x4B);
				
				mGameSettings.edit().putInt(SettingsCache.GAME_COLUMNS, SettingsCache.GameCols.fivebyfive).commit();
			}
		});		

		// Handlers for the classic / free mode game modes. 
		((ImageView)findViewById(R.id.ClassicGameMode)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ImageView)v).setAlpha(0xFF);
				((ImageView)findViewById(R.id.FreeStyleGameMode)).setAlpha(0x4B);
				
				mGameSettings.edit().putBoolean(SettingsCache.FREE_MODE, SettingsCache.GameMode.classic).commit();
			}
		});
		((ImageView)findViewById(R.id.FreeStyleGameMode)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				((ImageView)v).setAlpha(0xFF);
//				((ImageView)findViewById(R.id.ClassicGameMode)).setAlpha(0x4B);
//				
//				mGameSettings.edit().putBoolean(SettingsCache.FREE_MODE, SettingsCache.GameMode.freemode).commit();
			}
		});
	}
	
	private void saveSettingsAndFinish() {
		// Alert the user that the settings will take effect on next restart
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setCancelable(false);
		d.setMessage("Changes will take effect when you start a new game or restart the application!");
		d.setPositiveButton("Okay", new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mGameSettings = null;
				finish();
			}
		});
		d.show();
		d = null;
	}

	@Override
	public void onBackPressed() {
		// Prompt the user to save and or close
		saveSettingsAndFinish();
	}

}
