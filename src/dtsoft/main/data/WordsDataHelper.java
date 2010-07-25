package dtsoft.main.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import dtsoft.main.R;

public class WordsDataHelper {

	private static final String DATABASE_NAME = "WORDS";
	private static final int DATABASE_VERSION = 4;
	public static ArrayAdapter<String> WORDS_FOUND;
	
	private Context mContext;
	
	private HashMap<Integer, Integer> mWordListByLetter;

	private SQLiteDatabase mDatabase;
	private WordsOpenHelper mWordsHelper;
	
	public static final String FTS_DATABASE = "wordboggle";
	public static final String WORD_TABLE = "words";
	public static final String WORD = "word";
	public static final String WORD_SIZE = "word_size";
	public static final String WORD_TABLE_PREFIX = "word_len_";
	
	public WordsDataHelper(Context context) {
		mContext = context;
		this.initWordListMap();	
		this.mWordsHelper = new WordsOpenHelper(context);
	}
	
	private void dbOpen() {
		this.mDatabase = mWordsHelper.getWritableDatabase();
	}
	
	private void dbClose() {
		this.mDatabase.close();
	}
	
	public void destroy() {
		this.mDatabase.close();
		this.mDatabase = null;
	}
	
	private long insert(String word) {
		ContentValues cv = new ContentValues();
		cv.put(WORD, word);
		return this.mDatabase.insert(WORD_TABLE_PREFIX + word.length() + "_" + WORD_TABLE, null,cv);
	}
	
	// Test query
	private boolean testQuery(int length) {
		String tablePrefix = Integer.toString(length);
		Cursor cursor = this.mDatabase.query(WORD_TABLE_PREFIX + tablePrefix + "_" + WORD_TABLE, null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	public boolean selectWord(String word) {
		String tablePrefix = Integer.toString(word.length());
		String result = null;
		Cursor cursor = this.mDatabase.query(WORD_TABLE_PREFIX + tablePrefix + "_" + WORD_TABLE, new String[] {WORD}, WORD + "=?", new String[] {word}, null, null, null);
		
		if (cursor.moveToFirst())
			do {
				result = cursor.getString(0);
			} while (cursor.moveToNext());
		cursor.close();
		return (result == null)? false : true;
	}
	
	private void loadWordList(int length) {
		final Resources resources = mContext.getResources();

		InputStream inputStream = resources.openRawResource(getWordListResourceByLength(length));
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			String word;
			while ((word = reader.readLine()) != null) {
				if (insert(word) == -1) {
					Log.w("WordsDataHelper", "Failed to load word: " + word);
				}
				
			}
			reader.close();
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		} finally {
			reader = null;
			inputStream = null;
		}
	}
	
	private int getWordListResourceByLength(int length) {
		if (this.mWordListByLetter.containsKey(length))
			return this.mWordListByLetter.get(length);
		return -1;
	}
	
	public boolean getWordFromCache(String word, int length) {
		// Open the db
		dbOpen();
		boolean result = false;
		
		if (testQuery(length))
			result = selectWord(word);
		else {
			loadWordList(length);
			result =  selectWord(word);
		}
		
		dbClose();
		return result;
	}
	
	private void initWordListMap() {
		mWordListByLetter = new HashMap<Integer, Integer>();
		mWordListByLetter.put(2, R.raw.wordlen_2);
		mWordListByLetter.put(3, R.raw.wordlen_3);
		mWordListByLetter.put(4, R.raw.wordlen_4);
		mWordListByLetter.put(5, R.raw.wordlen_5);
		mWordListByLetter.put(6, R.raw.wordlen_6);
		mWordListByLetter.put(7, R.raw.wordlen_7);
		mWordListByLetter.put(8, R.raw.wordlen_8);
		mWordListByLetter.put(9, R.raw.wordlen_9);
		mWordListByLetter.put(10, R.raw.wordlen_10);
		mWordListByLetter.put(11, R.raw.wordlen_11);
		mWordListByLetter.put(12, R.raw.wordlen_12);
		mWordListByLetter.put(13, R.raw.wordlen_13);
		mWordListByLetter.put(14, R.raw.wordlen_14);
		mWordListByLetter.put(15, R.raw.wordlen_15);
	}

	private static class WordsOpenHelper extends SQLiteOpenHelper {

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
}