/*
 * CODE LICENSE =====================
 * Copyright (c) 2015, Netsyms Technologies
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
 * software are copyright (c) 2015 Netsyms Technologies.  You may not distribute
 * the graphics or any program, source code repository, or other digital storage
 * media containing them without written permission from Netsyms Technologies.
 * This ban on distribution only applies to publicly available systems.
 * A password-protected network file share, USB drive, or other storage scheme that
 * cannot be easily accessed by the public is generally allowed.  If in doubt,
 * contact Netsyms Technologies.  If Netsyms Technologies allows or denies
 * you permission, that decision is considered final and binding.
 */
package net.apocalypselabs.symat;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;

/**
 *
 * @author Skylar
 */
public class Interpreter extends javax.swing.JInternalFrame {

    private final CodeRunner cr;
    private String[] history = new String[10]; // Command history buffer
    private String commandsForExport = ""; // History for saving
    private int historyIndex = 0; // For going back in time and keeping things straight
    private String lang = "javascript";
    private Object ans = 0;

    private CompletionProvider jscomp = new CodeCompleter("js").getProvider();
    private CompletionProvider pycomp = new CodeCompleter("py").getProvider();
    private AutoCompletion jsac = new AutoCompletion(jscomp);
    private AutoCompletion pyac = new AutoCompletion(pycomp);
    
    private Interpreter thisobject;

    /**
     * Creates new form Interpreter
     *
     * @param useLang The script language to use. "javascript", "python", or
     * "default".
     */
    public Interpreter(String useLang) {
        thisobject = this;
        initComponents();

        // Setup code runner
        lang = useLang;
        if (lang.equals("default")) {
            lang = PrefStorage.getSetting("shellLang", "javascript");
        }
        cr = new CodeRunner(lang);

        // Setup language
        if (lang.equals("python")) {
            javascriptMenu.setSelected(false);
            pythonMenu.setSelected(true);
            pyac.install(inputBox);
            setTitle("Shell [python]");
        } else {
            jsac.install(inputBox);
            setTitle("Shell [javascript]");
        }

        // Set font
        int font_size = 12;
        try {
            font_size = Integer.valueOf(PrefStorage.getSetting("editfont", "12"));
        } catch (Exception ex) {
        }
        mainBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, font_size));
        inputBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, font_size));

        // Set theme
        loadTheme();

        // Misc. setup
        mainBox.setText(">>");
        inputBox.requestFocus();
    }

    public Interpreter() {
        this("default");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        langGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainBox = new javax.swing.JTextArea();
        inputBox = new javax.swing.JTextField();
        runBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        exportHistoryBtn = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        clearBtn = new javax.swing.JMenuItem();
        langMenu = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        javascriptMenu = new javax.swing.JRadioButtonMenuItem();
        pythonMenu = new javax.swing.JRadioButtonMenuItem();
        setDefaultLang = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Shell");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/shell.png"))); // NOI18N
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        mainBox.setEditable(false);
        mainBox.setColumns(20);
        mainBox.setFont(new java.awt.Font("Courier New", 0, 15)); // NOI18N
        mainBox.setLineWrap(true);
        mainBox.setRows(2);
        mainBox.setTabSize(4);
        mainBox.setWrapStyleWord(true);
        DefaultCaret caret = (DefaultCaret)mainBox.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        mainBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainBoxMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(mainBox);

        inputBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inputBoxMouseClicked(evt);
            }
        });
        inputBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputBoxKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                inputBoxKeyTyped(evt);
            }
        });

        runBtn.setText("Run");
        runBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                runBtnMouseClicked(evt);
            }
        });
        runBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runBtnActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(">>");

        jMenu3.setText("File");

        exportHistoryBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        exportHistoryBtn.setText("Save history...");
        exportHistoryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportHistoryBtnActionPerformed(evt);
            }
        });
        jMenu3.add(exportHistoryBtn);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Edit");

        clearBtn.setText("Clear window");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });
        jMenu4.add(clearBtn);

        jMenuBar1.add(jMenu4);

        langMenu.setText("Language");

        jMenu1.setText("Switch");

        langGroup.add(javascriptMenu);
        javascriptMenu.setSelected(true);
        javascriptMenu.setText("JavaScript");
        javascriptMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javascriptMenuActionPerformed(evt);
            }
        });
        jMenu1.add(javascriptMenu);

        langGroup.add(pythonMenu);
        pythonMenu.setText("Python");
        pythonMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pythonMenuActionPerformed(evt);
            }
        });
        jMenu1.add(pythonMenu);

        langMenu.add(jMenu1);

        setDefaultLang.setText("Set as default");
        setDefaultLang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultLangActionPerformed(evt);
            }
        });
        langMenu.add(setDefaultLang);

        jMenuBar1.add(langMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runBtn))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputBox, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(runBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runBtnActionPerformed
        doRunCode();
    }//GEN-LAST:event_runBtnActionPerformed

    private void loadTheme() {
        mainBox.setBackground(Theme.boxColor());
        mainBox.setForeground(Theme.textColor());
        inputBox.setBackground(Theme.boxColor());
        inputBox.setForeground(Theme.textColor());
        setBackground(Theme.windowColor());
    }

    private void inputBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputBoxKeyTyped
        if (evt.getKeyChar() == '\n') {
            doRunCode();
        }
    }//GEN-LAST:event_inputBoxKeyTyped

    private void inputBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputBoxKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (historyIndex < 9) {
                    if (historyIndex < 0) {
                        historyIndex++;
                    }
                    inputBox.setText(history[historyIndex]);
                    historyIndex++;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (historyIndex >= 0) {
                    historyIndex--;
                    if (historyIndex < 0) {
                        historyIndex = 0;
                        inputBox.setText("");
                    } else {
                        inputBox.setText(history[historyIndex]);
                    }
                }
                break;
        }
    }//GEN-LAST:event_inputBoxKeyPressed

    private void setDefaultLangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDefaultLangActionPerformed
        String pref = "javascript";
        if (pythonMenu.isSelected()) {
            pref = "python";
        }
        PrefStorage.saveSetting("shellLang", pref);
        PrefStorage.save();
    }//GEN-LAST:event_setDefaultLangActionPerformed

    private void javascriptMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javascriptMenuActionPerformed
        if (!lang.equals("javascript")) {
            Main.loadFrame(new Interpreter("javascript"));
            dispose();
        }
    }//GEN-LAST:event_javascriptMenuActionPerformed

    private void pythonMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pythonMenuActionPerformed
        if (!lang.equals("python")) {
            Main.loadFrame(new Interpreter("python"));
            dispose();
        }
    }//GEN-LAST:event_pythonMenuActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        loadTheme();
    }//GEN-LAST:event_formMouseClicked

    private void mainBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainBoxMouseClicked
        formMouseClicked(evt);
    }//GEN-LAST:event_mainBoxMouseClicked

    private void inputBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputBoxMouseClicked
        formMouseClicked(evt);
    }//GEN-LAST:event_inputBoxMouseClicked

    private void runBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_runBtnMouseClicked
        formMouseClicked(evt);
    }//GEN-LAST:event_runBtnMouseClicked

    private void exportHistoryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportHistoryBtnActionPerformed
        JFileChooser fc = new JFileChooser();
        FileFilter filter;
        if (javascriptMenu.isSelected()) {
            filter = new FileNameExtensionFilter("SyMAT JavaScript (.syjs)", "syjs");
        } else {
            filter = new FileNameExtensionFilter("SyMAT Python (.sypy)", "sypy");
        }
        fc.setFileFilter(filter);
        fc.addChoosableFileFilter(filter);
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                FileUtils.saveFile(commandsForExport,
                        FileUtils.getFileWithExtension(fc).toString(),
                        true);
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(this,
                        "Error saving: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_exportHistoryBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        mainBox.setText(">>");
        commandsForExport = "";
    }//GEN-LAST:event_clearBtnActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        inputBox.requestFocusInWindow();
    }//GEN-LAST:event_formFocusGained

    private void doRunCode() {
        String code = inputBox.getText();
        commandsForExport += code + "\n";
        mainBox.append(" " + code + "\n");
        runBtn.setEnabled(false);
        inputBox.setEnabled(false);
        new EvalThread(code).start();
    }

    private class EvalThread extends Thread {

        private String code = "";

        public EvalThread(String cmd) {
            code = cmd;
        }

        /**
         * Process control commands.
         *
         * @return True if the modified code should be executed, false if not.
         */
        private boolean doSpecialCommands() {
            switch (code) {
                case "clc":
                case "clear":
                    clrOutput();
                    return false;
                case "exit":
                    thisobject.dispose();
            }

            // Implement ans command
            String ansfill;
            try {
                ansfill = String.valueOf(Double.parseDouble(ans.toString()));
            } catch (NumberFormatException ex) {
                ansfill = "\"" + ans.toString() + "\"";
            }
            code = code.replace("ans", ansfill);
            return true;
        }

        @Override
        @SuppressWarnings("null")
        public void run() {
            try {
                if (doSpecialCommands()) {
                    Object result = cr.evalString(code);
                    if (result != null && !result.toString().trim().equals("")) {
                        ans = result;
                    }
                    append(result.toString() + "\n");
                }
            } catch (NullPointerException ex) {

            }
            append(">>");
            for (int i = 9; i > 0; i--) {
                history[i] = history[i - 1];
            }
            history[0] = code;
            clrInput();
            historyIndex = -1;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    inputBox.setEnabled(true);
                    runBtn.setEnabled(true);
                    inputBox.requestFocusInWindow();
                }
            });
        }

        private void clrInput() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    inputBox.setText("");
                }
            });
        }

        private void clrOutput() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mainBox.setText("");
                    commandsForExport = "";
                }
            });
        }

        private void append(String out) {
            final String output = out;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mainBox.append(output);
                    mainBox.setCaretPosition(mainBox.getText().length());
                }
            });
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem clearBtn;
    private javax.swing.JMenuItem exportHistoryBtn;
    private javax.swing.JTextField inputBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButtonMenuItem javascriptMenu;
    private javax.swing.ButtonGroup langGroup;
    private javax.swing.JMenu langMenu;
    private javax.swing.JTextArea mainBox;
    private javax.swing.JRadioButtonMenuItem pythonMenu;
    private javax.swing.JButton runBtn;
    private javax.swing.JMenuItem setDefaultLang;
    // End of variables declaration//GEN-END:variables
}
