package it.iuland.tetris.activity;

import android.content.Intent;
import it.iuland.tetris.activity.MainActivity;

public class MainActivityTest extends android.test.ActivityUnitTestCase<MainActivity> {

	private MainActivity activity;

	public MainActivityTest(Class<MainActivity> activityClass) {
		super(activityClass);
	}
	
//	public MainActivityTest(Class<MainActivity> activityClass) {
//		super(MainActivity.class);
//	}


	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				MainActivity.class);
		startActivity(intent, null, null);
		activity = getActivity();
	}

	public void testOnCreateBundle() {
		assertEquals(true, true);
	}

}
