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

import fi.iki.elonen.NanoHTTPD;
import java.io.File;
import java.util.Map;

/**
 *
 * @author Skylar
 */
public class SingleInstanceServer extends NanoHTTPD {

    public SingleInstanceServer() {
        super(26879);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Debug.println("Another instance detected");
        String msg = "OK";
        Map<String, String> parms = session.getParms();
        if (parms.get("arg") != null) {
            (new LaunchThread(parms.get("arg"))).start();
        }
        return new NanoHTTPD.Response(msg);
    }
    
    private class LaunchThread extends Thread {
        String arg;
        @Override
        public void run() {
            if (arg.endsWith(".sytt")) {
                Tasks tt = new Tasks(new File(arg));
                Main.loadFrame(tt);
            } else if (arg.endsWith(".sypl")) {
                Main.loadFrame(new InstallPlugin(new File(arg)));
            } else {
                Editor ed = new Editor();
                Main.loadFrame(ed);
                ed.openFileFromName(arg);
            }
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Main.maingui.toFront();
                    Main.maingui.repaint();
                }
            });
        }
        
        public LaunchThread(String a) {
            arg = a;
        }
    }

}
