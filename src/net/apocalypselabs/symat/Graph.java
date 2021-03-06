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
package net.apocalypselabs.symat;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.math.plot.plotObjects.BaseLabel;
import org.matheclipse.core.eval.EvalUtilities;

/**
 *
 * @author Skylar
 */
public class Graph extends javax.swing.JInternalFrame {

    private final JFileChooser fc = new JFileChooser();

    private boolean standalone = true;

    private BaseLabel lbl = new BaseLabel("", Color.black, 0.5, 1.1);

    // History, used for redrawing when scale changed.
    private String history = "";

    // If a graph is being drawn, set to true, else false
    boolean graphing = false;

    // Graph min and max
    private double xmin = -10;
    private double xmax = 10;
//    private double ymin = -10;
//    private double ymax = 10;

    /**
     * Creates new form Graph
     */
    public Graph() {
        init();
    }

    /**
     *
     * @param isInternal
     */
    public Graph(boolean isInternal) {
        init();
        standalone = !isInternal;
    }

    private void init() {
        initComponents();
        FileFilter filter = new FileNameExtensionFilter("PNG image (.png)", "png");
        fc.setFileFilter(filter);
        fc.addChoosableFileFilter(filter);
        plot.plotToolBar.remove(5);
        plot.plotToolBar.remove(4);
        plot.plotToolBar.remove(3);
        lbl.setFont(new Font("Courier", Font.BOLD, 18));
        plot.addPlotable(lbl);
    }

    @Override
    public void doDefaultCloseAction() {
        if (standalone) {
            dispose();
        } else {
            hide();
        }
    }

    /**
     *
     */
    public void forceClose() {
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inBox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        plotBtn = new javax.swing.JButton();
        plot = new org.math.plot.Plot2DPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exportBtn = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        clrGraphBtn = new javax.swing.JMenuItem();
        setTitleBtn = new javax.swing.JMenuItem();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Graph");
        setToolTipText("");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/graph.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(326, 402));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        inBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                inBoxKeyTyped(evt);
            }
        });

        jLabel1.setText("f(x)=");

        plotBtn.setText(">>");
        plotBtn.setToolTipText("");
        plotBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plotBtnActionPerformed(evt);
            }
        });

        plot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                plotMouseClicked(evt);
            }
        });

        jMenu1.setText("File");

        exportBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        exportBtn.setText("Export graph...");
        exportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportBtnActionPerformed(evt);
            }
        });
        jMenu1.add(exportBtn);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        clrGraphBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        clrGraphBtn.setText("Clear Graph");
        clrGraphBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clrGraphBtnActionPerformed(evt);
            }
        });
        jMenu2.add(clrGraphBtn);

        setTitleBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        setTitleBtn.setText("Set Title...");
        setTitleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTitleBtnActionPerformed(evt);
            }
        });
        jMenu2.add(setTitleBtn);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inBox, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(plotBtn)
                .addGap(10, 10, 10))
            .addComponent(plot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(plot, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(plotBtn))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void plotBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plotBtnActionPerformed
        String[] frmlas = inBox.getText().split(";");
        for (String f : frmlas) {
            new GraphThread(f).start();
        }
    }//GEN-LAST:event_plotBtnActionPerformed

    /**
     * Graph the graphs without holding up everything else.
     */
    private class GraphThread extends Thread {

        String[] frmlas;

        public GraphThread(String frmla) {
            frmlas = new String[1];
            frmlas[0] = frmla;
        }

        public GraphThread(String[] frmla) {
            frmlas = frmla;
        }

        @Override
        public void run() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    inBox.setEnabled(false);
                    plotBtn.setEnabled(false);
                    for (Component mu : jMenuBar1.getComponents()) {
                        mu.setEnabled(false);
                    }
                }
            });
            for (String formula : frmlas) {
                String niceformula = formula;
                CodeRunner cr = new CodeRunner();
                formula = formula.replaceAll("x", "\\$x");
                EvalUtilities solver = new EvalUtilities();
                String xx = "";
                String yy = "";
                double x;
                for (x = xmin; x <= xmax; x += ((xmax - xmin) / 400.0)) {
                    String res;
                    try {
                        cr.setVar("x", x);
                        res = solver.evaluate("$x=" + x + ";N[" + formula + "]").toString();
                    } catch (Exception ex) {
                        res = "0";
                    }
                    // Omit crazy numbers like 1/0 and stuff
                    try {
                        if (Double.parseDouble(res) > Integer.MIN_VALUE) {
                            xx += String.valueOf(x) + " ";
                            yy += res + " ";
                        } else {

                        }
                    } catch (Exception ex) {
                        xx += String.valueOf(x) + " ";
                        yy += res + " ";
                    }
                }
                Debug.println(xx);
                Debug.println(yy);
                String[] xs = xx.trim().split(" ");
                String[] ys = yy.trim().split(" ");
                double[] xd = new double[xs.length];
                double[] yd = new double[ys.length];
                for (int i = 0; i < xs.length; i++) {
                    try {
                        xd[i] = Double.parseDouble(xs[i]);
                    } catch (Exception ex) {
                        xd[i] = 0.0;
                    }
                }
                for (int i = 0; i < ys.length; i++) {
                    try {
                        yd[i] = Double.parseDouble(ys[i]);
                    } catch (Exception ex) {
                        yd[i] = 0.0;
                    }
                }
                SwingUtilities.invokeLater(new Updater(niceformula, xd, yd));
            }
            SwingUtilities.invokeLater(new Finisher());
        }

        private class Updater implements Runnable {

            final double[] xd;
            final double[] yd;
            final String formula;

            public Updater(String frmla, double[] x, double[] y) {
                xd = x;
                yd = y;
                formula = frmla;
            }

            @Override
            public void run() {
                plot.addLinePlot(formula, xd, yd);
                history += formula + "\n";
            }

        }

        private class Finisher implements Runnable {

            @Override
            public void run() {
                inBox.setEnabled(true);
                plotBtn.setEnabled(true);
                for (Component mu : jMenuBar1.getComponents()) {
                    mu.setEnabled(true);
                }
                inBox.requestFocusInWindow();
            }

        }
    }

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    }//GEN-LAST:event_formComponentShown

    /**
     *
     * @param x
     * @param y
     */
    public void plotPoints(double[] x, double[] y) {
        plotPoints(x, y, "Points");
    }

    /**
     *
     * @param x
     * @param y
     * @param name
     */
    public void plotPoints(double[] x, double[] y, String name) {
        plot.addLinePlot(name, x, y);
    }

    /**
     * Get the zoom ratio.
     *
     * @param zoomLevel The zoom level to calculate from.
     * @return The ratio.
     */
    @Deprecated
    public static double getScale(int zoomLevel) {
        return 15.0;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void drawDot(double x, double y) {
        double[] xpt = {x};
        double[] ypt = {y};

        plot.addScatterPlot("", Color.BLACK, xpt, ypt);
    }

    private void inBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inBoxKeyTyped
        if (evt.getKeyChar() == '\n') {
            plotBtn.doClick();
        }
    }//GEN-LAST:event_inBoxKeyTyped

    private void clrGraphBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clrGraphBtnActionPerformed
        clearDraw();
    }//GEN-LAST:event_clrGraphBtnActionPerformed

    private void exportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportBtnActionPerformed
        int result = fc.showSaveDialog(Main.mainPane);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = new File(addSaveExt(fc.getSelectedFile().toString()));
            try {
                plot.toGraphicFile(file);
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(this,
                        "Image export failed!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_exportBtnActionPerformed

    private void setTitleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTitleBtnActionPerformed
        String wintitle = JOptionPane.showInternalInputDialog(this,
                "New window title:",
                "Rename",
                JOptionPane.QUESTION_MESSAGE);
        if (wintitle != null && !wintitle.equals("")) {
            setWindowTitle(wintitle);
            setLabel(wintitle);
        }
    }//GEN-LAST:event_setTitleBtnActionPerformed

    private void plotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plotMouseClicked

    }//GEN-LAST:event_plotMouseClicked

    /**
     * Get the range of the graph.
     *
     * @return {xmin, xmax}
     */
    public double[] getRange() {
        double[] range = {xmin, xmax};
        return range;
    }

    /**
     *
     * @param min
     * @param max
     */
    public void setRange(double min, double max) {
        xmin = min;
        xmax = max;

        clearDraw(false);
        plot.setFixedBounds(0, min, max);
        if (!history.trim().equals("")) {
            String temp = "";
            for (String cmd : history.trim().split("\n")) {
                cmd = cmd.trim();
                if (!cmd.equals("")) {
                    temp += cmd + "\n";
                }
            }
            history = temp.trim();
            new GraphThread(history.split("\n")).start();
            inBox.setText("");
        }
    }

    /**
     * Graph the given function. Same as typing into input box and pressing
     * Enter.
     *
     * @param f f(x) = f
     */
    public void graphFunction(String f) {
        inBox.setText(f);
        plotBtnActionPerformed(null);
    }

    /**
     *
     * @param label
     */
    public void setLabel(String label) {
        lbl.setText(label);
    }

    private String addSaveExt(String path) {
        if (!path.matches(".*\\.(png)")) {
            path += ".png";
        }
        return path;
    }

    /**
     * Set the wintitle of this graph window.
     *
     * @param t The wintitle.
     */
    public void setWindowTitle(String t) {
        setTitle(t);
    }

    /**
     * Get the wintitle of the window.
     *
     * @return the wintitle, stupid!
     */
    public String getWindowTitle() {
        return getTitle();
    }

    /**
     * Erase the graph.
     */
    public void clearDraw() {
        clearDraw(true);
    }

    /**
     * Erase the graph.
     *
     * @param alsoHistory True if history should be removed
     */
    public void clearDraw(boolean alsoHistory) {
        if (alsoHistory) {
            history = "";
        }
        plot.removeAllPlots();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem clrGraphBtn;
    private javax.swing.JMenuItem exportBtn;
    private javax.swing.JTextField inBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private org.math.plot.Plot2DPanel plot;
    private javax.swing.JButton plotBtn;
    private javax.swing.JMenuItem setTitleBtn;
    // End of variables declaration//GEN-END:variables
}
