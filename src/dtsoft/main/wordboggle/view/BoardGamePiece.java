package dtsoft.main.wordboggle.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import dtsoft.main.wordboggle.R;
import dtsoft.main.wordboggle.WordBoggle;
import dtsoft.main.wordboggle.util.sound.AudioProvider;
import dtsoft.main.wordboggle.view.CustomColors.GamePieceColor;
import dtsoft.util.drawable.InsetBorders;
import dtsoft.util.drawable.InsetBounds;
import dtsoft.util.drawable.QuickDraw;

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
	private ShapeDrawable mGamePiece;
	private ShapeDrawable mGamePieceGloss;
	private ShapeDrawable mInset;
	private InsetBorders mInsetBorders; 
	
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
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.drawOnce(canvas);
	}
	
	public String getLetter() {
		return mLetter;
	}
	
	public void setLetter(String letter) {
		if (letter.length() > 1) {
			this.mLetter = letter.substring(0, 1).toUpperCase() + letter.substring(1, 2).toLowerCase();
		} else {
			this.mLetter = letter.substring(0, 1).toUpperCase();
		}
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
				mContext.getBoardGameActions().addLetterToWordTracker(getLetter().toUpperCase(), getId());
				
				// Change the image resource
				makeHighlight(false, false);
				mIsClicked = true;
			}
			
		}
		return true;		
	}
	
	/**
	 * Highlights this piece as an item that can be clicked next
	 */
	public void highlightPiece() {		
		makeHighlight(true, false);
		super.invalidate();
		mUnClickable = false;		
	}
	
	
	/**
	 * Changes the color of the game piece if reset is true then color is returned to default  
	 * @param highlight
	 * @param reset
	 */
	private void makeHighlight(boolean highlight, boolean reset) {
		Rect drawingRect = new Rect(); 
		super.getDrawingRect(drawingRect);
		
		int color0 = 0;
		int color1 = 0;
		
		if (reset) {
			mGamePieceGloss = null;
			color0 = GamePieceColor.Normal.GRADIENT_BOTTOM;
			color1 = GamePieceColor.Normal.GRADIENT_TOP;
		} else {
			if (highlight) {
				color0 = GamePieceColor.Clicked.GRADIENT_TOP;
				color1 = GamePieceColor.Clicked.GRADIENT_BOTTOM;
			} else {color0 = GamePieceColor.Highlight.GRADIENT_TOP;
			color1 = GamePieceColor.Highlight.GRADIENT_BOTTOM;


			}
			mGamePieceGloss = QuickDraw.drawRoundRectangle(
					drawingRect.left + super.getPaddingLeft(), 
					drawingRect.top + super.getPaddingTop(), 
					drawingRect.right - super.getPaddingRight(), 
					drawingRect.bottom - super.getPaddingBottom(),
					null);
			Rect r = mGamePieceGloss.copyBounds();
			mGamePieceGloss.setBounds(r.left, r.top, r.right, r.bottom /2);
			mGamePieceGloss.getPaint().setColor(0x44FFFFFF);
		}
		
		QuickDraw.colorShapeLinear(color0, color1, mGamePiece, drawingRect);
	}
	
	/**
	 * Sets this piece as an unclickable 
	 */
	public void deactivatePiece() {
		makeHighlight(false, true);
		mUnClickable = true;
	}

	private void drawOnce(Canvas canvas) {
		Rect drawingRect = new Rect(); 
		super.getDrawingRect(drawingRect);
		
		mX = drawingRect.exactCenterX();
		mY = drawingRect.exactCenterY();
		
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
		if (mGamePiece == null) {
			float insetBuffer = (float) Math.ceil((float) (drawingRect.width() *  0.20));
			RectF inset = new RectF(insetBuffer, insetBuffer, insetBuffer, insetBuffer);
			
			mGamePiece = QuickDraw.drawRoundRectangle(
					drawingRect.left + super.getPaddingLeft(), 
					drawingRect.top + super.getPaddingTop(), 
					drawingRect.right - super.getPaddingRight(), 
					drawingRect.bottom - super.getPaddingBottom(),
					inset);
			makeHighlight(false, true);
			// this.drawInset(drawingRect, insetBuffer);
			
			// Draw the inset for this shit
			mInset = QuickDraw.drawInset(
					drawingRect, 
					GamePieceColor.Inset.GRADIENT_TOP, 
					GamePieceColor.Inset.GRADIENT_BOTTOM, 
					super.getPaddingLeft(), 
					super.getPaddingTop(), 
					super.getPaddingRight(), 
					super.getPaddingBottom(),
					insetBuffer, 
					insetBuffer);
			
			InsetBounds insetBounds = new InsetBounds();
			insetBounds.x = mInset.getBounds().centerX();
			insetBounds.y = mInset.getBounds().centerY();
			insetBounds.bottom = mInset.getBounds().bottom;
			insetBounds.top = mInset.getBounds().top;
			insetBounds.left = mInset.getBounds().left;
			insetBounds.right = mInset.getBounds().right;
			insetBounds.width = mInset.getBounds().width();
			insetBounds.height = mInset.getBounds().height();
			
			mInsetBorders = QuickDraw.drawInsetBorder(mInset, insetBounds);
		}
		
		mInset.draw(canvas);
		mGamePiece.draw(canvas);
		mInsetBorders.getInsetBottom().draw(canvas);
		mInsetBorders.getInsetTop().draw(canvas);
		mInsetBorders.getInsetRight().draw(canvas);
		mInsetBorders.getInsetLeft().draw(canvas);
		
		if (mGamePieceGloss != null) {
			mGamePieceGloss.draw(canvas);
		}
		
		Rect textBounds = new Rect();
		mLetterPaint.getTextBounds(mLetter, 0, 1, textBounds);
		float textHeight = Math.abs((float)textBounds.top);
		float kern = Math.abs(mLetterPaint.ascent());
		
		canvas.drawText(mLetter, mX, mY + (textHeight - (kern - textHeight)), mLetterPaint);
		super.invalidate();
	}
	
	/**
	 * Recycles any drawable resources - they will be re-drawn if needed
	 */
	public void recyle() {
		mLetterPaint = null;
		mScorePaint = null;
		mInset = null;
		mGamePiece = null;
		mGamePieceGloss = null;
		
	}
	
	public void reset() {
		mIsClicked = false;
		mUnClickable = false;
		mLetterPaint = null;
		mScorePaint = null;
		makeHighlight(false, true);
		super.invalidate();
	}
}
