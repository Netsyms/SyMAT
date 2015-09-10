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
package net.apocalypselabs.symat.plugin;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 * Plugin container class.
 */
public class Plugin extends PluginParent implements Serializable {

    private static final long serialVersionUID = 13371L;

    public final int LANG_JS = 0;
    public final int LANG_PY = 1;
    public final int LANG_JAVA = 2;

    private ImageIcon icon;
    private String packageName = "";
    private String script = "";
    private String title = "";
    private String desc = "";
    private String longtitle = "";
    private String author = "";
    private String website = "";
    private String other = "";
    private String version = "";
    private int lang = LANG_JS;

    public Plugin() {

    }

    /**
     * Get plugin version.
     *
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set plugin version.
     *
     * @param v
     */
    public void setVersion(String v) {
        version = v;
    }

    /**
     * Get the plugin package name.
     *
     * @return
     */
    public String getPackage() {
        return packageName;
    }

    /**
     * Set the plugin package name.
     *
     * @param pkg
     */
    public void setPackage(String pkg) {
        packageName = pkg;
    }

    /**
     * Get the plugin author name.
     *
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the plugin author name.
     *
     * @param s
     */
    public void setAuthor(String s) {
        author = s;
    }

    /**
     * Get the plugin website.
     *
     * @return
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Set the plugin website.
     *
     * @param url
     */
    public void setWebsite(String url) {
        website = url;
    }

    /**
     * Get extra text data.
     *
     * @return
     */
    public String getOther() {
        return other;
    }

    /**
     * Set extra text data.
     *
     * @param o
     */
    public void setOther(String o) {
        other = o;
    }

    /**
     * Get the plugin icon.
     *
     * @return
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Set the plugin icon.
     *
     * @param i
     */
    public void setIcon(ImageIcon i) {
        icon = i;
    }

    /**
     * Get the plugin code.
     *
     * @return
     */
    public String getScript() {
        return script;
    }

    /**
     * Set the plugin code.
     *
     * @param s
     */
    public void setScript(String s) {
        script = s;
    }

    /**
     * See getTitle().
     *
     * @return
     */
    @Override
    public String toString() {
        return title;
    }

    /**
     * Get the plugin short name/title.
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the plugin short name/title.
     *
     * @param n
     */
    public void setTitle(String n) {
        title = n;
    }

    /**
     * Get the plugin description text.
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Set the plugin description text.
     *
     * @param d
     */
    public void setDesc(String d) {
        desc = d;
    }

    /**
     * Get the long title/short description.
     *
     * @return
     */
    public String getLongTitle() {
        return longtitle;
    }

    /**
     * Set the long title/short description.
     *
     * @param t
     */
    public void setLongTitle(String t) {
        longtitle = t;
    }

    /**
     * Get the script language.
     *
     * @return LANG_JS or LANG_PY
     */
    public int getLang() {
        return lang;
    }

    /**
     * Set the script language.
     *
     * @param l LANG_JS or LANG_PY
     */
    public void setLang(int l) {
        lang = l;
    }

}
