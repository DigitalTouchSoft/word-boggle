package dtsoft.main.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import dtsoft.main.R;
import dtsoft.main.WordBoggle;
import dtsoft.main.util.Alphas;
import dtsoft.main.util.sound.AudioProvider;

public class BoardGamePiece extends ImageView {
	
	private float mX = 0,
				mY = 0;
	private String mLetter;
	private boolean mIsClicked = false; 
	private boolean mUnClickable = false;
	private Paint mLetterPaint;
	private Paint mScorePaint;
	private WordBoggle mContext;
	private int mLetterFontSize;
	private int mScoreFontSize;
	
	public BoardGamePiece(Context context) {
		super(context);
		mContext = (WordBoggle) context;
		
		switch(mContext.getResources().getConfiguration().screenLayout) {
		case (Configuration.SCREENLAYOUT_SIZE_LARGE + Configuration.SCREENLAYOUT_LONG_YES):
			mLetterFontSize = (32);
			mScoreFontSize = 20;
			break; 
		case (Configuration.SCREENLAYOUT_SIZE_LARGE + Configuration.SCREENLAYOUT_LONG_NO):
			mLetterFontSize = (32);
			mScoreFontSize = 20;
			break;
		default:
			mLetterFontSize = (20);
			mScoreFontSize = 12;
			break;
		}
	}
	
	public String getLetter() {
		return mLetter;
	}
	
	public void setLetter(String letter) {
		this.mLetter = String.valueOf(Character.toUpperCase(letter.charAt(0)));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private void init(Context context, AttributeSet attributeSet) {
		TypedArray params = context.obtainStyledAttributes(attributeSet, R.styleable.BoardGamePiece);
		mLetter = params.getString(R.styleable.BoardGamePiece_letter);
		if (mLetter == null) {
			mLetter = "Err";
		}
	}
	

	
	public BoardGamePiece(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.init(context, attributeSet);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP) {
			// Get the activity 
			if (mUnClickable) {
				return true;
			}
			
			// Handle a clicking
			if (mIsClicked) {
				if (mContext.getBoardGameActions().isLastPieceClicked(getId())) {
					mContext.getAudioProvider().playSounds(new int[] {AudioProvider.LETTER_CLICKED});
					mContext.getBoardGameActions().removeLastLetterFromWordTracker();
					mIsClicked = false;	
				}
			} else {
				mContext.getAudioProvider().playSounds(new int[] {AudioProvider.LETTER_CLICKED});
				
				// Set which buttons can be clicked		
				mContext.getBoardGameActions().highlightOtherGamePieces(getId());
				mContext.getBoardGameActions().addLetterToWordTracker(getLetter(), getId());
				
				// Change the image resource
				super.setImageResource(R.drawable.boardgamepiece_on);
				mIsClicked = true;
			}
			
		}
		return true;		
	}
	
	/**
	 * Highlights this piece as an item that can be clicked next
	 */
	public void highlightPiece() {		
		super.setImageResource(R.drawable.boardgamepiece_next);
		super.invalidate();
		mUnClickable = false;		
	}
	
	/**
	 * Sets this piece as an unclickable 
	 */
	public void deactivatePiece() {
		super.setImageResource(R.drawable.boardgamepiece);
		mUnClickable = true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.drawOnce(canvas);
	}
	
	private void drawOnce(Canvas canvas) {
		if (mLetterPaint == null) {
			mLetterPaint = new Paint();
			mLetterPaint.setTextSize(mLetterFontSize);
			mLetterPaint.setColor(Color.BLACK);
			mLetterPaint.setAntiAlias(true);			
			mLetterPaint.setTextAlign(Align.CENTER);
			mLetterPaint.setTypeface(Typeface.DEFAULT_BOLD);
		}
		if (mScorePaint == null) {
			mScorePaint = new Paint();
			mScorePaint.setColor(Color.WHITE);
			mScorePaint.setTextAlign(Align.CENTER);
			mScorePaint.setTextSize(mScoreFontSize);
			mScorePaint.setTypeface(Typeface.DEFAULT_BOLD);
			mScorePaint.setAntiAlias(true);
		}
		
		Rect d = new Rect(); 
		super.getDrawingRect(d);
		float[] roundedCorner = new float[] { 12, 12, 12, 12, 12, 12, 12, 12 };
		float insetBuffer = (float) (d.width() *  0.20);
		RectF inset = new RectF(insetBuffer, insetBuffer, insetBuffer, insetBuffer);
		RoundRectShape rrs = new RoundRectShape(roundedCorner, inset, null);
		ShapeDrawable roundRect = new ShapeDrawable(rrs);
		roundRect.getPaint().setShader(
				new LinearGradient(d.bottom, 
									d.bottom, 
									d.top, 
									d.top, 
									new int[] { 
										CustomColors.BoardGamePieceColor.GRADIENT_TOP,
										CustomColors.BoardGamePieceColor.GRADIENT_BOTTOM } , 
									null, 
									Shader.TileMode.MIRROR));

		roundRect.setBounds(d );
		roundRect.setPadding(3, 3, 3, 3);
		roundRect.draw(canvas);

		mX = d.exactCenterX();
		mY = d.exactCenterY();
		
		Rect scoreBounds = new Rect();
		mScorePaint.getTextBounds("x" + Alphas.getLetterValue(mLetter),0, ("x" + Alphas.getLetterValue(mLetter)).length(), scoreBounds);
		
		Rect textBounds = new Rect();
		mLetterPaint.getTextBounds(mLetter, 0, 1, textBounds);
		float textHeight = Math.abs((float)textBounds.top);
		float kern = Math.abs(mLetterPaint.ascent());
		
		canvas.drawText(mLetter.toUpperCase(), mX, mY + (textHeight - (kern - textHeight)), mLetterPaint);
		canvas.drawText("x" + Alphas.getLetterValue(mLetter), d.bottom - this.getPaddingBottom() - scoreBounds.width(), d.right - this.getPaddingRight(), mScorePaint);
		super.invalidate();
	}
	
	/**
	 * Recycles any drawable resources - they will be re-drawn if needed
	 */
	public void recyle() {
		mLetterPaint = null;
		mScorePaint = null;
	}
	
	public void reset() {
		mIsClicked = false;
		mUnClickable = false;
		mLetterPaint = null;
		super.setImageResource(R.drawable.boardgamepiece);
		super.invalidate();
	}
	
}
