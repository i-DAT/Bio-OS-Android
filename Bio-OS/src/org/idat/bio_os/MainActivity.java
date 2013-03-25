package org.idat.bio_os;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
	/** A reference to the BioSingleton instance. */
	private BioSingleton bio_;
	/** The main button */
	private ToggleButton button_;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// this creates the singleton the first time.
		bio_ = BioSingleton.getBioSingleton();
		
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
		bio_.start();
	}
	
	/**
	 * Called when the application is paused.
	 * 
	 * Tell the Singleton that it should stop accessing the hardware.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		bio_.stop();
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
	
	/**
	 * Responds to menu taps.
	 * 
	 * @return true if there is an action to be performed.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			// show the about activity
			return true;
		}
		
		return false;
	}

}
