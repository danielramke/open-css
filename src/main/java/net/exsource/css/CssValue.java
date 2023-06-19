package net.exsource.css;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public record CssValue(@NotNull Object value) {

    public String asString() {
        return (String) value;
    }

    public CharSequence asCharSequence() {
        return (CharSequence) value;
    }

    public Integer asInt() {
        return (Integer) value;
    }

    public Boolean asBoolean() {
        return (Boolean) value;
    }

    public Float asFloat() {
        return (Float) value;
    }

    public Double asDouble() {
        return (Double) value;
    }

    public Byte asByte() {
        return (Byte) value;
    }

    public Short asShort() {
        return (Short) value;
    }

}
