/*
 * Apocalypse Laboratories
 * Open Source License
 * 
 * Source code can be used for any purpose, as long as:
 *  - Compiled binaries are rebranded and trademarks are not
 *    visible by the end user at any time, except to give
 *    credit to Apocalypse Laboratories, such as by showing
 *    "Based on <product> by Apocalypse Laboratories" or a
 *    similar notice;
 *  - You do not use the code for evil;
 *  - Rebranded compiled applications have significant
 *    differences in functionality;
 *  - and you provide your modified source code for download,
 *    under the terms of the GNU LGPL v3 or a comparable
 *    license.
 * 
 * Compiled binaries cannot be redistributed or mirrored,
 * unless:
 *  - You have written permission from Apocalypse Laboratories;
 *  - Downloads are not available from Apocalypse Laboratories,
 *    not even behind a paywall or other blocking mechanism;
 *  - or you have received a multi-computer license, in which
 *    case you should take measures to prevent unauthorized
 *    downloads, such as preventing download access from the 
 *    Internet.
 */
package net.apocalypselabs.symat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.FunctionCompletion;
import org.fife.ui.autocomplete.MarkupTagCompletion;
import org.fife.ui.autocomplete.ShorthandCompletion;
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
                                CodeEditor.class.getResourceAsStream("resources/" + lang + fileid + ".txt")));
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
