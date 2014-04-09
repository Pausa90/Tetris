package it.iuland.tetris.gesture;

import it.iuland.tetris.GameManager;
import android.content.Context;
import android.content.res.Resources;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureManager implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
	
	//Gesture Variables
	private GestureDetector gestureDetector; 
	private static final int SWIPE_MIN_DISTANCE = 60;
	private static final int SWIPE_THRESHOLD_VELOCITY = 150;
	private int swype_min;
	private int swype_min_vel;
	private Resources resources;
	private GameManager gameManager;
	private boolean gameOver = false;

	public GestureManager(GameManager gameManager, Context applicationContext) {
		this.gestureDetector = new GestureDetector(applicationContext, this);
		this.gameManager = gameManager;
		this.resources = applicationContext.getResources();
		//Inizializzo le variabili di istanza
		this.swype_min = (int) dp2px(SWIPE_MIN_DISTANCE);
		this.swype_min_vel = (int) dp2px(SWIPE_THRESHOLD_VELOCITY);
	}

	/** Gesture **/

	private float dp2px(int dip) {
		float scale = this.resources.getDisplayMetrics().density;
		return dip * scale + 0.5f;
	}

	public boolean onTouchEvent(MotionEvent event){ 
		this.gestureDetector.onTouchEvent(event);
		return true;
	}
	
	public void setGameOver(){
		this.gameOver  = true;
	}


	@Override
	//Intercetta l'evento di swype
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
		if (this.gameOver)
			return false;
		try {
			//Se mi sono mosso in vericale
			if (Math.abs(event1.getY() - event2.getY()) > this.swype_min){

				if (this.isUpSwype(event1,event2,velocityY)){
					//this.gameManager.rotateClockWise();
				}
				else if (this.isDownSwype(event1,event2,velocityY))
					this.gameManager.traslateToBelow();
			}
			//Se mi sono mosso in orizzontale
			else if (Math.abs(event1.getX() - event2.getX()) > this.swype_min){
				if (this.isLeftSwype(event1,event2,velocityX))
					this.gameManager.traslateToLeft();
				else if (this.isRightSwype(event1,event2,velocityX))
					this.gameManager.traslateToRight();
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

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		this.gameManager.rotateClockWise();
		return true;
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
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	

}