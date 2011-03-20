package dtsoft.main.wordboggle.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.Log;
import dtsoft.main.wordboggle.R;
import dtsoft.main.wordboggle.view.CustomColors.GamePieceColor;
import dtsoft.util.drawable.InsetBorders;
import dtsoft.util.drawable.InsetBounds;
import dtsoft.util.drawable.QuickDraw;

public class BoardGameButton extends android.widget.Button {
	ShapeDrawable mButton = null; 
	ShapeDrawable mInset = null;
	InsetBorders mInsetBorders = null;
	boolean mDrawInset = true;

	public BoardGameButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray sa = getContext().obtainStyledAttributes(attrs, R.styleable.BoardGameButton);
		if ( sa.length() > 0 ) {
			try { 
				Log.i("BoardGameButton", "Draw Inset is set to: " + sa.getString(R.styleable.BoardGameButton_inset));
				mDrawInset = Boolean.valueOf(sa.getString(R.styleable.BoardGameButton_inset));
			} catch (NullPointerException e) {
				Log.e("BoardGameButton", "Inset Value not found!");
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Rect drawingRect = new Rect();
		super.getDrawingRect(drawingRect);
		
		float insetBufferW = (float) Math.ceil((float) (drawingRect.width() *  0.050));
		float insetBufferH = (float) Math.ceil((float) (drawingRect.height() * 0.100));
		RectF inset; 
		if (mDrawInset) { 
			inset = new RectF( insetBufferW, insetBufferH, insetBufferW, insetBufferH );
		} else { 
			inset = null;
		}

		mButton = QuickDraw.drawRoundRectangle(
				drawingRect.left, 
				drawingRect.top, 
				drawingRect.right, 
				drawingRect.bottom,
				inset);

		if (!isPressed()) { 
			QuickDraw.colorShapeLinear(GamePieceColor.Normal.GRADIENT_BOTTOM, GamePieceColor.Normal.GRADIENT_TOP, mButton, drawingRect);
		} else {
			QuickDraw.colorShapeLinear(GamePieceColor.Highlight.GRADIENT_BOTTOM, GamePieceColor.Highlight.GRADIENT_TOP, mButton, drawingRect);
		}
		if (mDrawInset) { 
			mInset = QuickDraw.drawInset(drawingRect, 
					GamePieceColor.Inset.GRADIENT_TOP, 
					GamePieceColor.Inset.GRADIENT_BOTTOM, 
					0, 
					0, 
					0, 
					0, 
					insetBufferW,
					insetBufferH);
			
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
		super.setBackgroundDrawable(mButton);
		
		if (mDrawInset) {
			mInset.draw(canvas);
			mInsetBorders.getInsetLeft().draw(canvas);
			mInsetBorders.getInsetTop().draw(canvas);
			mInsetBorders.getInsetRight().draw(canvas);
			mInsetBorders.getInsetBottom().draw(canvas);
		}
		
		super.onDraw(canvas);
	}
}
