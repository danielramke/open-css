package net.exsource.css.maps;

import net.exsource.css.CssHandle;
import net.exsource.css.CssValue;
import net.exsource.css.help.CssAttribute;
import net.exsource.exception.CssParseException;
import net.exsource.openlogger.util.ConsoleColor;
import net.exsource.util.CssUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SuppressWarnings("unused")
public abstract class CssObject implements CssHandle {

    private final String name;
    private Map<String, CssValue> entries;

    public CssObject(@NotNull String name, String css) {
        this.name = name;
        this.entries = CssUtils.generateMap(css, null);
    }

    @Override
    public String getName() {
        return name.trim();
    }

    @Override
    public Map<String, CssValue> getEntries() {
        return entries;
    }

    @Override
    public void setValue(@NotNull String key, Object value) {
        if(entries == null) {
            logger.warn("Can't set value for key [ " + ConsoleColor.YELLOW + key + ConsoleColor.RESET +  " ], no storage found in css_object [ " + ConsoleColor.YELLOW + name + ConsoleColor.RESET + " ]");
            return;
        }
        String check = (String) value;
        if(check.contains("{") || check.contains("}")) {
            logger.error(new CssParseException("Detected unsupported characters [ " + ConsoleColor.YELLOW + "{}" + ConsoleColor.RESET + " ]"));
            return;
        }
        if(entries.containsKey(key)) {
            entries.replace(key, new CssValue(value));
        } else {
            entries.put(key, new CssValue(value));
        }
        logger.debug("Add [ " + ConsoleColor.CYAN + key + ConsoleColor.RESET + " ] with value [ "
                + ConsoleColor.CYAN + entries.get(key).asString() + ConsoleColor.RESET + " ] to css_object [ " + ConsoleColor.CYAN + name + ConsoleColor.RESET + " ]");
    }

    @Override
    public CssValue getValue(@NotNull String key) {
        if(entries == null || entries.isEmpty()) {
            return null;
        }
        CssValue value = null;
        if(entries.containsKey(key)) {
            value = entries.get(key);
        }
        return value;
    }

    public CssValue getValue(@NotNull CssAttribute key) {
        return getValue(key.getCode());
    }

    @Override
    public void clearBody() {
        if(entries != null && !entries.isEmpty()) {
            entries.clear();
        }
    }

    @Override
    public void setBody(@NotNull String css) {
        this.entries = CssUtils.generateMap(css, entries);
        logger.debug("Override css body for [ " + name + " ]");
    }

    @Override
    public String bodyAsString() {
        if(entries == null || entries.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for(String key : entries.keySet()) {
            builder.append(key).append(": ").append(getValue(key).asString()).append(";").append(" ");
        }

        return builder.toString();
    }

    @Override
    public String bodyAsJsonString() {
        return "{}";
    }

}
