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

    EvalUtilities util = new EvalUtilities(false, true);
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
    public String D(String function, String idv) {
        return util.evaluate("diff(" + function + "," + idv + ")").toString();
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
    public void plot(String function) {
        graphwin.setVisible(true);
        graphwin.graphFunction(function);
    }

    public void ezplot(String f) {
        plot(f);
    }

    public void graph(String f) {
        plot(f);
    }

    public void plotname(String t) {
        graphwin.setVisible(true);
        graphwin.setWindowTitle(t);
    }

    public String plotname() {
        return graphwin.getTitle();
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

    public void plotscale(int level) {
        graphwin.setVisible(true);
        graphwin.setZoom(level);
    }

    public void drawdot(double x, double y) {
        graphwin.setVisible(true);
        graphwin.drawDot(x, y);
    }

    /*
     Other
     */
    public String sysinfo() {
        String info = "==Java System Information==\n";
        info += "Java version: " + System.getProperty("java.version");
        info += "\nJava vendor: " + System.getProperty("java.vendor");
        info += "\nJava home: " + System.getProperty("java.home");
        return info;
    }

    /*
     Constructor.
     */
    public Functions() {
        MainGUI.mainPane.add(graphwin);
    }
}
