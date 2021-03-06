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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import prettify.PrettifyParser;
import syntaxhighlight.ParseResult;

/**
 *
 * @author Skylar
 */
public class CodeExport extends javax.swing.JInternalFrame {

    private String codeLang = "js";
    private String html;
    private final String origCode;
    private String outputTxt = "";

    /**
     * Creates new form CodeExport
     *
     * @param code The code.
     */
    public CodeExport(String code) {
        origCode = code;
        initComponents();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                html = genHtml(code);
            }
        });
        html = genHtml(code);
        previewPane.setText(html);
        previewPane.setCaretPosition(0);
    }

    /**
     * Create CodeExport window with a set language for syntax highlighting.
     *
     * @param code The code.
     * @param lang Options are "js" or "python".
     */
    public CodeExport(String code, String lang) {
        this(code);
        codeLang = lang;
    }

    /**
     *
     * @param code
     * @param lang
     * @param out
     */
    public CodeExport(String code, String lang, String out) {
        this(code, lang);
        outputTxt = out;
    }

    private String genHtml(String code) {
        String css = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        CodeExport.class
                        .getResourceAsStream("pretty.css")));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                css += line;
            }
        } catch (IOException ex) {
            Logger.getLogger(CodeExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        html = "<!DOCTYPE html>"
                + "<html><head>"
                + "<meta charset=\"utf-8\">"
                + "<title></title>"
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
        if (outputPlaceBox.getSelectedIndex() == 1) {
            html += getOutput();
        }
        PrettifyParser parser = new PrettifyParser();
        List<ParseResult> parseResults = parser.parse(codeLang, code);
        html += PrettifyToHtml.toHtml(code, parseResults);
        if (outputPlaceBox.getSelectedIndex() == 0) {
            html += getOutput();
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

    private String getOutput() {
        return "<p><b>Output:</b></p><pre class=\"plaintxt\">"
                + outputTxt.replace("<", "&lt;").
                replace("&", "&amp;").
                replace(">", "&gt;").
                replace("\"", "''")
                + "\n\n</pre>";
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
        previewPane = new javax.swing.JTextPane();
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
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        exHtml = new javax.swing.JButton();
        exPdf = new javax.swing.JButton();
        outputPlaceBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Export Code");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/export.png"))); // NOI18N

        previewPane.setEditable(false);
        previewPane.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(previewPane);

        titleBox.setText("SyMAT Project");

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

        jButton1.setText("Update Preview");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Export to:"));

        exHtml.setText("HTML");
        exHtml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exHtmlActionPerformed(evt);
            }
        });

        exPdf.setText("PDF");
        exPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exPdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exPdf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exHtml, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(exHtml)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(exPdf)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outputPlaceBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Below code", "Above code", "No" }));
        outputPlaceBox.setSelectedIndex(2);

        jLabel5.setText("Include output:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(outputPlaceBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(titleBox, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(authBox)
                            .addComponent(dateBox))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(titleBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(authBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(dateBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(outputPlaceBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        html = genHtml(origCode);
        previewPane.setText(html);
        previewPane.setCaretPosition(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void exHtmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exHtmlActionPerformed
        doSave("html");
    }//GEN-LAST:event_exHtmlActionPerformed

    private void exPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exPdfActionPerformed
        doSave("pdf");
    }//GEN-LAST:event_exPdfActionPerformed

    private void doSave(String format) {
        html = genHtml(origCode);
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

    private void savePdfFile(String html, String path) {
        try {
            String k = html;
            try (OutputStream file = new FileOutputStream(new File(path))) {
                Document document = new Document();
                PdfWriter.getInstance(document, file);
                document.open();
                HTMLWorker htmlWorker = new HTMLWorker(document);
                StyleSheet styles = new StyleSheet();
                styles.loadStyle("com", "color", "green");
                styles.loadStyle("kwd", "color", "blue");
                styles.loadStyle("pln", "color", "black");
                styles.loadStyle("lit", "color", "#0099cc");
                styles.loadStyle("pun", "color", "black");
                styles.loadStyle("pun", "font-weight", "bold");
                htmlWorker.setStyleSheet(styles);
                htmlWorker.parse(new StringReader(k));
                document.close();
                savedMsg();
            }
        } catch (IOException | DocumentException e) {
            JOptionPane.showInternalMessageDialog(this, "Error saving: " + e.getMessage());
        }
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
    private javax.swing.JButton exPdf;
    private javax.swing.JTextArea headerBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox outputPlaceBox;
    private javax.swing.JTextPane previewPane;
    private javax.swing.JTextField titleBox;
    // End of variables declaration//GEN-END:variables
}
