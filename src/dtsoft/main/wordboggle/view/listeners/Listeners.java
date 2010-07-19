package dtsoft.main.wordboggle.view.listeners;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import dtsoft.main.wordboggle.WordBoggle;
import dtsoft.main.wordboggle.util.sound.AudioProvider;
import dtsoft.main.wordboggle.view.BoardGamePiece;

public class Listeners {
	
	private static Listeners mThis;
	public static Listeners getInstance() {
		if (mThis == null) {
			mThis = new Listeners();
		}
		return mThis;
	}
	
	public OnItemClickListener boardGamePieceClick() {
		return new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BoardGamePiece gv = (BoardGamePiece) arg1;
				gv.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0));
			}
			
		};
	}
	
	public OnClickListener submitWordClick() {
		return new OnClickListener() {
			
			public void onClick(View v) {
				
				WordBoggle wb = (WordBoggle)v.getContext();
				String word;
				word = wb.getWordTracker().getText().toString();
				
				int wordValue = wb.getBoardGameActions().getWordValue(wb.getWordTracker().getText().toString());
			
				if (wordValue > 0) {
					// Bank the score
					String curScore = wb.getScoreBoard().getText().toString();
					int score = Integer.parseInt(curScore);
					score += wordValue;
					
					wb.getScoreBoard().setText(String.valueOf(score));
					wb.getBoardGameActions().resetGameBoard(false, true);
					
					this.setupWord(word, wordValue, v.getContext());
					wb.getAudioProvider().playSounds(new int[] {AudioProvider.GOOD_WORD_SUBMITED});
				} else {
					wb.getAudioProvider().playSounds(new int[] {AudioProvider.BAD_WORD_SUBMITED});
				}
			}
			
			private void setupWord(String word, int value, Context context) {
				WordBoggle wb = (WordBoggle) context;
				wb.getWordsInBank().add(word + "\t-\t" + Integer.toString(value));
			}
			
		};
	}
	
	public OnClickListener cancelWordClick() {
		return new OnClickListener() {
			
			public void onClick(View v) {
				WordBoggle wb = (WordBoggle)v.getContext();
				wb.getBoardGameActions().resetGameBoard(false, false);		
				wb.getAudioProvider().playSounds(new int[] {AudioProvider.WORD_CANCELED});
			}
		};
	}
}
