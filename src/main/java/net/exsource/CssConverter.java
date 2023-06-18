package net.exsource;

import net.exsource.css.CssPOJO;
import net.exsource.css.CssType;
import net.exsource.openlogger.Logger;
import net.exsource.openlogger.level.LogLevel;
import net.exsource.openlogger.util.ConsoleColor;
import net.exsource.openlogger.util.DevTools;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CssConverter implements Parser {

    private static final Logger logger = Logger.getLogger();

    private File toConvert;
    private CssParser parser;
    @Override
    public void parse(File file) {
        if(file == null) {
            logger.warn("We can't handle null files please don't give null as param!");
            return;
        }
        logger.debug("Input: " + file.getAbsolutePath() + " : " + file.exists());
        if(!file.exists()) {
            logger.error(new FileNotFoundException("Can't find css file!"));
            return;
        }

        this.toConvert = file;
        List<String> rawText = DevTools.readTxT(file.getAbsolutePath());
        if(rawText.isEmpty()) {
            logger.warn("Css file [ " + file.getPath() + " ] is empty and not usefully!");
            return;
        }
        String cssText;
        if(rawText.size() > 1) {
            StringBuilder builder = new StringBuilder();
            for(String line : rawText) {
                builder.append(line);
            }
            cssText = builder.toString();
        } else {
            cssText = rawText.get(0);
        }
        this.parser = generateParser(cssText);
        logger.info("Creating parser for [ " + file.getName() + " ]");
    }


    @Override
    public void parse(String file) {
        this.parse(new File(file));
    }

    @Override
    public void parse(URI file) {
        this.parse(file.getPath());
    }

    public void fromPOJO(Class<CssPOJO> pojoClass) {

    }

    public void fromJSON(String file) {

    }

    public void fromJSON(File file) {

    }

    public void fromJSON(URI file) {

    }

    @Override
    public File getFile() {
        return toConvert;
    }

    public CssParser getParser() {
        return parser;
    }

    private CssParser generateParser(String css) {
        String formattedCss;
        StringBuilder build = new StringBuilder();
        commentRemover(css.replaceAll("\\s+", " "), build);
        formattedCss = build.toString();

        List<String> classesRaw = new ArrayList<>();
        cssObjectLocator(formattedCss, CssType.CLASS, classesRaw);

        List<String> idsRaw = new ArrayList<>();
        cssObjectLocator(formattedCss, CssType.ID, idsRaw);

        List<String> pseudoRaw = new ArrayList<>();
        cssObjectLocator(formattedCss, CssType.PSEUDO, pseudoRaw);

        List<String> tagsRaw = new ArrayList<>();
        cssObjectLocator(formattedCss, CssType.TAG, tagsRaw);

        List<String> keyframesRaw = new ArrayList<>();
        cssObjectLocator(formattedCss, CssType.KEYFRAME, keyframesRaw);

        //logger.info(formattedCss);
        return new CssParser();
    }

    private void cssObjectLocator(String input, CssType type, List<String> output) {
        String next;

        try {
            String codeBlock = extendedCodeBlocks(input).trim();
            String codeHolder = input.substring(0, input.indexOf(codeBlock));

            if (!type.equals(CssType.TAG)) {
                if (codeHolder.startsWith(type.getIndicator())) {
                    output.add(codeHolder + codeBlock);
                }
            } else {
                if (codeHolder.substring(0, 1).matches("[a-zA-Z0-9*]")) {
                    output.add(codeHolder + codeBlock);
                }
            }

            next = input.replace(codeHolder + codeBlock, "").trim();
            if (next.isEmpty() || next.isBlank()) {
                logger.debug(type.name().toLowerCase() +
                        (type.equals(CssType.CLASS) ? "es" : "'s") + " found [ " + LogLevel.INFO.getColor() + output.size() + ConsoleColor.RESET + " ]");
                logger.list(output, type.getIndicator(), '#', LogLevel.DEBUG);
                return;
            }
            cssObjectLocator(next, type, output);
        } catch (StringIndexOutOfBoundsException exception) {
            logger.error(exception);
        }
    }

    private String extendedCodeBlocks(@NotNull String input) {
        int openCount = 0;
        int closeCount = 0;
        String start = input.substring(input.indexOf("{"));
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < start.length(); i++) {
            if(start.charAt(i) == '{') {
                openCount++;
            }
            if(start.charAt(i) == '}') {
                closeCount++;
            }
            builder.append(start.charAt(i));
            if(openCount == closeCount || closeCount > openCount) {
                break;
            }
        }
        return builder.toString();
    }

    private void commentRemover(String input, StringBuilder output) {
        if(input.contains("/*") || input.contains("*/")) {
            String found = input.substring(input.indexOf("/*"), input.indexOf("*/", 1) + 2);
            String removed = input.replace(found, "");
            this.commentRemover(removed, output);
            return;
        }
        output.append(input);
    }

    private static class CssParser {

    }
}
