<<<<<<< HEAD
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

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.apocalypselabs.symat.components.Task;
import net.apocalypselabs.symat.components.TaskList;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;

/**
 *
 * @author Skylar
 */
public class TasksExport extends javax.swing.JInternalFrame {

    private String html;
    private TaskList tl;

    private WebView browser;
    private WebEngine webEngine;
    private JFXPanel jfxPanel;
    private Group root;
    private Scene scene;

    /**
     * Creates new form TasksExport
     *
     * @param t Task List to generate from.
     */
    public TasksExport(TaskList t) {
        initComponents();
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
                webEngine.setUserAgent("SyMAT " + Main.VERSION_NAME);
                webEngine.loadContent("<html><head><title></title></head><body><h3 style=\"font-family: sans-serif; text-align: center;\">Loading...</h3></body></html>");
                webPanel.add(jfxPanel, BorderLayout.CENTER);
            }
        });
        tl = t;
        titleBox.setText(t.getTitle());
        html = genHtml(t);
        loadString(html);
    }

    private void resizeAll() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                jfxPanel.setSize(webPanel.getWidth(), webPanel.getHeight());
                browser.setPrefSize(webPanel.getWidth(), webPanel.getHeight());
                browser.resize(webPanel.getWidth(), webPanel.getHeight());
            }
        });
    }

    public void loadString(final String content) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webEngine.loadContent(content);
                resizeAll();
            }
        });
    }

    private String genHtml(TaskList tt) {
        String css = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        TasksExport.class
                        .getResourceAsStream("tasks.css")));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                css += line;
            }
        } catch (IOException ex) {
            Logger.getLogger(TasksExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        html = "<!DOCTYPE html>"
                + "<html><head>"
                + "<meta charset=\"utf-8\">"
                + "<title>" + titleBox.getText() + "</title>"
                + "<style type=\"text/css\"><!--" + css + "--></style>"
                + "</head>"
                + "<body>"
                + "<h1>" + titleBox.getText() + "</h1>";
        if (!authBox.getText().equals("")) {
            html += "<h2>Author: " + authBox.getText() + "</h2>";
        }
        if (!dateBox.getText().equals("")) {
            html += "<h3>Date: " + dateBox.getText() + "</h3>";
        }
        if (!headerBox.getText().equals("")) {
            html += "<p class=\"header\">" + headerBox.getText() + "</p>";
        }

        for (Task t : tt.getTasks()) {
            html += "<div class=\"task\">"
                    + "<h4 class=\"taskheading\">" + t.toString() + "</h4>"
                    + percentBar(t.getComplete())
                    + "<p>" + t.getDesc() + "</p>"
                    + "</div>";
        }

        html += "</body></html>";
        html = html.replace("\t", "<span class=\"tab\">&nbsp;&nbsp;&nbsp;&nbsp;</span>");
        html = html.replace("\n", "<br>");
        HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        TagNode node = cleaner.clean(html);
        html = new SimpleHtmlSerializer(props).getAsString(node);
        html = html.replace("&apos;", "'");
        html = html.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        Debug.println(html);
        return html;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        titleBox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        authBox = new javax.swing.JTextField();
        dateBox = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        headerBox = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        previewBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        exHtml = new javax.swing.JButton();
        webPanel = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Export Task List");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/export.png"))); // NOI18N
        setMinimumSize(new java.awt.Dimension(599, 300));
        setPreferredSize(new java.awt.Dimension(599, 418));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        titleBox.setText("Untitled");

        jLabel1.setText("Title:");

        jLabel2.setText("Author:");

        authBox.setText(PrefStorage.getSetting("author", ""));

        jLabel3.setText("Date:");

        headerBox.setColumns(20);
        headerBox.setLineWrap(true);
        headerBox.setRows(2);
        headerBox.setTabSize(4);
        headerBox.setWrapStyleWord(true);
        jScrollPane2.setViewportView(headerBox);

        jLabel4.setText("Header:");

        previewBtn.setText("Update Preview");
        previewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewBtnActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Export to:"));

        exHtml.setText("HTML");
        exHtml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exHtmlActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(exHtml, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(exHtml)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(titleBox, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateBox)
                            .addComponent(authBox))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(previewBtn))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(titleBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(previewBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(authBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(dateBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        webPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(webPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(webPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void previewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewBtnActionPerformed
        html = genHtml(tl);
        loadString(html);
    }//GEN-LAST:event_previewBtnActionPerformed

    private void exHtmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exHtmlActionPerformed
        html = genHtml(tl);
        doSave("html");
    }//GEN-LAST:event_exHtmlActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        resizeAll();
    }//GEN-LAST:event_formComponentResized

    private void doSave(String format) {
        JFileChooser fc = new JFileChooser();
        FileFilter filter;
        switch (format) {
            case "pdf":
                filter = new FileNameExtensionFilter("Portable Document Format (PDF)", "pdf");
                break;
            default:
                filter = new FileNameExtensionFilter("Web Document (HTML)", "html");
        }
        fc.setFileFilter(filter);
        fc.addChoosableFileFilter(filter);
        int r = fc.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            String file = FileUtils.getFileWithExtension(fc).getAbsolutePath();
            switch (format) {
                case "html":
                    saveFile(html, file);
                    break;
                case "pdf":
                    savePdfFile(html, file);
                    break;
            }
        }
    }

    private String percentBar(int p) {
        int by5 = 5 * (Math.round(p / 5));
        String result = "<p>" + by5 + "% <span class=\"progress progress-" + by5 + "\""
                + "style=\"width: " + by5 + "%\"></span></p>";
        return result;
    }

    private void savePdfFile(String html, String path) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    String k = html;
                    OutputStream file = new FileOutputStream(new File(path));
                    Document document = new Document();
                    PdfWriter writer = PdfWriter.getInstance(document, file);
                    document.open();
                    PdfContentByte pdfCB = new PdfContentByte(writer);
                    WritableImage image = browser.snapshot(null, null);
                    BufferedImage buffered = SwingFXUtils.fromFXImage(image, null);
                    Image img = Image.getInstance(pdfCB, buffered, 1);
                    document.open();
                    document.add(img);
                    document.close();
                    savedMsg();
                } catch (Exception ex) {
                    Debug.stacktrace(ex);
                    java.awt.EventQueue.invokeLater(() -> {
                        JOptionPane.showInternalMessageDialog(Main.mainPane, "Error saving: " + ex.getMessage());
                    });
                }
            }
        });
    }

    private void saveFile(String content, String path) {
        try {
            PrintStream out = new PrintStream(new FileOutputStream(path));
            out.print(content);
            savedMsg();
        } catch (Exception ex) {
            JOptionPane.showInternalMessageDialog(this, "Error saving: " + ex.getMessage());
        }
    }

    private void savedMsg() {
        JOptionPane.showInternalMessageDialog(this, "Export complete!");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField authBox;
    private javax.swing.JTextField dateBox;
    private javax.swing.JButton exHtml;
    private javax.swing.JTextArea headerBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton previewBtn;
    private javax.swing.JTextField titleBox;
    private javax.swing.JPanel webPanel;
    // End of variables declaration//GEN-END:variables
}
=======
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

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.apocalypselabs.symat.components.Task;
import net.apocalypselabs.symat.components.TaskList;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;

/**
 *
 * @author Skylar
 */
public class TasksExport extends javax.swing.JInternalFrame {

    private String html;
    private TaskList tl;

    private WebView browser;
    private WebEngine webEngine;
    private JFXPanel jfxPanel;
    private Group root;
    private Scene scene;

    /**
     * Creates new form TasksExport
     *
     * @param t Task List to generate from.
     */
    public TasksExport(TaskList t) {
        initComponents();
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
                webEngine.setUserAgent("SyMAT " + Main.VERSION_NAME);
                webEngine.loadContent("<html><head><title></title></head><body><h3 style=\"font-family: sans-serif; text-align: center;\">Loading...</h3></body></html>");
                webPanel.add(jfxPanel, BorderLayout.CENTER);
            }
        });
        tl = t;
        titleBox.setText(t.getTitle());
        html = genHtml(t);
        loadString(html);
    }

    private void resizeAll() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                jfxPanel.setSize(webPanel.getWidth(), webPanel.getHeight());
                browser.setPrefSize(webPanel.getWidth(), webPanel.getHeight());
                browser.resize(webPanel.getWidth(), webPanel.getHeight());
            }
        });
    }

    public void loadString(final String content) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webEngine.loadContent(content);
                resizeAll();
            }
        });
    }

    private String genHtml(TaskList tt) {
        String css = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        TasksExport.class
                        .getResourceAsStream("tasks.css")));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                css += line;
            }
        } catch (IOException ex) {
            Logger.getLogger(TasksExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        html = "<!DOCTYPE html>"
                + "<html><head>"
                + "<meta charset=\"utf-8\">"
                + "<title>" + titleBox.getText() + "</title>"
                + "<style type=\"text/css\"><!--" + css + "--></style>"
                + "</head>"
                + "<body>"
                + "<h1>" + titleBox.getText() + "</h1>";
        if (!authBox.getText().equals("")) {
            html += "<h2>Author: " + authBox.getText() + "</h2>";
        }
        if (!dateBox.getText().equals("")) {
            html += "<h3>Date: " + dateBox.getText() + "</h3>";
        }
        if (!headerBox.getText().equals("")) {
            html += "<p class=\"header\">" + headerBox.getText() + "</p>";
        }

        for (Task t : tt.getTasks()) {
            html += "<div class=\"task\">"
                    + "<h4 class=\"taskheading\">" + t.toString() + "</h4>"
                    + percentBar(t.getComplete())
                    + "<p>" + t.getDesc() + "</p>"
                    + "</div>";
        }

        html += "</body></html>";
        html = html.replace("\t", "<span class=\"tab\">&nbsp;&nbsp;&nbsp;&nbsp;</span>");
        html = html.replace("\n", "<br>");
        HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        TagNode node = cleaner.clean(html);
        html = new SimpleHtmlSerializer(props).getAsString(node);
        html = html.replace("&apos;", "'");
        html = html.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        Debug.println(html);
        return html;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        titleBox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        authBox = new javax.swing.JTextField();
        dateBox = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        headerBox = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        previewBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        exHtml = new javax.swing.JButton();
        webPanel = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Export Task List");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/export.png"))); // NOI18N
        setMinimumSize(new java.awt.Dimension(599, 300));
        setPreferredSize(new java.awt.Dimension(599, 418));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        titleBox.setText("Untitled");

        jLabel1.setText("Title:");

        jLabel2.setText("Author:");

        authBox.setText(PrefStorage.getSetting("author", ""));

        jLabel3.setText("Date:");

        headerBox.setColumns(20);
        headerBox.setLineWrap(true);
        headerBox.setRows(2);
        headerBox.setTabSize(4);
        headerBox.setWrapStyleWord(true);
        jScrollPane2.setViewportView(headerBox);

        jLabel4.setText("Header:");

        previewBtn.setText("Update Preview");
        previewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewBtnActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Export to:"));

        exHtml.setText("HTML");
        exHtml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exHtmlActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(exHtml, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(exHtml)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(titleBox, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateBox)
                            .addComponent(authBox))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(previewBtn))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(titleBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(previewBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(authBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(dateBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        webPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(webPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(webPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void previewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewBtnActionPerformed
        html = genHtml(tl);
        loadString(html);
    }//GEN-LAST:event_previewBtnActionPerformed

    private void exHtmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exHtmlActionPerformed
        html = genHtml(tl);
        doSave("html");
    }//GEN-LAST:event_exHtmlActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        resizeAll();
    }//GEN-LAST:event_formComponentResized

    private void doSave(String format) {
        JFileChooser fc = new JFileChooser();
        FileFilter filter;
        switch (format) {
            case "pdf":
                filter = new FileNameExtensionFilter("Portable Document Format (PDF)", "pdf");
                break;
            default:
                filter = new FileNameExtensionFilter("Web Document (HTML)", "html");
        }
        fc.setFileFilter(filter);
        fc.addChoosableFileFilter(filter);
        int r = fc.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            String file = FileUtils.getFileWithExtension(fc).getAbsolutePath();
            switch (format) {
                case "html":
                    saveFile(html, file);
                    break;
                case "pdf":
                    savePdfFile(html, file);
                    break;
            }
        }
    }

    private String percentBar(int p) {
        int by5 = 5 * (Math.round(p / 5));
        String result = "<p>" + by5 + "% <span class=\"progress progress-" + by5 + "\""
                + "style=\"width: " + by5 + "%\"></span></p>";
        return result;
    }

    private void savePdfFile(String html, String path) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    String k = html;
                    OutputStream file = new FileOutputStream(new File(path));
                    Document document = new Document();
                    PdfWriter writer = PdfWriter.getInstance(document, file);
                    document.open();
                    PdfContentByte pdfCB = new PdfContentByte(writer);
                    WritableImage image = browser.snapshot(null, null);
                    BufferedImage buffered = SwingFXUtils.fromFXImage(image, null);
                    Image img = Image.getInstance(pdfCB, buffered, 1);
                    document.open();
                    document.add(img);
                    document.close();
                    savedMsg();
                } catch (Exception ex) {
                    Debug.stacktrace(ex);
                    java.awt.EventQueue.invokeLater(() -> {
                        JOptionPane.showInternalMessageDialog(Main.mainPane, "Error saving: " + ex.getMessage());
                    });
                }
            }
        });
    }

    private void saveFile(String content, String path) {
        try {
            PrintStream out = new PrintStream(new FileOutputStream(path));
            out.print(content);
            savedMsg();
        } catch (Exception ex) {
            JOptionPane.showInternalMessageDialog(this, "Error saving: " + ex.getMessage());
        }
    }

    private void savedMsg() {
        JOptionPane.showInternalMessageDialog(this, "Export complete!");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField authBox;
    private javax.swing.JTextField dateBox;
    private javax.swing.JButton exHtml;
    private javax.swing.JTextArea headerBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton previewBtn;
    private javax.swing.JTextField titleBox;
    private javax.swing.JPanel webPanel;
    // End of variables declaration//GEN-END:variables
}
>>>>>>> c1bd78b886a57d8f285747c749491ece75862a43
