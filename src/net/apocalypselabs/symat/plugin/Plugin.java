/*
 This code file defines a data storage standard.
 It can be used for any purpose, and is hereby released into the public domain.
 */
package net.apocalypselabs.symat.plugin;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 * Plugin container class.
 */
public class Plugin implements Serializable {
    
    private static final long serialVersionUID = 13371L;

    public final int LANG_JS = 0;
    public final int LANG_PY = 1;
    
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
     * @return 
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * Set plugin version.
     * @param v 
     */
    public void setVersion(String v) {
        version = v;
    }
    
    /**
     * Get the plugin package name.
     * @return 
     */
    public String getPackage() {
        return packageName;
    }
    
    /**
     * Set the plugin package name.
     * @param pkg 
     */
    public void setPackage(String pkg) {
        packageName = pkg;
    }

    /**
     * Get the plugin author name.
     * @return 
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * Set the plugin author name.
     * @param s 
     */
    public void setAuthor(String s) {
        author = s;
    }
    
    /**
     * Get the plugin website.
     * @return 
     */
    public String getWebsite() {
        return website;
    }
    
    /**
     * Set the plugin website.
     * @param url 
     */
    public void setWebsite(String url) {
        website = url;
    }
    
    /**
     * Get extra text data.
     * @return 
     */
    public String getOther() {
        return other;
    }
    
    /**
     * Set extra text data.
     * @param o 
     */
    public void setOther(String o) {
        other = o;
    }
    
    /**
     * Get the plugin icon.
     * @return 
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Set the plugin icon.
     * @param i 
     */
    public void setIcon(ImageIcon i) {
        icon = i;
    }

    /**
     * Get the plugin code.
     * @return 
     */
    public String getScript() {
        return script;
    }

    /**
     * Set the plugin code.
     * @param s 
     */
    public void setScript(String s) {
        script = s;
    }

    /**
     * See getTitle().
     * @return 
     */
    @Override
    public String toString() {
        return title;
    }

    /**
     * Get the plugin short name/title.
     * @return 
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the plugin short name/title.
     * @param n 
     */
    public void setTitle(String n) {
        title = n;
    }

    /**
     * Get the plugin description text.
     * @return 
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Set the plugin description text.
     * @param d 
     */
    public void setDesc(String d) {
        desc = d;
    }

    /**
     * Get the long title/short description.
     * @return 
     */
    public String getLongTitle() {
        return longtitle;
    }

    /**
     * Set the long title/short description.
     * @param t 
     */
    public void setLongTitle(String t) {
        longtitle = t;
    }
    
    /**
     * Get the script language.
     * @return LANG_JS or LANG_PY
     */
    public int getLang() {
        return lang;
    }
    
    /**
     * Set the script language.
     * @param l LANG_JS or LANG_PY
     */
    public void setLang(int l) {
        lang = l;
    }

}
