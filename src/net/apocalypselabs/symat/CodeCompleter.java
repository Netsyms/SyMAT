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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.VariableCompletion;

/**
 *
 * @author Skylar
 */
public class CodeCompleter {

    private final CompletionProvider provider;
    private final String lang;

    /**
     *
     * @param language Either js or py.
     */
    public CodeCompleter(String language) {
        lang = language;
        provider = makeCompletions();
    }

    public CompletionProvider getProvider() {
        return provider;
    }

    private CompletionProvider makeCompletions() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        try {
            String[] files = {"functions", "constants"};
            for (String fileid : files) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                CodeCompleter.class.getResourceAsStream("resources/" + (fileid.equals(files[0]) ? "" : lang) + fileid + ".txt")));
                String line;
                while ((line = reader.readLine()) != null) {
                    switch (fileid) {
                        case "functions":
                            String[] args = line.split("\\|");
                            if (args.length == 2) {
                                provider.addCompletion(new BasicCompletion(provider, args[0], args[1]));
                            } else if (args.length == 3) {
                                provider.addCompletion(new BasicCompletion(provider, args[0], args[1], args[2]));
                            } else {
                                provider.addCompletion(new BasicCompletion(provider, args[0]));
                            }
                            break;
                        case "constants":
                            provider.addCompletion(new VariableCompletion(provider, line, "double"));
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            Debug.printerr(ex.getMessage());
        }
        return provider;
    }

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.VariableCompletion;

/**
 *
 * @author Skylar
 */
public class CodeCompleter {

    private final CompletionProvider provider;
    private final String lang;

    /**
     *
     * @param language Either js or py.
     */
    public CodeCompleter(String language) {
        lang = language;
        provider = makeCompletions();
    }

    public CompletionProvider getProvider() {
        return provider;
    }

    private CompletionProvider makeCompletions() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        try {
            String[] files = {"functions", "constants"};
            for (String fileid : files) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                CodeCompleter.class.getResourceAsStream("resources/" + (fileid.equals(files[0]) ? "" : lang) + fileid + ".txt")));
                String line;
                while ((line = reader.readLine()) != null) {
                    switch (fileid) {
                        case "functions":
                            String[] args = line.split("\\|");
                            if (args.length == 2) {
                                provider.addCompletion(new BasicCompletion(provider, args[0], args[1]));
                            } else if (args.length == 3) {
                                provider.addCompletion(new BasicCompletion(provider, args[0], args[1], args[2]));
                            } else {
                                provider.addCompletion(new BasicCompletion(provider, args[0]));
                            }
                            break;
                        case "constants":
                            provider.addCompletion(new VariableCompletion(provider, line, "double"));
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            Debug.printerr(ex.getMessage());
        }
        return provider;
    }

}
>>>>>>> c1bd78b886a57d8f285747c749491ece75862a43
