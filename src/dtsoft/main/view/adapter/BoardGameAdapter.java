package dtsoft.main.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView.ScaleType;
import android.widget.TableLayout;
import android.widget.TableRow;
import dtsoft.main.R;
import dtsoft.main.WordBoggle;
import dtsoft.main.util.GameUtils;
import dtsoft.main.view.BoardGamePiece;

public class BoardGameAdapter {
	private WordBoggle mContext;
	private BoardGamePiece[] mBoardGamePieces;
	private TableRow[] mTableRows;
	private int mGamePieceDimension = 0;
	private static int GAME_PIECE_PADDING = 3;
	
	public BoardGameAdapter(Context context) {
		mContext = (WordBoggle)context;
		
		mBoardGamePieces = new BoardGamePiece[mContext.getGameUtils().getGamePieces()];
		mTableRows = new TableRow[mContext.getGameUtils().getGameCols()];

		mGamePieceDimension = ((WordBoggle)mContext).getWindowManager().getDefaultDisplay().getWidth();
		mGamePieceDimension = mGamePieceDimension / mContext.getGameUtils().getGameCols();
		
		this.buildGameBoard();
	}
	
	public int getCount() {
		return mBoardGamePieces.length;
	}

	public BoardGamePiece getChildAt(int position) {
		if (position >= mBoardGamePieces.length || position < 0)
			return null;
		return (BoardGamePiece)mBoardGamePieces[position];
	}

	public void validateGameBoard() {
		mContext.getGameUtils().gameBoardValidation(this);
	}
	
	public BoardGamePiece[] getBoardGamePieces() {
		return mBoardGamePieces;
	}

	private void buildGameBoard() {
		TableLayout gameBoard = (TableLayout)((WordBoggle)mContext).findViewById(R.id.GameBoard);
		// Clean the game board of course
		gameBoard.removeAllViews();
		
		int bgpId = 0;
		for (int rowId = 0; rowId < mTableRows.length; rowId++) {
			LayoutInflater inflater = ((WordBoggle)mContext).getLayoutInflater();
			TableRow tableRow = (TableRow) inflater.inflate(R.layout.table_row, null);
			
			tableRow.setId(rowId);
			for (int bgp = bgpId; bgp < mBoardGamePieces.length; bgp++) {
				bgpId++;
				
				BoardGamePiece bgPiece = new BoardGamePiece(mContext);
				bgPiece.setId(bgp);
				bgPiece.setPadding(GAME_PIECE_PADDING, 
									GAME_PIECE_PADDING, 
									GAME_PIECE_PADDING, 
									GAME_PIECE_PADDING);
				//bgPiece.setImageResource(R.drawable.boardgamepiece);
				TableRow.LayoutParams lp = new TableRow.LayoutParams (mGamePieceDimension,mGamePieceDimension);
				bgPiece.setLayoutParams(lp);
				bgPiece.setClickable(true);
				bgPiece.setScaleType(ScaleType.FIT_CENTER);
				mContext.getGameUtils().getLetter(bgPiece);
				this.mBoardGamePieces[bgp] = bgPiece;
				
				// Add it to the table row
				tableRow.addView(bgPiece);
				
				if (bgp == (mContext.getGameUtils().getGameCols() - 1) ||
						((bgp - (mContext.getGameUtils().getGameCols() - 1)) % mContext.getGameUtils().getGameCols()) == 0)
					break;
			}
			mTableRows[rowId] = tableRow;
			Log.d("BoardGameAdapter", "Adding tableRow{"+tableRow.getId()+"} to board game with "+tableRow.getChildCount()+" children");
			gameBoard.addView(tableRow);
		}
	}
	
	public void destroy() {
		
	}
}
