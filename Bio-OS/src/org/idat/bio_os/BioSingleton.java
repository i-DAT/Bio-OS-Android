package org.idat.bio_os;

/**
 * A singleton instance for accessing the hardware. It manages a single thread
 * which is used to request (and send) data from (to) the IOIO board. 
 * 
 * It's a singleton to allow constant data updates (without flinging the 
 * hardware thread around) but allowing you to access it from more than one 
 * Activity.
 * 
 * @author Nick Charlton
 *
 */
public class BioSingleton {
	/**
	 * A set of possible IOIO connection types.
	 */
	public enum IOIOConnectionType {
		NONE,
		USB,
		BLUETOOTH
	}
	
	/**
	 * Hold on to a firm reference to this class.
	 *
	 */
	private static class BioSingletonHolder {
        public static BioSingleton bio = new BioSingleton();
    }

	/**
	 * Return the BioSingleton instance.
	 * 
	 * @return The current BioSingleton instance.
	 */
    public static BioSingleton getBioSingleton() {
        return BioSingletonHolder.bio;
    }
    
    /**
     * Start the hardware thread.
     */
    public void start() {
    	
    }
    
    /**
     * Stop the hardware thread.
     */
    public void stop() {
    	
    }
    
    /**
     * Get the hardware connection status.
     * 
     * @return true if connected, false if not.
     */
    public boolean isConnected() {
    	return false;
    }
    
    /**
     * Get the hardware connection type.
     */
    public IOIOConnectionType getConnectionType() {
		return IOIOConnectionType.NONE;
    }
}
