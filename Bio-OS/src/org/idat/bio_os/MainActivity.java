package org.idat.bio_os;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ToggleButton;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

/**
 * This is the main activity.
 * 
 * The device connection is handled from here and so is
 * most of the user interaction.
 * 
 * @author Nick Charlton <hello@nickcharlton.net>
 *
 */
public class MainActivity extends IOIOActivity {
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
	 * The thread where the IOIO activity happens.
	 * 
	 * setup() is called when the device is connected. loop() is then 
	 * called continuously.
	 */
	class Looper extends BaseIOIOLooper {
		/** The default LED. */
		private DigitalOutput led_;
		
		/**
		 * Called every time a connection with the IOIO is established.
		 * 
		 * @throws ConnectionLostException
		 * 			When the IOIO connection is lost.
		 */
		@Override
		protected void setup() throws ConnectionLostException {
			led_ = ioio_.openDigitalOutput(0, true);
		}
		
		/**
		 * Called continuously when connected.
		 * 
		 * @throws ConnectionLostException
		 * 			When the IOIO connection is lost.
		 */
		@Override
		public void loop() throws ConnectionLostException {
			led_.write(!button_.isChecked());
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	/**
	 * A method to build up the IOIO thread.
	 * 
	 * @return IOIOLooper instance.
	 */
	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}

}
