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

import java.io.IOException;
import static java.lang.Math.*;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.math.MathException;

/**
 * These functions are accessible from JavaScript.
 *
 * There are a lot of aliases in here too.
 *
 * @author Skylar
 */
public class Functions {

    public static final HashMap<String, Object> GLOBALS = new HashMap<>();

    private final EvalUtilities util = new EvalUtilities(true, true);
    Graph graphwin = new Graph(true);

    /*
     Useful interactions
     */
    public void notify(Object message) {
        JOptionPane.showInternalMessageDialog(MainGUI.mainPane, message.toString());
    }

    public String ask(String question) {
        return JOptionPane.showInternalInputDialog(MainGUI.mainPane, question);
    }

    /*
     Math commands
     */
    // Derivative of function with respect to idv
    public String diff(String function, String idv) {
        return util.evaluate("diff(" + function + "," + idv + ")").toString();
    }

    public String diff(String function) {
        // Assume "x" as var
        return diff(function, "x");
    }  

    @Deprecated
    public String D(String function, String idv) {
        return diff(function, idv);
    }

    public String integrate(String function, String idv) {
        return util.evaluate("integrate(" + function + "," + idv + ")").toString();
    }

    public String integrate(String function) {
        return integrate(function, "x");
    }

    public String factor(String function) {
        return sym("Factor(" + function + ")");
    }
    
    public String simplify(String expr) {
        return sym("Simplify("+expr+")");
    }
    
    public Object vpa(String expr) {
        IExpr ans = util.evaluate("N("+expr+")");
        if (ans.isNumber()) {
            return Double.parseDouble(ans.toString());
        }
        return ans.toString();
    }

    public double rad(double degrees) {
        return degrees * (PI / 180);
    }

    public double deg(double radians) {
        return radians * (180 / PI);
    }

    public String sym(String input) {
        return util.evaluate(input).toString();
    }

    // Shortcut for symja evaluation.
    public String $(String input) {
        return sym(input);
    }

    public String replace(String function, String variable, String newvar) {
        return function.replaceAll(variable, newvar);
    }

    public double subs(String function, String variable, String newvar) {
        return numof(function.replaceAll(variable, newvar));
    }

    public double numof(String f) {
        try {
            return Double.parseDouble(util.evaluate("N(" + f + ")").toString());
        } catch (MathException | NumberFormatException ex) {
            return 0.0;
        }
    }

    /*
     Graphing interfaces
     */
    public void xlim(double xmin, double xmax) {
        graphwin.setRange(xmin, xmax);
    }

    public void plot(String function) {
        showGraph();
        graphwin.graphFunction(function);
    }

    public void plot(double[] x, double[] y) {
        graphwin.plotPoints(x, y);
    }

    public void plot(double[] x, double[] y, String name) {
        graphwin.plotPoints(x, y, name);
    }

    public void plot(String function, double xmin, double xmax) {
        graphwin.setRange(xmin, xmax);
        plot(function);
    }

    public void ezplot(String f) {
        plot(f);
    }

    public void ezplot(String function, double xmin, double xmax) {
        plot(function, xmin, xmax);
    }

    public void graph(String f) {
        plot(f);
    }

    public void plotname(String t) {
        graphwin.setWindowTitle(t);
        graphwin.setLabel(t);
    }

    public String plotname() {
        return graphwin.getTitle();
    }

    public void plot() {
        showGraph();
    }

    public void plotclr() {
        graphwin.clearDraw();
    }

    public void clearplot() {
        plotclr();
    }

    public void plotclear() {
        plotclr();
    }

    public void drawdot(double x, double y) {
        showGraph();
        graphwin.drawDot(x, y);
    }
    
    public String readfile(String path) throws IOException {
        return FileUtils.readFile(path);
    }
    
    public void savefile(String content, String path) throws IOException {
        FileUtils.saveFile(content, path, false);
    }
    
    public String md5sum(String data) {
        return FileUtils.MD5(data);
    }

    
    // TODO: Make globals work!
//    /*
//     Global variables are accessible across scripts.
//     */
//    /**
//     * Set a global variable.
//     *
//     * @param name The variable name
//     * @param var The variable
//     */
//    public static void global(String name, Object var) {
//        GLOBALS.put(name, var);
//    }
//
//    /**
//     * Get a global variable.
//     *
//     * @param name The variable name
//     * @return The variable
//     */
//    public static Object global(String name) {
//        Object item = GLOBALS.get(name);
//        return item;
//    }
//    
//    // Fix for Python reserved word "global"
//    public static void setglobal(String name, Object var) {
//        global(name, var);
//    }
//    
//    // Fix for Python reserved word "global"
//    public static void getglobal(String name) {
//        global(name);
//    }
//    
//    /**
//     * Clear all the GLOBALS.
//     */
//    public static void clrglobals() {
//        GLOBALS.clear();
//    }
//    
//    /**
//     * Check if the given global key is set.
//     * @param name The key to check.
//     * @return True if the key exists, else false.
//     */
//    public static boolean globalcontains(String name) {
//        return GLOBALS.containsKey(name);
//    }

    /*
     Other
     */
    public String sysinfo() {
        String info = "==Java Information==\n";
        info += "Java version: " + System.getProperty("java.version");
        info += "\nJava vendor: " + System.getProperty("java.vendor");
        info += "\nJava home: " + System.getProperty("java.home");
        return info;
    }

    /**
     * Reset the license, quit the application.
     */
    public void resetlicense() {
        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to reset your license?\n"
                + "This will close SyMAT and all open files!",
                "Reset license",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            PrefStorage.unset("license");
            System.exit(0);
        }
    }

    /**
     * Make sure the graph window shows.
     */
    private void showGraph() {
        graphwin.setVisible(true);
        graphwin.toFront();
    }
    /*
     Constructor.
     */

    public Functions() {
        MainGUI.loadFrame(graphwin, false);
    }
}
