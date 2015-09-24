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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.apocalypselabs.symat.plugin.LoadPlugin;
import org.pushingpixels.flamingo.api.ribbon.*;
import org.pushingpixels.flamingo.api.ribbon.resize.*;
import org.pushingpixels.flamingo.api.common.*;
import org.pushingpixels.flamingo.api.common.icon.*;

/**
 * This class is like the Force: A light theme, a dark theme, and it binds the
 * app together. Also like duct tape, but that's not as cool.
 *
 * Contains a bunch of important variables and constants that are used all over.
 *
 * There is only one Main per app instance, the important things are static.
 *
 * @author Skylar Ittner
 */
public class Main extends JRibbonFrame {

    /**
     * Version name, as it should be displayed.
     */
    public static final String VERSION_NAME = "2.1";

    /**
     * The word "SyMAT".
     */
    public static final String SYMAT = "SyMAT";
    /**
     * Program name, with version name
     */
    public static final String APP_NAME = SYMAT + " " + VERSION_NAME;
    /**
     * Version number, for updates and //needs in scripts
     */
    public static final double APP_CODE = 24;
    /**
     * Base URL for building API calls
     */
    public static final String API_URL = "http://apis.symatapp.com/";
    /**
     * Contains the filename argument passed to SyMAT, if any.
     */
    public static String argfile = "";

    /**
     * Ubuntu font. Loaded from Ubuntu-R.ttf in the default package at runtime.
     * Becomes default sans-serif if something bad happens.
     */
    public static Font ubuntuRegular;

    /**
     *
     */
    public static boolean skipPython = false; // Skip python init on start?

    /**
     *
     */
    public static boolean skipEditor = false; // Skip editor init on start?

    private static boolean recentItemsMinimized = false;

    /**
     *
     */
    public static boolean updateAvailable = false; // Update available?

    /**
     *
     */
    public static String updateString = "";

    /**
     *
     */
    public static boolean licValid = false; // License valid?

    /**
     * Application icon, for setting frame icons. Has different sizes.
     */
    public static ArrayList<Image> symatlogo = new ArrayList<>();

    /**
     * The http server that handles opening other instances.
     */
    public static SingleInstanceServer sisrv;

    /**
     *
     */
    public static Main maingui;

    /**
     *
     */
    public JRibbonBand pluginband;

    /**
     * Creates the main app window and does some quick things that aren't
     * threaded in SplashScreen.
     */
    public Main() {
        maingui = this;
        // Set icon
        setIconImages(symatlogo);
        initComponents();
        loadRibbon();

        // Center screen
        setLocationRelativeTo(null);

        // Run things when app closed
        addWindowListener(new ExitControl());

        // Open initial windows
        boolean loaded = false;
        if (!argfile.equals("")) {
            if (argfile.endsWith(".sytt")) {
                Tasks tt = new Tasks(new File(argfile));
                loadFrame(tt);
                argfile = "";
            } else if (argfile.endsWith(".sypl")) {
                loadFrame(new InstallPlugin(new File(argfile)));
            } else {
                Editor ed = new Editor();
                loadFrame(ed);
                ed.openFileFromName(argfile);
                argfile = "";
            }
            loaded = true;
        }

        if (!licValid) {
            licenseRestrict(true);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    loadFrame(new License());
                }
            });
            loaded = true;
        }
        // Only load shell if nothing else is going on

        if (argfile.equals("") && !loaded) {
            loadFrame(new Interpreter());
        }
        if (updateAvailable) {
            loadFrame(new Update(updateString));
        }
        loadRecentFiles();
        updateDisplay();
        setVisible(true);
        if (PrefStorage.getSetting("framemaxed", "no").equals("yes")) {
            java.awt.EventQueue.invokeLater(() -> {
                setExtendedState(MAXIMIZED_BOTH);
            });
        }

        if (!PrefStorage.getSetting("showrecent", "").equals("")) {
            recentItemsPanel.setVisible(false);
        }

        // Pi Day easter egg
        GregorianCalendar piday = new GregorianCalendar();
        if ((piday.get(Calendar.MONTH) == 2)
                && (piday.get(Calendar.DAY_OF_MONTH) == 14)) {
            JOptionPane.showInternalMessageDialog(mainPane,
                    "Happy Pi Day from the SyMAT team!",
                    "3/14",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * Load plugins from disk.
     */
    public void loadPlugins() {
        pluginband = new JRibbonBand("Plugins", null);
        installpluginbtn.setActionRichTooltip(new RichTooltip("Install Plugin",
                "Install a plugin from a file and view plugin info."));
        installpluginbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadFrame(new InstallPlugin());
            }
        });
        pluginband.setResizePolicies((List) Arrays.asList(
                new CoreRibbonResizePolicies.Mirror(pluginband.getControlPanel())));
        pluginband.addCommandButton(installpluginbtn, RibbonElementPriority.TOP);
        String fsep = System.getProperty("file.separator");
        File dir = new File(System.getProperty("user.home") + fsep + ".symat" + fsep + "plugins");
        dir.mkdirs();
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".sypl");
            }
        });

        Map<RibbonElementPriority, Integer> galleryVisibleButtonCounts = new HashMap<>();
        galleryVisibleButtonCounts.put(RibbonElementPriority.LOW, 4);
        galleryVisibleButtonCounts.put(RibbonElementPriority.MEDIUM, 4);
        galleryVisibleButtonCounts.put(RibbonElementPriority.TOP, 4);
        List<StringValuePair<List<JCommandToggleButton>>> appGalleryButtons = new ArrayList<>();
        List<JCommandToggleButton> appGalleryButtonsList = new ArrayList<>();
        if (files != null) {
            for (File pl : files) {
                LoadPlugin lp;
                try {
                    lp = new LoadPlugin(pl);
                    appGalleryButtonsList.add(lp.getGalleryBtn());
                } catch (Exception ex) {
                    Debug.stacktrace(ex);
                    System.err.println("Error loading plugin " + pl.getName() + ": " + ex.getMessage());
                }
            }
        }

        appGalleryButtons.add(new StringValuePair<>("Plugins",
                appGalleryButtonsList));
        pluginband.addRibbonGallery("Plugins", appGalleryButtons,
                galleryVisibleButtonCounts, 4, 3,
                RibbonElementPriority.TOP);
    }

    /**
     * Reload the Ribbon tabs and all sub-components.
     */
    public void reloadRibbon() {
        getRibbon().removeAllTasks();
        loadRibbon();
    }

    /**
     * Load the ribbon in all its glory.
     */
    private void loadRibbon() {
        ResizableIcon appico = ImageWrapperResizableIcon.getIcon(
                Main.class.getResource("icon32.png"),
                new Dimension(32, 32));
        setApplicationIcon(appico);
        int tries = 0; // no infinite loops for us!
        while (tries < 20) {
            try {
                Thread.sleep(10);
                if (getApplicationIcon().equals(appico)) {
                    break;
                }
                setApplicationIcon(appico);
            } catch (Exception ex) {
            }
            tries++;
        }
        JRibbon ribbon = getRibbon();
        JRibbonBand coreband = new JRibbonBand("Core", null);
        JRibbonBand appsband = new JRibbonBand("Apps", null);
        JRibbonBand webband = new JRibbonBand("Community", null);
        JRibbonBand collabband = new JRibbonBand("Team", null);
        //JRibbonBand getpluginband = new JRibbonBand("Install", null);

        try {
            loadPlugins();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "An error occurred while loading plugins: "
                    + ex.getMessage());
        }

        shellbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadFrame(new Interpreter());
            }
        });
        editorbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadFrame(new Editor());
            }
        });
        graphbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadFrame(new Graph());
            }
        });
        notepadbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadFrame(new Notepad());
            }
        });
        wikibtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadFrame(new WebBrowser("SyMAT Wiki",
                        "http://wiki.symatapp.com",
                        WebBrowser.WIKI_LOGO));
            }
        });
        forumbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadFrame(new WebBrowser("Community Forum",
                        "http://forum.symatapp.com/",
                        WebBrowser.FORUM_LOGO));
            }
        });
        padsbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadFrame(new Pads());
            }
        });
        tasksbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadFrame(new Tasks());
            }
        });

        shellbtn.setActionRichTooltip(new RichTooltip("Command Shell",
                "Open a window for running interactive commands."));
        editorbtn.setActionRichTooltip(new RichTooltip("Code Editor",
                "Write and run JavaScript and Python scripts."));
        graphbtn.setActionRichTooltip(new RichTooltip("Graph",
                "Plot mathematical functions on a 2D graph."));
        notepadbtn.setActionRichTooltip(new RichTooltip("Notepad",
                "Write quick notes on a virtual napkin."));
        wikibtn.setActionRichTooltip(new RichTooltip("SyMAT Wiki",
                "View and edit online documentation and tutorials."));
        forumbtn.setActionRichTooltip(new RichTooltip("Support Forum",
                "Discuss and share with the SyMAT community."));
        padsbtn.setActionRichTooltip(new RichTooltip("Code Pads",
                "Collaborate over the Internet on projects."));
        tasksbtn.setActionRichTooltip(new RichTooltip("Task List",
                "Manage tasks and to-do lists for projects."));

        coreband.addCommandButton(shellbtn, RibbonElementPriority.TOP);
        coreband.addCommandButton(editorbtn, RibbonElementPriority.TOP);

        appsband.addCommandButton(graphbtn, RibbonElementPriority.MEDIUM);
        appsband.addCommandButton(notepadbtn, RibbonElementPriority.MEDIUM);

        webband.addCommandButton(wikibtn, RibbonElementPriority.LOW);
        webband.addCommandButton(forumbtn, RibbonElementPriority.LOW);

        collabband.addCommandButton(padsbtn, RibbonElementPriority.MEDIUM);
        collabband.addCommandButton(tasksbtn, RibbonElementPriority.MEDIUM);

        coreband.setResizePolicies((List) Arrays.asList(
                new CoreRibbonResizePolicies.None(coreband.getControlPanel()),
                new IconRibbonBandResizePolicy(coreband.getControlPanel())));
        appsband.setResizePolicies((List) Arrays.asList(
                new CoreRibbonResizePolicies.None(appsband.getControlPanel()),
                new IconRibbonBandResizePolicy(appsband.getControlPanel())));
        webband.setResizePolicies((List) Arrays.asList(
                new CoreRibbonResizePolicies.None(webband.getControlPanel()),
                new IconRibbonBandResizePolicy(webband.getControlPanel())));
        collabband.setResizePolicies((List) Arrays.asList(
                new CoreRibbonResizePolicies.None(collabband.getControlPanel()),
                new IconRibbonBandResizePolicy(collabband.getControlPanel())));
//        getpluginband.setResizePolicies((List) Arrays.asList(
//                new CoreRibbonResizePolicies.None(appsband.getControlPanel()),
//                new IconRibbonBandResizePolicy(pluginband.getControlPanel())));
//        pluginband.setResizePolicies((List) Arrays.asList(
//                new CoreRibbonResizePolicies.None(appsband.getControlPanel()),
//                new IconRibbonBandResizePolicy(pluginband.getControlPanel())));

        RibbonTask hometask = new RibbonTask("Home", coreband, appsband);
        RibbonTask webtask = new RibbonTask("Tools", webband, collabband);
        RibbonTask plugintask = new RibbonTask("Plugins", pluginband);

        loadRibbonMenu(null);

        ribbon.addTask(hometask);
        ribbon.addTask(webtask);
        ribbon.addTask(plugintask);
    }

    /**
     *
     * @param name
     * @return
     */
    public static ResizableIcon getRibbonIcon(String name) {
        return ImageWrapperResizableIcon.getIcon(
                Main.class.getResource("images/" + name + ".png"),
                new Dimension(100, 76));
    }

    /**
     *
     * @param name
     * @return
     */
    public static ResizableIcon getTinyRibbonIcon(String name) {
        int d = 32;
        return ImageWrapperResizableIcon.getIcon(
                Main.class.getResource("icons/" + name + ".png"),
                new Dimension(d, d));
    }

    /**
     *
     * @param restricted
     */
    public static void licenseRestrict(boolean restricted) {
        graphbtn.setEnabled(!restricted);
        padsbtn.setEnabled(!restricted);
        recentFileList.setEnabled(!restricted);
    }

    /**
     * (Re)load display settings.
     */
    public static void updateDisplay() {
        maingui.getRibbon().setBackground(Theme.tabColor());
        recentFileList.setForeground(Theme.textColor());
        recentFileList.setBackground(Theme.boxColor());
        recentItemsPanel.setVisible(PrefStorage.getSetting("showrecent", "")
                .equals(""));
        maingui.getRibbon().setMinimized(PrefStorage.getSetting(
                "miniribbon", "").equals("yes"));
        mainPane.paintImmediately(0, 0,
                mainPane.getWidth(), mainPane.getHeight());
    }

    /**
     *
     * @param recent
     */
    public static void loadRibbonMenu(RibbonApplicationMenuEntrySecondary[] recent) {
        RibbonApplicationMenuEntryPrimary helpbtn
                = new RibbonApplicationMenuEntryPrimary(
                        getRibbonIcon("help"),
                        "Manual",
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                loadFrame(new Help());
                            }
                        },
                        JCommandButton.CommandButtonKind.ACTION_ONLY);
        RibbonApplicationMenuEntryPrimary cascadebtn
                = new RibbonApplicationMenuEntryPrimary(
                        getRibbonIcon("cascade"),
                        "Arrange all",
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                cascade();
                            }
                        },
                        JCommandButton.CommandButtonKind.ACTION_ONLY);
        RibbonApplicationMenuEntryPrimary exitbtn
                = new RibbonApplicationMenuEntryPrimary(
                        getRibbonIcon("closeall"),
                        "Exit",
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                maingui.dispatchEvent(new WindowEvent(maingui,
                                                WindowEvent.WINDOW_CLOSING));
                            }
                        },
                        JCommandButton.CommandButtonKind.ACTION_ONLY);
        RibbonApplicationMenuEntrySecondary newjsbtn
                = new RibbonApplicationMenuEntrySecondary(
                        getTinyRibbonIcon("jsicon"),
                        "JavaScript",
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                loadFrame(new Editor());
                            }
                        },
                        JCommandButton.CommandButtonKind.ACTION_ONLY);
        RibbonApplicationMenuEntrySecondary newpybtn
                = new RibbonApplicationMenuEntrySecondary(
                        getTinyRibbonIcon("pyicon"),
                        "Python",
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                loadFrame(new Editor(true));
                            }
                        },
                        JCommandButton.CommandButtonKind.ACTION_ONLY);
        RibbonApplicationMenuEntrySecondary newtaskbtn
                = new RibbonApplicationMenuEntrySecondary(
                        getTinyRibbonIcon("taskicon"),
                        "Task List",
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                loadFrame(new Tasks());
                            }
                        },
                        JCommandButton.CommandButtonKind.ACTION_ONLY);
        RibbonApplicationMenuEntryPrimary newbtn
                = new RibbonApplicationMenuEntryPrimary(
                        getRibbonIcon("newfile"),
                        "New",
                        null,
                        JCommandButton.CommandButtonKind.POPUP_ONLY);
        openbtn = new RibbonApplicationMenuEntryPrimary(
                getRibbonIcon("openfile"),
                "Open",
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        JFileChooser fc = new JFileChooser();
                        FileFilter script
                        = new FileNameExtensionFilter("Script"
                                + "(syjs, sypy, js, py)",
                                "syjs", "sypy", "js", "py");
                        FileFilter all
                        = new FileNameExtensionFilter("SyMAT File"
                                + "(syjs, sypy, js, py, sytt)",
                                "syjs", "sypy", "js", "py", "sytt");
                        FileFilter tasklist
                        = new FileNameExtensionFilter("Task List (sytt)",
                                "sytt");
                        fc.setFileFilter(all);
                        fc.addChoosableFileFilter(script);
                        fc.addChoosableFileFilter(tasklist);
                        int result = fc.showOpenDialog(maingui);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File f = fc.getSelectedFile();
                            if (f.getName().endsWith(".sytt")) {
                                loadFrame(new Tasks(f));
                            } else {
                                Editor ed = new Editor();
                                ed.openFileFromName(f.getAbsolutePath());
                                loadFrame(ed);
                            }
                        }
                    }
                },
                JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);

        if (recent == null) {
            RibbonApplicationMenuEntrySecondary blanksubbtn
                    = new RibbonApplicationMenuEntrySecondary(
                            null, "No recent files", null,
                            JCommandButton.CommandButtonKind.ACTION_ONLY);
            blanksubbtn.setEnabled(false);
            openbtn.addSecondaryMenuGroup("Recent Files", blanksubbtn);
        } else {
            openbtn.addSecondaryMenuGroup("Recent Files", recent);
        }
        newbtn.addSecondaryMenuGroup("Code File", newjsbtn, newpybtn);
        newbtn.addSecondaryMenuGroup("Other", newtaskbtn);

        RibbonApplicationMenuEntryFooter displaybtn
                = new RibbonApplicationMenuEntryFooter(
                        getTinyRibbonIcon("settings"),
                        "Settings",
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                loadFrame(new Settings());
                            }
                        });

        RibbonApplicationMenuEntryPrimary blankbtn
                = new RibbonApplicationMenuEntryPrimary(
                        null, "", null,
                        JCommandButton.CommandButtonKind.ACTION_ONLY);
        blankbtn.setEnabled(false);

        RibbonApplicationMenu menu = new RibbonApplicationMenu();
        menu.addMenuEntry(newbtn);
        menu.addMenuEntry(openbtn);
        menu.addMenuSeparator();
        menu.addMenuEntry(helpbtn);
        menu.addMenuEntry(cascadebtn);
        menu.addMenuSeparator();
        menu.addMenuEntry(exitbtn);
        menu.addFooterEntry(displaybtn);

        menu.addMenuEntry(blankbtn);
        menu.addMenuEntry(blankbtn);
        menu.addMenuEntry(blankbtn);
        menu.addMenuEntry(blankbtn);

        maingui.getRibbon().setApplicationMenu(menu);
        maingui.getRibbon().setApplicationMenuRichTooltip(
                new RichTooltip("SyMAT Menu",
                        "Create files, open documents, "
                        + "get help, and change settings")
        );
    }

    /**
     * (Re)load recent file lists from storage.
     */
    public static void loadRecentFiles() {
        String files = PrefStorage.getSetting("recentfiles");
        if (files.equals("")) {
            loadRibbonMenu(null);
            recentFileList.setListData(new String[0]);
            return;
        }
        String[] fileList = files.split("\n");
        int neededLength = 0;
        for (String file : fileList) {
            if ((new File(file)).isFile()) {
                neededLength++;
            }
        }
        if (neededLength > 10) {
            neededLength = 10;
        }
        KeyValListItem[] items = new KeyValListItem[neededLength];
        int i = 0;
        RibbonApplicationMenuEntrySecondary[] recentmenu
                = new RibbonApplicationMenuEntrySecondary[fileList.length];
        for (String f : fileList) {
            File file = new File(f);
            if (file.isFile() && i < neededLength) {
                items[i] = new KeyValListItem(file.getName(), file.getPath());
                recentmenu[i] = (new RibbonApplicationMenuEntrySecondary(
                        null,
                        file.getName(),
                        new ActionListener() {
                            final String path = file.getPath();

                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                Editor edit = new Editor();
                                edit.openFileFromName(path);
                                loadFrame(edit);
                            }
                        },
                        JCommandButton.CommandButtonKind.ACTION_ONLY));
                i++;
            }
        }

        loadRibbonMenu(recentmenu);
        recentFileList.setListData(items);

        // Re-save list to remove bad entries
        String list = "";
        for (KeyValListItem item : items) {
            list += item.getValue() + "\n";
        }
        PrefStorage.saveSetting("recentfiles", list);
    }

    /**
     * Add a file to the recent files lists.
     *
     * @param file The file path.
     */
    public static void addRecentFile(String file) {
        file = (new File(file)).getAbsolutePath();
        String files = PrefStorage.getSetting("recentfiles");
        String[] fileList = files.split("\n");
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].trim().equals(file)) {
                fileList[i] = "";
            }
        }
        files = file + "\n";
        for (String f : fileList) {
            if (!f.trim().equals("")) {
                files += f + "\n";
            }
        }
        PrefStorage.saveSetting("recentfiles", files);
        PrefStorage.save();
        loadRecentFiles();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        mainPane = mainPane = new javax.swing.JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Theme.windowColor());
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
        ;
        jLabel2 = new javax.swing.JLabel();
        recentItemsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        recentFileList = new javax.swing.JList();
        recentFileBtn = new javax.swing.JButton();
        recentItemsTitle = new javax.swing.JLabel();
        appPanel = new javax.swing.JPanel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SyMAT");
        setMinimumSize(new java.awt.Dimension(640, 540));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        mainPane.setBackground(new java.awt.Color(204, 204, 204));
        mainPane.setAutoscrolls(true);
        mainPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        mainPane.setOpaque(false);

        jLabel2.setFont(Main.ubuntuRegular.deriveFont(48.0F));
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("SyMAT");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        recentItemsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        recentItemsPanel.setForeground(new java.awt.Color(153, 153, 153));
        recentItemsPanel.setMaximumSize(new java.awt.Dimension(160, 273));
        recentItemsPanel.setOpaque(false);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(23, 206));
        jScrollPane1.setName(""); // NOI18N

        recentFileList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        recentFileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recentFileListMouseClicked(evt);
            }
        });
        recentFileList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                recentFileListMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(recentFileList);

        recentFileBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/apocalypselabs/symat/icons/openfile.png"))); // NOI18N
        recentFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recentFileBtnActionPerformed(evt);
            }
        });

        recentItemsTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        recentItemsTitle.setForeground(new java.awt.Color(102, 102, 102));
        recentItemsTitle.setText("  Recent Files");
        recentItemsTitle.setOpaque(true);
        recentItemsTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recentItemsTitleMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout recentItemsPanelLayout = new javax.swing.GroupLayout(recentItemsPanel);
        recentItemsPanel.setLayout(recentItemsPanelLayout);
        recentItemsPanelLayout.setHorizontalGroup(
            recentItemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recentItemsPanelLayout.createSequentialGroup()
                .addGroup(recentItemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recentItemsPanelLayout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(recentFileBtn))
                    .addGroup(recentItemsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(recentItemsTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        recentItemsPanelLayout.setVerticalGroup(
            recentItemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recentItemsPanelLayout.createSequentialGroup()
                .addComponent(recentItemsTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recentFileBtn)
                .addContainerGap())
        );

        appPanel.setOpaque(false);
        appPanel.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout mainPaneLayout = new javax.swing.GroupLayout(mainPane);
        mainPane.setLayout(mainPaneLayout);
        mainPaneLayout.setHorizontalGroup(
            mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(recentItemsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        mainPaneLayout.setVerticalGroup(
            mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(appPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainPaneLayout.createSequentialGroup()
                        .addComponent(recentItemsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addContainerGap())
        );
        mainPane.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainPane.setLayer(recentItemsPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainPane.setLayer(appPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        getContentPane().add(mainPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        setLocationRelativeTo(null);
    }//GEN-LAST:event_formComponentShown

    private void recentFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recentFileBtnActionPerformed
        if (recentFileList.getSelectedValue() == null) {
            return;
        }
        KeyValListItem file = (KeyValListItem) recentFileList.getSelectedValue();
        if (file.isEmpty()) {
            return;
        }
        Editor edit = new Editor();
        Debug.println(file.getValue());
        edit.openFileFromName(file.getValue());
        loadFrame(edit);
    }//GEN-LAST:event_recentFileBtnActionPerformed

    private void recentFileListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentFileListMouseClicked
        if (evt.getClickCount() == 2) {
            recentFileBtnActionPerformed(null);
        }
    }//GEN-LAST:event_recentFileListMouseClicked

    private void recentFileListMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentFileListMouseMoved
        try {
            ListModel m = recentFileList.getModel();
            int index = recentFileList.locationToIndex(evt.getPoint());
            if (index > -1) {
                recentFileList.setToolTipText(
                        ((KeyValListItem) m.getElementAt(index)).getValue());
            }
        } catch (Exception ex) {
            // This feature is optional.  Just skip it if it's broken.
            recentFileList.setToolTipText("");
        }
    }//GEN-LAST:event_recentFileListMouseMoved

    private void recentItemsTitleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentItemsTitleMouseClicked

    }//GEN-LAST:event_recentItemsTitleMouseClicked

    /**
     * Adds the given JInternalFrame to the mainPane. Automatically does layout
     * and sets visible (if show==true).
     *
     * @param frame The frame
     * @param show Should the frame be visible?
     */
    public static void loadFrame(JInternalFrame frame, boolean show) {
        int w = frame.getWidth();
        int h = frame.getHeight();
        int pw = mainPane.getWidth();
        int ph = mainPane.getHeight();
        if (frame.isResizable()) {
            if (h > ph) {
                h = ph;
            }
            if (w > pw) {
                w = pw;
            }
            frame.setSize(w, h);
        }
        mainPane.add(frame);
        frame.setLocation(
                (pw / 2) - (w / 2),
                (ph / 2) - (h / 2));
        if (frame.getLocation().y < 0) {
            frame.setLocation(frame.getLocation().x, 0);
        }
        if (show) {
            frame.setVisible(true);
        }
    }

    /**
     * Adds the given JInternalFrame to the mainPane. Automatically does layout
     * and sets visible.
     *
     * @param frame The frame
     */
    public static void loadFrame(JInternalFrame frame) {
        loadFrame(frame, true);
    }

    /**
     * Cascade all the frames in a stack. Somehow reverses the order each time
     * around, I have no idea why but it's a "feature" now!
     */
    public static void cascade() {
        JInternalFrame[] frames = mainPane.getAllFrames();
        int x = 12;
        int y = 24;
        Debug.println("Cascading " + frames.length + " frames...");
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].isVisible()) {
                Debug.println("Frame: "
                        + frames[i].getTitle()
                        + ", Times: " + i
                        + ", Xpos: " + x * i
                        + ", Ypos: " + y * i);
                frames[i].setBounds(x * i,
                        y * i,
                        frames[i].getWidth(),
                        frames[i].getHeight());
                frames[i].toFront();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        try {
            // Ubuntu font for prettifying
            ubuntuRegular = Font.createFont(Font.TRUETYPE_FONT, Main.class.
                    getResourceAsStream("/Ubuntu-R.ttf"));
        } catch (FontFormatException | IOException ex) {
            ubuntuRegular = Font.getFont(Font.SANS_SERIF);
            System.err.println("Error loading fonts: " + ex.getMessage());
        } catch (Exception ex) {
            ubuntuRegular = Font.getFont(Font.SANS_SERIF);
            System.err.println("Error loading fonts: " + ex.getMessage());
        }

        // Set icon
        String[] sizes = {"16", "24", "32", "48", "64", "96", "128", "256", "512"};
        for (String s : sizes) {
            symatlogo.add(new ImageIcon(
                    Main.class.getResource(s + "x" + s + ".png")).getImage());
        }

        // Command line args
        for (String arg : args) {
            switch (arg) {
                case "skippython":
                    skipPython = true;
                    break;
                case "skipeditor":
                    skipEditor = true;
                    break;
                case "quickstart":
                    skipPython = true;
                    skipEditor = true;
                    break;
                case "licensereset":
                    PrefStorage.saveSetting("license", "");
                    break;
                default:
                    argfile = arg;
                    break;
            }
        }

        SingleInstanceClient sicli = new SingleInstanceClient(argfile);

        try {
            new SingleInstanceServer().start();
        } catch (IOException ex) {
            Debug.printerr("Cannot start instance listener:\n\n");
            Debug.stacktrace(ex);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // this will prepare JavaFX toolkit and environment
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Platform.setImplicitExit(false);
                    }
                });
            }
        });


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new SplashScreen().setVisible(true);
        });
    }

    /**
     *
     */
    public static JCommandButton shellbtn
            = new JCommandButton("Shell", getRibbonIcon("shell"));

    /**
     *
     */
    public static JCommandButton editorbtn
            = new JCommandButton("Editor", getRibbonIcon("editor"));

    /**
     *
     */
    public static JCommandButton graphbtn
            = new JCommandButton("Graph", getRibbonIcon("graph"));

    /**
     *
     */
    public static JCommandButton notepadbtn
            = new JCommandButton("Notepad", getRibbonIcon("notepad"));

    /**
     *
     */
    public static JCommandButton wikibtn
            = new JCommandButton("Wiki", getRibbonIcon("wiki"));

    /**
     *
     */
    public static JCommandButton forumbtn
            = new JCommandButton("Forum", getRibbonIcon("forum"));

    /**
     *
     */
    public static JCommandButton padsbtn
            = new JCommandButton("Pads", getRibbonIcon("pads"));

    /**
     *
     */
    public static JCommandButton tasksbtn
            = new JCommandButton("Tasks", getRibbonIcon("tasks"));

    /**
     *
     */
    public static JCommandButton installpluginbtn
            = new JCommandButton("Install", getRibbonIcon("installplugin"));

    /**
     *
     */
    public static RibbonApplicationMenuEntryPrimary openbtn;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel appPanel;
    public static javax.swing.JLabel jLabel2;
    public static javax.swing.JMenuItem jMenuItem1;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JDesktopPane mainPane;
    public static javax.swing.JButton recentFileBtn;
    public static javax.swing.JList recentFileList;
    public static javax.swing.JPanel recentItemsPanel;
    public static javax.swing.JLabel recentItemsTitle;
    // End of variables declaration//GEN-END:variables
}
