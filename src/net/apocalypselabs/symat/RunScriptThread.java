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

import java.io.File;
import java.io.IOException;
import javax.swing.JTextArea;

/**
 *
 * @author Skylar
 */
public class RunScriptThread extends Thread {

    String lang = "";
    String code = "";
    ScriptOutputThread sot;
    CodeRunner cr;
    File filedata;
    JTextArea outputBox;
    RunScriptListener rsl = null;

    /**
     *
     * @param codelang Code language.
     * @param script Script to run.
     * @param output A JTextArea to output to (output window).
     * @param fd A File pointing to the script file.  Set to null if unavailable.
     */
    public RunScriptThread(String codelang, String script, JTextArea output, File fd) {
        lang = codelang;
        code = script;
        filedata = fd;
        outputBox = output;
        cr = new CodeRunner(lang);
        sot = new ScriptOutputThread(output, cr);
    }
    
    public void registerListener(RunScriptListener r) {
        rsl = r;
    }
    
    public void killme() {
        sot.kill();
        stop();
    }

    @Override
    public void run() {
        String script = loadExternalScripts(code, lang);
        Debug.println(lang);
        Debug.println(script);
        sot.start();
        cr.evalCode(script);
        sot.kill();
        rsl.scriptFinished();
    }
    
    /**
     * Load external script files, relative to the current file.
     *
     * @param script The file to parse for includes.
     * @param lang The script language.
     * @return The script modified as needed.
     */
    private String loadExternalScripts(String script, String lang) {
        if (filedata == null) {
            return script;
        }
        String[] lines = script.split("\n");
        String temp;
        String result = "";
        String pre = "//";
        if (lang.startsWith("p")) {
            pre = "##";
        }
        for (String line : lines) {
            if (line.startsWith(pre + "include ")
                    && !line.trim().endsWith(pre + "include")) {
                temp = line.split(" ", 2)[1];
                try {
                    line = loadExternalScripts(FileUtils.readFile(
                            filedata.getParent()
                            + "./" + temp), lang);
                } catch (IOException ex) {
                    outputBox.append("Error: Cannot read "
                            + "referenced script file: " + ex.getMessage()
                            + "\n");
                }
            }
            result += line + "\n";
        }
        return result;
    }
}
