package dtsoft.main.util;

import java.util.Random;

import dtsoft.main.view.BoardGamePiece;
import dtsoft.main.view.adapter.BoardGameAdapter;


public class GameUtils {
	private final int mGameCols = 5;
	
	private static GameUtils mThis = null;
	
	public static GameUtils getInstance() {
		if (mThis == null) {
			mThis =  new GameUtils();
		} 
		
		return mThis;
	}
	
	public GameUtils() {
		mRand = new Random();
	}
	
	public int getGameCols() {
		return mGameCols;
	}

	public int getGamePieces() {
		return (int)Math.pow(getGameCols(), 2);
	}

	public  int[] activateGamePieces(int gamePiece) {
		int above,
			right,
			bottom,
			left;
		
		// Determine what is above
		above = (gamePiece - mGameCols);
		
		// Determine what is to the right
		right = (gamePiece + 1);
		if (right % mGameCols == 0) {
			// a left most piece
			right = -1;
		}
		
		// Determine what is so the bottom
		bottom = (gamePiece + mGameCols);
		
		// Determine what is to the left
		left = (gamePiece - 1);
		if (gamePiece % mGameCols == 0) {
			left = -1;
		}
		return new int[] {above, right, bottom, left};
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
	}
}
