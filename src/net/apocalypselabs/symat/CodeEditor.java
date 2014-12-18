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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIDefaults;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

/**
 *
 * @author Skylar
 */
public class CodeEditor extends javax.swing.JInternalFrame {

    private final JFileChooser fc = new JFileChooser();
    private boolean isSaved = false;
    private final JTextPane codeBox = new JTextPane();
    private final TextLineNumber tln;
    private String lastSaved = "";

    /**
     * Creates new form CodeEditor
     */
    public CodeEditor() {
        initComponents();

        if (!PrefStorage.isset("advancedcontrols")) {
            runMenu.remove(codeLangMenu);
        }

        FileFilter filter = new FileNameExtensionFilter("SyMAT JavaScript (.syjs)", "syjs");
        fc.setFileFilter(filter);
        fc.addChoosableFileFilter(filter);
        filter = new FileNameExtensionFilter("SyMAT Python (.sypy)", "sypy");
        fc.addChoosableFileFilter(filter);
        filter = new FileNameExtensionFilter("JavaScript file (.js)", "js");
        fc.addChoosableFileFilter(filter);
        filter = new FileNameExtensionFilter("Python script (.py)", "py");
        fc.addChoosableFileFilter(filter);
        int font_size = 12;
        try {
            font_size = Integer.valueOf(PrefStorage.getSetting("editfont"));
        } catch (Exception ex) {
        }
        codeBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, font_size));
        outputBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, font_size));
        if (PrefStorage.getSetting("theme").equals("dark")) {
            setBackgroundOfEditor(Color.BLACK);
            codeBox.setForeground(Color.WHITE);
            outputBox.setBackground(Color.BLACK);
            outputBox.setForeground(Color.WHITE);
            setBackground(Color.DARK_GRAY);
        } else {
            setBackgroundOfEditor(Color.WHITE);
            codeBox.setForeground(Color.BLACK);
            outputBox.setBackground(Color.WHITE);
            outputBox.setForeground(Color.BLACK);
            setBackground(Color.LIGHT_GRAY);
        }
        TabStop[] tabs = new TabStop[30];
        for (int i = 0; i < tabs.length; i++) {
            tabs[i] = new TabStop(15 * i, TabStop.ALIGN_RIGHT, TabStop.LEAD_NONE);
        }
        TabSet tabset = new TabSet(tabs);
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.TabSet, tabset);
        codeBox.setParagraphAttributes(aset, false);
        tln = new TextLineNumber(codeBox);
        scrollPane.setRowHeaderView(tln);
        codeBox.setVisible(true);
        tln.setVisible(true);
        codeBox.requestFocus();
    }

    private void setBackgroundOfEditor(Color c) {
        UIDefaults defaults = new UIDefaults();
        defaults.put("TextPane[Enabled].backgroundPainter", c);
        codeBox.putClientProperty("Nimbus.Overrides", defaults);
        codeBox.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
        codeBox.setBackground(c);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        langBtnGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        scrollPane = new JScrollPane(codeBox);
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputBox = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenu = new javax.swing.JMenuItem();
        saveMenu = new javax.swing.JMenuItem();
        saveAsMenu = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        clrOutputMenu = new javax.swing.JMenuItem();
        codeLangMenu = new javax.swing.JMenu();
        javascriptOption = new javax.swing.JRadioButtonMenuItem();
        pythonOption = new javax.swing.JRadioButtonMenuItem();
        runMenu = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();

        jMenuItem4.setText("jMenuItem4");

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Editor");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/editor.png"))); // NOI18N
        setMinimumSize(new java.awt.Dimension(125, 50));

        jSplitPane1.setDividerLocation(220);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.7);

        scrollPane.setOpaque(false);
        scrollPane.setRequestFocusEnabled(false);
        jSplitPane1.setTopComponent(scrollPane);

        jLabel1.setText("Output:");

        outputBox.setEditable(false);
        outputBox.setColumns(20);
        outputBox.setLineWrap(true);
        outputBox.setRows(3);
        outputBox.setWrapStyleWord(true);
        jScrollPane1.setViewportView(outputBox);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
        );

        fileMenu.setText("File");

        openMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenu.setText("Open...");
        openMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuActionPerformed(evt);
            }
        });
        fileMenu.add(openMenu);

        saveMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenu.setText("Save...");
        saveMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenu);

        saveAsMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenu.setText("Save as...");
        saveAsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenu);

        exportMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportMenu.setText("Export...");
        exportMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMenuActionPerformed(evt);
            }
        });
        fileMenu.add(exportMenu);

        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");

        clrOutputMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        clrOutputMenu.setText("Clear output");
        clrOutputMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clrOutputMenuActionPerformed(evt);
            }
        });
        editMenu.add(clrOutputMenu);

        codeLangMenu.setText("Language");

        langBtnGroup.add(javascriptOption);
        javascriptOption.setSelected(true);
        javascriptOption.setText("Javascript");
        codeLangMenu.add(javascriptOption);

        langBtnGroup.add(pythonOption);
        pythonOption.setText("Python");
        codeLangMenu.add(pythonOption);

        editMenu.add(codeLangMenu);

        jMenuBar1.add(editMenu);

        runMenu.setText("Run");
        runMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runMenuActionPerformed(evt);
            }
        });

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        jMenuItem5.setText("Run code");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        runMenu.add(jMenuItem5);

        jMenuBar1.add(runMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuActionPerformed
        int r = fc.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            try {
                File f = fc.getSelectedFile();
                codeBox.setText(readFile(f.toString(), StandardCharsets.UTF_8));
                isSaved = true;
                lastSaved = codeBox.getText();
                setTitle("Editor - " + f.getName());
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(this,
                        "Error:  Cannot load file: " + ex.getMessage());
            }
        }
        codeBox.setCaretPosition(0);
    }//GEN-LAST:event_openMenuActionPerformed

    public void openFileFromString(String file) {
        try {
            File f = new File(file);
            codeBox.setText(readFile(f.toString(), StandardCharsets.UTF_8));
            isSaved = true;
            lastSaved = codeBox.getText();
            setTitle("Editor - " + f.getName());
            if (file.matches(".*\\.(js|mls|symt|syjs)")) {
                javascriptOption.setSelected(true);
                pythonOption.setSelected(false);
            } else if (file.matches(".*\\.(sypy|py)")) {
                javascriptOption.setSelected(false);
                pythonOption.setSelected(true);
            }
        } catch (IOException ex) {
            JOptionPane.showInternalMessageDialog(this,
                    "Error:  Cannot load file: " + ex.getMessage());
        }
    }

    private void saveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuActionPerformed
        if (!isSaved) {
            int r = fc.showSaveDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                try {
                    saveFile(codeBox.getText(), addSaveExt(fc.getSelectedFile().toString()));
                } catch (IOException ex) {
                    JOptionPane.showInternalMessageDialog(this, "Error:  Cannot save file: " + ex.getMessage());
                }
            }
        } else {
            try {
                saveFile(codeBox.getText(), addSaveExt(fc.getSelectedFile().toString()));
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(this, "Error:  Cannot save file: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_saveMenuActionPerformed

    private String addSaveExt(String path) {
        if (!path.matches(".*\\.(js|mls|symt|syjs|sypy|py)")) {
            if (pythonOption.isSelected()) {
                path += ".sypy";
            } else {
                path += ".syjs";
            }
        }
        return path;
    }
    private void saveAsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuActionPerformed
        isSaved = false; // Reset saved status, force dialog
        saveMenuActionPerformed(evt);
    }//GEN-LAST:event_saveAsMenuActionPerformed

    private void runMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runMenuActionPerformed

    }//GEN-LAST:event_runMenuActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        if (javascriptOption.isSelected()) {
            execCode("javascript");
        } else if (pythonOption.isSelected()) {
            execCode("python");
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void execCode(String lang) {
        CodeRunner cr = new CodeRunner(lang);
        System.out.println(lang);
        System.out.println(codeBox.getText());
        Object result = cr.evalString(codeBox.getText());
        try {
            outputBox.append(result.toString() + "\n");
        } catch (NullPointerException ex) {

        }
    }
    private void clrOutputMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clrOutputMenuActionPerformed
        outputBox.setText("");
    }//GEN-LAST:event_clrOutputMenuActionPerformed

    private void exportMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMenuActionPerformed
        String lang = "js";
        if (pythonOption.isSelected()) {
            lang = "python";
        }
        CodeExport ce = new CodeExport(codeBox.getText(), lang);
        MainGUI.loadFrame(ce);
    }//GEN-LAST:event_exportMenuActionPerformed

    private void saveFile(String content, String path)
            throws IOException {
        try (PrintStream out = new PrintStream(new FileOutputStream(path))) {
            out.print(content);
        }
        setTitle("Editor - " + (new File(path)).getName());
        lastSaved = content;
        isSaved = true;
    }

    @Override
    public void doDefaultCloseAction() {
        if (lastSaved.equals(codeBox.getText())) {
            dispose();
        } else {
            int p = JOptionPane.showInternalConfirmDialog(this,
                    "Are you sure you want to exit without saving this file?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (p == JOptionPane.YES_OPTION) {
                dispose();
            } else {
                saveMenuActionPerformed(null);
            }
        }
    }

    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem clrOutputMenu;
    private javax.swing.JMenu codeLangMenu;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exportMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JRadioButtonMenuItem javascriptOption;
    private javax.swing.ButtonGroup langBtnGroup;
    private javax.swing.JMenuItem openMenu;
    private javax.swing.JTextArea outputBox;
    private javax.swing.JRadioButtonMenuItem pythonOption;
    private javax.swing.JMenu runMenu;
    private javax.swing.JMenuItem saveAsMenu;
    private javax.swing.JMenuItem saveMenu;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
