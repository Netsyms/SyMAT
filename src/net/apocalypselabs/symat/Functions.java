/* 
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
 */
package net.apocalypselabs.symat;

import static java.lang.Math.*;
import javax.swing.JOptionPane;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.parser.client.math.MathException;

/**
 * These functions are accessible from JavaScript.
 *
 * There are a lot of aliases in here too.
 *
 * @author Skylar
 */
public class Functions {

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

    public double rad(double degrees) {
        return degrees * (PI / 180);
    }

    public double deg(double radians) {
        return radians * (180 / PI);
    }

    public String sym(String input) {
        return util.evaluate(input).toString();
    }

    // Shortcut for non-JS math evaluation.
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
