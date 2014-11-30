/* 
 * Copyright (C) 2014 Apocalypse Laboratories.
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
     * Allows use of Math.blah() as just blah().  
     * Also gives access to SyMAT Java functions.
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
}
