package org.idat.bio_os;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
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
	/** Hardware update timer. */
	private Timer update_timer_;
	private boolean previous_connection_state = false;

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
		
		// start the hardware update loop
		update_timer_ = new Timer();
		update_timer_.schedule(new TimerTask() {
			@Override
			public void run() {
				timer_invocation();
			}
		}, 0, 100);
	}
	
	/**
	 * Hardware Update Timer Loop
	 * 
	 * This pools the BioSingleton instance to check for hardware updates.
	 * Once this is invoked, all of the fields will be updated and Toasts will
	 * be sent for hardware connections/disconnections.
	 */
	private Runnable timer_tick = new Runnable() {
		public void run() {
			// check for and notify about connection changes
			boolean connection_state = bio_.isConnected();
			
			if (previous_connection_state != connection_state) {
				// toast
				if (connection_state) {
					Toast.makeText(getApplicationContext(), "IOIO Connected", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "IOIO Disconnected", Toast.LENGTH_LONG).show();
				}
				
				// mark as the last
				previous_connection_state = connection_state;
			}
			
			// read data and update fields
			
			// send commands
			bio_.setLedOn(button_.isChecked());
		}
	};
	
	/**
	 * Called when the application is paused.
	 * 
	 * Tell the Singleton that it should stop accessing the hardware.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		
		// stop the hardware loop
		update_timer_.cancel();
		
		// and stop the hardware thread
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
	
	/**
	 * Method Invoked by Timer
	 * 
	 * This just calls the method below. This is so that the current activity
	 * can be referred to.
	 */
	private void timer_invocation() {
		this.runOnUiThread(timer_tick);
	}
}
