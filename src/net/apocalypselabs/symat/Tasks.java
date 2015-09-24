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

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.apocalypselabs.symat.components.Task;
import net.apocalypselabs.symat.components.TaskList;

/**
 *
 * @author Skylar
 */
public class Tasks extends javax.swing.JInternalFrame {

    private String tltitle = "Untitled";

    private final JFileChooser fc = new JFileChooser();

    private File ondisk = null;

    /**
     * Creates new form Tasks
     */
    public Tasks() {
        FileFilter filter = new FileNameExtensionFilter("Task List (.sytt)", "sytt");
        fc.setFileFilter(filter);
        initComponents();
    }

    /**
     *
     * @param f
     */
    public Tasks(File f) {
        this();
        try {
            openTaskFile(f);
        } catch (Exception ex) {
            JOptionPane.showInternalMessageDialog(Main.mainPane,
                    "Cannot open task list: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            Debug.stacktrace(ex);
        }
    }

    private void openTaskFile(File f) throws IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fin);
        TaskList tl = (TaskList) ois.readObject();
        ois.close();

        Task[] list = tl.getTasks();
        for (Task t : list) {
            listPanel.add(t);
        }
        setTitle(tl.getTitle());
        tltitle = tl.getTitle();
        ondisk = f;
        redraw();
    }

    private void saveTasks(File f) throws FileNotFoundException, IOException {
        TaskList tl = new TaskList();
        for (Component c : listPanel.getComponents()) {
            tl.addTask((Task) c);
        }
        tl.setTitle(tltitle);
        FileOutputStream fout = new FileOutputStream(f);
        try (ObjectOutputStream oos = new ObjectOutputStream(fout)) {
            oos.writeObject(tl);
            oos.close();
            ondisk = f;
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

        taskList = new javax.swing.JScrollPane();
        listPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        openBtn = new javax.swing.JMenuItem();
        appendBtn = new javax.swing.JMenuItem();
        saveBtn = new javax.swing.JMenuItem();
        saveAsBtn = new javax.swing.JMenuItem();
        exportBtn = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        insertItemBtn = new javax.swing.JMenuItem();
        setTitleBtn = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Tasks");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/tasks.png"))); // NOI18N

        listPanel.setLayout(new javax.swing.BoxLayout(listPanel, javax.swing.BoxLayout.Y_AXIS));
        taskList.setViewportView(listPanel);

        jMenu1.setText("File");

        openBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openBtn.setText("Open...");
        openBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openBtnActionPerformed(evt);
            }
        });
        jMenu1.add(openBtn);

        appendBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        appendBtn.setText("Append...");
        appendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appendBtnActionPerformed(evt);
            }
        });
        jMenu1.add(appendBtn);

        saveBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveBtn.setText("Save...");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });
        jMenu1.add(saveBtn);

        saveAsBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsBtn.setText("Save As...");
        saveAsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsBtnActionPerformed(evt);
            }
        });
        jMenu1.add(saveAsBtn);

        exportBtn.setText("Export...");
        exportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportBtnActionPerformed(evt);
            }
        });
        jMenu1.add(exportBtn);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        insertItemBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        insertItemBtn.setText("Insert item");
        insertItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertItemBtnActionPerformed(evt);
            }
        });
        jMenu2.add(insertItemBtn);

        setTitleBtn.setText("List title...");
        setTitleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTitleBtnActionPerformed(evt);
            }
        });
        jMenu2.add(setTitleBtn);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(taskList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(taskList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void insertItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertItemBtnActionPerformed
        Task t = new Task(0, "Untitled Task",
                "No description");
        listPanel.add(t);
        t.setVisible(true);
        redraw();
    }//GEN-LAST:event_insertItemBtnActionPerformed

    private void openBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openBtnActionPerformed
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            listPanel.removeAll();
            try {
                openTaskFile(fc.getSelectedFile());
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(Main.mainPane,
                        "Cannot open task list: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                Debug.stacktrace(ex);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showInternalMessageDialog(Main.mainPane,
                        "Cannot open task list: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                Debug.stacktrace(ex);
            }
        }
    }//GEN-LAST:event_openBtnActionPerformed

    private void setTitleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTitleBtnActionPerformed
        String res = JOptionPane.showInternalInputDialog(this,
                "Task list title:",
                "Change Title",
                JOptionPane.QUESTION_MESSAGE);
        if (res == null) {
            return;
        }
        if (res.equals("")) {
            return;
        }
        setTitle(res);
        tltitle = res;
    }//GEN-LAST:event_setTitleBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        int result;
        if (ondisk == null) {
            result = fc.showSaveDialog(this);
        } else {
            fc.setSelectedFile(ondisk);
            result = JFileChooser.APPROVE_OPTION;
        }
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                saveTasks(FileUtils.getFileWithExtension(fc));
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(Main.mainPane,
                        "Cannot save task list: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                Debug.stacktrace(ex);
            }
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void saveAsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsBtnActionPerformed
        ondisk = null;
        saveBtn.doClick();
    }//GEN-LAST:event_saveAsBtnActionPerformed

    private void appendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appendBtnActionPerformed
        int result = fc.showDialog(this, "Merge List");
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                openTaskFile(fc.getSelectedFile());
            } catch (IOException ex) {
                JOptionPane.showInternalMessageDialog(Main.mainPane,
                        "Cannot open task list: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                Debug.stacktrace(ex);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showInternalMessageDialog(Main.mainPane,
                        "Cannot open task list: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                Debug.stacktrace(ex);
            }
        }
    }//GEN-LAST:event_appendBtnActionPerformed

    private void exportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportBtnActionPerformed
        TaskList tl = new TaskList();
        tl.setTitle(tltitle);
        for (Component c : listPanel.getComponents()) {
            tl.addTask((Task) c);
        }
        Main.loadFrame(new TasksExport(tl));
    }//GEN-LAST:event_exportBtnActionPerformed

    private void redraw() {
        setSize(getWidth() + 1, getHeight());
        setSize(getWidth() - 1, getHeight());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem appendBtn;
    private javax.swing.JMenuItem exportBtn;
    private javax.swing.JMenuItem insertItemBtn;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel listPanel;
    private javax.swing.JMenuItem openBtn;
    private javax.swing.JMenuItem saveAsBtn;
    private javax.swing.JMenuItem saveBtn;
    private javax.swing.JMenuItem setTitleBtn;
    private javax.swing.JScrollPane taskList;
    // End of variables declaration//GEN-END:variables
}
