package dtsoft.main.util;

import java.util.Random;

import dtsoft.main.data.cache.SettingsCache.Settings;
import dtsoft.main.view.BoardGamePiece;
import dtsoft.main.view.adapter.BoardGameAdapter;


public class GameUtils {
	private Settings mSettings;
	public GameUtils(Settings settings) {
		mRand = new Random();
		mSettings = settings;
	}
	
	public int getGameCols() {
		return mSettings.gameColumns;
	}
	public int getGamePieces() {
		return (int) Math.pow(mSettings.gameColumns, 2);
	}
	
	
	public  int[] activateGamePieces(int gamePiece) {
		int top,
			right,
			bottom,
			left,
			topRight,
			topLeft,
			bottomRight,
			bottomLeft;
		
		// Determine what is above
		top = (gamePiece - getGameCols());
		topRight = top + 1;
		topLeft = top - 1;
		
		if ((topLeft - (getGameCols() - 1)) % getGameCols() == 0)
			topLeft = -1;
		if (topRight % getGameCols() == 0)
			topRight = -1;
		
		// Determine what is to the right
		right = (gamePiece + 1);
		if (right % getGameCols() == 0) {
			// a left most piece
			right = -1;
		}
		
		// Determine what is so the bottom
		bottom = (gamePiece + getGameCols());
		bottomLeft = bottom - 1;
		bottomRight = bottom + 1;
		
		if ((bottomLeft - (getGameCols() - 1)) % getGameCols() == 0 )
			bottomLeft = -1;
		if (bottomRight % getGameCols() == 0)
			bottomRight = -1;
		
		// Determine what is to the left
		left = (gamePiece - 1);
		if (gamePiece % getGameCols() == 0) {
			left = -1;
		}
		int[] results = new int[8];
		results[GamePieceCoord.GP_BOTTOM] = bottom;
		results[GamePieceCoord.GP_BOTTOM_LEFT] = bottomLeft;
		results[GamePieceCoord.GP_BOTTOM_RIGHT] = bottomRight;
		results[GamePieceCoord.GP_TOP] = top;
		results[GamePieceCoord.GP_TOP_RIGHT] = topRight;
		results[GamePieceCoord.GP_TOP_LEFT] = topLeft;
		results[GamePieceCoord.GP_RIGHT] = right;
		results[GamePieceCoord.GP_LEFT] = left;
		
		return results;
	}
	
	public  int[] deactivateGamePieces(int[] activeGamePieces, int gamePiece, int totGamePieces) {
		int[] results = new int[totGamePieces];
		
		for (int i = 0; i < totGamePieces; i++) {
			for (int n : activeGamePieces) {
				if (i == gamePiece) {
					results[i] = -1;
					break;
				}
				if (i == n) {
					results[i] = -1;
					break;
				} else {
					results[i] = i;
				}
			}
		}		
		return results;
	}
	
	public void getLetter(BoardGamePiece bgPiece) {
		String curLetter = getRandLetter();
		bgPiece.setLetter(curLetter);
	}
	
	
	/**
	 * Validates the given game board piece
	 * @param bgPiece
	 * The board game piece to validate
	 */
	public void gameBoardValidation(BoardGameAdapter boardGame ) {
		BoardGamePiece[] bgPieces = boardGame.getBoardGamePieces();
		
		for (BoardGamePiece bgPiece : bgPieces) {
			//validateGamePiece(boardGame, bgPiece);
		}
	}

	private void validateGamePiece(BoardGameAdapter boardGame,
			BoardGamePiece bgPiece) {
		
		String curLetter = bgPiece.getLetter();
		int gamePiece = bgPiece.getId();
		int[] gamePieces = activateGamePieces(gamePiece);
		int above = gamePieces[GamePieceCoord.GP_TOP],
			right = gamePieces[GamePieceCoord.GP_RIGHT],
			bottom = gamePieces[GamePieceCoord.GP_BOTTOM],
			left = gamePieces[GamePieceCoord.GP_LEFT];
		
		BoardGamePiece bgAbove = boardGame.getChildAt(above);
		BoardGamePiece bgBelow = boardGame.getChildAt(bottom);
		BoardGamePiece bgRight = boardGame.getChildAt(right);
		BoardGamePiece bgLeft = boardGame.getChildAt(left);
		
		// This prevents us from having too many of the same letter in a row and too many damn consonants
		int totalLikeIt = 0;
		if (bgAbove != null) 
			if (bgBelow != null) {
				if (bgAbove.getLetter().equalsIgnoreCase(bgBelow.getLetter()))
					if (curLetter.equalsIgnoreCase(bgAbove.getLetter())) {
						getLetter(bgPiece);
						this.validateGamePiece(boardGame, bgPiece);
					}
				
				if (Alphas.isConsonant(bgAbove.getLetter()) && Alphas.isConsonant(bgBelow.getLetter()))
					if (Alphas.isConsonant(curLetter)) {
							getLetter(bgPiece);
							this.validateGamePiece(boardGame, bgPiece);
					}
				
				if (bgAbove.getLetter().equalsIgnoreCase(curLetter))
					totalLikeIt++;
				if (bgBelow.getLetter().equalsIgnoreCase(curLetter))
					totalLikeIt++;
			}

		if (bgLeft != null) 
			if (bgRight != null) {
				if (bgLeft.getLetter().equalsIgnoreCase(bgRight.getLetter()))	
					if (curLetter.equalsIgnoreCase(bgLeft.getLetter())) {
						getLetter(bgPiece);
						this.validateGamePiece(boardGame, bgPiece);
					}
				
				if (Alphas.isConsonant(bgLeft.getLetter()) && Alphas.isConsonant(bgRight.getLetter()))
						if (Alphas.isConsonant(curLetter)) {
								getLetter(bgPiece);
								this.validateGamePiece(boardGame, bgPiece);
						}
				
				if (bgLeft.getLetter().equalsIgnoreCase(curLetter))
					totalLikeIt++;
				if (bgRight.getLetter().equalsIgnoreCase(curLetter))
					totalLikeIt++;
			}
		
		// If two or more attached are like it then get a new letter
		if (totalLikeIt >= 2) {
			getLetter(bgPiece);
			this.validateGamePiece(boardGame, bgPiece);
		}

	}
	
	private int[] mPrevRand = new int[] { -1, -1 };
	private  String getRandLetter() {
		int rand = mRand.nextInt(Alphas.WeightedAlphas.WEIGHTED_ALPHAS.length);
		String letter = Alphas.WeightedAlphas.WEIGHTED_ALPHAS[rand];
		
		boolean one = false, 
				two = false;
		
		for (int i = 0; i < mPrevRand.length; i++) {
			if (i == 0 && mPrevRand[i] == rand)
				one = true;
			if (i == 1 && mPrevRand[i] == rand)
				two= true;
		}
		
		if (one && two) {
			letter = getRandLetter();
		} else {
			mPrevRand[0] = mPrevRand[1];
			mPrevRand[1] = rand;
		}
		
		return letter;
	}
	
	private Random mRand = new Random();
	public class GamePieceCoord {
		public static final int GP_TOP = 0;
		public static final int GP_RIGHT = 1;
		public static final int GP_BOTTOM = 2;
		public static final int GP_LEFT = 3;
		public static final int GP_TOP_LEFT = 4;
		public static final int GP_TOP_RIGHT = 5;
		public static final int GP_BOTTOM_LEFT = 6;
		public static final int GP_BOTTOM_RIGHT = 7;
	}
}
