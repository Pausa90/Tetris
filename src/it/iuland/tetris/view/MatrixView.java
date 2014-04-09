package it.iuland.tetris.view;

import it.iuland.tetris.GameManager;
import it.iuland.tetris.Log;
import it.iuland.tetris.R;

import java.util.LinkedList;
import java.util.List;

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
	private List<RenderedTetromino> newList; //Utile in fase di eliminazione delle linee
	private GameManager controller;
	private Log log;

	public MatrixView(Context context, AttributeSet attrs) {
		super(context, attrs);		
		this.matrixBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.matrix);
		this.MAX_X = this.matrixBitmap.getWidth();
		this.MAX_Y = this.matrixBitmap.getHeight();
		this.SQUARE_X = this.MAX_X/12;
		this.SQUARE_Y = this.MAX_Y/20;

		this.tetrominoes = new LinkedList<RenderedTetromino>();
		this.newList = new LinkedList<RenderedTetromino>();
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

	public void setController(GameManager gameManager){
		this.controller = gameManager;
	}
	
	public void putTetromino(String name, int drawableId) {
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), drawableId);
		RenderedTetromino current = new RenderedTetromino(bitmap, name);
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
	
	private void traslateToBelow(RenderedTetromino tetromino){
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
		tetromino.setRotation(afterRotation);

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

	public void rowsCleaned(int[] rowsToClean) {
		for (RenderedTetromino tetromino : this.tetrominoes){
			int[][] tetrominoCoord = this.getTetrominoCoordinate(tetromino);
			this.log.toLog(this, "tetromino: "+tetromino.toString() + " coordinate: " + tetrominoCoord.toString());
			if (this.isIn(tetrominoCoord, rowsToClean))
				this.splitAndUpdateCoord(tetromino, tetrominoCoord, rowsToClean);
			else if (this.isUpTo(tetrominoCoord, rowsToClean))
				this.updateCoord(tetromino, rowsToClean.length);
		}
		//Aggiorno la lista
		this.log.toLog(this, "listaVecchia: " + this.tetrominoes.toString());
		this.log.toLog(this, "listaNuova: " + this.newList.toString());
		this.tetrominoes.clear();
		this.tetrominoes.addAll(this.newList);
		this.newList.clear();
	}

	/**
	 * Ritorna true se il tetromino è posizionato su quella r  
	 * @param tetromino
	 * @param rowsToClean
	 * @return
	 */
	private boolean isIn(int[][] tetrominoCoord, int[] rowsToClean) {
		for (int[] i : tetrominoCoord)
			for (int j : rowsToClean)
				if (i[1] == j)
					return true;
		return false;
	}
	
	
	private boolean isUpTo(int[][] tetrominoCoord, int[] rowsToClean) {
		for (int i[] : tetrominoCoord)
			for (int j : rowsToClean) //TODO: Se sono ordinate posso prendere il più piccolo
				if (i[1] < j)
					return true;
		return false;
	}

	/**
	 * Il risultato coodifica [x,y,deltax,deltay], ove delta indica la distanza dall'origine
	 * @param tetromino
	 * @return
	 */
	private int[][] getTetrominoCoordinate(RenderedTetromino tetromino) {
		String name = tetromino.getName();		
		int y = this.getRowNumber(tetromino.getY());
		int x = this.getColumnNumber(tetromino.getX());
		if (name.equals("square"))
			return new int[][] { {x,y,0,0} };
		if (name.equals("tetromino_o"))
			return new int[][] { {x,y,0,0}, {x,y+1,0,1}, {x+1,y,1,0}, {x+1,y+1,1,1} }; 
		if (name.equals("tetromino_i")){
			switch (tetromino.getRotation()) {
			case 0: return new int[][] { {x,y,0,0}, {x+1,y,1,0}, {x+2,y,2,0}, {x+3,y,3,0} };
			case 1: return new int[][] { {x,y,0,0}, {x,y+1,0,1}, {x,y+2,0,2}, {x,y+3,0,3} };
			}
		}
		if (name.equals("tetromino_j")){
			switch (tetromino.getRotation()) {
			case 0: return new int[][] { {x,y,0,0}, {x,y+1,0,1}, {x+1,y+1,1,1}, {x+2,y+1,2,1} };
			case 1: return new int[][] { {x,y,0,0}, {x+1,y,1,0}, {x,y+1,0,1}, {x,y+2,0,2} };
			case 2: return new int[][] { {x,y,0,0}, {x+1,y,1,0}, {x+2,y,2,0}, {x+2,y+1,2,1} };
			case 3: return new int[][] { {x+1,y,1,0}, {x+1,y+1,1,1}, {x+1,y+2,1,2}, {x,y+2,0,2} };
			}
		}
		if (name.equals("tetromino_l")){
			switch (tetromino.getRotation()) {
			case 0: return new int[][] { {x,y+1,0,1}, {x+1,y+1,1,1}, {x+2,y+1,2,1}, {x+2,y,2,0} };
			case 1: return new int[][] { {x,y,0,0}, {x,y+1,0,1}, {x,y+2,0,2}, {x+1,y+2,1,2} };
			case 2: return new int[][] { {x,y,0,0}, {x+1,y,1,0}, {x+2,y,2,0}, {x,y+1,0,1} };
			case 3: return new int[][] { {x,y,0,0}, {x+1,y,1,0}, {x+1,y+1,1,1}, {x+1,y+2,1,2} };
			}
		}
		if (name.equals("tetromino_s")){
			switch (tetromino.getRotation()) {
			case 0: case 2: return new int[][] { {x,y+1,0,1}, {x+1,y+1,1,1}, {x+1,y,1,0}, {x+2,y,2,0} };
			case 1: case 3: return new int[][] { {x,y,0,0}, {x,y+1,0,1}, {x+1,y,1,0}, {x+1,y+2,1,2} };
			}
		}
		if (name.equals("tetromino_z")){
			switch (tetromino.getRotation()) {
			case 0: case 2: return new int[][] { {x,y,0,0}, {x+1,y,1,0}, {x+1,y+1,1,1}, {x+2,y+1,2,1} };
			case 1: case 3: return new int[][] { {x+1,y,1,0}, {x+1,y+1,1,1}, {x,y+1,0,1}, {x,y+2,0,2} };
			}
		}
		if (name.equals("tetromino_t")){
			switch (tetromino.getRotation()) {
			case 0: return new int[][] { {x,y+1,0,1}, {x+1,y+1,1,1}, {x+1,y,1,0}, {x+2,y+1,2,1} };
			case 1: return new int[][] { {x,y,0,0}, {x,y+1,0,1}, {x,y+2,0,2}, {x+1,y+1,1,1} };
			case 2: return new int[][] { {x,y,0,0}, {x+1,y,1,0}, {x+2,y,2,0}, {x+1,y+1,1,1} };
			case 3: return new int[][] { {x+1,y,1,0}, {x+1,y+1,1,1}, {x+1,y+2,1,2}, {x,y+1,0,1} };
			}
		}
		return null;
	}

//	private int getRowNumber(float y) {
//		//y : maxY = return : 20
//		float out = 20 * y / MAX_Y; // Da controllare con il debugger
//		return (int) out;
//	}
//	
//	private float getYPositionInMatrix(int row){
//		//return : maxY = row : 20
//		float out = MAX_Y * row / 20; // Da controllare con il debugger
//		return (int) out;
//	}
//	
//	private int getColumnNumber(float x) {
//		//x : maxX = return : 12
//		float out = 12 * x / MAX_X; // Da controllare con il debugger
//		return (int) out;
//	}
//	
//	private float getXPositionInMatrix(int column){
//		float out = MAX_X * column / 12; // Da controllare con il debugger
//		return (int) out;
//	}

	private int getRowNumber(float y) {
		float out = y/SQUARE_Y; 					
		return (int) (out+0.9);
	}
	
	private float getYPositionInMatrix(int row){
		float out = row*SQUARE_Y; 
		return (int) out;
	}
	
	private int getColumnNumber(float x) {		
		float out = x/SQUARE_X; 
		return (int) (out+0.9);
	}
	
	private float getXPositionInMatrix(int column){
		float out = column*SQUARE_X;
		return (int) out;
	}
	
	private void splitAndUpdateCoord(RenderedTetromino tetromino, int[][] tetrominoCoord, int[] rowsToClean) {		
		//Ricavo le celle rimenenti con la differenza tra i due array
		int[][] remainingYCells = this.subtraction(tetrominoCoord, rowsToClean); 
		this.log.toLog(this, "remainsCell: " + remainingYCells.toString());
		for (int[] cell : remainingYCells){

			//Creo il nuovo tetromino, traslandolo
			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), this.controller.getBitmapID("square"));
			RenderedTetromino square = new RenderedTetromino(bitmap, "square");
			square.setX(tetromino.getX()+(cell[2]*SQUARE_X));
			square.setY(tetromino.getY()+((cell[3]+1)*SQUARE_Y));
			this.log.toLog(this, "cell: " + cell.toString() + " tetromino: " + tetromino.toString() + " square: " + square.toString());
			
			this.newList.add(square);
		}
	}
	
	private int[][] subtraction(int[][] tetrominoCoord, int[] rowsToClean) {
		int[][] output = new int[tetrominoCoord.length][4];
		boolean notFound = true;
		int i = 0;
		for (int[] coord : tetrominoCoord){
			for (int clean : rowsToClean)
				if (coord[1] == clean)
					notFound = false;
			if (notFound){
				output[i] = coord;
				i++;
			}
			notFound = true;
		}
		return this.optimizeArray(output, i);
	}

	private int[][] optimizeArray(int[][] array, int maxIndex) {
		if (array.length == maxIndex)
			return array;
		int[][] output = new int[maxIndex][4];
		for (int i=0; i<maxIndex; i++)
			output[i] = array[i];
		return output;
	}

	private void updateCoord(RenderedTetromino tetromino, int numRowsToClean) {
		this.log.toLog(this, "updateCoord tetromino: " + tetromino.toString() + " numRowsToClean:" + numRowsToClean);
		for (int i=0; i<numRowsToClean; i++)
			this.traslateToBelow(tetromino);
		this.newList.add(tetromino);
	}
	
	public void setLog(Log log){
		this.log = log;
	}
	
}

class RenderedTetromino{
	private Bitmap bitmap;
	private float width;
	private float height;
	private float x;
	private float y;
	private String name;
	private int rotation;

	public RenderedTetromino(Bitmap tetromino, String name){
		this.bitmap = tetromino;
		this.width = this.bitmap.getWidth();
		this.height = this.bitmap.getHeight();
		this.setX(0);
		this.setY(0);
		this.rotation = 0;
		this.name = name;
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

	public void setRotation(int rotation){
		this.rotation = rotation;
	}

	public int getRotation(){
		return this.rotation;
	}

	public String getName(){
		return this.name;
	}
	
	@Override
	public String toString(){
		return this.name + " (" + this.x + "," + this.y + ") rotazione:" + this.rotation; 
	}
}