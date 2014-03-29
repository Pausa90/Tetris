package it.iuland.tetris;

import java.util.Locale;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.TextView;
import it.andclaval.tetris.model.TetrisGame;

public class GameManager {

	private MatrixView matrixView;
	private ImageView nextTetrominoView;
	private TextView levelView;
	private TetrisGame game;	
	private Resources resources;
	private String packageName;
	private String level;
	
	private int currentTetrominoRotation;
	private String currentTetrominoName;
	
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
	}
	
	public void setCurrentLevel() {
		this.levelView.setText(this.level + " " + this.game.getCurrentLevel());
	}
	
	public void setNextTetromino(){
		String nextTetrominoName = this.game.getNextTetromino();
		int drawableId = this.getResourcesID(nextTetrominoName);
		this.nextTetrominoView.setImageResource(drawableId);		
	}
	
	public void setCurrentTetromino(){
		this.currentTetrominoName = this.game.getCurrentTetrominoName();
		this.currentTetrominoName = this.pathFiltering(this.currentTetrominoName).toLowerCase(Locale.getDefault());
		this.currentTetrominoRotation = 0;
		int drawableId = this.getResourcesID(this.currentTetrominoName);
		this.matrixView.putTetromino(this.currentTetrominoName, drawableId);
	}
	
	private int getResourcesID(String name){
		name = this.pathFiltering(name).toLowerCase(Locale.getDefault());
		return this.resources.getIdentifier(name+this.currentTetrominoRotation, "drawable", this.packageName);
	}
	
	private String pathFiltering(String name){
		String[] splitted = name.split("\\.");
		return splitted[splitted.length - 1];
	}

	public void swypeUp() {
		if (this.game.rotateClockWise()){
			if (!this.currentTetrominoName.equals("tetromino_o")){
				if (this.currentTetrominoName.equals("tetromino_i") || 
						this.currentTetrominoName.equals("tetromino_s") || 
						this.currentTetrominoName.equals("tetromino_z"))
					this.currentTetrominoRotation = (this.currentTetrominoRotation + 1)%2;
				else
					this.currentTetrominoRotation = (this.currentTetrominoRotation + 1)%4;
				
				this.matrixView.rotateClockWise(this.getResourcesID(this.currentTetrominoName));	
			}
		}	
	}

	public void swypeLeft() {
		if (this.game.traslateToLeft())
			this.matrixView.traslateToLeft();		
	}

	public void swypeRight() {
		if (this.game.traslateToRight())
			this.matrixView.traslateToRight();	
	}

	public void swypeDown() {
		if (this.game.traslateToBelow())
			this.matrixView.traslateToBelow();
		else {
			//Controllare se si cancellano delle righe
			this.setNextTetromino();
			this.setCurrentLevel();
			this.setCurrentTetromino();
		}
	}
	
	
	
}
