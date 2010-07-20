package dtsoft.main.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Window;
import android.widget.ArrayAdapter;
import dtsoft.main.R;
import dtsoft.main.WordBoggle;

public class WordDatabase {

	private static final String DATABASE_NAME = "WORDS";
	private static final int DATABASE_VERSION = 1;
	public static ArrayAdapter<String> WORDS_FOUND;
	
	private Context mContext;
	private ArrayList<String> mShortWords;
	private ArrayList<String> mLongWords;
	
	private HashMap<String, Boolean> mInCache;
	private HashMap<String, Integer> mWordListByLetter;
	
	public void destroy() {
		mShortWords.clear();
		mLongWords.clear();
		mInCache.clear();
		mWordListByLetter.clear();
		
		mShortWords = null;
		mLongWords = null;
		mInCache = null;
		mWordListByLetter = null;
	}
	public WordDatabase(Context context) {
		mContext = context;
		mShortWords = new ArrayList<String>();
		mLongWords = new ArrayList<String>();
		mInCache = new HashMap<String, Boolean>();
		initWordListMap();
	}

	
	private void loadWordList(String firstLetter) {
		final Resources resources = mContext.getResources();

		InputStream inputStream = resources.openRawResource(getWordListResourceByLetter(firstLetter));
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			String word;
			while ((word = reader.readLine()) != null) {
				
				int length = word.length();
				if (length <= 6 && length >= 2) {
					mShortWords.add(word.toLowerCase().trim());					
				} else if (length > 6) {
					mLongWords.add(word.toLowerCase().trim());
				}
			}
			
			reader.close();
			mInCache.put(firstLetter, true);
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		} finally {
			((WordBoggle)mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, 10000);
			reader = null;
			inputStream = null;
		}
	}
	
	private int getWordListResourceByLetter(String firstLetter) {
		if (this.mWordListByLetter.containsKey(firstLetter.toUpperCase()))
			return this.mWordListByLetter.get(firstLetter);
		return -1;
	}
	
	public boolean getWordFromCache(String word, int length) {
		if (word == null) 
			return false;
		if (word.equals(""))
			return false;
		
		boolean found = false;
		// Try consuming the letter file for that for the specific word
		String firstLetter = String.valueOf(word.charAt(0));
		if (!mInCache.containsKey(firstLetter)) {
			loadWordList(firstLetter);
		}
		
		if (length <= 6 && length >= 2) {
			if (mShortWords.contains(word.toLowerCase().trim())) 
				found = true;					
		} else if (length > 6) {
			if (mLongWords.contains(word.toLowerCase().trim())) 
				found = true;
		}
		
		return found;
	}
	
	private void initWordListMap() {
		mWordListByLetter = new HashMap<String, Integer>();
		mWordListByLetter.put("A", R.raw.awords);
		mWordListByLetter.put("B", R.raw.bwords);
		mWordListByLetter.put("C", R.raw.cwords);
		mWordListByLetter.put("D", R.raw.dwords);
		mWordListByLetter.put("E", R.raw.ewords);
		mWordListByLetter.put("F", R.raw.fwords);
		mWordListByLetter.put("G", R.raw.gwords);
		mWordListByLetter.put("H", R.raw.hwords);
		mWordListByLetter.put("I", R.raw.iwords);
		mWordListByLetter.put("J", R.raw.jwords);
		mWordListByLetter.put("K", R.raw.kwords);
		mWordListByLetter.put("L", R.raw.lwords);
		mWordListByLetter.put("M", R.raw.mwords);
		mWordListByLetter.put("N", R.raw.nwords);
		mWordListByLetter.put("O", R.raw.owords);
		mWordListByLetter.put("P", R.raw.pwords);
		mWordListByLetter.put("Q", R.raw.qwords);
		mWordListByLetter.put("R", R.raw.rwords);
		mWordListByLetter.put("S", R.raw.swords);
		mWordListByLetter.put("T", R.raw.twords);
		mWordListByLetter.put("U", R.raw.uwords);
		mWordListByLetter.put("V", R.raw.vwords);
		mWordListByLetter.put("W", R.raw.wwords);
		mWordListByLetter.put("X", R.raw.xwords);
		mWordListByLetter.put("Y", R.raw.ywords);
		mWordListByLetter.put("Z", R.raw.zwords);
	}
	
	public static final String FTS_DATABASE = "wordboggle";
	public static final String LARGE_WORD_TABLE = "large_word_table";
	public static final String SMALL_WORD_TABLE = "small_word_table";
	
	private static class WordOpenHelper extends SQLiteOpenHelper {

		public WordOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

	

		@Override
		public void onCreate(SQLiteDatabase db) {
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}}
}