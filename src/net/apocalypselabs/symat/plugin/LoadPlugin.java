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
package net.apocalypselabs.symat.plugin;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import net.apocalypselabs.symat.CodeRunner;
import net.apocalypselabs.symat.Main;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandToggleButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

/**
 * Plugin loader class.
 *
 * @author Skylar
 */
public class LoadPlugin {

    private Plugin p;

    /**
     *
     * @param f
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvalidClassException
     */
    public LoadPlugin(File f) throws FileNotFoundException, IOException, ClassNotFoundException, InvalidClassException {
        FileInputStream fin = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fin);
        p = (Plugin) ois.readObject();
        ois.close();
    }

    /**
     *
     * @param path
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public LoadPlugin(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
        this(new File(path));
    }

    private ResizableIcon getRibbonIcon() {
        try {
            ResizableIcon ico = ImageWrapperResizableIcon.getIcon(
                    p.getIcon().getImage(),
                    new Dimension(100, 76));
            return ico;
        } catch (Exception ex) {
            return Main.getRibbonIcon("plugin");
        }
    }

    /**
     *
     * @return
     */
    public JCommandButton getRibbonBtn() {
        JCommandButton b = new JCommandButton((p.getTitle().equals("") ? "Untitled" : p.getTitle()), getRibbonIcon());
        if (!p.getLongTitle().equals("")) {
            b.setActionRichTooltip(new RichTooltip(p.getLongTitle(),
                    (p.getDesc().equals("") ? "   " : p.getDesc())));
        }
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                exec();
            }
        });
        b.setCommandButtonKind(JCommandButton.CommandButtonKind.ACTION_ONLY);
        return b;
    }

    /**
     *
     * @return
     */
    public JCommandToggleButton getGalleryBtn() {
        JCommandToggleButton b = new JCommandToggleButton((p.getTitle().equals("") ? "Untitled" : p.getTitle()), getRibbonIcon());
        if (!p.getLongTitle().equals("")) {
            b.setActionRichTooltip(new RichTooltip(p.getLongTitle(),
                    (p.getDesc().equals("") ? "   " : p.getDesc())));
        }
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                exec();
            }
        });
        return b;
    }

    /**
     *
     */
    public void exec() {
        CodeRunner cr = new CodeRunner(p.getLang());
        cr.evalString(p.getScript());
    }

    /**
     *
     * @return
     */
    public Plugin getPlugin() {
        return p;
    }

}
