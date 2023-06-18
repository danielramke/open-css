package net.exsource.exception;

import java.io.Serial;

public class CssObjectNotFound extends IllegalStateException {

    @Serial
    private static final long serialVersionUID = 3034288555195293967L;

    private final String message;

    public CssObjectNotFound(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
