package org.idat.bio_os;

import java.util.Collection;

import android.util.Log;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.IOIOConnection;
import ioio.lib.api.IOIOFactory;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.spi.IOIOConnectionFactory;
import ioio.lib.util.IOIOConnectionRegistry;

/**
 * This is the thread which handles IOIO interaction.
 * <br>
 * This is called by BioSingleton. Don't talk to it another way, you'll ruin
 * the nice abstraction.
 * <br>
 * It creates an IOIO instance, then waits until it can connect to it. Once it
 * connects, it continues to read data from it until it's told otherwise.
 * <br>
 * The data is stored in the public variables. Use synchronized() when 
 * accessing these.
 * <br>
 * If the connection fails, it will reconnect itself, unless told to abort.
 * 
 * @author Nick Charlton
 *
 */
public class IOIOThread extends Thread {
	/** The IOIO instance. */
	private IOIO ioio_;
	/** The abort flag. */
	private boolean abort_ = false;
	/** Hold the device connection. */
	private IOIOConnection ioioConnection_;
	/** The Status LED. */
	private DigitalOutput led_;
	
	/*
	 * These below are configurable outside of the thread.
	 * You should talk to BioSingleton about this though.
	 * He's a bit protective like that.
	 */
	public boolean is_connected = false;
	public boolean led_on = true;
	
	static {
		IOIOConnectionRegistry.addBootstraps(new String[] {
			"ioio.lib.android.accessory.AccessoryConnectionBootstrap",
			"ioio.lib.android.bluetooth.BluetoothIOIOConnectionBootstrap"
		});
	}
	
	/**
	 * Setup
	 * 
	 * Use this to setup the pins.
	 * 
	 * @throws ConnectionLostException
	 * 			If the IOIO is disconnected or lost.
	 */
	public void setup(IOIO ioio) throws ConnectionLostException {
		synchronized(this) {
			is_connected = true;
		}
		
		led_ = ioio.openDigitalOutput(IOIO.LED_PIN);
	}
	
	/**
	 * Loop
	 * 
	 * Use this to interact continuously.
	 * 
	 * @throws ConnectionLostException
	 * 			If the IOIO is disconnected or lost.
	 */
	public void loop() throws ConnectionLostException {
		// the LED goes high when low, so do the opposite of the variable.
		led_.write(!led_on);
	}
	
	/**
	 * The thread itself.
	 */
	@Override
	public void run() {
		super.run();
		
		while(true) {
			synchronized(this) {
				if (abort_) {
					break;
				}
				
				Log.i("BioOS", "Attempting to create the IOIO device.");
				
				Collection<IOIOConnectionFactory> connectionFactories = IOIOConnectionRegistry.getConnectionFactories();
				
				for (IOIOConnectionFactory factory : connectionFactories) {
					if (factory.getType().contentEquals("ioio.lib.android.bluetooth.BluetoothIOIOConnection")) {
						ioioConnection_ = factory.createConnection();
					}
				}
				
				ioio_ = IOIOFactory.create(ioioConnection_);
			}
			
			try {
				ioio_.waitForConnect();
				
				// do the setup
				setup(ioio_);
				
				// do the hardware loop
				while (!abort_) {
					loop();
					sleep(100);
				}
			} catch (ConnectionLostException e) {
				// do something about a lost connection
				Log.e("BioOS", "Connection lost", e);
			} catch (Exception e) {
				Log.e("BioOS", "Unexcepted exception caught", e);
				ioio_.disconnect();
				break;
			} finally {
				if (ioio_ != null) {
					try {
						Log.i("BioOS", "Disconnecting the hardware.");
						ioio_.waitForDisconnect();
						
						synchronized(this) {
							is_connected = false;
						}
						
					} catch (InterruptedException e) {
						// i guess the disconnect was interrupted?
					}
				}
				
				synchronized(this) {
					ioio_ = null;
				}
			}
		}
	}
	
	/**
	 * Abort the connection.
	 */
	synchronized public void abort() {
		Log.i("BioOS", "Aborting the IOIOThread.");
		abort_ = true;
		
		if (ioio_ != null) {
			ioio_.disconnect();
			
			synchronized(this) {
				is_connected = false;
			}
		}
	}
}
