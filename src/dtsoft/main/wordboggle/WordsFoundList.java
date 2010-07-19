package dtsoft.main.wordboggle;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.ListView;
import dtsoft.main.wordboggle.data.WordDatabase;

public class WordsFoundList extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.wordlist);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        ListView lv = (ListView) findViewById(R.id.WordBank);
        if (WordDatabase.WORDS_FOUND != null) {
        	lv.setAdapter(WordDatabase.WORDS_FOUND);
        }
	}
	
}
