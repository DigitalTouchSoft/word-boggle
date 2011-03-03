package dtsoft.main.wordboggle.data;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import dtsoft.main.wordboggle.R;

public class WordsDataHelper {
	private byte[] mWords;
	private Context mContext;
	public static ArrayAdapter<String> WORDS_FOUND;
	
	public WordsDataHelper(Context context) {
		this.mContext = context;
		this.loadWords();
	}
	
	public boolean checkWord(String word) { 
		return true;
		//Arrays.binarySearch(mWords, word.hashCode());
	}

	private void loadWords() { 
			DataInputStream hashedWords = null;
			InputStream words = mContext.getResources().openRawResource(R.raw.sorted_hashwords);
			Log.i("WordsDataHelper", "Loading Sorted Dictionary! sortedhashwords.txt");
			int available = 0;
			try {
				available = words.available();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			BufferedInputStream br = new BufferedInputStream(words, available);
			
			//hashedWords = new DataInputStream(words);
			mWords = new byte[available];

			Log.i("WordsDataHelper", "Loading: "+ (mWords.length));
			Log.i("WordsDataHelper", "Loading the: hashed words files" );
			
			long start = System.currentTimeMillis();
			try {
				br.read(mWords);
			} catch (EOFException e) {
			} catch (IOException e) {
			}
			long end = System.currentTimeMillis();
			long diff = end - start;
			Log.i ("WordsDataHelper", "Took: " + diff + "m to complete");
			
			Log.i("WordsDataHelper", "Finished loading the sorted hash words!");

			try { 
				br.close();
			} catch (IOException e) {
				// Do nothing
			}
		}
	}

 /*	private static class WordsOpenHelper extends SQLiteOpenHelper {

		public WordsOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}
	

		@Override
		public void onCreate(SQLiteDatabase db) {

			// For each letter in the alphabet
			for (int i = 2; i < 16; i++) {
				Log.i("WordsOpenHelper", "Creating table" + WORD_TABLE_PREFIX + i + "_" + WORD_TABLE + "!");
				
				// Create the table for small words
				db.execSQL("CREATE TABLE " + WORD_TABLE_PREFIX + Integer.toString(i) + "_" + WORD_TABLE + " ("
						+ "ID INTEGER PRIMARY KEY,"
						+ WORD + " TEXT,"
						+ WORD_SIZE + " INTEGER"
						+ ");");
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("WordsOpenHelper", "Upgrading database, this will drop all tables and recreate");
			for (int i = 2; i < 16; i++)
				db.execSQL("DROP TABLE IF EXISTS " + WORD_TABLE_PREFIX + Integer.toString(i) + "_" + WORD_TABLE);
			onCreate(db);
		}}
}*/