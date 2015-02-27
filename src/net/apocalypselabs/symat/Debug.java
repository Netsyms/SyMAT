/* 
 * This class is public domain because it is standalone and 
 * is potentially useful to other projects.
 * 
 * If this cannot be placed in the public domain, it is licensed under
 * the Creative Commons Zero license.
 * 
 * Feel free to use it however you wish!
 */
package net.apocalypselabs.symat;

/**
 * This class handles debug logging, so it's easy to disable.
 *
 * @author Skylar
 */
public class Debug {

    // If output should be on or off
    public static boolean debug = false;
    
    /**
     * Turn debug output on and off.
     * @param b 
     */
    public static void setDebug(boolean b) {
        debug = b;
    }
    
    /**
     * Check if debug output is enabled.
     * @return true if it is.
     */
    public static boolean getDebug() {
        return debug;
    }

    /**
     * Call System.out.println(data) if debug output enabled.
     * @param data Data to print.
     */
    public static void println(Object data) {
        if (debug) {
            System.out.println(data);
        }
    }

    /**
     * Call System.err.println(data) if debug output enabled.
     * @param data Data to print.
     */
    public static void printerr(Object data) {
        if (debug) {
            System.err.println(data);
        }
    }

    /**
     * Call e.printStackTrace() if debug output enabled.
     * @param e an Exception.
     */
    @SuppressWarnings(value = {"CallToPrintStackTrace"})
    public static void stacktrace(Exception e) {
        if (debug) {
            e.printStackTrace();
        }
    }
}
