package it.iuland.tetris.activity;

import it.iuland.tetris.gesture.GestureManager;
import it.iuland.tetris.GameManager;
import it.iuland.tetris.R;
import it.iuland.tetris.view.MatrixView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayActivity extends Activity {
	
	private GameManager gameManager;

	//View Variables
	private Handler frame = new Handler();//Divide the frame by 1000 to calculate how many times per second the screen will update.
	private static final int FRAME_RATE = 20; //50 frames per second
	
	private GestureManager gestureManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		this.initGameManager();
		this.gestureManager = new GestureManager(gameManager, getApplicationContext());		
		
		//Imposto l'update grafico
		frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);        
	}

	private Runnable frameUpdate = new Runnable() {
		@Override
		synchronized public void run() {
			frame.removeCallbacks(frameUpdate);
			((MatrixView)findViewById(R.id.matrix)).invalidate();
			frame.postDelayed(frameUpdate, FRAME_RATE);
		}
	};

	private void initGameManager() {
		MatrixView matrixView = (MatrixView)findViewById(R.id.matrix);
		ImageView nextTetromino = (ImageView) findViewById(R.id.next_tetromino);
		TextView level = (TextView) findViewById(R.id.level);
		this.gameManager = new GameManager(matrixView, nextTetromino, level, this.getResources(), this.getPackageName());		
	}
	
	@Override 
	public boolean onTouchEvent(MotionEvent event){ 
		this.gestureManager.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

}
