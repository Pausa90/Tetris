package it.iuland.tetris.view;

import it.iuland.tetris.R;
import it.iuland.tetris.R.drawable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class MatrixView extends View {
	
	private float MAX_X;
	private float MAX_Y;
	private float SQUARE_X; //Dimensione di una cella della matrice
	private float SQUARE_Y; //Dimensione di una cella della matrice
	
	private Bitmap matrixBitmap;
	private List<RenderedTetromino> tetrominoes;
	
	public MatrixView(Context context, AttributeSet attrs) {
		super(context, attrs);		
		this.matrixBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.matrix);
		this.MAX_X = this.matrixBitmap.getWidth();
		this.MAX_Y = this.matrixBitmap.getHeight();
		this.SQUARE_X = this.MAX_X/12;
		this.SQUARE_Y = this.MAX_Y/20;
			
		this.tetrominoes = new LinkedList<RenderedTetromino>();
	}

	@Override
	synchronized protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);				
		canvas.drawBitmap(this.matrixBitmap, 0, 0, null);
		
		//Disegno i tetromini
		for (RenderedTetromino tetromino : this.tetrominoes){
			canvas.drawBitmap(tetromino.getBitmap(), tetromino.getX(), tetromino.getY(), null);
		}
	}
	
    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
       //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	this.setMeasuredDimension(matrixBitmap.getWidth(), matrixBitmap.getHeight());

    }

	public void putTetromino(String name, int drawableId) {
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), drawableId);
		RenderedTetromino current = new RenderedTetromino(bitmap);
		this.centerTetromino(current, name);
		this.tetrominoes.add(current);		
		if (name.equals("tetromino_i"))
			this.traslateToBelow();
	}

	private void centerTetromino(RenderedTetromino current, String name) {
		if (name.equals("tetromino_o") || name.equals("tetromino_i"))
			current.setX(this.MAX_X/2 - current.getWidth()/2);		
		else
			current.setX(5*this.SQUARE_X);		
	}

	public void traslateToLeft() {
		RenderedTetromino tetromino = this.tetrominoes.get(this.tetrominoes.size()-1);
		tetromino.setX(tetromino.getX()-SQUARE_X);		
	}

	public void traslateToRight() {
		RenderedTetromino tetromino = this.tetrominoes.get(this.tetrominoes.size()-1);
		tetromino.setX(tetromino.getX()+SQUARE_X);		
	}

	public void traslateToBelow() {
		RenderedTetromino tetromino = this.tetrominoes.get(this.tetrominoes.size()-1);
		tetromino.setY(tetromino.getY()+SQUARE_Y);			
	}
	
	private void traslateToUp() {
		RenderedTetromino tetromino = this.tetrominoes.get(this.tetrominoes.size()-1);
		tetromino.setY(tetromino.getY()-SQUARE_Y);	
	}

	public void rotateClockWise(int drawableID, String currentTetrominoName, int afterRotation) {
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), drawableID);
		RenderedTetromino tetromino = this.tetrominoes.get(this.tetrominoes.size()-1);
		tetromino.setBitmap(bitmap);
		
		//Sistemo la posizione della nuova immagine
		this.fixBitmapPosition(currentTetrominoName, afterRotation);
	}

	private void fixBitmapPosition(String currentTetrominoName, int afterRotation) {
		if (currentTetrominoName.equals("tetromino_i")){
			switch (afterRotation){
			case 0:
				this.traslateToLeft();
				this.traslateToBelow();
				break;
			case 1:
				this.traslateToUp();
				this.traslateToRight();
				this.traslateToRight();
				break;
			case 2:
				this.traslateToLeft();
				this.traslateToLeft();
				this.traslateToBelow();
				this.traslateToBelow();
				break;
			case 3:
				this.traslateToUp();
				this.traslateToUp();
				this.traslateToRight();
				break;
		}
		} else {
			switch (afterRotation){
				case 0:
					break;
				case 1:
					this.traslateToRight();
					break;
				case 2:
					this.traslateToLeft();
					this.traslateToBelow();
					break;
				case 3:
					this.traslateToUp();
					break;
			}
		}
	}

	
	
}

class RenderedTetromino{
	private Bitmap bitmap;
	private float width;
	private float height;
	private float x;
	private float y;
	
	public RenderedTetromino(Bitmap tetromino){
		this.bitmap = tetromino;
		this.width = this.bitmap.getWidth();
		this.height = this.bitmap.getHeight();
		this.setX(0);
		this.setY(0);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public Bitmap getBitmap(){
		return this.bitmap;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
		
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}