package net.exsource;

import net.exsource.css.maps.CssClass;
import net.exsource.openlogger.Logger;

public class Example {

    private static final Logger logger = Logger.getLogger();

    public static void main(String[] args) throws InterruptedException {
        Logger.enableDebug(false);
        CssParser parser = new CssParser();

        parser.parse("example.css");
        CssClass cssClass = parser.getCssClass("tested .love");
        logger.info(cssClass.getName() + "," + cssClass.bodyAsString());
    }

}
