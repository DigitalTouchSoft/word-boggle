package dtsoft.main.wordboggle.data.cache;


/**
 * This class is used for reading / writing the settings
 * 
 * @author Michael
 * 
 */
public class SettingsCache {
	/**
	 * The number of game columns - valid 4 or 5
	 */
	public static final String GAME_COLUMNS = "gamecolumns";

	public static class GameCols {
		public static final int fourbyfour = 4;
		public static final int fivebyfive = 5;
	}

	/**
	 * The mode for the game, free mode or classic mode (assume class if
	 * freemode is false)
	 */
	public static final String FREE_MODE = "freemode";

	public static class GameMode {
		public static final boolean freemode = false;
		public static final boolean classic = false;
	}
	
	/**
	 * Whether or not the word dictionary has been loaded since last launch
	 */
	public static final String LOAD_DICTIONARY = "loaddictionary";
}
