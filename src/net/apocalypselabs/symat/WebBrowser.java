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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Skylar
 */
public class WebBrowser extends javax.swing.JInternalFrame {

    private WebView browser;
    private WebEngine webEngine;
    private JFXPanel jfxPanel;
    private Group root;
    private Scene scene;

    /**
     *
     */
    public static final int DEFAULT_LOGO = 0;

    /**
     *
     */
    public static final int WIKI_LOGO = 1;

    /**
     *
     */
    public static final int FORUM_LOGO = 2;

    /**
     *
     */
    public static final int PAD_LOGO = 3;
    private JInternalFrame thisFrame;

    /**
     * Creates new form WebBrowser
     */
    public WebBrowser() {
        initComponents();
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
                                    urlBox.setText("Loading...");
                                } else if (newState == Worker.State.SCHEDULED
                                || newState == Worker.State.READY) {

                                } else {
                                    urlBox.setText(webEngine.getLocation());
                                }
                            }
                        });
                webEngine.setOnAlert(
                        new EventHandler<WebEvent<String>>() {
                            @Override
                            public void handle(WebEvent<String> t) {
                                JOptionPane.showMessageDialog(thisFrame, t.getData(), "Message from webpage", JOptionPane.INFORMATION_MESSAGE);
                            }
                        });

                webEngine.setPromptHandler(
                        new Callback<PromptData, String>() {
                            @Override
                            public String call(PromptData p) {
                                return JOptionPane.showInputDialog(thisFrame, p.getMessage(), "Question from webpage", JOptionPane.QUESTION_MESSAGE);
                            }
                        });
                webEngine.setConfirmHandler(
                        new Callback<String, Boolean>() {

                            @Override
                            public Boolean call(String p) {
                                return (JOptionPane.showConfirmDialog(
                                        thisFrame,
                                        p,
                                        "Question from webpage",
                                        JOptionPane.OK_CANCEL_OPTION)
                                == JOptionPane.OK_OPTION ? true : false);
                            }

                        });
                webEngine.setUserAgent("Mozilla/5.0 SyMAT/" + Main.VERSION_NAME);
                webEngine.loadContent(homepage());
            }
        });
        browserBox.add(jfxPanel, BorderLayout.CENTER);
    }

    /**
     *
     * @param title
     */
    public WebBrowser(String title) {
        this();
        setTitle(title);
        loadURL("http://wiki.symatapp.com/");
    }

    /**
     *
     * @param title
     * @param url
     */
    public WebBrowser(String title, String url) {
        this();
        setTitle(title);
        loadURL(url);
    }

    /**
     *
     * @param title
     * @param url
     * @param icon
     */
    public WebBrowser(String title, String url, int icon) {
        this(title, url);
        switch (icon) {
            case WIKI_LOGO:
                homeBtn.setVisible(false);
                sepBar.setVisible(false);
                setFrameIcon(new ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/wiki.png")));
                break;
            case FORUM_LOGO:
                homeBtn.setVisible(false);
                sepBar.setVisible(false);
                setFrameIcon(new ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/forum.png")));
                break;
            case PAD_LOGO:
                navBar.setVisible(false);
                goBtn.setEnabled(false);
                backBtn.setEnabled(false);
                homeBtn.setVisible(false);
                sepBar.setVisible(false);
                setFrameIcon(new ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/editor.png")));
                break;
            default:
                setFrameIcon(new ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/browser.png")));
        }

    }

    /**
     *
     * @param url
     * @param isurl
     */
    public WebBrowser(String url, boolean isurl) {
        this();
        loadURL(url);
    }

    /**
     *
     * @param yesno
     */
    public void showNavbar(boolean yesno) {
        navBar.setVisible(yesno);
        goBtn.setEnabled(yesno);
        backBtn.setEnabled(yesno);
    }

    /**
     *
     * @param url
     */
    public void loadURL(final String url) {
        if (url.startsWith("about:")) {
            final String action = url.replace("about:", "");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    switch (action) {
                        case "home":
                            webEngine.loadContent(homepage());
                            break;
                        case "blank":
                            webEngine.loadContent("");
                            break;
                        case "new":
                            Main.loadFrame(new WebBrowser());
                            break;
                        default:
                            webEngine.loadContent(errorpage("Invalid URL", "That isn't a valid address."));
                    }
                    resizeAll();
                }
            });
            urlBox.setText(url);
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    webEngine.load(url);
                    resizeAll();
                }
            });
            urlBox.setText(url);
        }
    }

    /**
     * Get the homepage/startpage HTML.
     *
     * @return
     */
    public String homepage() {
        try {
            String text = "";
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            WebBrowser.class
                            .getResourceAsStream("resources/homepage.html")));
            String line;
            while ((line = reader.readLine()) != null) {
                text += line;
            }
            return text;
        } catch (IOException ex) {
            return errorpage("Error: " + ex.getMessage(), ex.toString());
        }
    }

    /**
     * Returns a webpage suitable for showing error messages.
     *
     * @param error Short error message
     * @param details Error information
     * @return HTML page content
     */
    public String errorpage(String error, String details) {
        try {
            String text = "";
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            WebBrowser.class
                            .getResourceAsStream("resources/errorpage.html")));
            String line;
            while ((line = reader.readLine()) != null) {
                text += line;
            }
            text = text.replaceAll("<<<ERROR>>>", error);
            text = text.replaceAll("<<<DETAILS>>>", details);
            return text;
        } catch (IOException ex) {
            return "Oh, no!  Something bad happened:<br />"
                    + error
                    + "<br />Also, an error occured "
                    + "while displaying the error page: "
                    + ex.getMessage();
        }
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
        urlBox.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        navBar = new javax.swing.JToolBar();
        backBtn = new javax.swing.JButton();
        urlBox = new javax.swing.JTextField();
        buttonBar = new javax.swing.JToolBar();
        goBtn = new javax.swing.JButton();
        sepBar = new javax.swing.JToolBar.Separator();
        homeBtn = new javax.swing.JButton();
        browserBox = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Browser");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/browser.png"))); // NOI18N
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

        navBar.setFloatable(false);
        navBar.setRollover(true);
        navBar.setLayout(new java.awt.BorderLayout());

        backBtn.setFont(Main.ubuntuRegular.deriveFont(16.0f));
        backBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/arrow-left.png"))); // NOI18N
        backBtn.setToolTipText("Go back a page");
        backBtn.setFocusable(false);
        backBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        backBtn.setMaximumSize(new java.awt.Dimension(50, 50));
        backBtn.setMinimumSize(new java.awt.Dimension(30, 21));
        backBtn.setPreferredSize(new java.awt.Dimension(30, 21));
        backBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        navBar.add(backBtn, java.awt.BorderLayout.WEST);
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });
        /*
        navBar.add(backBtn);
        */

        urlBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                urlBoxKeyTyped(evt);
            }
        });
        navBar.add(urlBox, java.awt.BorderLayout.CENTER);
        /*
        navBar.add(urlBox);
        */

        buttonBar.setFloatable(false);
        buttonBar.setRollover(true);
        buttonBar.setBorderPainted(false);
        navBar.add(buttonBar, BorderLayout.EAST);

        goBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/goarrow.png"))); // NOI18N
        goBtn.setToolTipText("Navigate");
        goBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        goBtn.setMaximumSize(new java.awt.Dimension(50, 50));
        goBtn.setMinimumSize(new java.awt.Dimension(30, 21));
        goBtn.setPreferredSize(new java.awt.Dimension(30, 30));
        goBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonBar.add(goBtn);
        goBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goBtnActionPerformed(evt);
            }
        });
        /*
        buttonBar.add(goBtn);
        */
        buttonBar.add(sepBar);

        homeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/home.png"))); // NOI18N
        homeBtn.setToolTipText("Go to homepage");
        homeBtn.setFocusable(false);
        homeBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        homeBtn.setMaximumSize(new java.awt.Dimension(50, 50));
        homeBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });
        buttonBar.add(homeBtn);

        /*

        navBar.add(buttonBar);
        */

        getContentPane().add(navBar, java.awt.BorderLayout.PAGE_START);

        browserBox.setLayout(new java.awt.BorderLayout());
        getContentPane().add(browserBox, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
//        resizeAll();
//        // Ensure scrollbars show up correctly.
//        setSize(getWidth() + 1, getHeight());
//        setSize(getWidth() - 1, getHeight());
    }//GEN-LAST:event_formInternalFrameOpened

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        resizeAll();
    }//GEN-LAST:event_formComponentResized

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        resizeAll();
    }//GEN-LAST:event_formComponentShown

    private void urlBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_urlBoxKeyTyped
        if (evt.getKeyChar() == '\n') {
            goBtn.doClick();
        }
    }//GEN-LAST:event_urlBoxKeyTyped

    private void goBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBtnActionPerformed
        if (urlBox.getText().equals("about:home")) {
            loadString(homepage());
        } else {
            if (!urlBox.getText().startsWith("http") && !urlBox.getText().startsWith("about:")) {
                urlBox.setText("http://" + urlBox.getText());
            }
            loadURL(urlBox.getText());
        }
    }//GEN-LAST:event_goBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    browser.getEngine().getHistory().go(-1);
                } catch (Exception ex) {
                }
            }
        });
    }//GEN-LAST:event_backBtnActionPerformed

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        loadURL("about:home");
    }//GEN-LAST:event_homeBtnActionPerformed

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
    private javax.swing.JButton backBtn;
    private javax.swing.JPanel browserBox;
    public javax.swing.JToolBar buttonBar;
    private javax.swing.JButton goBtn;
    private javax.swing.JButton homeBtn;
    private javax.swing.JToolBar navBar;
    private javax.swing.JToolBar.Separator sepBar;
    private javax.swing.JTextField urlBox;
    // End of variables declaration//GEN-END:variables
}
