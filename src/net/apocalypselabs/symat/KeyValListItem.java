/*
 * Apocalypse Laboratories
 * Open Source License
 * 
 * Source code can be used for any purpose, as long as:
 *  - Compiled binaries are rebranded and trademarks are not
 *    visible by the end user at any time, except to give
 *    credit to Apocalypse Laboratories, such as by showing
 *    "Based on <product> by Apocalypse Laboratories" or a
 *    similar notice;
 *  - You do not use the code for evil;
 *  - Rebranded compiled applications have significant
 *    differences in functionality;
 *  - and you provide your modified source code for download,
 *    under the terms of the GNU LGPL v3 or a comparable
 *    license.
 * 
 * Compiled binaries cannot be redistributed or mirrored,
 * unless:
 *  - You have written permission from Apocalypse Laboratories;
 *  - Downloads are not available from Apocalypse Laboratories,
 *    not even behind a paywall or other blocking mechanism;
 *  - or you have received a multi-computer license, in which
 *    case you should take measures to prevent unauthorized
 *    downloads, such as preventing download access from the 
 *    Internet.
 */
package net.apocalypselabs.symat;

/**
 * A simple key/value pair for lists and stuff.
 * @author Skylar
 */
public class KeyValListItem {
    // I know, not Java code standards.
    // But it's easier and cleaner this way.
    private String VAL = "";
    private String KEY = "";
    
    public KeyValListItem() {
        
    }
    
    public KeyValListItem(String key) {
        KEY = key;
    }
    
    public KeyValListItem(String key, String val) {
        KEY = key;
        VAL = val;
    }
    
    /**
     * Get the key.
     * @return the key.
     */
    @Override
    public String toString() {
        return KEY;
    }
    
    /**
     * Get the value of this pair.
     * @return duh.
     */
    public String getValue() {
        return VAL;
    }
    
    /**
     * Set the key.
     * <br>To get it back use toString().
     * @param key the key.
     */
    public void setKey(String key) {
        KEY = key;
    }
    
    /**
     * Set the value for this pair.
     * @param value 
     */
    public void setValue(String value) {
        VAL = value;
    }
    
    /**
     * Is this pair populated?
     * @return True if key and value are empty.
     */
    public boolean isEmpty() {
        return (KEY.equals("") && VAL.equals(""));
    }
}
