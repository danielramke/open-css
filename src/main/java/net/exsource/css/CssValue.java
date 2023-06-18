package net.exsource.css;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class CssValue {

    private final Object value;

    public CssValue(@NotNull Object value) {
        this.value = value;
    }

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
