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
import javax.script.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Skylar
 */
public class CodeRunner {

    ScriptEngine jse = new ScriptEngineManager().getEngineByName("rhino");
    boolean isJava8 = false;

    public CodeRunner() {
        try {
            // Add custom functions.
            jse.eval("importClass(net.apocalypselabs.symat.Functions);"
                    + "SyMAT_Functions = new net.apocalypselabs.symat.Functions();");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: "
                    + "Could not properly initialize scripting engine."
                    + "\n\nSome functions may not work.\n\n"
                    + "(" + ex.getMessage() + ")");
            ex.printStackTrace();
        }
    }

    /**
     * Parse a String of JavaScript.
     *
     * @param eval A String of JavaScript to evaluate.
     * @return the result.
     */
    public Object evalString(String eval) {
        try {
            return jse.eval(wrapMath(eval));
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
        String with = "with(SyMAT_Functions){with(Math){" + eval + "}}";
        //System.out.println(with);
        return with;
    }

    public void setVar(String var, Object val) {
        jse.put(var, val);
    }

    public Object getVar(String var) {
        return jse.get(var);
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
