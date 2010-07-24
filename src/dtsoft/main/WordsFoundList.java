package dtsoft.main;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import dtsoft.main.data.WordDatabase;

public class WordsFoundList extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.wordlist);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        ListView lv = (ListView) findViewById(R.id.WordBank);
        if (WordDatabase.WORDS_FOUND != null) {
        	lv.setAdapter(WordDatabase.WORDS_FOUND);
        }
        
        ((Button)findViewById(R.id.WordListBack)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
        });
	}
	
}

