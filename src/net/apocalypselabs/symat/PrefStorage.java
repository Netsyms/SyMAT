/* 
 * Copyright (c) 2015, Netsyms Technologies
 * All rights reserved.
 * 
 * 
 * CODE LICENSE ==========
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 * may be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * 4. You adhere to the Media License detailed below.  If you do not, this license
 * is automatically revoked and you must purge all copies of the software you
 * possess, in source or binary form.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 * MEDIA LICENSE ==========
 * All images and other graphical files ("media") included with this
 * software are copyright (c) 2015 Netsyms Technologies.  You may not distribute
 * the graphics or any program, source code repository, or other digital storage 
 * media containing them without permission from Netsyms Technologies.
 * This ban on distribution only applies to publicly available systems.
 * A password-protected network file share, USB drive, or other storage scheme that
 * cannot be easily accessed by the public is generally allowed.  If in doubt,
 * contact Netsyms Technologies.  If Netsyms Technologies allows or denies
 * you permission, that decision is considered final and binding.
 * 
 * You may only use the media for personal, 
 * non-commercial, non-educational use unless: 
 * 1, You have paid for the software and media via the SyMAT website,
 * or 2, you are using it as part of the 15-day trial period.  
 * Other uses are prohibited without permission.
 * If any part of this license is deemed unenforcable, the remainder 
 * of the license remains in full effect.
 */
package net.apocalypselabs.symat;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Skylar Ittner
 */
public class PrefStorage {

    private static final Preferences prefs = Preferences.userNodeForPackage(PrefStorage.class);

    /**
     *
     * @param key
     * @param value
     */
    public static void saveSetting(String key, String value) {
        prefs.put(key, value);
    }

    /**
     *
     * @param key
     * @return
     */
    public static boolean isset(String key) {
        return !getSetting(key, "NULL").equals("NULL");
    }

    /**
     *
     * @param key
     */
    public static void unset(String key) {
        saveSetting(key, "");
        save();
        prefs.remove(key);
        save();
    }

    /**
     *
     * @param key
     * @return
     */
    public static String getSetting(String key) {
        return prefs.get(key, "");
    }

    /**
     *
     * @param key
     * @param emptyResponse
     * @return
     */
    public static String getSetting(String key, String emptyResponse) {
        return prefs.get(key, emptyResponse);
    }

    /**
     *
     * @return
     */
    public static boolean save() {
        try {
            prefs.flush();
            prefs.sync();
        } catch (BackingStoreException ex) {
            System.err.println("Settings could not be saved!");
            return false;
        }
        return true;
    }

    /**
     * Wipe all settings.
     *
     * @throws java.util.prefs.BackingStoreException
     */
    public static void wipe() throws BackingStoreException {
        prefs.clear();
    }
    
    /**
     * Get all settings in a List for a Sync.
     * @return List of NameValuePairs.
     * @throws BackingStoreException 
     */
    public static List syncdump() throws BackingStoreException {
        String[] keys = prefs.keys();
        List<NameValuePair> nvps = new ArrayList<>();
        for (String key : keys) {
            nvps.add(new BasicNameValuePair(key, prefs.get(key, "")));
        }
        return nvps;
    }

    // xkcd 221 compliance.
    int getRandomNumber() {
        return 4; // chosen by fair dice roll.
        // guaranteed to be random.
    }
}
