/* 
 * Copyright (C) 2014 Apocalypse Laboratories.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package net.apocalypselabs.symat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Skylar
 */
public class Help extends javax.swing.JInternalFrame {

    /**
     * Creates new form Help
     */
    public Help() {
        initComponents();
    }

    public void loadTopic(String name) {
        try {
            String text = "";
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            CodeRunner.class
                            .getResourceAsStream("help/"+name+".html")));
            String line;
            while ((line = reader.readLine()) != null) {
                text += line;
            }
            topicBrowser.setText(text);
            topicBrowser.setCaretPosition(0);
        } catch (Exception e) {
            //JOptionPane.showInternalMessageDialog(MainGUI.mainPane, 
            //"Error: Cannot load help topic "+name+".\n\n"+e.getMessage());
            topicBrowser.setText("<html><head></head><body><p><b>Error:</b><br>Cannot get help topic \""
                    +name+"\".<br>("+e.getMessage()+")</p></body></html>");
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        topicList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        topicBrowser = new javax.swing.JEditorPane();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Help");
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
            String[] strings = { "Welcome", "Basics", "Editor", "Graphing", "Commands", "Licenses" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        topicList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        topicList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                topicListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(topicList);

        jSplitPane1.setLeftComponent(jScrollPane1);

        topicBrowser.setEditable(false);
        topicBrowser.setContentType("text/html"); // NOI18N
        topicBrowser.setText("<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      \rHi there\n    </p>\r\n  </body>\r\n</html>\r\n");
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
        loadTopic("welcome");
    }//GEN-LAST:event_formComponentShown


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JEditorPane topicBrowser;
    private javax.swing.JList topicList;
    // End of variables declaration//GEN-END:variables
}
