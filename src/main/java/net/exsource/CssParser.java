package net.exsource;

import net.exsource.css.help.CssType;
import net.exsource.css.maps.*;
import net.exsource.exception.CssObjectNotFound;
import net.exsource.openlogger.Logger;
import net.exsource.openlogger.level.LogLevel;
import net.exsource.openlogger.util.ConsoleColor;
import net.exsource.openlogger.util.DevTools;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.*;

@SuppressWarnings("unused")
public class CssParser implements Parser {

    private static final Logger logger = Logger.getLogger();

    private final Map<CssType, List<CssObject>> cssObjects = new HashMap<>();
    private File toConvert;

    public CssParser() {
        cssObjects.put(CssType.CLASS, new ArrayList<>());
        cssObjects.put(CssType.ID, new ArrayList<>());
        cssObjects.put(CssType.TAG, new ArrayList<>());
        cssObjects.put(CssType.PSEUDO, new ArrayList<>());
    }

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
        generateParser(cssText);
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

    @Override
    public File getFile() {
        return toConvert;
    }

    public Map<CssType, List<CssObject>> getCssObjects() {
        return cssObjects;
    }

    public List<CssClass> getClasses() {
        List<CssClass> classes = new ArrayList<>();
        for (CssObject object : getObjectsByType(CssType.CLASS)) {
            classes.add((CssClass) object);
        }
        return classes;
    }

    public CssClass getCssClass(@NotNull String className) {
        if(!className.startsWith(".")) {
            className = "." + className;
        }
        CssClass cssClass = null;
        for (CssClass entries : getClasses()) {
            if (entries.getName().equals(className.trim())) {
                cssClass = entries;
            }
        }
        if(cssClass == null) {
            cssClass = new CssClass("", "{}");
            logger.error(new CssObjectNotFound(className + " CLASS wasn't found!"));
        }
        return cssClass;
    }

    public List<CssID> getIDs() {
        List<CssID> ids = new ArrayList<>();
        for (CssObject object : getObjectsByType(CssType.ID)) {
            ids.add((CssID) object);
        }
        return ids;
    }

    public CssID getCssID(@NotNull String idName) {
        if(!idName.startsWith("#")) {
            idName = "#" + idName;
        }
        CssID cssID = null;
        for (CssID entries : getIDs()) {
            if (entries.getName().equals(idName.trim())) {
                cssID = entries;
            }
        }
        if(cssID == null) {
            cssID = new CssID("", "{}");
            logger.error(new CssObjectNotFound(cssID + " ID wasn't found!"));
        }
        return cssID;
    }

    public List<CssTag> getTags() {
        List<CssTag> tags = new ArrayList<>();
        for (CssObject object : getObjectsByType(CssType.TAG)) {
            tags.add((CssTag) object);
        }
        return tags;
    }

    public CssTag getCssTag(@NotNull String tagName) {
        CssTag cssTag = null;
        for (CssTag entries : getTags()) {
            if (entries.getName().equals(tagName)) {
                cssTag = entries;
            }
        }
        if(cssTag == null) {
            cssTag = new CssTag("", "{}");
            logger.error(new CssObjectNotFound(cssTag + " TAG wasn't found!"));
        }
        return cssTag;
    }

    public List<CssPseudo> getPseudos() {
        List<CssPseudo> pseudos = new ArrayList<>();
        for (CssObject object : getObjectsByType(CssType.PSEUDO)) {
            pseudos.add((CssPseudo) object);
        }
        return pseudos;
    }

    public CssPseudo getCssPseudo(@NotNull String pseudoName) {
        if(!pseudoName.startsWith(":")) {
            pseudoName = ":" + pseudoName;
        }
        CssPseudo cssPseudo = null;
        for (CssPseudo entries : getPseudos()) {
            if (entries.getName().equals(pseudoName.trim())) {
                cssPseudo = entries;
            }
        }
        if(cssPseudo == null) {
            cssPseudo = new CssPseudo("", "{}");
            logger.error(new CssObjectNotFound(cssPseudo + " PSEUDO wasn't found!"));
        }
        return cssPseudo;
    }

    public List<CssObject> getObjectsByType(@NotNull CssType type) {
        if(cssObjects.containsKey(type)) {
            if(!cssObjects.get(type).isEmpty()) {
                return cssObjects.get(type);
            }
        }
        return new ArrayList<>();
    }

    private void generateParser(String css) {
        String formattedCss;
        StringBuilder build = new StringBuilder();
        commentRemover(css.replaceAll("\\s+", " "), build);
        formattedCss = build.toString();

        List<String> classesRaw = new ArrayList<>();
        cssObjectLocator(formattedCss, CssType.CLASS, classesRaw);
        convertToCssObject(CssType.CLASS, classesRaw);

        List<String> idsRaw = new ArrayList<>();
        cssObjectLocator(formattedCss, CssType.ID, idsRaw);
        convertToCssObject(CssType.ID, idsRaw);

        List<String> pseudoRaw = new ArrayList<>();
        cssObjectLocator(formattedCss, CssType.PSEUDO, pseudoRaw);
        convertToCssObject(CssType.PSEUDO, pseudoRaw);

        List<String> tagsRaw = new ArrayList<>();
        cssObjectLocator(formattedCss, CssType.TAG, tagsRaw);
        convertToCssObject(CssType.TAG, tagsRaw);

        List<String> keyframesRaw = new ArrayList<>();
        cssObjectLocator(formattedCss, CssType.KEYFRAME, keyframesRaw);

        logger.info("File: " + getFile().getName() + ", classes = " + getClasses().size()
                + ", ids = " + getIDs().size() + ", tags = " + getTags().size() + ", pseudo = " + getPseudos().size());
    }

    private void convertToCssObject(@NotNull CssType type, List<String> objects) {
        if(type.equals(CssType.KEYFRAME)) {
            return;
        }

        if(objects.isEmpty()) {
            logger.debug("Objects for type [ " + ConsoleColor.CYAN + type.getIndicator() + ConsoleColor.RESET + " ] is empty and will skipped!");
            return;
        }

        for(String object : objects) {
            String onlyName = object.substring(0, object.indexOf("{"));
            String code = object.replace(onlyName, "").trim();
            List<CssObject> storage = cssObjects.get(type);

            switch (type) {
                case CLASS -> {
                    storage.add(new CssClass(onlyName, code));
                }
                case ID -> {
                    storage.add(new CssID(onlyName, code));
                }
                case TAG -> {
                    storage.add(new CssTag(onlyName, code));
                }
                case PSEUDO -> {
                    storage.add(new CssPseudo(onlyName, code));
                }
            }
        }
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
                logger.info(type.name().toLowerCase() +
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
}
