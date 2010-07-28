package dtsoft.main.wordboggle.data;

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
import dtsoft.main.wordboggle.R;

public class WordsDataHelper {

	private static final String DATABASE_NAME = "WORDS";
	private static final int DATABASE_VERSION = 7;
	public static ArrayAdapter<String> WORDS_FOUND;
	
	private Context mContext;
	
	private HashMap<String, Integer> mWordListByLetter;

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
	
	/**
	 * Opens the database - should always be called before interacting with the db
	 */
	private void dbOpen() {
		this.mDatabase = mWordsHelper.getWritableDatabase();
	}
	
	/**
	 * Closes the database - should always be called when finished with the database
	 */
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
	
	/**
	 * Queries the database to determine if there are any records starting with the letter supplied
	 * If there are none then it will attempt to load that letter file
	 * If there is then it will assume the letter file has been loaded 
	 * @param firstLetter
	 * @param length
	 * @return
	 * Return true or false if words like this letter exist
	 */
	private boolean testQuery(String firstLetter, int length) {
		String tablePrefix = Integer.toString(length);
		
		Cursor cursor = this.mDatabase.query(
				WORD_TABLE_PREFIX + tablePrefix + "_" + WORD_TABLE, 	// What table to check
				null, 
				WORD + " like \"" + firstLetter + "%\"",				// Where clause 
				null, 
				null, 
				null, 
				null);
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
		Cursor cursor = this.mDatabase.query(
				WORD_TABLE_PREFIX + tablePrefix + "_" + WORD_TABLE, 
				new String[] {WORD}, 
				WORD + " like \""+word+"\"", 
				null, 
				null, 
				null, 
				null);
		
		if (cursor.moveToFirst())
			do {
				result = cursor.getString(0);
			} while (cursor.moveToNext());
		cursor.close();
		return (result == null)? false : true;
	}
	
	private void loadWordList(String firstLetter, int length) {
		final Resources resources = mContext.getResources();
		int resource = getWordListResourceByLength(firstLetter, length);
		
		// If we didn't get a damn resource...
		if (resource == -1)
			return;
		
		InputStream inputStream = resources.openRawResource(resource);
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
	
	private int getWordListResourceByLength(String firstLetter, int length) {
		if (this.mWordListByLetter.containsKey(firstLetter.toLowerCase() + "_" + length))
			return this.mWordListByLetter.get(firstLetter.toLowerCase() + "_" + length);
		return -1;
	}
	
	public boolean getWordFromCache(String word) {
		// Open the db
		dbOpen();
		int length = word.length();
		String firstLetter = Character.toString(word.charAt(0));
		boolean result = false;
		
		if (testQuery(firstLetter, length))
			result = selectWord(word);
		else {
			loadWordList(firstLetter, length);
			result =  selectWord(word);
		}
		
		dbClose();
		return result;
	}
	
	private void initWordListMap() {
		mWordListByLetter = new HashMap<String, Integer>();
		mWordListByLetter.put("h_4", R.raw.h_4);
		mWordListByLetter.put("h_3", R.raw.h_3);
		mWordListByLetter.put("h_6", R.raw.h_6);
		mWordListByLetter.put("h_5", R.raw.h_5);
		mWordListByLetter.put("h_2", R.raw.h_2);
		mWordListByLetter.put("h_13", R.raw.h_13);
		mWordListByLetter.put("h_12", R.raw.h_12);
		mWordListByLetter.put("h_15", R.raw.h_15);
		mWordListByLetter.put("h_14", R.raw.h_14);
		mWordListByLetter.put("h_11", R.raw.h_11);
		mWordListByLetter.put("h_10", R.raw.h_10);
		mWordListByLetter.put("d_9", R.raw.d_9);
		mWordListByLetter.put("d_7", R.raw.d_7);
		mWordListByLetter.put("d_8", R.raw.d_8);
		mWordListByLetter.put("d_5", R.raw.d_5);
		mWordListByLetter.put("d_6", R.raw.d_6);
		mWordListByLetter.put("d_3", R.raw.d_3);
		mWordListByLetter.put("d_4", R.raw.d_4);
		mWordListByLetter.put("d_2", R.raw.d_2);
		mWordListByLetter.put("h_9", R.raw.h_9);
		mWordListByLetter.put("h_7", R.raw.h_7);
		mWordListByLetter.put("h_8", R.raw.h_8);
		mWordListByLetter.put("c_9", R.raw.c_9);
		mWordListByLetter.put("c_8", R.raw.c_8);
		mWordListByLetter.put("c_5", R.raw.c_5);
		mWordListByLetter.put("c_4", R.raw.c_4);
		mWordListByLetter.put("c_7", R.raw.c_7);
		mWordListByLetter.put("c_6", R.raw.c_6);
		mWordListByLetter.put("c_3", R.raw.c_3);
		mWordListByLetter.put("c_2", R.raw.c_2);
		mWordListByLetter.put("p_7", R.raw.p_7);
		mWordListByLetter.put("a_2", R.raw.a_2);
		mWordListByLetter.put("p_8", R.raw.p_8);
		mWordListByLetter.put("a_3", R.raw.a_3);
		mWordListByLetter.put("p_9", R.raw.p_9);
		mWordListByLetter.put("a_4", R.raw.a_4);
		mWordListByLetter.put("a_5", R.raw.a_5);
		mWordListByLetter.put("l_2", R.raw.l_2);
		mWordListByLetter.put("p_2", R.raw.p_2);
		mWordListByLetter.put("p_3", R.raw.p_3);
		mWordListByLetter.put("a_6", R.raw.a_6);
		mWordListByLetter.put("p_4", R.raw.p_4);
		mWordListByLetter.put("a_7", R.raw.a_7);
		mWordListByLetter.put("p_5", R.raw.p_5);
		mWordListByLetter.put("a_8", R.raw.a_8);
		mWordListByLetter.put("p_6", R.raw.p_6);
		mWordListByLetter.put("a_9", R.raw.a_9);
		mWordListByLetter.put("l_6", R.raw.l_6);
		mWordListByLetter.put("l_5", R.raw.l_5);
		mWordListByLetter.put("l_4", R.raw.l_4);
		mWordListByLetter.put("l_3", R.raw.l_3);
		mWordListByLetter.put("l_9", R.raw.l_9);
		mWordListByLetter.put("s_15", R.raw.s_15);
		mWordListByLetter.put("l_8", R.raw.l_8);
		mWordListByLetter.put("l_7", R.raw.l_7);
		mWordListByLetter.put("y_12", R.raw.y_12);
		mWordListByLetter.put("s_14", R.raw.s_14);
		mWordListByLetter.put("y_11", R.raw.y_11);
		mWordListByLetter.put("s_13", R.raw.s_13);
		mWordListByLetter.put("y_10", R.raw.y_10);
		mWordListByLetter.put("s_12", R.raw.s_12);
		mWordListByLetter.put("s_11", R.raw.s_11);
		mWordListByLetter.put("z_12", R.raw.z_12);
		mWordListByLetter.put("s_10", R.raw.s_10);
		mWordListByLetter.put("z_13", R.raw.z_13);
		mWordListByLetter.put("z_10", R.raw.z_10);
		mWordListByLetter.put("z_11", R.raw.z_11);
		mWordListByLetter.put("g_10", R.raw.g_10);
		mWordListByLetter.put("g_11", R.raw.g_11);
		mWordListByLetter.put("g_12", R.raw.g_12);
		mWordListByLetter.put("z_14", R.raw.z_14);
		mWordListByLetter.put("g_13", R.raw.g_13);
		mWordListByLetter.put("z_15", R.raw.z_15);
		mWordListByLetter.put("g_14", R.raw.g_14);
		mWordListByLetter.put("g_15", R.raw.g_15);
		mWordListByLetter.put("y_15", R.raw.y_15);
		mWordListByLetter.put("y_14", R.raw.y_14);
		mWordListByLetter.put("y_13", R.raw.y_13);
		mWordListByLetter.put("i_11", R.raw.i_11);
		mWordListByLetter.put("i_12", R.raw.i_12);
		mWordListByLetter.put("i_10", R.raw.i_10);
		mWordListByLetter.put("w_10", R.raw.w_10);
		mWordListByLetter.put("w_12", R.raw.w_12);
		mWordListByLetter.put("t_14", R.raw.t_14);
		mWordListByLetter.put("w_11", R.raw.w_11);
		mWordListByLetter.put("t_15", R.raw.t_15);
		mWordListByLetter.put("w_14", R.raw.w_14);
		mWordListByLetter.put("t_12", R.raw.t_12);
		mWordListByLetter.put("w_13", R.raw.w_13);
		mWordListByLetter.put("t_13", R.raw.t_13);
		mWordListByLetter.put("t_10", R.raw.t_10);
		mWordListByLetter.put("i_15", R.raw.i_15);
		mWordListByLetter.put("w_15", R.raw.w_15);
		mWordListByLetter.put("t_11", R.raw.t_11);
		mWordListByLetter.put("i_13", R.raw.i_13);
		mWordListByLetter.put("i_14", R.raw.i_14);
		mWordListByLetter.put("x_11", R.raw.x_11);
		mWordListByLetter.put("x_10", R.raw.x_10);
		mWordListByLetter.put("x_15", R.raw.x_15);
		mWordListByLetter.put("x_14", R.raw.x_14);
		mWordListByLetter.put("x_13", R.raw.x_13);
		mWordListByLetter.put("x_12", R.raw.x_12);
		mWordListByLetter.put("o_10", R.raw.o_10);
		mWordListByLetter.put("m_14", R.raw.m_14);
		mWordListByLetter.put("m_13", R.raw.m_13);
		mWordListByLetter.put("m_15", R.raw.m_15);
		mWordListByLetter.put("m_10", R.raw.m_10);
		mWordListByLetter.put("m_12", R.raw.m_12);
		mWordListByLetter.put("m_11", R.raw.m_11);
		mWordListByLetter.put("o_14", R.raw.o_14);
		mWordListByLetter.put("o_13", R.raw.o_13);
		mWordListByLetter.put("o_12", R.raw.o_12);
		mWordListByLetter.put("o_11", R.raw.o_11);
		mWordListByLetter.put("o_15", R.raw.o_15);
		mWordListByLetter.put("b_5", R.raw.b_5);
		mWordListByLetter.put("b_6", R.raw.b_6);
		mWordListByLetter.put("b_7", R.raw.b_7);
		mWordListByLetter.put("b_8", R.raw.b_8);
		mWordListByLetter.put("b_9", R.raw.b_9);
		mWordListByLetter.put("q_11", R.raw.q_11);
		mWordListByLetter.put("p_13", R.raw.p_13);
		mWordListByLetter.put("q_12", R.raw.q_12);
		mWordListByLetter.put("p_12", R.raw.p_12);
		mWordListByLetter.put("p_15", R.raw.p_15);
		mWordListByLetter.put("q_10", R.raw.q_10);
		mWordListByLetter.put("p_14", R.raw.p_14);
		mWordListByLetter.put("b_2", R.raw.b_2);
		mWordListByLetter.put("b_3", R.raw.b_3);
		mWordListByLetter.put("b_4", R.raw.b_4);
		mWordListByLetter.put("q_15", R.raw.q_15);
		mWordListByLetter.put("q_14", R.raw.q_14);
		mWordListByLetter.put("p_10", R.raw.p_10);
		mWordListByLetter.put("q_13", R.raw.q_13);
		mWordListByLetter.put("p_11", R.raw.p_11);
		mWordListByLetter.put("l_15", R.raw.l_15);
		mWordListByLetter.put("l_14", R.raw.l_14);
		mWordListByLetter.put("l_13", R.raw.l_13);
		mWordListByLetter.put("l_12", R.raw.l_12);
		mWordListByLetter.put("l_11", R.raw.l_11);
		mWordListByLetter.put("l_10", R.raw.l_10);
		mWordListByLetter.put("n_14", R.raw.n_14);
		mWordListByLetter.put("n_15", R.raw.n_15);
		mWordListByLetter.put("n_12", R.raw.n_12);
		mWordListByLetter.put("n_13", R.raw.n_13);
		mWordListByLetter.put("n_10", R.raw.n_10);
		mWordListByLetter.put("n_11", R.raw.n_11);
		mWordListByLetter.put("f_9", R.raw.f_9);
		mWordListByLetter.put("e_15", R.raw.e_15);
		mWordListByLetter.put("e_13", R.raw.e_13);
		mWordListByLetter.put("e_14", R.raw.e_14);
		mWordListByLetter.put("e_11", R.raw.e_11);
		mWordListByLetter.put("e_12", R.raw.e_12);
		mWordListByLetter.put("e_10", R.raw.e_10);
		mWordListByLetter.put("j_6", R.raw.j_6);
		mWordListByLetter.put("j_5", R.raw.j_5);
		mWordListByLetter.put("j_8", R.raw.j_8);
		mWordListByLetter.put("j_7", R.raw.j_7);
		mWordListByLetter.put("j_9", R.raw.j_9);
		mWordListByLetter.put("a_15", R.raw.a_15);
		mWordListByLetter.put("a_14", R.raw.a_14);
		mWordListByLetter.put("a_13", R.raw.a_13);
		mWordListByLetter.put("c_15", R.raw.c_15);
		mWordListByLetter.put("j_3", R.raw.j_3);
		mWordListByLetter.put("j_4", R.raw.j_4);
		mWordListByLetter.put("j_2", R.raw.j_2);
		mWordListByLetter.put("f_6", R.raw.f_6);
		mWordListByLetter.put("f_5", R.raw.f_5);
		mWordListByLetter.put("v_15", R.raw.v_15);
		mWordListByLetter.put("f_8", R.raw.f_8);
		mWordListByLetter.put("v_14", R.raw.v_14);
		mWordListByLetter.put("f_7", R.raw.f_7);
		mWordListByLetter.put("v_13", R.raw.v_13);
		mWordListByLetter.put("f_2", R.raw.f_2);
		mWordListByLetter.put("v_12", R.raw.v_12);
		mWordListByLetter.put("v_11", R.raw.v_11);
		mWordListByLetter.put("f_4", R.raw.f_4);
		mWordListByLetter.put("v_10", R.raw.v_10);
		mWordListByLetter.put("f_3", R.raw.f_3);
		mWordListByLetter.put("z_2", R.raw.z_2);
		mWordListByLetter.put("w_7", R.raw.w_7);
		mWordListByLetter.put("w_6", R.raw.w_6);
		mWordListByLetter.put("z_4", R.raw.z_4);
		mWordListByLetter.put("w_5", R.raw.w_5);
		mWordListByLetter.put("z_3", R.raw.z_3);
		mWordListByLetter.put("w_4", R.raw.w_4);
		mWordListByLetter.put("w_3", R.raw.w_3);
		mWordListByLetter.put("w_2", R.raw.w_2);
		mWordListByLetter.put("x_2", R.raw.x_2);
		mWordListByLetter.put("b_15", R.raw.b_15);
		mWordListByLetter.put("b_14", R.raw.b_14);
		mWordListByLetter.put("v_9", R.raw.v_9);
		mWordListByLetter.put("y_3", R.raw.y_3);
		mWordListByLetter.put("x_6", R.raw.x_6);
		mWordListByLetter.put("y_2", R.raw.y_2);
		mWordListByLetter.put("x_5", R.raw.x_5);
		mWordListByLetter.put("y_5", R.raw.y_5);
		mWordListByLetter.put("x_4", R.raw.x_4);
		mWordListByLetter.put("y_4", R.raw.y_4);
		mWordListByLetter.put("x_3", R.raw.x_3);
		mWordListByLetter.put("y_6", R.raw.y_6);
		mWordListByLetter.put("x_9", R.raw.x_9);
		mWordListByLetter.put("v_3", R.raw.v_3);
		mWordListByLetter.put("f_14", R.raw.f_14);
		mWordListByLetter.put("y_7", R.raw.y_7);
		mWordListByLetter.put("v_4", R.raw.v_4);
		mWordListByLetter.put("f_15", R.raw.f_15);
		mWordListByLetter.put("y_8", R.raw.y_8);
		mWordListByLetter.put("x_7", R.raw.x_7);
		mWordListByLetter.put("y_9", R.raw.y_9);
		mWordListByLetter.put("x_8", R.raw.x_8);
		mWordListByLetter.put("v_7", R.raw.v_7);
		mWordListByLetter.put("f_10", R.raw.f_10);
		mWordListByLetter.put("v_8", R.raw.v_8);
		mWordListByLetter.put("f_11", R.raw.f_11);
		mWordListByLetter.put("v_5", R.raw.v_5);
		mWordListByLetter.put("f_12", R.raw.f_12);
		mWordListByLetter.put("v_6", R.raw.v_6);
		mWordListByLetter.put("f_13", R.raw.f_13);
		mWordListByLetter.put("z_9", R.raw.z_9);
		mWordListByLetter.put("u_8", R.raw.u_8);
		mWordListByLetter.put("d_14", R.raw.d_14);
		mWordListByLetter.put("u_9", R.raw.u_9);
		mWordListByLetter.put("d_15", R.raw.d_15);
		mWordListByLetter.put("u_6", R.raw.u_6);
		mWordListByLetter.put("d_12", R.raw.d_12);
		mWordListByLetter.put("u_7", R.raw.u_7);
		mWordListByLetter.put("d_13", R.raw.d_13);
		mWordListByLetter.put("z_5", R.raw.z_5);
		mWordListByLetter.put("u_4", R.raw.u_4);
		mWordListByLetter.put("d_10", R.raw.d_10);
		mWordListByLetter.put("z_6", R.raw.z_6);
		mWordListByLetter.put("u_5", R.raw.u_5);
		mWordListByLetter.put("d_11", R.raw.d_11);
		mWordListByLetter.put("z_7", R.raw.z_7);
		mWordListByLetter.put("w_8", R.raw.w_8);
		mWordListByLetter.put("u_2", R.raw.u_2);
		mWordListByLetter.put("z_8", R.raw.z_8);
		mWordListByLetter.put("w_9", R.raw.w_9);
		mWordListByLetter.put("u_3", R.raw.u_3);
		mWordListByLetter.put("a_10", R.raw.a_10);
		mWordListByLetter.put("n_2", R.raw.n_2);
		mWordListByLetter.put("a_11", R.raw.a_11);
		mWordListByLetter.put("n_3", R.raw.n_3);
		mWordListByLetter.put("a_12", R.raw.a_12);
		mWordListByLetter.put("n_4", R.raw.n_4);
		mWordListByLetter.put("n_5", R.raw.n_5);
		mWordListByLetter.put("n_6", R.raw.n_6);
		mWordListByLetter.put("n_7", R.raw.n_7);
		mWordListByLetter.put("n_8", R.raw.n_8);
		mWordListByLetter.put("n_9", R.raw.n_9);
		mWordListByLetter.put("b_12", R.raw.b_12);
		mWordListByLetter.put("b_13", R.raw.b_13);
		mWordListByLetter.put("b_10", R.raw.b_10);
		mWordListByLetter.put("b_11", R.raw.b_11);
		mWordListByLetter.put("g_9", R.raw.g_9);
		mWordListByLetter.put("g_8", R.raw.g_8);
		mWordListByLetter.put("i_7", R.raw.i_7);
		mWordListByLetter.put("i_6", R.raw.i_6);
		mWordListByLetter.put("i_9", R.raw.i_9);
		mWordListByLetter.put("i_8", R.raw.i_8);
		mWordListByLetter.put("k_8", R.raw.k_8);
		mWordListByLetter.put("k_9", R.raw.k_9);
		mWordListByLetter.put("k_4", R.raw.k_4);
		mWordListByLetter.put("k_15", R.raw.k_15);
		mWordListByLetter.put("k_5", R.raw.k_5);
		mWordListByLetter.put("k_6", R.raw.k_6);
		mWordListByLetter.put("k_7", R.raw.k_7);
		mWordListByLetter.put("k_11", R.raw.k_11);
		mWordListByLetter.put("k_12", R.raw.k_12);
		mWordListByLetter.put("k_13", R.raw.k_13);
		mWordListByLetter.put("k_14", R.raw.k_14);
		mWordListByLetter.put("k_10", R.raw.k_10);
		mWordListByLetter.put("e_3", R.raw.e_3);
		mWordListByLetter.put("j_14", R.raw.j_14);
		mWordListByLetter.put("e_2", R.raw.e_2);
		mWordListByLetter.put("j_15", R.raw.j_15);
		mWordListByLetter.put("e_5", R.raw.e_5);
		mWordListByLetter.put("r_9", R.raw.r_9);
		mWordListByLetter.put("e_4", R.raw.e_4);
		mWordListByLetter.put("r_8", R.raw.r_8);
		mWordListByLetter.put("q_5", R.raw.q_5);
		mWordListByLetter.put("e_7", R.raw.e_7);
		mWordListByLetter.put("r_7", R.raw.r_7);
		mWordListByLetter.put("q_4", R.raw.q_4);
		mWordListByLetter.put("e_6", R.raw.e_6);
		mWordListByLetter.put("r_6", R.raw.r_6);
		mWordListByLetter.put("q_3", R.raw.q_3);
		mWordListByLetter.put("e_9", R.raw.e_9);
		mWordListByLetter.put("r_5", R.raw.r_5);
		mWordListByLetter.put("q_2", R.raw.q_2);
		mWordListByLetter.put("e_8", R.raw.e_8);
		mWordListByLetter.put("r_4", R.raw.r_4);
		mWordListByLetter.put("q_9", R.raw.q_9);
		mWordListByLetter.put("k_3", R.raw.k_3);
		mWordListByLetter.put("r_3", R.raw.r_3);
		mWordListByLetter.put("q_8", R.raw.q_8);
		mWordListByLetter.put("k_2", R.raw.k_2);
		mWordListByLetter.put("r_2", R.raw.r_2);
		mWordListByLetter.put("q_7", R.raw.q_7);
		mWordListByLetter.put("q_6", R.raw.q_6);
		mWordListByLetter.put("j_10", R.raw.j_10);
		mWordListByLetter.put("j_11", R.raw.j_11);
		mWordListByLetter.put("j_12", R.raw.j_12);
		mWordListByLetter.put("j_13", R.raw.j_13);
		mWordListByLetter.put("s_6", R.raw.s_6);
		mWordListByLetter.put("i_4", R.raw.i_4);
		mWordListByLetter.put("t_2", R.raw.t_2);
		mWordListByLetter.put("s_7", R.raw.s_7);
		mWordListByLetter.put("i_5", R.raw.i_5);
		mWordListByLetter.put("s_4", R.raw.s_4);
		mWordListByLetter.put("i_2", R.raw.i_2);
		mWordListByLetter.put("s_5", R.raw.s_5);
		mWordListByLetter.put("i_3", R.raw.i_3);
		mWordListByLetter.put("s_8", R.raw.s_8);
		mWordListByLetter.put("s_9", R.raw.s_9);
		mWordListByLetter.put("t_9", R.raw.t_9);
		mWordListByLetter.put("t_7", R.raw.t_7);
		mWordListByLetter.put("g_2", R.raw.g_2);
		mWordListByLetter.put("t_8", R.raw.t_8);
		mWordListByLetter.put("g_3", R.raw.g_3);
		mWordListByLetter.put("t_5", R.raw.t_5);
		mWordListByLetter.put("s_2", R.raw.s_2);
		mWordListByLetter.put("g_4", R.raw.g_4);
		mWordListByLetter.put("t_6", R.raw.t_6);
		mWordListByLetter.put("s_3", R.raw.s_3);
		mWordListByLetter.put("g_5", R.raw.g_5);
		mWordListByLetter.put("t_3", R.raw.t_3);
		mWordListByLetter.put("g_6", R.raw.g_6);
		mWordListByLetter.put("t_4", R.raw.t_4);
		mWordListByLetter.put("g_7", R.raw.g_7);
		mWordListByLetter.put("r_11", R.raw.r_11);
		mWordListByLetter.put("r_10", R.raw.r_10);
		mWordListByLetter.put("r_13", R.raw.r_13);
		mWordListByLetter.put("r_12", R.raw.r_12);
		mWordListByLetter.put("c_13", R.raw.c_13);
		mWordListByLetter.put("c_14", R.raw.c_14);
		mWordListByLetter.put("c_11", R.raw.c_11);
		mWordListByLetter.put("c_12", R.raw.c_12);
		mWordListByLetter.put("c_10", R.raw.c_10);
		mWordListByLetter.put("r_14", R.raw.r_14);
		mWordListByLetter.put("r_15", R.raw.r_15);
		mWordListByLetter.put("u_13", R.raw.u_13);
		mWordListByLetter.put("o_5", R.raw.o_5);
		mWordListByLetter.put("u_14", R.raw.u_14);
		mWordListByLetter.put("o_4", R.raw.o_4);
		mWordListByLetter.put("u_15", R.raw.u_15);
		mWordListByLetter.put("o_7", R.raw.o_7);
		mWordListByLetter.put("o_6", R.raw.o_6);
		mWordListByLetter.put("u_10", R.raw.u_10);
		mWordListByLetter.put("u_11", R.raw.u_11);
		mWordListByLetter.put("o_3", R.raw.o_3);
		mWordListByLetter.put("u_12", R.raw.u_12);
		mWordListByLetter.put("o_2", R.raw.o_2);
		mWordListByLetter.put("o_9", R.raw.o_9);
		mWordListByLetter.put("o_8", R.raw.o_8);
		mWordListByLetter.put("m_6", R.raw.m_6);
		mWordListByLetter.put("m_7", R.raw.m_7);
		mWordListByLetter.put("m_8", R.raw.m_8);
		mWordListByLetter.put("m_9", R.raw.m_9);
		mWordListByLetter.put("m_2", R.raw.m_2);
		mWordListByLetter.put("m_3", R.raw.m_3);
		mWordListByLetter.put("m_4", R.raw.m_4);
		mWordListByLetter.put("m_5", R.raw.m_5);

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