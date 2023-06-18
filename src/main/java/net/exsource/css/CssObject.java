package net.exsource.css;

import org.jetbrains.annotations.NotNull;

public interface CssObject {

    void setValue(@NotNull String key, Object value);

    Object getValue(@NotNull String key);

    void clearBody();

    String bodyAsString();

}
