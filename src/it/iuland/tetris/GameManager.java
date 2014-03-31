package it.iuland.tetris;

import java.util.Locale;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.TextView;
import it.andclaval.tetris.model.TetrisGame;
import it.andclaval.tetris.render.TextRendering;
import it.iuland.tetris.view.MatrixView;

public class GameManager {

	private MatrixView matrixView;
	private ImageView nextTetrominoView;
	private TextView levelView;
	private TetrisGame game;	
	private Resources resources;
	private String packageName;
	private String level;
	
	private int currentTetrominoRotation;
	private int currentTetrominoResource;
	private String currentTetrominoName;
	
	/**
	 * Per debug
	 */
	private TextRendering textRender;
	private String nextTetrominoName;
	
	public GameManager(MatrixView matrixView, ImageView nextTetromino, TextView level, Resources resources, String packageName){
		this.matrixView = matrixView;
		this.nextTetrominoView = nextTetromino;
		this.levelView = level;
		this.game = new TetrisGame();
		this.game.startGame();
		this.resources = resources;
		this.packageName = packageName;
		this.level = (String) this.levelView.getText();
		this.setCurrentLevel();
		this.setNextTetromino();
		this.setCurrentTetromino();
		
		this.textRender = new TextRendering();
		this.printMatrix();
	}
	
	public void setCurrentLevel() {
		this.levelView.setText(this.level + " " + this.game.getCurrentLevel());
	}
	
	public void setNextTetromino(){
		this.nextTetrominoName = this.game.getNextTetromino();
		this.nextTetrominoName = this.pathFiltering(this.nextTetrominoName).toLowerCase(Locale.getDefault());
		int drawableId = this.getResourcesID(this.nextTetrominoName);
		this.nextTetrominoView.setImageResource(drawableId);		
	}
	
	public void setCurrentTetromino(){
		this.currentTetrominoName = this.game.getCurrentTetrominoName();
		this.currentTetrominoName = this.pathFiltering(this.currentTetrominoName).toLowerCase(Locale.getDefault());
		this.currentTetrominoRotation = 0;
		this.currentTetrominoResource = 0;
		int drawableId = this.getResourcesID(this.currentTetrominoName);
		this.matrixView.putTetromino(this.currentTetrominoName, drawableId);
	}
	
	private int getResourcesID(String name){
		name = this.pathFiltering(name).toLowerCase(Locale.getDefault());
		return this.resources.getIdentifier(name+this.currentTetrominoResource, "drawable", this.packageName);
	}
	
	private String pathFiltering(String name){
		String[] splitted = name.split("\\.");
		return splitted[splitted.length - 1];
	}

	public void rotateClockWise() {
		if (this.game.rotateClockWise()){
			if (!this.currentTetrominoName.equals("tetromino_o")){
				if (this.currentTetrominoName.equals("tetromino_i") || 
						this.currentTetrominoName.equals("tetromino_s") || 
						this.currentTetrominoName.equals("tetromino_z"))
					this.currentTetrominoResource = (this.currentTetrominoResource + 1)%2;
				else
					this.currentTetrominoResource = (this.currentTetrominoResource + 1)%4;
				this.currentTetrominoRotation = (this.currentTetrominoRotation + 1)%4;
				this.matrixView.rotateClockWise(this.getResourcesID(this.currentTetrominoName), this.currentTetrominoName, this.currentTetrominoRotation);	
			}
		}	
		this.printMatrix();
	}

	public void traslateToLeft() {
		if (this.game.traslateToLeft())
			this.matrixView.traslateToLeft();		
		this.printMatrix();
	}

	public void traslateToRight() {
		if (this.game.traslateToRight())
			this.matrixView.traslateToRight();	
		this.printMatrix();
	}

	public void traslateToBelow() {
		if (this.game.traslateToBelow())
			this.matrixView.traslateToBelow();
		else {
			//Controllare se si cancellano delle righe
			this.setNextTetromino();
			this.setCurrentLevel();
			this.setCurrentTetromino();
		}
		this.printMatrix();
	}
	
	/**
	 * Metodo per il debug
	 */
	public void printMatrix(){
		Log.v("debugForMe",this.textRender.renderingToString(this.game.getMatrix(), this.nextTetrominoName));
	}
	
}
