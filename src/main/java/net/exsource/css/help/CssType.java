package net.exsource.css.help;

import org.jetbrains.annotations.NotNull;

public enum CssType {

    CLASS("."),
    ID("#"),
    KEYFRAME("@"),
    PSEUDO(":"),
    TAG("");

    private final String indicator;

    CssType(String indicator) {
        this.indicator = indicator;
    }

    public String getIndicator() {
        return indicator;
    }

    public static CssType byIndicator(@NotNull String indicator) {
        CssType type = TAG;
        for(CssType types : values()) {
            if(types.getIndicator().equals(indicator)) {
                type = types;
            }
        }
        return type;
    }
}
