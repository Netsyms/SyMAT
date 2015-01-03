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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.parser.client.math.MathException;

/**
 *
 * @author Skylar
 */
public class Graph extends javax.swing.JInternalFrame {

    private final JFileChooser fc = new JFileChooser();
    private BufferedImage gpnl;
    private Graphics gg; // Main graphics object
    private boolean standalone = true;
    private boolean customName = false;

    // History, used for redrawing when scale changed.
    private String history = "";

    // If a graph is being drawn, set to true, else false
    boolean graphing = false;

    // Graph scaling data.
    private double xtimes = 15;
    private double ytimes = 15;
    private double scale = 1;
    
    // The current value for the zoom/scale, as entered by the user
    private int scaleLevel = 0;

    /**
     * Creates new form Graph
     */
    public Graph() {
        init();
    }

    public Graph(boolean isInternal) {
        init();
        standalone = !isInternal;
    }

    private void init() {
        initComponents();
        gpnl = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        FileFilter filter = new FileNameExtensionFilter("PNG image (.png)", "png");
        fc.setFileFilter(filter);
        fc.addChoosableFileFilter(filter);
        filter = new FileNameExtensionFilter("JPEG image (.jpg)", "jpg");
        fc.addChoosableFileFilter(filter);
    }

    @Override
    public void doDefaultCloseAction() {
        if (standalone) {
            dispose();
        } else {
            hide();
        }
    }

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
        gLbl = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        clrGraphBtn = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        scaleLbl = new javax.swing.JMenu();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Graph");
        setToolTipText("");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/graph.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(326, 402));
        setMinimumSize(new java.awt.Dimension(326, 402));
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

        gLbl.setBackground(new java.awt.Color(255, 255, 255));
        gLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gLbl.setToolTipText("");
        gLbl.setMaximumSize(new java.awt.Dimension(300, 300));
        gLbl.setMinimumSize(new java.awt.Dimension(300, 300));
        gLbl.setOpaque(true);
        gLbl.setPreferredSize(new java.awt.Dimension(300, 300));

        jMenu1.setText("File");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Export graph...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

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

        jMenuItem6.setText("Scale...");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Set Title...");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        scaleLbl.setForeground(java.awt.Color.blue);
        scaleLbl.setText("Scale: 1 to 1");
        scaleLbl.setEnabled(false);
        jMenuBar1.add(scaleLbl);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inBox, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(plotBtn))
                    .addComponent(gLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        new GraphThread(inBox.getText()).start();
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
                CodeRunner cr = new CodeRunner();
                formula = formula.replaceAll("x", "\\$x");
                EvalUtilities solver = new EvalUtilities();
                double xx, yy, xo, yo, x;
                xo = 0;
                yo = 0;
                gg = gpnl.getGraphics();
                drawAxes();
                gg.setColor(Color.BLUE);
                for (x = (-10 * scale); x <= (10 * scale); x += (10 * scale * .01)) {
                    try {
                        cr.setVar("x", x);
                        yy = (-(Double.parseDouble(solver.evaluate("$x=" + x + ";N[" + formula + "]").toString())) * ytimes) + (gpnl.getHeight() / 2);
                        //System.err.println(solver.evaluate("$x="+x+";N["+formula+"]").toString());
                        xx = (x * xtimes) + (gpnl.getWidth() / 2);
                        //gg.drawOval(xx-1, yy-1, 2, 2);
                        if (x != (-10 * scale)) {
                            gg.drawLine((int) xo, (int) yo, (int) xx, (int) yy);
                        }
                        xo = xx;
                        yo = yy;
                    } catch (MathException | NumberFormatException ex) {

                    }
                }
                gg.translate(gpnl.getWidth() / 2, gpnl.getHeight() / 2);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        dispGraph();
                        if (!customName) {
                            setTitle("Graph | " + inBox.getText());
                        }
                        history += inBox.getText() + "\n";
                    }
                });
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    inBox.setEnabled(true);
                    plotBtn.setEnabled(true);
                    for (Component mu : jMenuBar1.getComponents()) {
                        mu.setEnabled(true);
                    }
                }
            });
        }
    }

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        gg = gpnl.getGraphics();
        drawAxes();
        gg.translate(gpnl.getWidth() / 2, gpnl.getHeight() / 2);
        dispGraph();
    }//GEN-LAST:event_formComponentShown

    /**
     * Get the zoom ratio.
     *
     * @param zoomLevel The zoom level to calculate from.
     * @return The ratio.
     */
    public static double getScale(int zoomLevel) {
        double gscale = 15.0;
        if (zoomLevel >= 0) {
            gscale = 1.0 / (zoomLevel + 1.0);
        } else {
            gscale = 1.0 * (abs(zoomLevel) + 1.0);
        }
        return gscale;
    }

    /**
     * Set the zoom level. The larger the int, the more zoomed it is.
     *
     * @param zoomLevel Level to zoom. 0 is default (10x10).
     */
    public void setZoom(int zoomLevel) {
        scaleLevel = zoomLevel;
        if (zoomLevel >= 0) {
            xtimes = 15.0 * (zoomLevel + 1.0);
            ytimes = 15.0 * (zoomLevel + 1.0);
        } else {
            xtimes = 15.0 / (abs(zoomLevel) + 1.0);
            ytimes = 15.0 / (abs(zoomLevel) + 1.0);
        }
        scale = getScale(zoomLevel);
        scaleLbl.setText("Scale: 1 to " + scale);
        Debug.println("Scaled to xtimes=" + xtimes + ", ytimes=" + ytimes + ", scale=1to" + scale);
        clearDraw(false);
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

    private void dispGraph() {
        gLbl.setIcon(new ImageIcon(gpnl));
    }

    public void drawDot(double x, double y) {
        gg.setColor(Color.RED);
        gg.drawOval((int) (x * xtimes - 2), (int) (y * -ytimes - 2), 4, 4);
        dispGraph();
    }

    private void inBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inBoxKeyTyped
        if (evt.getKeyChar() == '\n') {
            plotBtnActionPerformed(null);
        }
    }//GEN-LAST:event_inBoxKeyTyped

    private void clrGraphBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clrGraphBtnActionPerformed
        clearDraw();
    }//GEN-LAST:event_clrGraphBtnActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        int r = fc.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            try {
                File file = new File(addSaveExt(fc.getSelectedFile().toString()));
                if (file.toString().endsWith("png")) {
                    ImageIO.write(gpnl, "png", file);
                } else if (file.toString().endsWith("jpg")) {
                    ImageIO.write(gpnl, "jpg", file);
                }
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(this, "Error:  Cannot save file: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        String wintitle = JOptionPane.showInternalInputDialog(this,
                "New window title:",
                "Rename",
                JOptionPane.QUESTION_MESSAGE);
        if (wintitle != null && !wintitle.equals("")) {
            setWindowTitle(wintitle);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        GraphScale gs = new GraphScale(scaleLevel);
        int size = 0;
        int result = JOptionPane.showInternalConfirmDialog(this,
                gs,
                "Graph Scale",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            size = gs.getScale();
            Debug.println("Scaling to: " + size);
            setZoom(size);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

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

    private String addSaveExt(String path) {
        if (!path.matches(".*\\.(png|jpg)")) {
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
        customName = true;
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
        gpnl = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        gg = gpnl.getGraphics();
        drawAxes();
        gg.translate(gpnl.getWidth() / 2, gpnl.getHeight() / 2);
        dispGraph();
        setTitle("Graph");
        if (alsoHistory) {
            history = "";
        }
    }

    private void drawAxes() {
        gg.setColor(Color.GRAY);
        gg.drawLine(150, 0, 150, 300);
        gg.drawLine(0, 150, 300, 150);
        // Draw points
        for (int i = 0; i <= 315; i += 15) {
            gg.drawOval(150 - 1, i - 1, 2, 2);
            gg.drawOval(i - 1, 150 - 1, 2, 2);
        }
        gg.setColor(Color.BLUE);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem clrGraphBtn;
    private javax.swing.JLabel gLbl;
    private javax.swing.JTextField inBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JButton plotBtn;
    private javax.swing.JMenu scaleLbl;
    // End of variables declaration//GEN-END:variables
}
