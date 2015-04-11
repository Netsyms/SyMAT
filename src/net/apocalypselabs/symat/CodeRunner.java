/* 
 * CODE LICENSE =====================
 * Copyright (c) 2015, Apocalypse Laboratories
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
 * software are copyright (c) 2015 Apocalypse Laboratories.  You may not distribute
 * the graphics or any program, source code repository, or other digital storage 
 * media containing them without written permission from Apocalypse Laboratories.
 * This ban on distribution only applies to publicly available systems.
 * A password-protected network file share, USB drive, or other storage scheme that
 * cannot be easily accessed by the public is generally allowed.  If in doubt,
 * contact Apocalypse Laboratories.  If Apocalypse Laboratories allows or denies
 * you permission, that decision is considered final and binding.
 */
package net.apocalypselabs.symat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;

/**
 *
 * @author Skylar
 */
public class CodeRunner {

    private ScriptEngine se;
    private StringWriter sw = new StringWriter();
    private PrintWriter pw = new PrintWriter(sw);
    private String modules = "";
    private String moduleterm = "";

    // If we need to wrap code around input to make everything nice.
    private boolean wrapRequired = false;
    // What codez are we speaking?
    private String scriptLang = "";

    public CodeRunner() {
        this("javascript");
    }

    public CodeRunner(int lang) {
        this(lang == 0 ? "js" : "py");
    }

    public CodeRunner(String lang) {
        scriptLang = lang;
        switch (lang) {
            case "javascript":
            case "js":
            case "rhino":
                scriptLang = "javascript";
                se = new ScriptEngineManager().getEngineByName("rhino");
                wrapRequired = true;
                try {
                    // Add custom functions.
                    se.eval("importClass(net.apocalypselabs.symat.Functions);"
                            + "SyMAT_Functions = new net.apocalypselabs.symat.Functions();"
                            + "SyMAT_Functions.setLang('js');\n"
                            + getFunctions("js")
                            + loadToolkits());
                    // Allow engine access from scripts.
                    se.put("engine", se);
                    attachWriters();
                } catch (Exception ex) {
                    initError(ex);
                }
                break;
            case "python":
            case "jython":
            case "py":
                scriptLang = "python";
                se = new ScriptEngineManager().getEngineByName("python");
                try {
                    se.eval("from math import *\n"
                            + "from net.apocalypselabs.symat import Functions\n"
                            + "_=Functions()\n"
                            + "_.setLang('py')\n\n"
                            + getFunctions("py")
                            + loadToolkits());
                    // Allow engine access from scripts.
                    se.put("engine", se);
                    attachWriters();
                } catch (Exception ex) {
                    initError(ex);
                }
                break;
            default:
                throw new UnsupportedOperationException("Script language " + lang + " not supported.");
        }
    }

    public static List<InputStream> loadResources(final String name,
            final ClassLoader classLoader) throws IOException {
        final List<InputStream> list = new ArrayList<InputStream>();
        final Enumeration<URL> systemResources
                = (classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader)
                .getResources(name);
        while (systemResources.hasMoreElements()) {
            list.add(systemResources.nextElement().openStream());
        }
        return list;
    }

    private String loadToolkits() {
        String out = "";
        try {
            List<InputStream> il = loadResources("meta.txt", null);
            InputStream[] ii = new InputStream[il.size()];
            for (InputStream i : il.toArray(ii)) {
                BufferedReader br = new BufferedReader(new InputStreamReader(i));
                while (br.ready()) {
                    String l = br.readLine();
                    if (l.contains("=")) {
                        String classname = l.split("=")[0];
                        String varname = l.split("=")[1];
                        if (scriptLang.equals("javascript")) {
                            out += "importClass(" + classname + ");"
                                    + varname + " = new " + classname + "();";
                        } else {
                            out += "from "
                                    + classname.substring(0, classname.lastIndexOf("."))
                                    + " import " + classname.substring(classname.lastIndexOf(".") + 1) + "\n"
                                    + varname + " = " + classname.substring(classname.lastIndexOf(".") + 1) + "()\n";
                        }
                    }
                }
            }
        } catch (Exception ex) {

        }
        Debug.println(out);
        return out;
    }

    @Deprecated
    public CodeRunner(String lang, boolean shell) {
        this(lang);
    }

    private void attachWriters() {
        se.getContext().setWriter(pw);
        se.getContext().setErrorWriter(pw);
        Debug.println("Attached writers.");
    }

    /**
     * Inits the Python engine on application start.
     *
     * @param fakeInit Set it to true.
     */
    public CodeRunner(boolean fakeInit) {
        if (fakeInit) {
            se = new ScriptEngineManager().getEngineByName("python");
        }
    }

    public StringWriter getStringWriter() {
        return sw;
    }

    public PrintWriter getPrintWriter() {
        return pw;
    }

    public String getBufferDump() {
        String dump = sw.toString();
        sw.getBuffer().setLength(0);
        return dump;
    }

    private void initError(Exception ex) {
        JOptionPane.showMessageDialog(null, "Error: "
                + "Could not properly initialize " + scriptLang + " scripting engine."
                + "\n\nSome functions may not work.\n\n"
                + "(" + ex.getMessage() + ")");
    }

    /**
     * Parse a String of code.
     *
     * @param eval A String of code to evaluate.
     * @return the result.
     */
    public Object evalString(String eval) {
        try {
            Object res = se.eval(wrapMath(eval));
            if (res == null) {
                res = "";
            }
            String result = res + sw.getBuffer().toString().trim();
            return result;
        } catch (ScriptException ex) {
            return formatEx(ex);
        }
    }

    /**
     * Parse code and use the default output writers.
     *
     * @param eval A String of code to evaluate.
     */
    public void evalCode(String eval) {
        try {
            Object res = se.eval(wrapMath(eval));
            if (res == null) {
                res = "";
            }
        } catch (ScriptException ex) {
            sw.append(formatEx(ex));
        }
    }

    private String formatEx(ScriptException ex) {
        String err = ex.getMessage();
        //err = err.replace("sun.org.mozilla.javascript.internal.EcmaError: ReferenceError: ", "");
        //err = err.replace("<Unknown source>", "file");
        //err = err.replace("sun.org.mozilla.javascript.internal.EvaluatorException: ", "");
        return "Error: " + err;
    }

    /**
     * Allows use of Math.blah() as just blah(). Also gives access to SyMAT Java
     * functions.
     *
     * @param eval input string
     * @return wrapped input
     */
    private String wrapMath(String eval) {
        if (wrapRequired) {
            String with = "with(SyMAT_Functions){with(Math){" + eval + "}}";
            Debug.println(with);
            return with;
        }
        return eval;
    }

    public void setVar(String var, Object val) {
        se.put(var, val);
    }

    public Object getVar(String var) {
        return se.get(var);
    }

    private String getFunctions(String lang) {
        String text = "";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            CodeRunner.class
                            .getResourceAsStream("functions." + lang)));
            String line;
            while ((line = reader.readLine()) != null) {
                text += line + "\n";
            }
        } catch (Exception e) {
        }
        return text;
    }
}
