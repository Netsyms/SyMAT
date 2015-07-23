/*
 * CODE LICENSE =====================
 * Copyright (c) 2015, Netsyms Technologies
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation and/or
 *  other materials provided with the distribution.
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
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * MEDIA LICENSE ====================
 * All images and other graphical files (the "graphics") included with this
 * software are copyright (c) 2015 Netsyms Technologies.  You may not distribute
 * the graphics or any program, source code repository, or other digital storage 
 * media containing them without written permission from Netsyms Technologies.
 * This ban on distribution only applies to publicly available systems.
 * A password-protected network file share, USB drive, or other storage scheme that
 * cannot be easily accessed by the public is generally allowed.  If in doubt,
 * contact Netsyms Technologies.  If Netsyms Technologies allows or denies
 * you permission, that decision is considered final and binding.
 */
package net.apocalypselabs.symat;

import java.awt.Color;

/**
 * Handles theme colors and giving them out.
 *
 * @author Skylar
 */
public class Theme {

    // Theme colors.
    private static final Color WHITE = Color.WHITE;
    private static final Color BLACK = Color.BLACK;
    private static final Color DBACK = new Color(41, 49, 52);
    private static final Color LBACK = new Color(240, 240, 240);
    private static final Color DGRAY = Color.DARK_GRAY;
    private static final Color LGRAY = Color.LIGHT_GRAY;

    /**
     * Light theme.
     */
    public static final int THEME_LIGHT = 0;
    /**
     * Dark theme.
     */
    public static final int THEME_DARK = 1;

    /**
     * The current theme.
     */
    public static int currentTheme = (PrefStorage.getSetting("theme")
            .equals("dark") ? 1 : 0);

    /**
     * Set the theme for the application.
     *
     * @param theme the theme id number.
     */
    public static void setTheme(int theme) {
        switch (theme) {
            case THEME_DARK:
                PrefStorage.saveSetting("theme", "dark");
                break;
            case THEME_LIGHT:
            default:
                PrefStorage.saveSetting("theme", "light");
        }
        currentTheme = theme;
    }

    /**
     * Set the theme for the application.
     *
     * @param theme The theme name, either "light" or "dark".
     */
    public static void setTheme(String theme) {
        switch (theme) {
            case "dark":
                setTheme(THEME_DARK);
                break;
            case "light":
            default:
                setTheme(THEME_LIGHT);
        }
    }

    public static String getTheme() {
        return (currentTheme == THEME_LIGHT) ? "light" : "dark";
    }

    public static Color textColor() {
        return (currentTheme == THEME_LIGHT) ? BLACK : WHITE;
    }

    public static Color boxColor() {
        return (currentTheme == THEME_LIGHT) ? WHITE : DBACK;
    }

    public static Color windowColor() {
        return (currentTheme == THEME_LIGHT) ? LGRAY : DGRAY;
    }

    public static Color tabColor() {
        return (currentTheme == THEME_LIGHT) ? LBACK : DBACK;
    }

    public static String editorTheme() {
        return (currentTheme == THEME_LIGHT) ? "default" : "dark";
    }
}
