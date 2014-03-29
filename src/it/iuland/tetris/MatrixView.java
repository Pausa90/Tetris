package it.iuland.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class MatrixView extends View {
	
	private Bitmap matrixBitmap;
	private float matrixOffsetLeft;
	private float matrixOffsetTop;
	
	public MatrixView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.matrixBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.matrix);
		this.matrixOffsetLeft = 0;
		this.matrixOffsetTop = 0;
	}

	@Override
	synchronized protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);		
		
		canvas.drawBitmap(this.matrixBitmap, this.matrixOffsetLeft, this.matrixOffsetTop, null);
		
	}
	
    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
       //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	this.setMeasuredDimension(matrixBitmap.getWidth(), matrixBitmap.getHeight());

    }


}
