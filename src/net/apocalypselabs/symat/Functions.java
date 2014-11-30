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
    
    public void write(String content) {
        // TODO: enable output logging
    }
   
    
    /*
        Math commands
    */
    public String d(String function, String idv) {
        return util.evaluate("diff("+function+","+idv+")").toString();
    }
    
    public String sym(String input) {
        return util.evaluate(input).toString();
    }
    
    public String replace(String function, String variable, String newvar) {
        return function.replaceAll(variable, newvar);
    }
    
    public double subs(String function, String variable, String newvar) {
        return numof(function.replaceAll(variable, newvar));
    }
    
    public double numof(String f) {
        try {
            return Double.parseDouble(util.evaluate("N("+f+")").toString());
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
        info += "Java version: "+System.getProperty("java.version");
        info += "\nJava vendor: "+System.getProperty("java.vendor");
        info += "\nJava home: "+System.getProperty("java.home");
        return info;
    }
    
    /*
        Constructor.
    */
    public Functions() {
        MainGUI.mainPane.add(graphwin);
    }
}
