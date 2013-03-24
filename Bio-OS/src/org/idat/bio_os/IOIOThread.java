package org.idat.bio_os;

import ioio.lib.api.IOIO;

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
	
	/**
	 * The thread itself.
	 */
	@Override
	public void run() {
		super.run();
	}
	
	/**
	 * Abort the connection.
	 */
	synchronized public void abort() {
		abort_ = true;
		
		if (ioio_ != null) {
			ioio_.disconnect();
		}
	}
}
