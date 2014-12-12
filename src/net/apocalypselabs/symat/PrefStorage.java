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

import java.util.prefs.Preferences;

/**
 *
 * @author Skylar Ittner
 */
public class PrefStorage {

    private static Preferences prefs = Preferences.userNodeForPackage(PrefStorage.class);

    public static void saveSetting(String key, String value) {
        prefs.put(key, value);
    }

    public static boolean isset(String key) {
        if (!getSetting(key, "NULL").equals("NULL")) {
            return true;
        }
        return false;
    }

    public static String getSetting(String key) {
        return prefs.get(key, "");
    }

    public static String getSetting(String key, String emptyResponse) {
        return prefs.get(key, emptyResponse);
    }

    // xkcd 221 compliance.
    int getRandomNumber() {
        return 4; // chosen by fair dice roll.
        // guaranteed to be random.
    }
}
