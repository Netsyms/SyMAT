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
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.UIManager;

/**
 * This class is like the Force: A light theme, a dark theme, and it binds the
 * app together. Also like duct tape, but that's not as cool.
 *
 * @author Skylar
 */
public class MainGUI extends javax.swing.JFrame {

    // TODO: Add more code comments and stuff in case anybody else reads this
    public static final String APP_NAME = "SyMAT 1.0";
    public static final double APP_CODE = 12;
    public static final String VERSION_NAME = "1.0";
    public static final String API_URL = "https://apis.symatapp.com/";
    public static String argfile = "";
    public static boolean skipPython = false; // Skip python init on start?
    public static boolean skipEditor = false; // Skip editor init on start?

    private static boolean recentItemsMinimized = false;

    public static boolean updateAvailable = false;
    public static String updateString = "";

    public static Font ubuntuRegular;

    /**
     * Creates the main app window and does some quick things that aren't
     * threaded in SplashScreen.
     */
    public MainGUI() {
        initComponents();
        setIconImage((new ImageIcon(
                getClass().getResource("icon.png"))).getImage());
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                int p = JOptionPane.showConfirmDialog(MainGUI.mainPane,
                        "Are you sure you want to exit SyMAT?",
                        "Exit SyMAT",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (p == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        setButtonShortcuts();

        // Open initial windows
        boolean loaded = false;
        if (!argfile.equals("")) {
            CodeEditor ed = new CodeEditor();
            loadFrame(ed);
            ed.openFileFromName(argfile);
            argfile = "";
            loaded = true;
        }
        if (PrefStorage.getSetting("license").equals("")
                || PrefStorage.getSetting("licensetype").equals("demo")) {
            boolean licValid = false;
            if (PrefStorage.getSetting("licensetype").equals("demo")) {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                try {
                    long expire = Long.parseLong(PrefStorage.getSetting("license"));
                    if (expire > c.getTimeInMillis()) {
                        licValid = true;
                    }
                } catch (NumberFormatException e) {

                }
            }
            if (!licValid) {
                licenseRestrict(true);
                loadFrame(new FirstRun());
                loaded = true;
            }
        }
        // Only load shell if nothing else is going on
        if (argfile.equals("") && !loaded) {
            loadFrame(new Interpreter());
        }
        if (updateAvailable) {
            loadFrame(new Update(updateString));
        }
        loadRecentFiles();
        updateDisplay();
    }

    public static void licenseRestrict(boolean restricted) {
        editorBtn.setEnabled(!restricted);
        graphBtn.setEnabled(!restricted);
        helpBtn.setEnabled(!restricted);
    }

    /**
     * Set keyboard shortcuts for buttons.
     */
    private void setButtonShortcuts() {
        shellBtn.setMnemonic(KeyEvent.VK_S);
        editorBtn.setMnemonic(KeyEvent.VK_D);
        graphBtn.setMnemonic(KeyEvent.VK_G);
        helpBtn.setMnemonic(KeyEvent.VK_M);
        tabs.setMnemonicAt(0, KeyEvent.VK_A);
        tabs.setMnemonicAt(1, KeyEvent.VK_V);
    }

    /**
     * (Re)load display settings.
     */
    public static void updateDisplay() {
        mainPane.paintImmediately(0, 0,
                mainPane.getWidth(), mainPane.getHeight());
        if (PrefStorage.getSetting("theme").equals("dark")) {
            tabs.setBackground(new Color(41, 49, 52));
        } else {
            tabs.setBackground(new Color(240, 240, 240));
        }
    }

    /**
     * Get the markup for the watermark thing on the Ribbon.
     *
     * @return HTML for a JLabel.
     */
    private static String namemark() {
        String nbsp = "";
        String demo = "";
        for (int i = 0; i < 8; i++) {
            nbsp += "&nbsp;";
        }
        if (PrefStorage.getSetting("licensetype").equals("demo")) {
            demo = " Trial";
        }
        if (PrefStorage.getSetting("license").equals("")) {
            demo = " Unregistered";
        }
        return "<html>"
                + nbsp
                + "<span style=\"color: gray; font-size: 130%;\"><i>"
                + APP_NAME + demo
                + "</i></span>&nbsp;&nbsp;";
    }

    public static void updateNamemark() {
        jLabel1.setText(namemark());
        jLabel3.setText(namemark());
    }

    public static void loadRecentFiles() {
        String files = PrefStorage.getSetting("recentfiles");
        if (files.equals("")) {
            return;
        }
        String[] fileList = files.split("\n");
        int neededLength = 0;
        for (String file : fileList) {
            if ((new File(file)).isFile()) {
                neededLength++;
            }
        }
        if (neededLength > 10) {
            neededLength = 10;
        }
        KeyValListItem[] items = new KeyValListItem[neededLength];
        int i = 0;
        for (String f : fileList) {
            File file = new File(f);
            if (file.isFile() && i < neededLength) {
                items[i] = new KeyValListItem(file.getName(), file.getPath());
                i++;
            }
        }

        recentFileList.setListData(items);

        // Re-save list to remove bad entries
        String list = "";
        for (KeyValListItem item : items) {
            list += item.getValue() + "\n";
        }
        PrefStorage.saveSetting("recentfiles", list);
    }

    public static void addRecentFile(String file) {
        file = (new File(file)).getAbsolutePath();
        String files = PrefStorage.getSetting("recentfiles");
        String[] fileList = files.split("\n");
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].trim().equals(file)) {
                fileList[i] = "";
            }
        }
        files = file + "\n";
        for (String f : fileList) {
            if (!f.trim().equals("")) {
                files += f + "\n";
            }
        }
        PrefStorage.saveSetting("recentfiles", files);
        PrefStorage.save();
        loadRecentFiles();
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
        shellBtn = new javax.swing.JButton();
        editorBtn = new javax.swing.JButton();
        graphBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        helpBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        arrangeWindowsBtn = new javax.swing.JButton();
        closeAllBtn = new javax.swing.JButton();
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
        jLabel2 = new javax.swing.JLabel();
        recentItemsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        recentFileList = new javax.swing.JList();
        recentFileBtn = new javax.swing.JButton();
        recentItemsTitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SyMAT");
        setMinimumSize(new java.awt.Dimension(640, 540));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        tabs.setBackground(new Color(240,240,240));
        tabs.setOpaque(true);

        jPanel4.setFocusable(false);
        jPanel4.setOpaque(false);

        shellBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/images/shell.png"))); // NOI18N
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

        editorBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/images/editor.png"))); // NOI18N
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

        graphBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/images/graph.png"))); // NOI18N
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

        jLabel1.setFont(MainGUI.ubuntuRegular.deriveFont(11.0F));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText(namemark());
        jLabel1.setFocusable(false);

        helpBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/images/help.png"))); // NOI18N
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(shellBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editorBtn)
                .addGap(12, 12, 12)
                .addComponent(graphBtn)
                .addGap(11, 11, 11)
                .addComponent(helpBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(shellBtn)
                    .addComponent(editorBtn)
                    .addComponent(graphBtn)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(helpBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("Apps", jPanel4);

        jPanel2.setOpaque(false);

        jLabel3.setFont(MainGUI.ubuntuRegular.deriveFont(11.0F));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText(namemark());
        jLabel3.setFocusable(false);

        arrangeWindowsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/images/cascade.png"))); // NOI18N
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

        closeAllBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/images/closeall.png"))); // NOI18N
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

        displaySettingsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/images/display.png"))); // NOI18N
        displaySettingsBtn.setText("Theme");
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displaySettingsBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(arrangeWindowsBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeAllBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrangeWindowsBtn)
                    .addComponent(closeAllBtn)
                    .addComponent(displaySettingsBtn))
                .addContainerGap())
        );

        tabs.addTab("View", jPanel2);

        mainPane.setBackground(new java.awt.Color(204, 204, 204));
        mainPane.setAutoscrolls(true);
        mainPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        mainPane.setOpaque(false);

        jLabel2.setFont(MainGUI.ubuntuRegular.deriveFont(48.0F));
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("SyMAT");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        recentItemsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        recentItemsPanel.setForeground(new java.awt.Color(153, 153, 153));
        recentItemsPanel.setMaximumSize(new java.awt.Dimension(160, 234));
        recentItemsPanel.setOpaque(false);

        recentFileList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        recentFileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recentFileListMouseClicked(evt);
            }
        });
        recentFileList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                recentFileListMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(recentFileList);

        recentFileBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/openfile.png"))); // NOI18N
        recentFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recentFileBtnActionPerformed(evt);
            }
        });

        recentItemsTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        recentItemsTitle.setForeground(new java.awt.Color(102, 102, 102));
        recentItemsTitle.setText("  Recent Files");
        recentItemsTitle.setOpaque(true);
        recentItemsTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recentItemsTitleMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout recentItemsPanelLayout = new javax.swing.GroupLayout(recentItemsPanel);
        recentItemsPanel.setLayout(recentItemsPanelLayout);
        recentItemsPanelLayout.setHorizontalGroup(
            recentItemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recentItemsPanelLayout.createSequentialGroup()
                .addGroup(recentItemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recentItemsPanelLayout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(recentFileBtn))
                    .addGroup(recentItemsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(recentItemsTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        recentItemsPanelLayout.setVerticalGroup(
            recentItemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recentItemsPanelLayout.createSequentialGroup()
                .addComponent(recentItemsTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recentFileBtn)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPaneLayout = new javax.swing.GroupLayout(mainPane);
        mainPane.setLayout(mainPaneLayout);
        mainPaneLayout.setHorizontalGroup(
            mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(recentItemsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        mainPaneLayout.setVerticalGroup(
            mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recentItemsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        mainPane.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainPane.setLayer(recentItemsPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        setLocationRelativeTo(null);
    }//GEN-LAST:event_formComponentShown

    /*
     This section has all the buttons!
     */

    private void displaySettingsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displaySettingsBtnActionPerformed
        loadFrame(new Display());
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

    private void arrangeWindowsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrangeWindowsBtnActionPerformed
        cascade();
    }//GEN-LAST:event_arrangeWindowsBtnActionPerformed

    private void helpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpBtnActionPerformed
        loadFrame(new Help());
    }//GEN-LAST:event_helpBtnActionPerformed

    private void graphBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphBtnActionPerformed
        loadFrame(new Graph());
    }//GEN-LAST:event_graphBtnActionPerformed

    private void editorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editorBtnActionPerformed
        loadFrame(new CodeEditor());
    }//GEN-LAST:event_editorBtnActionPerformed

    private void shellBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shellBtnActionPerformed
        loadFrame(new Interpreter());
    }//GEN-LAST:event_shellBtnActionPerformed

    private void recentFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recentFileBtnActionPerformed
        if (recentFileList.getSelectedValue() == null) {
            return;
        }
        KeyValListItem file = (KeyValListItem) recentFileList.getSelectedValue();
        if (file.isEmpty()) {
            return;
        }
        CodeEditor edit = new CodeEditor();
        Debug.println(file.getValue());
        edit.openFileFromName(file.getValue());
        loadFrame(edit);
    }//GEN-LAST:event_recentFileBtnActionPerformed

    private void recentFileListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentFileListMouseClicked
        if (evt.getClickCount() == 2) {
            recentFileBtnActionPerformed(null);
        }
    }//GEN-LAST:event_recentFileListMouseClicked

    private void recentFileListMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentFileListMouseMoved
        try {
            ListModel m = recentFileList.getModel();
            int index = recentFileList.locationToIndex(evt.getPoint());
            if (index > -1) {
                recentFileList.setToolTipText(
                        ((KeyValListItem) m.getElementAt(index)).getValue());
            }
        } catch (Exception ex) {
            // This feature is optional.  Just skip it if it's broken.
            recentFileList.setToolTipText("");
        }
    }//GEN-LAST:event_recentFileListMouseMoved

    private void recentItemsTitleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentItemsTitleMouseClicked
        if (evt.getClickCount() == 2) {
            if (recentItemsMinimized) {
                recentItemsPanel.setSize(recentItemsPanel.getWidth(),
                        (int) recentItemsPanel.getMaximumSize().getHeight());
                recentItemsMinimized = false;
            } else {
                recentItemsPanel.setSize(recentItemsPanel.getWidth(),
                        recentItemsTitle.getHeight());
                recentItemsMinimized = true;
            }
        }
    }//GEN-LAST:event_recentItemsTitleMouseClicked

    /*
     End the button handlers.
     */
    /**
     * Adds the given JInternalFrame to the mainPane. Automatically does layout
     * and sets visible (if show==true).
     *
     * @param frame The frame
     * @param show Should the frame be visible?
     */
    public static void loadFrame(JInternalFrame frame, boolean show) {
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
        if (show) {
            frame.setVisible(true);
        }
    }

    /**
     * Adds the given JInternalFrame to the mainPane. Automatically does layout
     * and sets visible.
     *
     * @param frame The frame
     */
    public static void loadFrame(JInternalFrame frame) {
        loadFrame(frame, true);
    }

    /**
     * Cascade all the frames in a stack. Somehow reverses the order each time
     * around, I have no idea why but it's a "feature" now!
     */
    public static void cascade() {
        JInternalFrame[] frames = mainPane.getAllFrames();
        int x = 12;
        int y = 24;
        Debug.println("Cascading " + frames.length + " frames...");
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].isVisible()) {
                Debug.println("Frame: "
                        + frames[i].getTitle()
                        + ", Times: " + i
                        + ", Xpos: " + x * i
                        + ", Ypos: " + y * i);
                frames[i].setBounds(x * i,
                        y * i,
                        frames[i].getWidth(),
                        frames[i].getHeight());
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
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        try {
            // Ubuntu font for prettifying
            ubuntuRegular = Font.createFont(Font.TRUETYPE_FONT,
                    new File(MainGUI.class.
                            getResource("resources/Ubuntu-R.ttf").toURI()));
        } catch (URISyntaxException | FontFormatException | IOException ex) {
            ubuntuRegular = Font.getFont(Font.SANS_SERIF);
        }

        // Command line args
        for (String arg : args) {
            switch (arg) {
                case "skippython":
                    skipPython = true;
                    break;
                case "skipeditor":
                    skipEditor = true;
                    break;
                case "quickstart":
                    skipPython = true;
                    skipEditor = true;
                    break;
                case "licensereset":
                    PrefStorage.saveSetting("license", "");
                    break;
                default:
                    argfile = arg;
                    break;
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
    public static javax.swing.JLabel jLabel2;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JPanel jPanel2;
    public static javax.swing.JPanel jPanel4;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JDesktopPane mainPane;
    public static javax.swing.JButton recentFileBtn;
    public static javax.swing.JList recentFileList;
    public static javax.swing.JPanel recentItemsPanel;
    public static javax.swing.JLabel recentItemsTitle;
    public static javax.swing.JButton shellBtn;
    public static javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
