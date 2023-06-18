package net.exsource.css.help;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface CssSpecialAttributes {

    String name() default "";
}
