package it.andclaval.tetris.render;

import it.andclaval.tetris.model.Matrix;

import java.util.Scanner;

public class TextRendering {
	
	public void rendering(Matrix m_class, String nextTetromino){
		int[][] matrix = m_class.getMatrix();
		//scrivo gli indici delle colonne
		System.out.println("Lv: " + m_class.getLevel() + " Righe per lv up: " + m_class.getRowsToLevelUp());
		System.out.println("Score: " + m_class.getScore());
		System.out.println("Combo: #linee = " + m_class.getCombo() + " #hit = " + m_class.getComboInfo()[1]);
		System.out.println("Next Tetromino: " + nextTetromino);
		int i;
		System.out.print("\t");
		for (i=0; i<matrix[0].length; i++){
			System.out.print(i + "\t");
		}
		System.out.println();
		
		i=0;
		for (int r=0; r<matrix.length; r++){
			System.out.print(i + "\t");
			for (int c=0; c<matrix[0].length; c++){
				System.out.print(matrix[r][c] + "\t");
			}
			System.out.println();
			i++;
		}
		
		System.out.println();
		System.out.println();
	}
	
	public String renderingToString(Matrix m_class, String nextTetromino){
		int[][] matrix = m_class.getMatrix();
		String out = "";
		//scrivo gli indici delle colonne
		out += "Lv: " + m_class.getLevel() + " Righe per lv up: " + m_class.getRowsToLevelUp() +"\n";
		out += "Score: " + m_class.getScore() + "\n";
		out += "Combo: #linee = " + m_class.getCombo() + " #hit = " + m_class.getComboInfo()[1]+ "\n";
		out += "Current Tetromino: " + m_class.getCurrentTetromino() + "\n";
		out += "Next Tetromino: " + nextTetromino + "\n";
		int i;
		out += "\t";
		for (i=0; i<matrix[0].length; i++){
			out += i + "\t";
		}
		out += "\n";
		
		i=0;
		for (int r=0; r<matrix.length; r++){
			out += i + "\t";
			for (int c=0; c<matrix[0].length; c++){
				out += matrix[r][c] + "\t";
			}
			out += "\n";
			i++;
		}
		out += "\n";
		return out;
	}
	
	public String input(){
		Scanner scannerDiLinee = new Scanner(System.in);	
		return scannerDiLinee.nextLine();
	}
	

	
}
