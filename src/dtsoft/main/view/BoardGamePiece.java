package dtsoft.main.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import dtsoft.main.R;
import dtsoft.main.WordBoggle;
import dtsoft.main.util.Alphas;
import dtsoft.main.util.sound.AudioProvider;
import dtsoft.main.view.CustomColors.GamePieceColor;

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
	private ShapeDrawable mInsetLeft;
	private ShapeDrawable mInsetBottom;
	private ShapeDrawable mInsetRight;
	private ShapeDrawable mInsetTop;
	private static float[] ROUNDED_CORNERS = new float[] { 12, 12, 12, 12, 12, 12, 12, 12 };
	
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
		Rect d = new Rect(); 
		super.getDrawingRect(d);
		
		int color0 = 0;
		int color1 = 0;
		
		if (reset) {
			mGamePieceGloss = null;
			color0 = GamePieceColor.Normal.GRADIENT_BOTTOM;
			color1 = GamePieceColor.Normal.GRADIENT_TOP;
		} else {
			if (highlight) {
				color0 = GamePieceColor.Highlight.GRADIENT_TOP;
				color1 = GamePieceColor.Highlight.GRADIENT_BOTTOM;
			} else {
				color0 = GamePieceColor.Clicked.GRADIENT_TOP;
				color1 = GamePieceColor.Clicked.GRADIENT_BOTTOM;

			}
			mGamePieceGloss = drawRoundedRectangle(null);
			Rect r = mGamePieceGloss.copyBounds();
			mGamePieceGloss.setBounds(r.left, r.top, r.right, r.bottom /2);
			mGamePieceGloss.getPaint().setColor(0x44FFFFFF);
		}

		LinearGradient background = new LinearGradient(0, 0, 0, d.height(), color0, color1, TileMode.MIRROR);
		this.mGamePiece.getPaint().setShader(background);
	}
	
	/**
	 * Sets this piece as an unclickable 
	 */
	public void deactivatePiece() {
		makeHighlight(false, true);
		mUnClickable = true;
	}

	/**
	 * Draws and returns a rounded rectangle using the standard shit i setup
	 * @param inset
	 * If you want a fucking inset, use this shit
	 * @return
	 * a fucking rectangle
	 */
	private ShapeDrawable drawRoundedRectangle(RectF inset) {
		RoundRectShape rrs = new RoundRectShape(ROUNDED_CORNERS, inset, null);
		ShapeDrawable roundedRect = new ShapeDrawable(rrs);
		
		Rect drawingRect = new Rect(); 
		super.getDrawingRect(drawingRect);
		roundedRect.setBounds(
				drawingRect.left + super.getPaddingLeft(), 
				drawingRect.top + super.getPaddingTop(), 
				drawingRect.right - super.getPaddingRight(), 
				drawingRect.bottom - super.getPaddingBottom());
		roundedRect.getPaint().setAntiAlias(true);
		roundedRect.getPaint().setDither(true);		
		
		return roundedRect;
	}
	
	private void drawInset(Rect drawingRect, float insetBuffer) {
		RectShape rs = new RectShape();
		mInset = new ShapeDrawable(rs);
		mInset.setBounds(
				(int)(drawingRect.left + super.getPaddingLeft() + insetBuffer), 
				(int)(drawingRect.top + super.getPaddingTop() + insetBuffer), 
				(int)(drawingRect.right - super.getPaddingRight() - insetBuffer), 
				(int)(drawingRect.bottom - super.getPaddingBottom() -insetBuffer));
		mInset.getPaint().setAntiAlias(true);
		mInset.getPaint().setDither(true);
		mInset.getPaint().setShader(new RadialGradient(
				mInset.getBounds().width(), 
				mInset.getBounds().height(), 
				(float) Math.sqrt(Math.pow(mInset.getBounds().width(), 2) + Math.pow(mInset.getBounds().height(), 2)), 
				CustomColors.GamePieceColor.Inset.GRADIENT_BOTTOM, 
				CustomColors.GamePieceColor.Inset.GRADIENT_TOP, 
				TileMode.MIRROR));
		
		InsetBounds insetBounds = new InsetBounds();
		insetBounds.x = mInset.getBounds().centerX();
		insetBounds.y = mInset.getBounds().centerY();
		insetBounds.bottom = mInset.getBounds().bottom;
		insetBounds.top = mInset.getBounds().top;
		insetBounds.left = mInset.getBounds().left;
		insetBounds.right = mInset.getBounds().right;
		insetBounds.width = mInset.getBounds().width();
		insetBounds.height = mInset.getBounds().height();
		
		drawInsetBorder(insetBounds);
	}
	
	private void drawInsetBorder(InsetBounds bounds) {
		float borderWidth = 1.25f;
		Path left = new Path();
		Path right = new Path();
		Path top = new Path();
		Path bottom = new Path();
		
		left.moveTo(0, bounds.height);
		left.lineTo(0, 0);
		left.lineTo(borderWidth, 0);
		left.lineTo(borderWidth, bounds.height);
		left.close();
		
		top.moveTo(0, 0);
		top.lineTo(bounds.width, 0);
		top.lineTo(bounds.width, borderWidth);
		top.lineTo(0,borderWidth);
		top.close();
		
		right.moveTo(bounds.width,bounds.height);
		right.lineTo(bounds.width, 0);
		right.lineTo(bounds.height - borderWidth, 0);
		right.lineTo(bounds.height - borderWidth, bounds.height);
		right.close();
		
		bottom.moveTo(0, mInset.getBounds().height());
		bottom.lineTo(bounds.width,mInset.getBounds().height());
		bottom.lineTo(bounds.width,mInset.getBounds().height() - borderWidth);
		bottom.lineTo(0, mInset.getBounds().height() - borderWidth);
		bottom.close();
		
		
		mInsetLeft = new ShapeDrawable(new PathShape(left,bounds.width, bounds.height));
		mInsetTop = new ShapeDrawable(new PathShape(top, bounds.width, bounds.height));
		mInsetRight = new ShapeDrawable(new PathShape(right, bounds.width, bounds.height));
		mInsetBottom = new ShapeDrawable(new PathShape(bottom, bounds.width, bounds.height));
		
		Rect b = mInset.copyBounds();
		
		mInsetBottom.getPaint().setColor(0x88FEFEFE);
		mInsetBottom.setBounds(b);
		
		mInsetRight.getPaint().setColor(0x88FEFEFE);
		mInsetRight.setBounds(b);
		
		mInsetTop.getPaint().setColor(0x88373737);
		mInsetTop.setBounds(b);
		
		mInsetLeft.getPaint().setColor(0x88373737);
		mInsetLeft.setBounds(b);
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
			mGamePiece = drawRoundedRectangle(inset);
			makeHighlight(false, true);
			this.drawInset(drawingRect, insetBuffer);
		}

		mInset.draw(canvas);
		mInsetRight.draw(canvas);
		mInsetTop.draw(canvas);
		mInsetBottom.draw(canvas);
		mInsetLeft.draw(canvas);
		mGamePiece.draw(canvas);
		
		if (mGamePieceGloss != null) {
			mGamePieceGloss.draw(canvas);
		}
		
		Rect scoreBounds = new Rect();
		mScorePaint.getTextBounds("x" + Alphas.getLetterValue(mLetter),0, ("x" + Alphas.getLetterValue(mLetter)).length(), scoreBounds);
		
		Rect textBounds = new Rect();
		mLetterPaint.getTextBounds(mLetter, 0, 1, textBounds);
		float textHeight = Math.abs((float)textBounds.top);
		float kern = Math.abs(mLetterPaint.ascent());
		
		canvas.drawText(mLetter.toUpperCase(), mX, mY + (textHeight - (kern - textHeight)), mLetterPaint);
		canvas.drawText("x" + Alphas.getLetterValue(mLetter), drawingRect.bottom - this.getPaddingBottom() - scoreBounds.width(), drawingRect.right - this.getPaddingRight(), mScorePaint);
		super.invalidate();
	}
	
	/**
	 * Recycles any drawable resources - they will be re-drawn if needed
	 */
	public void recyle() {
		mLetterPaint = null;
		mScorePaint = null;
		mInset = null;
		mInsetBottom = null;
		mInsetLeft = null;
		mInsetRight = null;
		mInsetTop = null;
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
	
	public class InsetBounds {
		public int x, y, left, right, top, bottom, width, height;
	}
	
}
