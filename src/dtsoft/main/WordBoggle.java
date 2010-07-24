package dtsoft.main;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import dtsoft.main.data.WordDatabase;
import dtsoft.main.data.cache.SettingsCache;
import dtsoft.main.util.BoardGameActions;
import dtsoft.main.util.GameUtils;
import dtsoft.main.util.sound.AudioProvider;
import dtsoft.main.view.BoardGamePiece;
import dtsoft.main.view.adapter.BoardGameAdapter;
import dtsoft.main.view.listeners.Listeners;

public class WordBoggle extends Activity {
	private	ArrayList<Integer> mAlreadyClicked = new ArrayList<Integer>();
    private BoardGameAdapter mBoardGameAdapter;
	private Button mCancelWord;
	private TextView mScoreBoard;
	private TextView mWordTracker;
	private BoardGameActions mBoardGameActions;
	private WordDatabase mWordDatabase;
	private ArrayAdapter<String> mWordsInBank;
	private AudioProvider mAudioProvider;
	private GameUtils mGameUtils;

	// Menus
	private static final int WORD_LIST_MENU_ITEM = Menu.FIRST;
	private static final int NEW_GAME_MENU_ITEM = Menu.FIRST + 1;
	private static final int SETTINGS_MENU_ITEM = Menu.FIRST + 2;
	private static final int ABOUT_MENU_ITEM = Menu.FIRST + 3;
	
	// Activities 
	private static final int WORD_LIST_ACTIVITY = 0;
	private static final int ABOUT_INFO_ACTIVITY = 1;
	private static final int SETTINGS_ACTIVITY = 2;
	
	// Dialogs 
	private static final int NEW_GAME_DIALOG = 0;
	
	public ArrayList<Integer> getAlreadyClicked() {
		return mAlreadyClicked;
	}
	public void setAlreadyClicked(ArrayList<Integer> alreadyClicked) {
		mAlreadyClicked = alreadyClicked;
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.initSettings();
        this.init();
    }    
    
    /** 
     * is called part of onCreate() to setup the required components. 
     */
	public void init() {
        mCancelWord = (Button) findViewById(R.id.CancelWord);
        mScoreBoard = (TextView) findViewById(R.id.ScoreBoard);
        
    	mWordDatabase = new WordDatabase(this);

    	initBoardGame();
    	initWordBank();
    	
        mCancelWord.setOnClickListener(Listeners.getInstance().cancelWordClick());

        mWordTracker = (TextView) findViewById(R.id.WordTracker);
        mWordTracker.setOnClickListener(Listeners.getInstance().submitWordClick());
        
        mScoreBoard.setText("0");
	}
	
	private void initSettings() {
		SettingsCache sc = new SettingsCache(super.getCacheDir().getPath() + File.pathSeparator + SettingsCache.SETTINGS_FILE);
		mGameUtils = new GameUtils(sc.getSettings());
		sc = null;
	}
	
	public void initBoardGame() {
		mBoardGameActions = null;
		mBoardGameAdapter = null;
		
		mBoardGameAdapter = new BoardGameAdapter(this);
		mBoardGameActions = new BoardGameActions(this);
		mBoardGameAdapter.validateGameBoard();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.initSettings();
		mAudioProvider = new AudioProvider(this);
		mWordDatabase = new WordDatabase(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		this.mAudioProvider = null;
		this.mWordDatabase = null;
		this.mGameUtils = null;
		
		int totalItems = this.mBoardGameAdapter.getCount();
		for (int i = 0; i < totalItems; i++) {
			BoardGamePiece bgp = this.mBoardGameAdapter.getChildAt(i);
			
			if (bgp != null) {
				bgp.recyle();
			}
		}
	}
	
	public  void initWordBank() {
		mWordsInBank = new ArrayAdapter<String>(this, R.layout.listitem);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem settings,
				newGame,
				currentWords,
				about; 
		
		currentWords = menu.add(0, WORD_LIST_MENU_ITEM, 1, "Words Found");
		newGame = menu.add(0, NEW_GAME_MENU_ITEM, 2, "New Game");
		settings = menu.add(0, SETTINGS_MENU_ITEM, 3, "Settings");
		about = menu.add(0, ABOUT_MENU_ITEM, 4, "About");
		
		settings.setIcon(R.drawable.ic_menu_preferences);
		newGame.setIcon(R.drawable.ic_menu_add);
		currentWords.setIcon(R.drawable.ic_menu_sort_alphabetically);
		about.setIcon(R.drawable.ic_menu_info_details);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case NEW_GAME_MENU_ITEM:
			this.getDialog(NEW_GAME_DIALOG);
			break;
		case SETTINGS_MENU_ITEM:
			startActivity(SETTINGS_ACTIVITY);
			break;
		case WORD_LIST_MENU_ITEM:
			WordDatabase.WORDS_FOUND = this.getWordsInBank();
			startActivity(WORD_LIST_ACTIVITY);
			break;
		case ABOUT_MENU_ITEM:
			startActivity(ABOUT_INFO_ACTIVITY);
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void startActivity(int activity) {
		Intent newActivity;
		switch (activity) {
		case WORD_LIST_ACTIVITY:
			newActivity = new Intent(this, WordsFoundList.class);
			break;
		case ABOUT_INFO_ACTIVITY:
			newActivity = new Intent(this, AboutInfo.class);
			break;
		case SETTINGS_ACTIVITY:
			newActivity = new Intent(this, GameSettings.class);
			break;
			default:
				newActivity = new Intent();
		}
				
		startActivityForResult(newActivity, 0);	
	}
	
	private boolean getDialog(int dialog) {
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		switch(dialog) {
		case NEW_GAME_DIALOG:
			d.setMessage("Are you sure you want to start a new game?");
			d.setPositiveButton("Yes", new DialogListeners().newGameClick());
			d.setNegativeButton("No", null);
			d.show();
			break;
		default:			
			break;
		}
		
		return false;
	}
	
	public BoardGameActions getBoardGameActions() {
		return mBoardGameActions;
	}
    
	public TextView getScoreBoard() {
		return mScoreBoard;
	}
	
	public TextView getWordTracker() {
		return mWordTracker;
	}
	
	public BoardGameAdapter getGameBoard() {
		return mBoardGameAdapter;
	}
	
	public WordDatabase getWordDatabase() {
		return mWordDatabase;
	}
	
	public ArrayAdapter<String> getWordsInBank() {
		return mWordsInBank;
	}
	
	public Resources getGameResources() {
		return getResources();
	} 
	
	public AudioProvider getAudioProvider() {
		return mAudioProvider;
	}
	public GameUtils getGameUtils() {
		return mGameUtils;
	}
	
	public class DialogListeners {
		public DialogInterface.OnClickListener newGameClick() {

			return new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					// Destroy the current board
					initBoardGame();
					getScoreBoard().setText("0");
					getWordsInBank().clear();
				}
			};
		}
	}
}