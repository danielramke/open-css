package net.exsource;

import net.exsource.openlogger.Logger;

public class Example {

    public static void main(String[] args) {
        Logger.enableDebug(true);
        CssConverter cssConverter = new CssConverter();

        cssConverter.parse("big.css");
    }

}
