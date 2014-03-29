package it.iuland.tetris;

import android.widget.ImageView;
import android.widget.TextView;
import it.andclaval.tetris.model.TetrisGame;

public class GameManager {

	private MatrixView matrixView;
	private ImageView nextTetrominoView;
	private TextView levelView;
	private TetrisGame game;
	
	public GameManager(MatrixView matrixView, ImageView nextTetromino, TextView level){
		this.matrixView = matrixView;
		this.nextTetrominoView = nextTetromino;
		this.levelView = level;
		this.game = new TetrisGame();
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
