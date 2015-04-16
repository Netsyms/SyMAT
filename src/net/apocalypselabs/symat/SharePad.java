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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Skylar
 */
public class SharePad extends javax.swing.JPanel {

    String padid = "";

    /**
     * Creates new form SharePad
     *
     * @param id The pad ID to share.
     */
    public SharePad(String id) {
        initComponents();
        padid = id;
        padIDBox.setText(padid);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        padIDBox = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        copyIDBtn = new javax.swing.JButton();
        copyLinkBtn = new javax.swing.JButton();
        emailBtn = new javax.swing.JButton();
        emailBox = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(188, 172));

        jLabel1.setText("Pad ID:");

        padIDBox.setEditable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Share"));

        copyIDBtn.setText("Copy ID");
        copyIDBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyIDBtnActionPerformed(evt);
            }
        });

        copyLinkBtn.setText("Copy Link");
        copyLinkBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyLinkBtnActionPerformed(evt);
            }
        });

        emailBtn.setText("Send Email");
        emailBtn.setEnabled(false);
        emailBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailBtnActionPerformed(evt);
            }
        });

        emailBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                emailBoxKeyTyped(evt);
            }
        });

        jLabel2.setText("Email Link:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(copyIDBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(copyLinkBtn))
            .addComponent(emailBox)
            .addComponent(emailBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(copyIDBtn)
                    .addComponent(copyLinkBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailBtn)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(padIDBox)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(padIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void copyIDBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyIDBtnActionPerformed
        toClipboard(padid);
    }//GEN-LAST:event_copyIDBtnActionPerformed

    private void copyLinkBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyLinkBtnActionPerformed
        toClipboard(Pads.PADS_URL + "/p/" + padid);
    }//GEN-LAST:event_copyLinkBtnActionPerformed

    private void emailBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailBtnActionPerformed
        if (License.checkEmail(emailBox.getText())) {
            emailBtn.setEnabled(false);
            emailBtn.setText("Sending...");
            new EmailThread(emailBox.getText(), padid).start();
        }
    }//GEN-LAST:event_emailBtnActionPerformed

    private class EmailThread extends Thread {

        private final String email;
        private final String pad;

        @Override
        public void run() {
            try {
                String f = FileUtils.getUrl(
                        Main.API_URL + "sendpad.php?email="
                        + email + "&padid=" + pad);
                if (f.contains("OK")) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            emailBtn.setText("Link Sent!");
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            emailBtn.setText("Error.");
                        }
                    });
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        emailBtn.setText("Error, retry?");
                    }
                });
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {

            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    emailBtn.setEnabled(true);
                    emailBtn.setText("Send Email");
                }
            });
        }

        public EmailThread(String e, String p) {
            email = e;
            pad = p;
        }
    }

    private void emailBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailBoxKeyTyped
        emailBtn.setEnabled(License.checkEmail(emailBox.getText()));
    }//GEN-LAST:event_emailBoxKeyTyped

    private void toClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton copyIDBtn;
    private javax.swing.JButton copyLinkBtn;
    private javax.swing.JTextField emailBox;
    private javax.swing.JButton emailBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField padIDBox;
    // End of variables declaration//GEN-END:variables
}
