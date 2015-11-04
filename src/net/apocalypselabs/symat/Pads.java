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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import javax.swing.JOptionPane;
import org.etherpad_lite_client.EPLiteClient;
import org.etherpad_lite_client.EPLiteException;

/**
 *
 * @author Skylar
 */
public class Pads extends javax.swing.JInternalFrame {

    /**
     * URL of the pad server.
     */
    public static final String PADS_URL = "http://pad.symatapp.com";

    /**
     * Creates new form Pads
     */
    public Pads() {
        initComponents();
        updateTheme();
        padPane.setListData(getPads());
    }

    private void updateTheme() {
        padPane.setBackground(Theme.boxColor());
        padPane.setForeground(Theme.textColor());
        previewPane.setBackground(Theme.boxColor());
        previewPane.setForeground(Theme.textColor());
        setBackground(Theme.windowColor());
    }

    /**
     * Add the given pad ID to the local pad list.
     *
     * @param id the pad ID.
     */
    public static void addPad(String id) {
        String pads = PrefStorage.getSetting("pads");

        // Check for dupes
        for (String p : pads.split("\\|")) {
            if (p.equals(id)) {
                return;
            }
        }

        if (pads.equals("")) {
            pads = id;
        } else {
            pads += "|" + id;
        }
        PrefStorage.saveSetting("pads", pads);
    }

    /**
     * Delete the pad with the given ID from local memory.
     * <br>It will still exist online.
     *
     * @param id the pad ID.
     */
    public static void delPad(String id) {
        String pads = PrefStorage.getSetting("pads");
        String result = "";
        int i = 0;
        for (String pad : pads.split("\\|")) {
            if (!pad.equals(id)) {
                if (i > 0) {
                    result += "|";
                }
                result += pad;
                i++;
            }
        }

        PrefStorage.saveSetting("pads", result);
    }

    /**
     * Get an array of saved pads.
     *
     * @return String[] of pad IDs
     */
    public static String[] getPads() {
        String pads = PrefStorage.getSetting("pads");
        if (!pads.equals("")) {
            if (pads.contains("|")) {
                return pads.split("\\|");
            } else {
                String[] padlist = {pads};
                return padlist;
            }
        } else {
            String[] padlist = {};
            return padlist;
        }
    }

    /**
     * Create a new pad on the server with the given ID.
     *
     * @param id The pad ID to create. If blank is auto-generated.
     * @param content Text to initialize the pad with.
     * @return Created pad ID.
     * @throws Exception if things break.
     */
    public static String genPad(String id, String content) throws Exception {
        // Generate ID if blank
        if (id.equals("")) {
            id = genID();
        }

        // Create pad with given text.
        try {
            getClient().createPad(id, content);
        } catch (EPLiteException ex) {
            getClient().setText(id, content);
        }
        return id;
    }

    /**
     * Generate a random pad ID with length 15.
     * <br>There are about 1.217 x 10^26 possibilities (121 septillion).
     * <br>If this starts giving out used IDs, I'll be too rich to care.
     *
     * @return the ID.
     */
    public static String genID() {
        int length = 15;
        String[] chars = ("0123456789"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz").split("");
        String out = "";
        Random rng = new Random();

        for (int i = 0; i < length; i++) {
            out += chars[rng.nextInt(chars.length)];
        }

        return out;
    }

    /**
     * Get a client for the pad API.
     *
     * @return the client.
     * @throws IOException if things break.
     */
    public static EPLiteClient getClient() throws Exception {
        String apikey;
        // Enable key change without full SyMAT update
        if (!PrefStorage.getSetting("padkey-override", "").equals("")) {
            apikey = PrefStorage.getSetting("padkey-override", "");
        } else {
            // Load the API key from a file, so it's not included with Git.
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            Pads.class
                            .getResourceAsStream("/padkey")));
            apikey = reader.readLine();
        }
        // New client
        return new EPLiteClient(PADS_URL, apikey);
    }

    /**
     * Get the pad text.
     *
     * @param id the pad ID.
     * @return the text, or error message.
     */
    public static String getPad(String id) {
        String text = "";
        try {
            text = getClient().getText(id).getOrDefault("text", "").toString();
        } catch (Exception ex) {
            text = "Error: " + ex.getMessage();
        }
        return text;
    }

    /**
     * Set pad text.
     *
     * @param id Pad ID
     * @param content Pad text
     */
    public static void setPad(String id, String content) {
        try {
            getClient().setText(id, content);
        } catch (Exception ex) {
            Debug.stacktrace(ex);
            JOptionPane.showInternalMessageDialog(
                    Main.mainPane,
                    "Could not sync pad contents: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Delete the given pad ID.
     *
     * @param id the pad ID.
     * @param purge If true will delete from server also.
     */
    public static void delPad(String id, boolean purge) {
        delPad(id);
        if (purge) {
            try {
                getClient().deletePad(id);
            } catch (Exception ex) {
                // Meh.  No big deal.
            }
        }
    }

    /**
     * Display a pad.
     *
     * @param pad the pad ID
     */
    public static void loadPad(String pad) {
        String theme = "?theme=";
        if (Theme.currentTheme == Theme.THEME_DARK) {
            theme += "terminal";
        } else {
            theme += "default";
        }
        Main.loadFrame(new PadEditor(PADS_URL + "/p/" + pad + theme, pad));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        padPane = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        openBtn = new javax.swing.JButton();
        delBtn = new javax.swing.JButton();
        previewBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        previewPane = new javax.swing.JTextArea();
        shareBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        purgeBtn = new javax.swing.JButton();
        addBtn = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Collaboration");
        setMinimumSize(new java.awt.Dimension(450, 280));
        setPreferredSize(new java.awt.Dimension(450, 280));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });

        padPane.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        padPane.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                padPaneValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(padPane);

        jLabel1.setText("My Pads:");

        openBtn.setText("Open");
        openBtn.setToolTipText("Open pad for editing");
        openBtn.setEnabled(false);
        openBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openBtnActionPerformed(evt);
            }
        });

        delBtn.setText("Delete");
        delBtn.setToolTipText("Remove pad from list");
        delBtn.setEnabled(false);
        delBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delBtnActionPerformed(evt);
            }
        });

        previewBtn.setText("Preview");
        previewBtn.setToolTipText("Preview pad contents");
        previewBtn.setEnabled(false);
        previewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewBtnActionPerformed(evt);
            }
        });

        saveBtn.setText("Download");
        saveBtn.setToolTipText("Save pad locally");
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        previewPane.setEditable(false);
        previewPane.setColumns(1);
        previewPane.setRows(1);
        previewPane.setTabSize(4);
        jScrollPane2.setViewportView(previewPane);

        shareBtn.setText("Share");
        shareBtn.setToolTipText("Share pad");
        shareBtn.setEnabled(false);
        shareBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shareBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("Preview:");

        purgeBtn.setText("Purge");
        purgeBtn.setToolTipText("Delete pad completely");
        purgeBtn.setEnabled(false);
        purgeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purgeBtnActionPerformed(evt);
            }
        });

        addBtn.setText("+");
        addBtn.setToolTipText("Add pad by ID");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addBtn)))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(delBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(openBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(previewBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(shareBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purgeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 144, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(addBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(openBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purgeBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(previewBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(shareBtn)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void delBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delBtnActionPerformed
        int ans = JOptionPane.showInternalConfirmDialog(this,
                "Remove pad from list?  It will not be removed from the server.",
                "Delete?",
                JOptionPane.OK_CANCEL_OPTION);
        if (ans == JOptionPane.OK_OPTION) {
            delPad(getSelectedPad());
        }
        updateList();
    }//GEN-LAST:event_delBtnActionPerformed

    private void previewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewBtnActionPerformed
        previewPane.setText(getPad(getSelectedPad()));
        updateList();
    }//GEN-LAST:event_previewBtnActionPerformed

    private void openBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openBtnActionPerformed
        loadPad(getSelectedPad());
    }//GEN-LAST:event_openBtnActionPerformed

    private void purgeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purgeBtnActionPerformed
        int ans = JOptionPane.showInternalConfirmDialog(this,
                "Remove pad from server and list?  This cannot be undone!",
                "Delete?",
                JOptionPane.OK_CANCEL_OPTION);
        if (ans == JOptionPane.OK_OPTION) {
            delPad(getSelectedPad(), true);
        }
        updateList();
    }//GEN-LAST:event_purgeBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        Main.loadFrame(new Editor(getPad(getSelectedPad()), false));
        updateList();
    }//GEN-LAST:event_saveBtnActionPerformed

    private void padPaneValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_padPaneValueChanged
        // Enable/disable action buttons to prevent errors.
        boolean enable = true;
        if (padPane.getSelectedValue() == null) {
            enable = false;
        }
        openBtn.setEnabled(enable);
        saveBtn.setEnabled(enable);
        delBtn.setEnabled(enable);
        shareBtn.setEnabled(enable);
        previewBtn.setEnabled(enable);
        purgeBtn.setEnabled(enable);
    }//GEN-LAST:event_padPaneValueChanged

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        String id = JOptionPane.showInternalInputDialog(this,
                "What is the pad ID?",
                "Add Pad",
                JOptionPane.QUESTION_MESSAGE);
        if (id != null && !id.equals("")) {
            if (id.contains("pad.symatapp.com/p/")) {
                id = id.substring(id.lastIndexOf('/') + 1);
                Debug.println(id);
            }
            addPad(id);
        }
        updateList();
    }//GEN-LAST:event_addBtnActionPerformed

    private void shareBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shareBtnActionPerformed
        JOptionPane.showInternalMessageDialog(this,
                new SharePad(getSelectedPad()),
                "Share Pad",
                JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_shareBtnActionPerformed

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        updateTheme();
    }//GEN-LAST:event_formMouseEntered

    private String getSelectedPad() {
        return padPane.getSelectedValue().toString();
    }

    private void updateList() {
        int sel = padPane.getSelectedIndex();
        padPane.setListData(getPads());
        try {
            padPane.setSelectedIndex(sel);
        } catch (Exception ex) {
            
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton delBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton openBtn;
    private javax.swing.JList padPane;
    private javax.swing.JButton previewBtn;
    private javax.swing.JTextArea previewPane;
    private javax.swing.JButton purgeBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton shareBtn;
    // End of variables declaration//GEN-END:variables
}
