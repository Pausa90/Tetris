package it.iuland.tetris.activity;

import it.iuland.tetris.gesture.GestureManager;
import it.iuland.tetris.GameManager;
import it.iuland.tetris.R;
import it.iuland.tetris.view.MatrixView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayActivity extends Activity {
	
	private GameManager gameManager;
	private GestureManager gestureManager;

	//View Variables
	private Handler frame = new Handler();
	private static final int FRAME_RATE = 20; //50 frames per second
	private Handler update = new Handler();
	private int UPDATE_RATE = 1000;
	
	private boolean stopUpdateThread = false;
	private boolean stopFrameThread = false;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		this.initGameManager();
		this.gestureManager = new GestureManager(gameManager, getApplicationContext());	
		
		//Gestisco la checkbox
		this.setCheckBoxTask();
		
		//Imposto l'update grafico
		frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);        
        update.removeCallbacks(updateRunnable);
        update.postDelayed(updateRunnable, UPDATE_RATE);
	}
	
	private void setCheckBoxTask(){
		CheckBox fallingBox = (CheckBox) this.findViewById(R.id.fallingOnOff);
		fallingBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!((CheckBox) v).isChecked()){
					stopUpdateThread = true;
					update.removeCallbacks(updateRunnable);
				}
				else {
					if (stopUpdateThread == true){
						stopUpdateThread = false;
				        update.postDelayed(updateRunnable, UPDATE_RATE);
					}
				}					
			}
		});
	}

	private Runnable frameUpdate = new Runnable() {
		@Override
		synchronized public void run() {
			frame.removeCallbacks(frameUpdate);
			((MatrixView)findViewById(R.id.matrix)).invalidate();
			if (!stopFrameThread)
				frame.postDelayed(frameUpdate, FRAME_RATE);
		}
	};
	
	private Runnable updateRunnable = new Runnable() {
		@Override
		synchronized public void run() {
			update.removeCallbacks(updateRunnable);
			gameManager.traslateToBelow();
			((MatrixView)findViewById(R.id.matrix)).invalidate();
			if (!stopUpdateThread)
				update.postDelayed(updateRunnable, UPDATE_RATE);
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
	
	@Override
	protected void onPause() {
		super.onPause();
		this.stopUpdateThread = true;
	}

}
