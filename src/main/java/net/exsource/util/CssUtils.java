package net.exsource.util;

import net.exsource.css.CssValue;
import net.exsource.exception.CssParseException;
import net.exsource.openlogger.Logger;
import net.exsource.openlogger.util.ConsoleColor;

import java.util.HashMap;
import java.util.Map;

public class CssUtils {

    private static final Logger logger = Logger.getLogger();

    public static Map<String, CssValue> generateMap(String css, Map<String, CssValue> fallback) {
        Map<String, CssValue> backup = null;
        if(fallback != null)
            backup = fallback;

        Map<String, CssValue> internal = new HashMap<>();
        String body = css.substring(1, css.length() - 1);

        if(body.contains("{") || body.contains("}")) {
            logger.error(new CssParseException("Detected unsupported characters [ " + ConsoleColor.YELLOW + "{}" + ConsoleColor.RESET + " ]"));
            internal = backup;
            return internal;
        }
        String[] entryArray = body.split(";");
        if(entryArray.length >= 1) {
            for(String entry : entryArray) {
                String[] current = entry.split(":");
                if(current.length < 2) {
                    continue;
                }
                if(internal.containsKey(current[0].trim())) {
                    internal.replace(current[0].trim(), new CssValue(current[1].trim()));
                    continue;
                }
                internal.put(current[0].trim(), new CssValue(current[1].trim()));
            }
        }
        return internal;
    }

}
