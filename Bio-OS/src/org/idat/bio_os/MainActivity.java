package org.idat.bio_os;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ToggleButton;

/**
 * This is the main activity.
 * 
 * The device connection is handled from here and so is
 * most of the user interaction.
 * 
 * @author Nick Charlton <hello@nickcharlton.net>
 *
 */
public class MainActivity extends Activity {
	/** The main button */
	private ToggleButton button_;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button_ = (ToggleButton)findViewById(R.id.button);
	}
	
	/**
	 * Called when the application is resumed and first started.
	 * 
	 * This is the first call to the Singleton, setting up the hardware
	 * access.
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/**
	 * Called when the application is paused.
	 * 
	 * Tell the Singleton that it should stop accessing the hardware.
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * Creates the menu which is invoked by a menu button.
	 * 
	 * @return true if there is a menu to be expanded.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
