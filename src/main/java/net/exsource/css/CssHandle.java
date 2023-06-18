package net.exsource.css;

import net.exsource.openlogger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface CssHandle {

    static final Logger logger = Logger.getLogger();

    String getName();

    Map<String, CssValue> getEntries();

    void setValue(@NotNull String key, Object value);

    CssValue getValue(@NotNull String key);

    void clearBody();

    void setBody(@NotNull String css);

    String bodyAsString();

    //ToDo: make it readable for .json files.
    String bodyAsJsonString();

}
