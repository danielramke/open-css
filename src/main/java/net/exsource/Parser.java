package net.exsource;

import java.io.File;
import java.net.URI;

public interface Parser {

    void parse(File file);

    void parse(String file);

    void parse(URI file);

    File getFile();

}
