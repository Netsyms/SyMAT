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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 *
 * @author Skylar
 */
public class CodeEditor extends javax.swing.JInternalFrame {

    private final JFileChooser fc = new JFileChooser();
    private boolean isSaved = false;
    private RSyntaxTextArea codeBox = new RSyntaxTextArea();
    private final RTextScrollPane sp;
    private boolean fileChanged = false;

    private CompletionProvider jscomp = new CodeCompleter("js").getProvider();
    private CompletionProvider pycomp = new CodeCompleter("py").getProvider();
    private AutoCompletion jsac = new AutoCompletion(jscomp);
    private AutoCompletion pyac = new AutoCompletion(pycomp);

    private File filedata;

    /**
     * Creates new form CodeEditor
     */
    public CodeEditor() {
        initComponents();

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

        loadTheme();

        codeBox.setCodeFoldingEnabled(true);
        codeBox.setAntiAliasingEnabled(true);
        codeBox.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        sp = new RTextScrollPane(codeBox);
        sp.setFoldIndicatorEnabled(true);
        editPanel.add(sp);
        codeBox.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jsac.install(codeBox);
        sp.setVisible(true);
        codeBox.setVisible(true);
        codeBox.requestFocus();

        codeBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {
                fileChanged = true;
            }
        });
    }

    public CodeEditor(String text) {
        this();
        codeBox.setText(text);
    }

    public CodeEditor(String text, boolean openSaveDialog) {
        this(text);
        if (openSaveDialog) {
            saveAsMenuActionPerformed(null);
        }
    }

    private void setEditorTheme(String themeName) {
        try {
            Theme theme = Theme.load(CodeEditor.class
                    .getResourceAsStream("resources/" + themeName + ".xml"));
            theme.apply(codeBox);
        } catch (Exception e) {
        }
    }

    private void loadTheme() {
        if (PrefStorage.getSetting("theme").equals("dark")) {
            outputBox.setBackground(new Color(41, 49, 52));
            outputBox.setForeground(Color.WHITE);
            setBackground(Color.DARK_GRAY);
            setEditorTheme("dark");
        } else {
            outputBox.setBackground(Color.WHITE);
            outputBox.setForeground(Color.BLACK);
            setBackground(Color.LIGHT_GRAY);
            setEditorTheme("default");
        }
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
        outputPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputBox = new javax.swing.JTextArea();
        clearBtn = new javax.swing.JButton();
        editPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenu = new javax.swing.JMenuItem();
        openSampleBtn = new javax.swing.JMenu();
        sampleHelloWorld = new javax.swing.JMenuItem();
        sampleGraph = new javax.swing.JMenuItem();
        saveMenu = new javax.swing.JMenuItem();
        saveAsMenu = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenuItem();
        shareMenu = new javax.swing.JMenuItem();
        shareAsMenu = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoBtn = new javax.swing.JMenuItem();
        redoBtn = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        cutBtn = new javax.swing.JMenuItem();
        copyBtn = new javax.swing.JMenuItem();
        pasteBtn = new javax.swing.JMenuItem();
        runMenu = new javax.swing.JMenu();
        runCodeBtn = new javax.swing.JMenuItem();
        codeLangMenu = new javax.swing.JMenu();
        javascriptOption = new javax.swing.JRadioButtonMenuItem();
        pythonOption = new javax.swing.JRadioButtonMenuItem();

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
        setPreferredSize(new java.awt.Dimension(550, 375));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.7);

        jLabel1.setText("Output:");

        outputBox.setEditable(false);
        outputBox.setColumns(20);
        outputBox.setLineWrap(true);
        outputBox.setRows(3);
        outputBox.setWrapStyleWord(true);
        outputBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                outputBoxMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(outputBox);

        clearBtn.setText("Clear");
        clearBtn.setToolTipText("");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(clearBtn))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(outputPanel);

        editPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setLeftComponent(editPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
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

        openSampleBtn.setText("Open Code Sample");

        sampleHelloWorld.setText("helloworld");
        sampleHelloWorld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sampleHelloWorldActionPerformed(evt);
            }
        });
        openSampleBtn.add(sampleHelloWorld);

        sampleGraph.setText("graph");
        sampleGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sampleGraphActionPerformed(evt);
            }
        });
        openSampleBtn.add(sampleGraph);

        fileMenu.add(openSampleBtn);

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

        shareMenu.setText("Share...");
        shareMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shareMenuActionPerformed(evt);
            }
        });
        fileMenu.add(shareMenu);

        shareAsMenu.setText("Share as...");
        shareAsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shareAsMenuActionPerformed(evt);
            }
        });
        fileMenu.add(shareAsMenu);

        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");

        undoBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoBtn.setText("Undo");
        undoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoBtnActionPerformed(evt);
            }
        });
        editMenu.add(undoBtn);

        redoBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        redoBtn.setText("Redo");
        redoBtn.setToolTipText("");
        redoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoBtnActionPerformed(evt);
            }
        });
        editMenu.add(redoBtn);
        editMenu.add(jSeparator1);

        cutBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        cutBtn.setText("Cut");
        cutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cutBtnActionPerformed(evt);
            }
        });
        editMenu.add(cutBtn);

        copyBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyBtn.setText("Copy");
        copyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyBtnActionPerformed(evt);
            }
        });
        editMenu.add(copyBtn);

        pasteBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteBtn.setText("Paste");
        pasteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteBtnActionPerformed(evt);
            }
        });
        editMenu.add(pasteBtn);

        jMenuBar1.add(editMenu);

        runMenu.setText("Run");

        runCodeBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        runCodeBtn.setText("Run code");
        runCodeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runCodeBtnActionPerformed(evt);
            }
        });
        runMenu.add(runCodeBtn);

        codeLangMenu.setText("Language");

        langBtnGroup.add(javascriptOption);
        javascriptOption.setSelected(true);
        javascriptOption.setText("Javascript");
        javascriptOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javascriptOptionActionPerformed(evt);
            }
        });
        codeLangMenu.add(javascriptOption);

        langBtnGroup.add(pythonOption);
        pythonOption.setText("Python");
        pythonOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pythonOptionActionPerformed(evt);
            }
        });
        codeLangMenu.add(pythonOption);

        runMenu.add(codeLangMenu);

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
                codeBox.setText(FileUtils.readFile(f.toString()));
                isSaved = true;
                filedata = f;
                setTitle("Editor - " + f.getName());
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(this,
                        "Error:  Cannot load file: " + ex.getMessage());
            }
        }
        codeBox.setCaretPosition(0);
    }//GEN-LAST:event_openMenuActionPerformed

    /**
     * Open a file given its path as a String.
     *
     * @param file The path to the file.
     */
    public void openFileFromName(String file) {
        try {
            Debug.println(file);
            File f = new File(file);
            openString(FileUtils.readFile(f.getAbsolutePath()),
                    f.getAbsolutePath(), true);
        } catch (IOException ex) {
            JOptionPane.showInternalMessageDialog(MainGUI.mainPane,
                    "Error:  Cannot load file: " + ex.getMessage());
            MainGUI.loadRecentFiles();
        }
    }

    /**
     * Open a file.
     *
     * @param data The file contents
     * @param file Name of the file (ex. "foobar.syjs")
     */
    private void openString(String data, String file, boolean saved) {
        codeBox.setText(data);
        isSaved = saved;
        fileChanged = false;
        setTitle("Editor - " + (new File(file)).getName());
        if (file.matches(".*\\.(js|mls|symt|syjs)")) {
            javascriptOption.setSelected(true);
            pythonOption.setSelected(false);
            codeBox.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
            pyac.uninstall();
            jsac.install(codeBox);
        } else if (file.matches(".*\\.(sypy|py)")) {
            javascriptOption.setSelected(false);
            pythonOption.setSelected(true);
            codeBox.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
            jsac.uninstall();
            pyac.install(codeBox);
        }
        filedata = new File(file);
    }

    private void saveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuActionPerformed
        if (!isSaved) {
            int r = fc.showSaveDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                try {
                    filedata = FileUtils.getFileWithExtension(fc);
                    FileUtils.saveFile(codeBox.getText(), filedata.getAbsolutePath(), true);
                    isSaved = true;
                    fileChanged = false;
                    setTitle("Editor - "
                            + FileUtils.getFileWithExtension(fc).getName());
                } catch (IOException ex) {
                    JOptionPane.showInternalMessageDialog(this,
                            "Error:  Cannot save file: " + ex.getMessage());
                }
            }
        } else {
            try {
                FileUtils.saveFile(codeBox.getText(),
                        filedata.getAbsolutePath(), true);
                fileChanged = false;
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(this,
                        "Error:  Cannot save file: " + ex.getMessage());
            }
        }
        try {
            Debug.println(filedata.toString());
            Debug.println(filedata.getAbsolutePath());
        } catch (Exception ex) {

        }
    }//GEN-LAST:event_saveMenuActionPerformed

    private void saveAsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuActionPerformed
        isSaved = false; // Reset saved status, force dialog
        saveMenuActionPerformed(evt);
    }//GEN-LAST:event_saveAsMenuActionPerformed

    private void runCodeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runCodeBtnActionPerformed
        if (javascriptOption.isSelected()) {
            new RunThread("javascript").start();
        } else if (pythonOption.isSelected()) {
            new RunThread("python").start();
        }
    }//GEN-LAST:event_runCodeBtnActionPerformed

    /**
     * This thread runs the code.
     */
    private class RunThread extends Thread {

        String lang = "";

        public RunThread(String codelang) {
            lang = codelang;
        }

        @Override
        public void run() {
            setRunning(true);
            execCode(lang);
            setRunning(false);
        }

        public void setRunning(boolean isRunning) {
            final boolean running = isRunning;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (running) {
                        runMenu.setText("Running...");
                        codeBox.setEnabled(false);
                        for (Component mu : jMenuBar1.getComponents()) {
                            mu.setEnabled(false);
                        }
                        jMenuBar1.setEnabled(false);
                    } else {
                        runMenu.setText("Run");
                        codeBox.setEnabled(true);
                        for (Component mu : jMenuBar1.getComponents()) {
                            mu.setEnabled(true);
                        }
                        jMenuBar1.setEnabled(true);
                    }
                }
            });
        }
    }

    private void execCode(String lang) {
        if (!checkRequiredVersion(codeBox.getText(), lang)) {
            return;
        }
        CodeRunner cr = new CodeRunner(lang);
        String script = loadExternalScripts(codeBox.getText(), lang);
        Debug.println(lang);
        Debug.println(script);
        Object result = cr.evalString(script);
        try {
            outputBox.append(result.toString() + "\n");
        } catch (NullPointerException ex) {

        }
    }

    private boolean checkRequiredVersion(String script, String lang) {
        String prefix = "//";
        if (lang.startsWith("p")) {
            prefix = "##";
        }
        String line = script.trim().split("\\n", 2)[0];
        if (line.startsWith(prefix + "needs ")) {
            String versions = line.substring(8).trim();
            Debug.println(versions);
            String min = versions;
            String max = "999999999";
            if (versions.contains("-")) {
                min = versions.split("-")[0];
                max = versions.split("-")[1];
            }
            try {
                int minNum = Integer.parseInt(min);
                int maxNum = Integer.parseInt(max);
                if (!(minNum <= MainGUI.APP_CODE
                        && maxNum >= MainGUI.APP_CODE)) {
                    JOptionPane.showInternalMessageDialog(this, "This script "
                            + "cannot be run on this version of SyMAT.");
                    return false;
                }
                return true;
            } catch (Exception ex) {
                outputBox.append("Error: Bad version selection syntax: "
                        + ex.getMessage() + "\n");
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * Load external script files, relative to the current file.
     *
     * @param script The file to parse for includes.
     * @param lang The script language.
     * @return The script modified as needed.
     */
    private String loadExternalScripts(String script, String lang) {
        String[] lines = script.split("\n");
        String temp;
        String result = "";
        String pre = "//";
        if (lang.startsWith("p")) {
            pre = "##";
        }
        for (String line : lines) {
            if (line.startsWith(pre + "include ")
                    && !line.trim().endsWith(pre + "include")) {
                temp = line.split(" ", 2)[1];
                try {
                    line = loadExternalScripts(FileUtils.readFile(
                            filedata.getParent()
                            + "./" + temp), lang);
                } catch (IOException ex) {
                    outputBox.append("Error: Cannot read "
                            + "referenced script file: " + ex.getMessage()
                            + "\n");
                }
            }
            result += line + "\n";
        }
        return result;
    }

    private void exportMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMenuActionPerformed
        String lang = "js";
        if (pythonOption.isSelected()) {
            lang = "python";
        }
        MainGUI.loadFrame(new CodeExport(codeBox.getText(), lang));
    }//GEN-LAST:event_exportMenuActionPerformed

    private void javascriptOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javascriptOptionActionPerformed
        codeBox.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        pyac.uninstall();
        jsac.install(codeBox);
    }//GEN-LAST:event_javascriptOptionActionPerformed

    private void pythonOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pythonOptionActionPerformed
        codeBox.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
        jsac.uninstall();
        pyac.install(codeBox);
    }//GEN-LAST:event_pythonOptionActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        loadTheme();
    }//GEN-LAST:event_formMouseClicked

    private void outputBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outputBoxMouseClicked
        formMouseClicked(evt);
    }//GEN-LAST:event_outputBoxMouseClicked

    private void sampleHelloWorldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sampleHelloWorldActionPerformed
        openSample("helloworld");
    }//GEN-LAST:event_sampleHelloWorldActionPerformed

    private void sampleGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sampleGraphActionPerformed
        openSample("graph");
    }//GEN-LAST:event_sampleGraphActionPerformed

    private void pasteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteBtnActionPerformed
        codeBox.paste();
    }//GEN-LAST:event_pasteBtnActionPerformed

    private void copyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyBtnActionPerformed
        codeBox.copy();
    }//GEN-LAST:event_copyBtnActionPerformed

    private void cutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutBtnActionPerformed
        codeBox.cut();
    }//GEN-LAST:event_cutBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        outputBox.setText("");
    }//GEN-LAST:event_clearBtnActionPerformed

    private void undoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoBtnActionPerformed
        codeBox.undoLastAction();
    }//GEN-LAST:event_undoBtnActionPerformed

    private void redoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoBtnActionPerformed
        codeBox.redoLastAction();
    }//GEN-LAST:event_redoBtnActionPerformed

    private void shareMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shareMenuActionPerformed
        createShared("");
    }//GEN-LAST:event_shareMenuActionPerformed

    private void shareAsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shareAsMenuActionPerformed
        String id = JOptionPane.showInternalInputDialog(this, 
                "Enter the pad ID to share to.  "
                + "If the pad exists, it will be overwritten.",
                "Share",
                JOptionPane.QUESTION_MESSAGE);
        if (!(id == null || id.equals(""))) {
            createShared(id);
        }
    }//GEN-LAST:event_shareAsMenuActionPerformed

    private void createShared(String id) {
        try {
            String padid = Pads.genPad(id, codeBox.getText());
            Pads.addPad(padid);
            MainGUI.loadFrame(new WebBrowser("Pad " + padid,
                    Pads.PADS_URL + "/p/" + padid,
                    WebBrowser.PAD_LOGO));
            JOptionPane.showInternalMessageDialog(this,
                    new SharePad(padid),
                    "Share Pad",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            Debug.stacktrace(ex);
            JOptionPane.showInternalMessageDialog(this,
                    "Could not create new pad: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Open a sample code file with the given name.<p>
     * Uses the current language.
     *
     * @param name Name of file in codesamples package without extension
     */
    private void openSample(String name) {
        String ext = "js";
        if (!javascriptOption.isSelected()) {
            ext = "py";
        }
        String text = "";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            CodeEditor.class
                            .getResourceAsStream("codesamples/" + name + "." + ext)));
            String line;
            while ((line = reader.readLine()) != null) {
                text += line + "\n";
            }
        } catch (Exception e) {
            text = "Error: Could not open embedded sample file.";
            if (ext.equals("js")) {
                text = "/* " + text + " */";
            } else {
                text = "# " + text;
            }
        }
        openString(text, name + "." + ext, false);
    }

    @Override
    public void doDefaultCloseAction() {
        if (fileChanged == false) {
            dispose();
        } else {
            int p = JOptionPane.showInternalConfirmDialog(this,
                    "Do you want to save the file before closing?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (p == JOptionPane.NO_OPTION) {
                dispose();
            } else {
                saveMenuActionPerformed(null);
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearBtn;
    private javax.swing.JMenu codeLangMenu;
    private javax.swing.JMenuItem copyBtn;
    private javax.swing.JMenuItem cutBtn;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPanel editPanel;
    private javax.swing.JMenuItem exportMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JRadioButtonMenuItem javascriptOption;
    private javax.swing.ButtonGroup langBtnGroup;
    private javax.swing.JMenuItem openMenu;
    private javax.swing.JMenu openSampleBtn;
    private javax.swing.JTextArea outputBox;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JMenuItem pasteBtn;
    private javax.swing.JRadioButtonMenuItem pythonOption;
    private javax.swing.JMenuItem redoBtn;
    private javax.swing.JMenuItem runCodeBtn;
    private javax.swing.JMenu runMenu;
    private javax.swing.JMenuItem sampleGraph;
    private javax.swing.JMenuItem sampleHelloWorld;
    private javax.swing.JMenuItem saveAsMenu;
    private javax.swing.JMenuItem saveMenu;
    private javax.swing.JMenuItem shareAsMenu;
    private javax.swing.JMenuItem shareMenu;
    private javax.swing.JMenuItem undoBtn;
    // End of variables declaration//GEN-END:variables
}
