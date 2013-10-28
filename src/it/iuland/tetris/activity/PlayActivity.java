package it.iuland.tetris.activity;

import it.iuland.tetris.R;
import it.iuland.tetris.R.layout;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.widget.Toast;

public class PlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_LONG;

        int action = MotionEventCompat.getActionMasked(event);

        switch (action){
            case (MotionEvent.ACTION_DOWN):
                text = "down"; break;
            case (MotionEvent.ACTION_UP):
                text = "up"; break;
            case (MotionEvent.ACTION_MOVE):
                text = "move"; break;
            case (MotionEvent.ACTION_OUTSIDE):
                text = "outside"; break;
            default: return super.onTouchEvent(event);
        }

        Toast toast = Toast.makeText(context,text,duration);
        toast.show();

        return true;
    }
    
}