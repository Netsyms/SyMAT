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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.script.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Skylar
 */
public class CodeRunner {

    private ScriptEngine se;

    // If we need to wrap code around input to make everything nice.
    private boolean wrapRequired = false;
    // What codez are we speaking?
    private String scriptLang = "";

    private boolean isShell = false;

    public CodeRunner() {
        this("javascript");
    }

    public CodeRunner(String lang) {
        scriptLang = lang;
        switch (lang) {
            case "javascript":
                se = new ScriptEngineManager().getEngineByName("rhino");
                wrapRequired = true;
                try {
                    // Add custom functions.
                    se.eval("importClass(net.apocalypselabs.symat.Functions);"
                            + "SyMAT_Functions = new net.apocalypselabs.symat.Functions();"
                            + jsFunctions());
                } catch (Exception ex) {
                    initError(ex);
                }
                break;
            case "python":
                se = new ScriptEngineManager().getEngineByName("python");
                try {
                    se.eval("from net.apocalypselabs.symat import Functions\n_=Functions()\n");
                } catch (Exception ex) {
                    initError(ex);
                }
                break;
            default:
                throw new UnsupportedOperationException("Script language " + lang + " not supported.");
        }
    }

    public CodeRunner(String lang, boolean shell) {
        this(lang);
        isShell = shell;
    }

    private void initError(Exception ex) {
        JOptionPane.showMessageDialog(null, "Error: "
                + "Could not properly initialize " + scriptLang + " scripting engine."
                + "\n\nSome functions may not work.\n\n"
                + "(" + ex.getMessage() + ")");
        ex.printStackTrace();
    }

    /**
     * Parse a String of JavaScript.
     *
     * @param eval A String of JavaScript to evaluate.
     * @return the result.
     */
    public Object evalString(String eval) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            se.getContext().setWriter(pw);
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
            //System.out.println(with);
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

    private String jsFunctions() {
        String text = "";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            CodeRunner.class
                            .getResourceAsStream("functions.js")));
            String line;
            while ((line = reader.readLine()) != null) {
                text += line;
            }
        } catch (Exception e) {
        }
        return text;
    }

    // TODO: Run Javascript in separate thread from rest of app.
    private class JsThread extends Thread {

        @Override
        public void run() {

        }
    }
}
