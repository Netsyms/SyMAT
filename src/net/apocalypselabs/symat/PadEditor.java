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

import java.awt.BorderLayout;
import java.awt.Component;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.PromptData;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Skylar
 */
public class PadEditor extends javax.swing.JInternalFrame implements RunScriptListener {

    private WebView browser;
    private WebEngine webEngine;
    private JFXPanel jfxPanel;
    private Group root;
    private Scene scene;

    private String padID = "";
    private String padURL = "";

    private RunScriptThread rt;

    private JInternalFrame thisFrame;

    /**
     * Creates new form PadEditor
     *
     * @param padurl Pad URL to load
     * @param padid Pad ID to load
     */
    public PadEditor(String padurl, String padid) {
        padID = padid;
        padURL = padurl;
        initComponents();
        //setFrameIcon(new ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/editor.png")));
        thisFrame = this;
        jfxPanel = new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                browser = new WebView();
                browser.setPrefSize(getWidth(), getHeight());
                root = new Group();
                scene = new Scene(root);
                ObservableList<Node> children = root.getChildren();
                children.add(browser);
                jfxPanel.setScene(scene);
                webEngine = browser.getEngine();
                webEngine.getLoadWorker().stateProperty().addListener(
                        new ChangeListener<State>() {
                            @Override
                            public void changed(ObservableValue ov, State oldState, State newState) {
                                if (newState == Worker.State.RUNNING) {
                                    statusLbl.setText("Loading...");
                                } else if (newState == Worker.State.SCHEDULED
                                || newState == Worker.State.READY) {

                                } else {
                                    statusLbl.setText("");
                                }
                            }
                        });
                webEngine.setOnAlert(
                        new EventHandler<WebEvent<String>>() {
                            @Override
                            public void handle(WebEvent<String> t) {
                                JOptionPane.showMessageDialog(thisFrame, t.getData(), "Message", JOptionPane.INFORMATION_MESSAGE);
                            }
                        });

                webEngine.setPromptHandler(
                        new Callback<PromptData, String>() {
                            @Override
                            public String call(PromptData p) {
                                return JOptionPane.showInputDialog(thisFrame, p.getMessage(), "Question", JOptionPane.QUESTION_MESSAGE);
                            }
                        });
                webEngine.setConfirmHandler(
                        new Callback<String, Boolean>() {

                            @Override
                            public Boolean call(String p) {
                                return (JOptionPane.showConfirmDialog(
                                        thisFrame,
                                        p,
                                        "Question",
                                        JOptionPane.OK_CANCEL_OPTION)
                                == JOptionPane.OK_OPTION ? true : false);
                            }

                        });
                webEngine.setUserAgent("Mozilla/5.0 SyMAT/" + Main.VERSION_NAME);
                webEngine.loadContent("Loading...");
                webEngine.load(padurl);
            }
        });
        browserBox.add(jfxPanel, BorderLayout.CENTER);
        outputBox.setBackground(net.apocalypselabs.symat.Theme.boxColor());
        outputBox.setForeground(net.apocalypselabs.symat.Theme.textColor());
    }

    /**
     *
     * @param url
     */
    public void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webEngine.load(url);
                resizeAll();
            }
        });
    }

    /**
     *
     */
    public void open() {
        Main.loadFrame(this, true);
    }

    /**
     *
     * @param content
     */
    public void loadString(final String content) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webEngine.loadContent(content);
                resizeAll();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        browserBox = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputBox = new javax.swing.JTextArea();
        statusBar = new javax.swing.JToolBar();
        statusLbl = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        runMenu = new javax.swing.JMenu();
        runCodeBtn = new javax.swing.JMenuItem();
        killButton = new javax.swing.JMenuItem();
        codeLangMenu = new javax.swing.JMenu();
        javascriptOption = new javax.swing.JRadioButtonMenuItem();
        pythonOption = new javax.swing.JRadioButtonMenuItem();
        javaOption = new javax.swing.JRadioButtonMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Pad Editor");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/editor.png"))); // NOI18N
        setMinimumSize(new java.awt.Dimension(300, 300));
        setPreferredSize(new java.awt.Dimension(480, 400));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jSplitPane1.setDividerLocation(260);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.7);

        browserBox.setMinimumSize(new java.awt.Dimension(100, 25));
        browserBox.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                browserBoxComponentResized(evt);
            }
        });
        browserBox.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setLeftComponent(browserBox);

        outputBox.setColumns(20);
        outputBox.setRows(1);
        outputBox.setTabSize(4);
        jScrollPane1.setViewportView(outputBox);

        jSplitPane1.setRightComponent(jScrollPane1);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        statusBar.setFloatable(false);

        statusLbl.setText(" ");
        statusBar.add(statusLbl);

        getContentPane().add(statusBar, java.awt.BorderLayout.PAGE_END);

        runMenu.setText("Run");

        runCodeBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        runCodeBtn.setText("Run code");
        runCodeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runCodeBtnActionPerformed(evt);
            }
        });
        runMenu.add(runCodeBtn);

        killButton.setText("Kill script");
        killButton.setEnabled(false);
        killButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                killButtonActionPerformed(evt);
            }
        });
        runMenu.add(killButton);

        codeLangMenu.setText("Language");

        javascriptOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        javascriptOption.setSelected(true);
        javascriptOption.setText("Javascript");
        javascriptOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javascriptOptionActionPerformed(evt);
            }
        });
        codeLangMenu.add(javascriptOption);

        pythonOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        pythonOption.setText("Python");
        pythonOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pythonOptionActionPerformed(evt);
            }
        });
        codeLangMenu.add(pythonOption);

        javaOption.setText("Java");
        javaOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javaOptionActionPerformed(evt);
            }
        });
        codeLangMenu.add(javaOption);

        runMenu.add(codeLangMenu);

        jMenuBar1.add(runMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

    }//GEN-LAST:event_formInternalFrameOpened

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        resizeAll();
    }//GEN-LAST:event_formComponentResized

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        resizeAll();
    }//GEN-LAST:event_formComponentShown

    private void runCodeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runCodeBtnActionPerformed
        if (javascriptOption.isSelected()) {
            rt = new RunScriptThread("javascript", Pads.getPad(padID), outputBox, null);
        } else if (pythonOption.isSelected()) {
            rt = new RunScriptThread("python", Pads.getPad(padID), outputBox, null);
        } else if (javaOption.isSelected()) {
            rt = new RunScriptThread("java", Pads.getPad(padID), outputBox, null);
        } else {
            return;
        }
        statusLbl.setText("Running...");
        setRunning(true);
        rt.registerListener(this);
        rt.setDaemon(true);
        rt.start();
    }//GEN-LAST:event_runCodeBtnActionPerformed

    @Override
    public void scriptFinished() {
        statusLbl.setText("Done");
        setRunning(false);
    }

    private void killButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_killButtonActionPerformed
        if (rt.isAlive()) {
            rt.killme();
            outputBox.append(""
                    + "\n============="
                    + "\nScript killed"
                    + "\n=============\n");
        }
        statusLbl.setText("Killed");
        setRunning(false);
    }//GEN-LAST:event_killButtonActionPerformed

    private void setRunning(boolean isRunning) {
        final boolean running = isRunning;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    for (Component mu : jMenuBar1.getComponents()) {
                        mu.setEnabled(false);
                    }
                    runMenu.setEnabled(true);
                    runCodeBtn.setEnabled(false);
                    codeLangMenu.setEnabled(false);
                    killButton.setEnabled(true);
                } else {
                    for (Component mu : jMenuBar1.getComponents()) {
                        mu.setEnabled(true);
                    }
                    runCodeBtn.setEnabled(true);
                    codeLangMenu.setEnabled(true);
                    killButton.setEnabled(false);
                }
            }
        });
    }

    private void javascriptOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javascriptOptionActionPerformed

    }//GEN-LAST:event_javascriptOptionActionPerformed

    private void pythonOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pythonOptionActionPerformed

    }//GEN-LAST:event_pythonOptionActionPerformed

    private void javaOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javaOptionActionPerformed

    }//GEN-LAST:event_javaOptionActionPerformed

    private void browserBoxComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_browserBoxComponentResized
        resizeAll();
    }//GEN-LAST:event_browserBoxComponentResized

    private void resizeAll() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                jfxPanel.setSize(browserBox.getWidth(), browserBox.getHeight());
                browser.setPrefSize(browserBox.getWidth(), browserBox.getHeight());
                browser.resize(browserBox.getWidth(), browserBox.getHeight());
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel browserBox;
    private javax.swing.JMenu codeLangMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JRadioButtonMenuItem javaOption;
    private javax.swing.JRadioButtonMenuItem javascriptOption;
    private javax.swing.JMenuItem killButton;
    private javax.swing.JTextArea outputBox;
    private javax.swing.JRadioButtonMenuItem pythonOption;
    private javax.swing.JMenuItem runCodeBtn;
    private javax.swing.JMenu runMenu;
    private javax.swing.JToolBar statusBar;
    private javax.swing.JLabel statusLbl;
    // End of variables declaration//GEN-END:variables
}
