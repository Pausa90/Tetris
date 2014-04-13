package it.iuland.tetris;

import java.io.IOException;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.TextView;
import it.andclaval.tetris.model.TetrisGame;
import it.andclaval.tetris.render.TextRendering;
import it.iuland.tetris.activity.PlayActivity;
import it.iuland.tetris.view.MatrixView;

public class GameManager {

	private MatrixView matrixView;
	private ImageView nextTetrominoView;
	private TextView levelView;
	private TextView scoreView;
	private TetrisGame game;	
	private Resources resources;
	private String packageName;
	private String level;
	private String score;

	private int currentTetrominoRotation;
	private int currentTetrominoResource;
	private String currentTetrominoName;
	private it.iuland.tetris.Log log;

	/**
	 * Per debug
	 */
	private TextRendering textRender;
	private String nextTetrominoName;
	private PlayActivity activity;

	public GameManager(MatrixView matrixView, ImageView nextTetromino, TextView level, TextView score, Resources resources, String packageName){
		this.matrixView = matrixView;
		this.matrixView.setController(this);
		this.log = new it.iuland.tetris.Log();
		this.matrixView.setLog(this.log);
		this.nextTetrominoView = nextTetromino;
		this.levelView = level;
		this.scoreView = score;
		this.game = new TetrisGame(this);
		this.game.startGame();
		this.resources = resources;
		this.packageName = packageName;
		this.level = (String) this.levelView.getText();
		this.score = (String) this.scoreView.getText();
		this.setCurrentLevel();
		this.setCurrentScore();
		this.setNextTetromino();
		this.setCurrentTetromino();

		this.textRender = new TextRendering();
		this.printMatrix();
	}

	private void setCurrentScore() {
		this.updateCurrentScore(this.game.getScore());	
	}

	public void updateCurrentScore(int score){
		this.scoreView.setText(this.score + " " + score);
	}

	private void setCurrentLevel() {
		this.updateCurrentLevel(this.game.getCurrentLevel());	
	}

	public void updateCurrentLevel(int level){
		this.levelView.setText(this.level + " " + level);
	}

	public void setNextTetromino(){
		this.nextTetrominoName = this.game.getNextTetromino();
		this.nextTetrominoName = this.pathFiltering(this.nextTetrominoName).toLowerCase(Locale.getDefault());
		int drawableId = this.getResourcesID(this.nextTetrominoName);
		this.nextTetrominoView.setImageResource(drawableId);				
		this.log.toLog(this, "Next Tetromino name " + this.nextTetrominoName + " id: " + drawableId);  //LOG
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
			this.log.toLog(this, "rotateClockWise");
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
		if (this.game.traslateToLeft()){
			this.log.toLog(this, "traslateToLeft");
			this.matrixView.traslateToLeft();		
		}
		this.printMatrix();
	}

	public void traslateToRight() {
		if (this.game.traslateToRight()){	
			this.log.toLog(this, "traslateToRight");		
			this.matrixView.traslateToRight();
		}
		this.printMatrix();
	}

	public boolean traslateToBelow() {
		if (this.game.traslateToBelow()){
			this.log.toLog(this, "traslateToBelow");
			this.matrixView.traslateToBelow();
			this.printMatrix();
			return true;
		}
		else {
			if (this.game.isEnd())
				this.activity.gameOver();
			else {
				this.setNextTetromino();
				this.setCurrentLevel();
				this.setCurrentTetromino();
			}
		}
		this.printMatrix();
		return false;
	}

	public void updateAfterRowsCleaned(int[] rowsToClean) {
		this.log.toLog(this, "updateAfterRowsCleaned:" + rowsToClean.toString());
		this.matrixView.rowsCleaned(rowsToClean);
	}

	/**
	 * Metodo per il debug
	 */
	public void printMatrix(){
		this.log.toLog(this, "Tetris Model: " + this.textRender.renderingToString(this.game.getMatrix(), this.nextTetrominoName));
	}

	public int getBitmapID(String name) {
		return this.resources.getIdentifier(name, "drawable", this.packageName);
	}

	public boolean saveLog(Context context, String message) throws IOException{
		return this.log.save(context, message);
	}

	public void setActivity(PlayActivity activity) {
		this.activity = activity;		
	}


}
