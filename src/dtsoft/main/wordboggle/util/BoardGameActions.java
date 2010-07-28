package dtsoft.main.wordboggle.util;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import dtsoft.main.wordboggle.WordBoggle;
import dtsoft.main.wordboggle.view.BoardGamePiece;

public class BoardGameActions {
	
	private WordBoggle mContext;
	private	ArrayList<Integer> mAlreadyClicked = new ArrayList<Integer>();
	
	public BoardGameActions(Context context) {
		mContext = (WordBoggle)context;
	}
	
	
	public void resetGameBoard(boolean newLetters, boolean fromScore) {
		this.resetBoard(newLetters);
    	
    	// Empty the word tracker
    	mContext.getWordTracker().setText("");
    	
    	if (newLetters) {
    		mContext.getGameUtils().gameBoardValidation(mContext.getGameBoard());
    	}
    	
    	mAlreadyClicked = new ArrayList<Integer>();
	}
	
	private void resetBoard(boolean newLetters) {
    	int children = mContext.getGameBoard().getCount();
    	for (int i = 0; i < children; i++) { 
    		BoardGamePiece bgp = (BoardGamePiece)mContext.getGameBoard().getChildAt(i);
    		Log.d("BoardGameActions", "checking piece number: " + Integer.toString(i));
    		if (bgp != null) {
    			bgp.reset();
				if (newLetters) {
					mContext.getGameUtils().getLetter(bgp);
				}
    		}
    	}
	}
	
    public void highlightOtherGamePieces(int boardGamePieceId) {
    	int[] boardGamePieceIds = mContext.getGameUtils().activateGamePieces(boardGamePieceId); 
 
    	// Call the highlight functions on the int id's
    	for (int i : boardGamePieceIds) {
			BoardGamePiece bgp = (BoardGamePiece)mContext.getGameBoard().getChildAt(i);
			if (bgp != null && !mAlreadyClicked.contains(bgp.getId())) {
				bgp.highlightPiece();
			}
		}
    	
    	this.deactivateOtherGamePieces(boardGamePieceIds, boardGamePieceId);
    } 
    
    private void deactivateOtherGamePieces(int[] activeBoardPieces, int boardGamePieceId) {
    	int[] boardGamePieceIds = mContext.getGameUtils().deactivateGamePieces(activeBoardPieces, 
    																			boardGamePieceId, 
    																			mContext.getGameUtils().getGamePieces()); 
    	// call de-activate function on the int id's
    	for (int i : boardGamePieceIds) {
    			BoardGamePiece bgp = (BoardGamePiece)mContext.getGameBoard().getChildAt(i);
    			if (bgp != null && !mAlreadyClicked.contains(bgp.getId())) {
    				bgp.deactivatePiece();
    			}
    	}
    }
    
	public void addLetterToWordTracker(String letter, int boardGamePiece) {
		mAlreadyClicked.add(boardGamePiece);
		mContext.getWordTracker().setText(mContext.getWordTracker().getText() + letter);
	}
	
	public void removeLastLetterFromWordTracker() {
		mContext.getWordTracker().setText(mContext.getWordTracker().getText().subSequence(0 ,
														mContext.getWordTracker().getText().length() - 1));
		mAlreadyClicked.remove(mAlreadyClicked.size() - 1);
		
		if (mAlreadyClicked.size() > 0) {
			this.highlightOtherGamePieces(mAlreadyClicked.get(mAlreadyClicked.size() -1));
		} else {
			this.resetGameBoard(false, false);
		}
	}
    
    public boolean isLastPieceClicked(int id) {
    	if (getCurrentGamePiecesInPlay().indexOf(id) == getCurrentGamePiecesInPlay().size() -1) {
    		return true;
    	} 
    	return false;
    }
    
    public ArrayList<Integer> getCurrentGamePiecesInPlay() {
    	return mAlreadyClicked;
    }
    
	public int getWordValue(String word) {
		if (this.mContext.getWordDatabase().getWordFromCache(word)) {
			return wordValue(word);
		}
		return 0;
	}
	
	private int wordValue(String word) {
		int value = 0;
		char[] wordSplit = word.toCharArray();
		
		// Step through all of the letters and obtain the associated score for each letter 
		for (char letter : wordSplit) {
			for (int i = 0; i < 26; i++){ 
				if (Alphas.TRUE_ALPHAS[i].equalsIgnoreCase(Character.toString(letter))) {
					value += Alphas.ALPHASCORES[i];
				}
			}
		}
		return value;
	}
}
