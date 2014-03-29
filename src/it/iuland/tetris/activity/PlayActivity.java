package it.iuland.tetris.activity;

import it.iuland.tetris.GameManager;
import it.iuland.tetris.MatrixView;
import it.iuland.tetris.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity 
implements GestureDetector.OnGestureListener{
	
	private GameManager gameManager;

	//Gesture Variables
	private GestureDetector gestureDetector; 
	private static final int SWIPE_MIN_DISTANCE = 60;
	private static final int SWIPE_THRESHOLD_VELOCITY = 150;
	private int swype_min;
	private int swype_min_vel;

	//View Variables
	private Handler frame = new Handler();//Divide the frame by 1000 to calculate how many times per second the screen will update.
	private static final int FRAME_RATE = 20; //50 frames per second
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		this.initGameManager();
		this.initGestures();		
		
		//Imposto l'update grafico
		frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);        
	}
	
	private Runnable frameUpdate = new Runnable() {
		@Override
		synchronized public void run() {
			frame.removeCallbacks(frameUpdate);
			//make any updates to on screen objects here
			//then invoke the on draw by invalidating the canvas
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

	private void initGestures(){
		this.gestureDetector = new GestureDetector(this,this);

		//Inizializzo le variabili di istanza
		this.swype_min = (int) dp2px(SWIPE_MIN_DISTANCE);
		this.swype_min_vel = (int) dp2px(SWIPE_THRESHOLD_VELOCITY);
	}
	
	/** Gesture **/

	private float dp2px(int dip) {
		float scale = getResources().getDisplayMetrics().density;
		return dip * scale + 0.5f;
	}


	@Override 
	public boolean onTouchEvent(MotionEvent event){ 
		this.gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	//Intercetta l'evento di swype
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
		try {
			//Se mi sono mosso in vericale
			if (Math.abs(event1.getY() - event2.getY()) > this.swype_min){

				if (this.isUpSwype(event1,event2,velocityY))
					//Toast.makeText(getApplicationContext(), "Swipe in alto", Toast.LENGTH_SHORT).show();
					this.gameManager.swypeUp();
				else if (this.isDownSwype(event1,event2,velocityY))
					//Toast.makeText(getApplicationContext(), "Swipe in basso", Toast.LENGTH_SHORT).show();
					this.gameManager.swypeDown();
			}
			//Se mi sono mosso in orizzontale
			else if (Math.abs(event1.getX() - event2.getX()) > this.swype_min){
				if (this.isLeftSwype(event1,event2,velocityX))
					//Toast.makeText(getApplicationContext(), "Swipe a sinistra", Toast.LENGTH_SHORT).show();
					this.gameManager.swypeLeft();
				else if (this.isRightSwype(event1,event2,velocityX))
					//Toast.makeText(getApplicationContext(), "Swipe a destra", Toast.LENGTH_SHORT).show();
					this.gameManager.swypeRight();
			}
		} catch (Exception e) {
			//Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	private boolean isUpSwype(MotionEvent event1, MotionEvent event2, float velocityY) {
		if (event1.getY() - event2.getY() > this.swype_min
				&& Math.abs(velocityY) > this.swype_min_vel)
			return true;
		return false;
	}

	private boolean isDownSwype(MotionEvent event1, MotionEvent event2, float velocityY) {
		if (event2.getY() - event1.getY() > this.swype_min
				&& Math.abs(velocityY) > this.swype_min_vel)
			return true;
		return false;
	}


	private boolean isLeftSwype(MotionEvent event1, MotionEvent event2, float velocityX){
		if (event1.getX() - event2.getX() > this.swype_min
				&& Math.abs(velocityX) > this.swype_min_vel)
			return true;
		return false;
	}

	private boolean isRightSwype(MotionEvent event1, MotionEvent event2, float velocityX){
		if (event2.getX() - event1.getX() > this.swype_min
				&& Math.abs(velocityX) > this.swype_min_vel) 
			return true;
		return false;
	}







	/** Non utili ai fini dell'app **/

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}