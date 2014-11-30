/*
 * Copyright (C) 2014 Skylar.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
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

    public static String getSetting(String key) {
        return prefs.get(key, "");
    }

    public static String getSetting(String key, String emptyResponse) {
        return prefs.get(key, emptyResponse);
    }
}
