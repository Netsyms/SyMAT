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
import java.awt.Graphics;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

/**
 *
 * @author Skylar
 */
public class MainGUI extends javax.swing.JFrame {

    public static final String APP_NAME = "SyMAT 0.7";
    public static final double APP_CODE = 0.7;
    public static String argfile = "";
    public static boolean skipPython = false; // Skip python init on start?

    /**
     * Creates new form MainGUI
     */
    public MainGUI() {
        initComponents();
        setIconImage((new ImageIcon(getClass().getResource("icon.png"))).getImage());
        setLocationRelativeTo(null);
        try {
            URL url = new URL("http://symat.aplabs.us/version.txt");
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            br.close();
            is.close();
            double version = Double.parseDouble(line);
            if (version > APP_CODE) {
                if (PrefStorage.getSetting("update-ignore").equals(APP_CODE + "|" + version)) {
                    System.out.println("An update was found, but has been ignored by the user.");
                } else {
                    loadFrame(new Update(version));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Fail:  Cannot check update server.  \n"
                    + "       Assuming local copy up-to-date.");
        }

        // Open shell unless prog was run with argument
        if (argfile.equals("")) {
            Interpreter sh = new Interpreter();
            loadFrame(sh);
        } else {
            CodeEditor ed = new CodeEditor();
            loadFrame(ed);
            ed.openFileFromString(argfile);
            argfile = "";
        }
        updateDisplay();
    }

    /**
     * (Re)load display settings.
     */
    public static void updateDisplay() {
        mainPane.paintImmediately(0, 0, mainPane.getWidth(), mainPane.getHeight());
        if (PrefStorage.getSetting("theme").equals("dark")) {
            tabs.setBackground(Color.BLACK);
        } else {
            tabs.setBackground(new Color(240, 240, 240));
        }
    }

    private static String namemark() {
        String nbsp = "";
        for (int i = 0; i < 8; i++) {
            nbsp += "&nbsp;";
        }
        return "<html>"
                + nbsp
                + "<span style=\"color: gray; font-size: 130%;\"><i>"
                + APP_NAME
                + "</i></span>&nbsp;&nbsp;";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabs = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        shellBtn = new javax.swing.JButton();
        editorBtn = new javax.swing.JButton();
        graphBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        helpBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        arrangeWindowsBtn = new javax.swing.JButton();
        closeAllBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        displaySettingsBtn = new javax.swing.JButton();
        mainPane = mainPane = new javax.swing.JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (PrefStorage.getSetting("theme").equals("dark")) {
                    g.setColor(Color.DARK_GRAY);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
        ;

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(APP_NAME);
        setMinimumSize(new java.awt.Dimension(300, 400));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        tabs.setBackground(new Color(240,240,240));
        tabs.setOpaque(true);

        jPanel4.setFocusable(false);
        jPanel4.setLayout(null);
        tabs.addTab("", new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icon16.png")), jPanel4); // NOI18N

        jPanel1.setOpaque(false);

        shellBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/shell.png"))); // NOI18N
        shellBtn.setText("Shell");
        shellBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        shellBtn.setFocusable(false);
        shellBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        shellBtn.setOpaque(false);
        shellBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        shellBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shellBtnActionPerformed(evt);
            }
        });

        editorBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/editor.png"))); // NOI18N
        editorBtn.setText("Editor");
        editorBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        editorBtn.setFocusable(false);
        editorBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editorBtn.setOpaque(false);
        editorBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        editorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editorBtnActionPerformed(evt);
            }
        });

        graphBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/graph.png"))); // NOI18N
        graphBtn.setText("Graph");
        graphBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        graphBtn.setFocusable(false);
        graphBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        graphBtn.setOpaque(false);
        graphBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        graphBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphBtnActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText(namemark());
        jLabel1.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(shellBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(graphBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(shellBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editorBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(graphBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        tabs.addTab("Apps", jPanel1);

        jPanel2.setOpaque(false);

        helpBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/help.png"))); // NOI18N
        helpBtn.setText("Manual");
        helpBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        helpBtn.setFocusable(false);
        helpBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        helpBtn.setOpaque(false);
        helpBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        helpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpBtnActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText(namemark());
        jLabel3.setFocusable(false);

        arrangeWindowsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/cascade.png"))); // NOI18N
        arrangeWindowsBtn.setText("Cascade");
        arrangeWindowsBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        arrangeWindowsBtn.setFocusable(false);
        arrangeWindowsBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        arrangeWindowsBtn.setOpaque(false);
        arrangeWindowsBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        arrangeWindowsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrangeWindowsBtnActionPerformed(evt);
            }
        });

        closeAllBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/closeall.png"))); // NOI18N
        closeAllBtn.setText("Close All");
        closeAllBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        closeAllBtn.setFocusable(false);
        closeAllBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        closeAllBtn.setOpaque(false);
        closeAllBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        closeAllBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeAllBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(arrangeWindowsBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeAllBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(helpBtn)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(arrangeWindowsBtn)
            .addComponent(closeAllBtn)
        );

        tabs.addTab("Tools", jPanel2);

        jPanel3.setOpaque(false);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText(namemark());
        jLabel4.setFocusable(false);

        displaySettingsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/display.png"))); // NOI18N
        displaySettingsBtn.setText("Display");
        displaySettingsBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        displaySettingsBtn.setFocusable(false);
        displaySettingsBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        displaySettingsBtn.setOpaque(false);
        displaySettingsBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        displaySettingsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displaySettingsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displaySettingsBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(displaySettingsBtn)
        );

        tabs.addTab("Settings", jPanel3);

        tabs.setSelectedIndex(1);

        mainPane.setBackground(new java.awt.Color(204, 204, 204));
        mainPane.setAutoscrolls(true);
        mainPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        mainPane.setOpaque(false);

        javax.swing.GroupLayout mainPaneLayout = new javax.swing.GroupLayout(mainPane);
        mainPane.setLayout(mainPaneLayout);
        mainPaneLayout.setHorizontalGroup(
            mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        mainPaneLayout.setVerticalGroup(
            mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 481, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPane)
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mainPane))
        );

        tabs.setEnabledAt(0, false);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void shellBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shellBtnActionPerformed
        Interpreter i = new Interpreter();
        loadFrame(i);
    }//GEN-LAST:event_shellBtnActionPerformed

    private void editorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editorBtnActionPerformed
        CodeEditor e = new CodeEditor();
        loadFrame(e);
    }//GEN-LAST:event_editorBtnActionPerformed

    private void graphBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphBtnActionPerformed
        Graph g = new Graph();
        loadFrame(g);
    }//GEN-LAST:event_graphBtnActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        setLocationRelativeTo(null);
    }//GEN-LAST:event_formComponentShown

    private void helpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpBtnActionPerformed
        Help h = new Help();
        loadFrame(h);
    }//GEN-LAST:event_helpBtnActionPerformed

    private void arrangeWindowsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrangeWindowsBtnActionPerformed
        cascade();
    }//GEN-LAST:event_arrangeWindowsBtnActionPerformed

    private void displaySettingsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displaySettingsBtnActionPerformed
        Display d = new Display();
        loadFrame(d);
    }//GEN-LAST:event_displaySettingsBtnActionPerformed

    private void closeAllBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeAllBtnActionPerformed
        for (JInternalFrame ji : mainPane.getAllFrames()) {
            try {
                ji.setClosed(true);
            } catch (PropertyVetoException ex) {
                ji.dispose();
            }
        }
    }//GEN-LAST:event_closeAllBtnActionPerformed

    /**
     * Adds the given JInternalFrame to the mainPane. Automatically does layout
     * and sets visible as well.
     *
     * @param frame
     */
    public static void loadFrame(JInternalFrame frame) {
        int w = frame.getWidth();
        int h = frame.getHeight();
        int pw = mainPane.getWidth();
        int ph = mainPane.getHeight();
        if (frame.isResizable()) {
            if (h > ph) {
                h = ph;
            }
            if (w > pw) {
                w = pw;
            }
            frame.setSize(w, h);
        }
        mainPane.add(frame);
        frame.setLocation(
                (pw / 2) - (w / 2),
                (ph / 2) - (h / 2));
        if (frame.getLocation().y < 0) {
            frame.setLocation(frame.getLocation().x, 0);
        }
        frame.setVisible(true);
        //updateDisplay();
    }

    public static void cascade() {
        JInternalFrame[] frames = mainPane.getAllFrames();
        int x = 12;
        int y = 24;
        Debug.println("Cascading " + frames.length + " frames...");
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].isVisible()) {
                Debug.println("Frame: " + frames[i].getTitle() + ", Times: " + i + ", Xpos: " + x * i + ", Ypos: " + y * i);
                frames[i].setBounds(x * i, y * i, frames[i].getWidth(), frames[i].getHeight());
                frames[i].toFront();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        // Command line args
        for (String arg : args) {
            if (arg.equals("skippython")) {
                skipPython = true;
            } else {
                argfile = args[0];
            }
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SplashScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton arrangeWindowsBtn;
    public static javax.swing.JButton closeAllBtn;
    public static javax.swing.JButton displaySettingsBtn;
    public static javax.swing.JButton editorBtn;
    public static javax.swing.JButton graphBtn;
    public static javax.swing.JButton helpBtn;
    public static javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JPanel jPanel1;
    public static javax.swing.JPanel jPanel2;
    public static javax.swing.JPanel jPanel3;
    public static javax.swing.JPanel jPanel4;
    public static javax.swing.JDesktopPane mainPane;
    public static javax.swing.JButton shellBtn;
    public static javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
