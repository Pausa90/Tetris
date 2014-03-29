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
	
	public GameManager(MatrixView matrixView, ImageView nextTetromino, TextView level, Resources resources, String packageName){
		this.matrixView = matrixView;
		this.nextTetrominoView = nextTetromino;
		this.levelView = level;
		this.game = new TetrisGame();
		this.game.startGame();
		this.resources = resources;
		this.packageName = packageName;
		this.setCurrentLevel();
		this.setCurrentTetromino();
	}
	
	public void setCurrentLevel() {
		this.levelView.setText(this.levelView.getText() + " " + this.game.getCurrentLevel());
	}
	
	public void setCurrentTetromino(){
		String currentTetrominoName = this.game.getCurrentTetrominoName();
		currentTetrominoName = this.pathFiltering(currentTetrominoName).toLowerCase(Locale.getDefault());
		int drawableId = this.resources.getIdentifier(currentTetrominoName, "drawable", this.packageName);
		this.nextTetrominoView.setImageResource(drawableId);
	}
	
	private String pathFiltering(String name){
		String[] splitted = name.split("\\.");
		return splitted[splitted.length - 1];
	}

	public void swypeUp() {
		// TODO Auto-generated method stub
		
	}
	
	public void swypeDown() {
		// TODO Auto-generated method stub
		
	}

	public void swypeLeft() {
		// TODO Auto-generated method stub
		
	}

	public void swypeRight() {
		// TODO Auto-generated method stub
		
	}


	
	
	
}
