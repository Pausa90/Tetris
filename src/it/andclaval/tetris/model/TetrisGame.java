package it.andclaval.tetris.model;

import it.andclaval.tetris.model.tetromino.*;
import it.iuland.tetris.GameManager;

public class TetrisGame {
	/** da fare singleton **/
	private Matrix matrix;
	private boolean end;
	private Tetromino current;
	private Tetromino next;
	private GameManager controller;

	public TetrisGame(GameManager gameManager){
		this.matrix = new Matrix(this);
		this.end = false;
		this.controller = gameManager;
	}
	
	public boolean isEnd() {
		return end;
	}
	
	public void setEnd(boolean end) {
		this.end = end;
	}
	
	public Matrix getMatrix() {
		return matrix;
	}
	
	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
	
	public String getCurrentTetrominoName(){
		return this.current.toString();
	}
	
	public int getCurrentLevel(){
		return this.matrix.getLevel();
	}
	
	public String getNextTetromino(){
		return this.next.toString();
	}

	/** Metodo di avvio del gioco **/
	public void startGame(){
		this.next = this.createRandomTetromino();
		this.startNextTetromino();
	}
	
	public void startNextTetromino(){
		this.current = this.next;
		this.next = this.createRandomTetromino();
		this.matrix.putTetromino(current);
	}
	
	public boolean update(){
		return this.matrix.update();
	}
	
	public boolean rotateClockWise(){
		return this.matrix.rotateCurrent(true);
	}
	
	public boolean rotateAntiClockWise(){
		return this.matrix.rotateCurrent(false);
	}
	
	public boolean traslateToLeft(){
		return this.matrix.traslateCurrent(true);
	}
	
	public boolean traslateToRight(){
		return this.matrix.traslateCurrent(false);
	}
	
	public int getScore(){
		return this.matrix.getScore();
	}
	
	
	
	/**
	 * Returna true se è possibile scendere, false se viene creato un nuovo tetromino 
	 */
	public boolean traslateToBelow(){
		//riazzerare il tempo
		return this.matrix.update();
	}
	
	/** Se il pezzo deve cadere giù **/
	public void freeFall(){
		boolean isFallen;
		do{
			isFallen = this.matrix.update();
		} while (isFallen);
	}

	/* da inserire una creazione con probabilità */
	private Tetromino createRandomTetromino() {
		int rand = (int) (7*Math.random()) + 1;
		switch (rand){
			case 1: 
				return new Tetromino_I();
			case 2: 
				return new Tetromino_J();
			case 3: 
				return new Tetromino_L();
			case 4: 
				return new Tetromino_O();
			case 5: 
				return new Tetromino_S();
			case 6: 
				return new Tetromino_T();
			case 7: 
				return new Tetromino_Z();
		}
		return null;
	}
	
	/*
	 * Due TetrisGame sono uguali se hanno end uguale e la stessa matrice
	 */
	public boolean equals(Object o){
		TetrisGame tetrisGame = (TetrisGame) o;
		return this.matrix.equals(tetrisGame.getMatrix()) && this.end == tetrisGame.end; 
	}

	/** 	Aggiunti per aggiornare la View **/
	
	public void updateLevelView(int level) {
		this.controller.updateCurrentLevel(level);
	}
	
	public void updateScoreView(int score){
		this.controller.updateCurrentScore(score);
	}

	public void updateAfterRowsCleaned(int[] rowsToClean) {
		this.controller.updateAfterRowsCleaned(rowsToClean);
		
	}
}
