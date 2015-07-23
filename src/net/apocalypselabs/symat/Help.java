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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 *
 * @author Skylar
 */
public class Help extends javax.swing.JInternalFrame {

    /*
     Some styling stuff.
     */
    private final HTMLEditorKit kit;
    private final StyleSheet styleSheet;
    private final StyleSheet dark = new StyleSheet();
    private final StyleSheet light = new StyleSheet();
    private int styleloaded = -1;

    /**
     * Creates new form Help
     */
    public Help() {
        kit = new HTMLEditorKit();
        initComponents();
        styleSheet = kit.getStyleSheet();
        loadStyleSheets();
        loadTheme();
    }

    /**
     * Load the styles for the main box.
     */
    private void loadStyleSheets() {
        dark.addRule("body { background-color: #293134; }");
        dark.addRule("h1 { color: #ffffff; }");
        dark.addRule("h2 { color: #ffffff; }");
        dark.addRule("p { color: #ffffff; }");
        light.addRule("body { background-color: #ffffff; }");
        light.addRule("h1 { color: #000000; }");
        light.addRule("h2 { color: #000000; }");
        light.addRule("p { color: #000000; }");
    }

    /**
     * (Re)load the theme for this window.
     */
    private void loadTheme() {
        if ((PrefStorage.getSetting("theme").equals("dark") && styleloaded == 0)
                || (!PrefStorage.getSetting("theme").equals("dark") && styleloaded == 1)) {
            styleloaded = -1;
        }
        if (styleloaded == -1) {
            if (PrefStorage.getSetting("theme").equals("dark")) {
                topicList.setBackground(new Color(41, 49, 52));
                topicList.setForeground(Color.WHITE);
                styleSheet.addStyleSheet(dark);
                styleSheet.removeStyleSheet(light);
                setBackground(Color.DARK_GRAY);
                styleloaded = 1;
            } else {
                topicList.setBackground(Color.WHITE);
                topicList.setForeground(Color.BLACK);
                styleSheet.addStyleSheet(light);
                styleSheet.removeStyleSheet(dark);
                setBackground(Color.LIGHT_GRAY);
                styleloaded = 0;
            }
            loadTopic(topicList.getSelectedValue().toString().toLowerCase());
        }
    }

    /**
     * Load a help topic from the help package.
     *
     * @param topicName The name of the help file, without the ".html".
     */
    public void loadTopic(String topicName) {
        new LoadThread(topicName).start();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        topicList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        topicBrowser = new javax.swing.JEditorPane();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Manual");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/help.png"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setResizeWeight(0.1);

        topicList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Welcome", "Basics", "Editor", "Pads", "Graphing", "Tasks", "Commands", "Licenses" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        topicList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        topicList.setSelectedIndex(0);
        topicList.setVisibleRowCount(9);
        topicList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                topicListMouseClicked(evt);
            }
        });
        topicList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                topicListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(topicList);

        jSplitPane1.setLeftComponent(jScrollPane1);

        topicBrowser.setEditable(false);
        topicBrowser.setBorder(null);
        topicBrowser.setContentType("text/html"); // NOI18N
        topicBrowser.setText("<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      \rHi there\n    </p>\r\n  </body>\r\n</html>\r\n");
        topicBrowser.setEditorKit(kit);
        topicBrowser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                topicBrowserMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(topicBrowser);

        jSplitPane1.setRightComponent(jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void topicListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_topicListValueChanged
        loadTopic(topicList.getSelectedValue().toString().toLowerCase());
    }//GEN-LAST:event_topicListValueChanged

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        topicList.setSelectedIndex(0);
        loadTopic("welcome");
    }//GEN-LAST:event_formComponentShown

    private void topicListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_topicListMouseClicked
        loadTheme();
    }//GEN-LAST:event_topicListMouseClicked

    private void topicBrowserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_topicBrowserMouseClicked
        loadTheme();
    }//GEN-LAST:event_topicBrowserMouseClicked

    private class LoadThread extends Thread {

        String name;

        public LoadThread(String topic) {
            name = topic;
        }

        @Override
        public void run() {
            setText("<p><i>Loading...</i></p>", "Manual");
            if (name.equals("welcome")) {
                final String text = "<html><head><title>About SyMAT</title></head>"
                        + "<body>"
                        + "<h1>Welcome to SyMAT!</h1>"
                        + "<p>SyMAT is a Java-based algebra and calculus system.  "
                        + "Scripts and commands can be written in "
                        + "JavaScript or Python.</p>"
                        + "<p>This is SyMAT version "
                        + Main.VERSION_NAME + " (" + (int) Main.APP_CODE + ")."
                        + "</p>"
                        + "<p>SyMAT is copyright &copy; "
                        + Calendar.getInstance().get(Calendar.YEAR)
                        + " Netsyms Technologies."
                        + "<br />"
                        + "This built-in documentation falls under the SyMAT Media License.</p>";
                setText(text, "Manual");
            } else {
                try {
                    String text = "";
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    CodeRunner.class
                                    .getResourceAsStream("help/" + name + ".html")));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        text += line;
                    }
                    setText(text, "Manual (" + topicList.getSelectedValue().toString() + ")");
                } catch (Exception e) {
                    //JOptionPane.showInternalMessageDialog(Main.mainPane,
                    //"Error: Cannot load help topic "+name+".\n\n"+e.getMessage());
                    setText("<html><head></head><body><p><b>Error:</b><br>Cannot get help topic \""
                            + name + "\".<br>(" + e.getMessage() + ")</p></body></html>", "Manual");
                }
            }
        }

        private void setText(String string, String wintitle) {
            final String text = string;
            final String title = wintitle;
            SwingUtilities.invokeLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            topicBrowser.setText(text);
                            topicBrowser.setCaretPosition(0);
                            setTitle(title);
                        }
                    });
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JEditorPane topicBrowser;
    private javax.swing.JList topicList;
    // End of variables declaration//GEN-END:variables
}
