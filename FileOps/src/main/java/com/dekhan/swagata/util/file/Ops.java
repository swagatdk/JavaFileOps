package com.dekhan.swagata.util.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ops extends Base {

    private static final Logger log = LogManager.getLogger(com.dekhan.swagata.util.file.Ops.class);

    private final CommandLine getArgs(String[] args) {

	log.debug(getLogMsgPrefix() + "Start");
	Options options = new Options();

	Option dirO = new Option("d", "dir", true, "Directory");
	dirO.setRequired(true);
	options.addOption(dirO);

	Option patternO = new Option("p", "pattern", true, "File Name Pattern");
	patternO.setRequired(true);
	options.addOption(patternO);

	CommandLineParser parser = new DefaultParser();
	HelpFormatter formatter = new HelpFormatter();
	CommandLine cmd = null;

	try {
	    cmd = parser.parse(options, args);
	} catch (ParseException e) {
	    log.error(getLogMsgPrefix() + "Error!!!");
	    log.error(e);
	    formatter.printHelp("Ops", options);
	    System.exit(1);
	}

	log.debug(getLogMsgPrefix() + "End");
	return cmd;
    }

    private final Pattern getPattern(String pattern, boolean isGlob) {

	log.debug(getLogMsgPrefix() + "Start");
	Pattern p;

	if (isGlob) {
	    p = Pattern.compile("(?s)^\\Q"
		    + pattern.replace("\\E", "\\E\\\\E\\Q").replace("*", "\\E.*\\Q").replace("?", "\\E.\\Q") + "\\E$");
	} else {
	    p = Pattern.compile(pattern);
	}

	log.debug(getLogMsgPrefix() + "End");
	return p;
    }

    public List<String> listFileNames(String dir) throws IOException {

	log.debug(getLogMsgPrefix() + "Start");
	final List<String> pathList = new ArrayList<String>();
	Path topDir = Paths.get(dir);
	final AtomicInteger counter = new AtomicInteger(0);

	Files.walkFileTree(topDir, new SimpleFileVisitor<Path>() {
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		counter.incrementAndGet();
		if (!Files.isDirectory(file)) {
		    String path = file.getFileName().toString();
		    pathList.add(path);
		}
		return FileVisitResult.CONTINUE;
	    }
	});

	log.info(getLogMsgPrefix() + "Scanned File Count = " + counter.get());
	log.info(getLogMsgPrefix() + "Listed File Count = " + pathList.size());
	log.debug(getLogMsgPrefix() + "End");
	return pathList;
    }

    public List<String> listAbsPath(String dir) throws IOException {

	log.debug(getLogMsgPrefix() + "Start");
	final List<String> pathList = new ArrayList<String>();
	Path topDir = Paths.get(dir);
	final AtomicInteger counter = new AtomicInteger(0);

	Files.walkFileTree(topDir, new SimpleFileVisitor<Path>() {
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		counter.incrementAndGet();
		if (!Files.isDirectory(file)) {
		    String path = file.toRealPath().toString();
		    pathList.add(path);
		}
		return FileVisitResult.CONTINUE;
	    }
	});

	log.info(getLogMsgPrefix() + "Scanned File Count = " + counter.get());
	log.info(getLogMsgPrefix() + "Listed File Count = " + pathList.size());
	log.debug(getLogMsgPrefix() + "End");
	return pathList;
    }

    public List<String> listRelPath(String dir) throws IOException {

	log.debug(getLogMsgPrefix() + "Start");
	final List<String> pathList = new ArrayList<String>();
	Path topDir = Paths.get(dir);
	final int rootLength = topDir.toRealPath().toString().length();
	final AtomicInteger counter = new AtomicInteger(0);

	Files.walkFileTree(topDir, new SimpleFileVisitor<Path>() {
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		counter.incrementAndGet();
		if (!Files.isDirectory(file)) {
		    String path = file.toRealPath().toString();
		    pathList.add(path.substring(rootLength + 1));
		}
		return FileVisitResult.CONTINUE;
	    }
	});

	log.info(getLogMsgPrefix() + "Scanned File Count = " + counter.get());
	log.info(getLogMsgPrefix() + "Listed File Count = " + pathList.size());
	log.debug(getLogMsgPrefix() + "End");
	return pathList;
    }

    public List<String> listMatchedAbsPath(String dir, String pattern, boolean isGlob) throws IOException {

	log.debug(getLogMsgPrefix() + "Start");
	final List<String> pathList = new ArrayList<String>();
	final Pattern p = getPattern(pattern, isGlob);
	Path topDir = Paths.get(dir);
	final AtomicInteger counter = new AtomicInteger(0);

	Files.walkFileTree(topDir, new SimpleFileVisitor<Path>() {
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		counter.incrementAndGet();
		if (!Files.isDirectory(file)) {
		    String path = file.toRealPath().toString();
		    Matcher m = p.matcher(path);
		    if (m.find()) {
			pathList.add(path);
		    }
		}
		return FileVisitResult.CONTINUE;
	    }
	});

	log.info(getLogMsgPrefix() + "Scanned File Count = " + counter.get());
	log.info(getLogMsgPrefix() + "Matched File Count = " + pathList.size());
	log.debug(getLogMsgPrefix() + "End");
	return pathList;
    }

    public List<String> listMatchedRelPath(String dir, String pattern, boolean isGlob) throws IOException {

	log.debug(getLogMsgPrefix() + "Start");
	final List<String> pathList = new ArrayList<String>();
	final Pattern p = getPattern(pattern, isGlob);
	Path topDir = Paths.get(dir);
	final int rootLength = topDir.toRealPath().toString().length();
	final AtomicInteger counter = new AtomicInteger(0);

	Files.walkFileTree(topDir, new SimpleFileVisitor<Path>() {
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		counter.incrementAndGet();
		if (!Files.isDirectory(file)) {
		    String path = file.toRealPath().toString();
		    Matcher m = p.matcher(path);
		    if (m.find()) {
			pathList.add(path.substring(rootLength + 1));
		    }
		}
		return FileVisitResult.CONTINUE;
	    }
	});

	log.info(getLogMsgPrefix() + "Scanned File Count = " + counter.get());
	log.info(getLogMsgPrefix() + "Matched File Count = " + pathList.size());
	log.debug(getLogMsgPrefix() + "End");
	return pathList;
    }

    public List<String> copyMatchedFiles(String dir, String tgtDir, String pattern, boolean isGlob) throws IOException {

	log.debug(getLogMsgPrefix() + "Start");
	final List<String> pathList = new ArrayList<String>();
	final Pattern p = getPattern(pattern, isGlob);
	Path topDir = Paths.get(dir);
	final String tgtRootPath = tgtDir;
	final int rootLength = topDir.toRealPath().toString().length();
	final AtomicInteger counter = new AtomicInteger(0);

	Files.walkFileTree(topDir, new SimpleFileVisitor<Path>() {
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		counter.incrementAndGet();
		if (!Files.isDirectory(file)) {
		    String path = file.toRealPath().toString();
		    Matcher m = p.matcher(path);
		    if (m.find()) {
			try {
			    Path tgt = Paths.get(tgtRootPath, path.substring(rootLength + 1));

			    if (!Files.exists(tgt)) {
				Files.createDirectories(tgt);
			    }

			    Files.copy(file, tgt, StandardCopyOption.REPLACE_EXISTING);
			    pathList.add(tgt.toString());

			} catch (IOException e) {
			    log.error(getLogMsgPrefix() + "Error!!!");
			    log.error(getLogMsgPrefix() + e.getMessage(), e);
			}
		    }
		}
		return FileVisitResult.CONTINUE;
	    }
	});

	log.info(getLogMsgPrefix() + "Scanned File Count = " + counter.get());
	log.info(getLogMsgPrefix() + "Copied File Count = " + pathList.size());
	log.debug(getLogMsgPrefix() + "End");
	return pathList;
    }

    public List<String> moveMatchedFiles(String dir, String tgtDir, String pattern, boolean isGlob) throws IOException {

	log.debug(getLogMsgPrefix() + "Start");
	final List<String> pathList = new ArrayList<String>();
	final Pattern p = getPattern(pattern, isGlob);
	Path topDir = Paths.get(dir);
	final String tgtRootPath = tgtDir;
	final int rootLength = topDir.toRealPath().toString().length();
	final AtomicInteger counter = new AtomicInteger(0);

	Files.walkFileTree(topDir, new SimpleFileVisitor<Path>() {
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		counter.incrementAndGet();
		if (!Files.isDirectory(file)) {
		    String path = file.toRealPath().toString();
		    Matcher m = p.matcher(path);
		    if (m.find()) {
			try {
			    Path tgt = Paths.get(tgtRootPath, path.substring(rootLength + 1));
			    if (!Files.exists(tgt)) {
				Files.createDirectories(tgt);
			    }
			    Files.move(file, tgt, StandardCopyOption.REPLACE_EXISTING);
			    pathList.add(tgt.toString());

			} catch (IOException e) {
			    log.error(getLogMsgPrefix() + "Error!!!");
			    log.error(getLogMsgPrefix() + e.getMessage(), e);
			}
		    }
		}
		return FileVisitResult.CONTINUE;
	    }
	});

	log.info(getLogMsgPrefix() + "Scanned File Count = " + counter.get());
	log.info(getLogMsgPrefix() + "Moved File Count = " + pathList.size());
	log.debug(getLogMsgPrefix() + "End");
	return pathList;
    }

    public static void main(String[] args) throws Exception {

	Ops ops = new Ops();

	log.info(ops.getLogMsgPrefix() + "==============");
	log.info(ops.getLogMsgPrefix() + "Start");
	log.info(ops.getLogMsgPrefix() + "==============");

	/*
	 * CommandLine cmd = ops.getArgs(args); String dir = cmd.getOptionValue("dir");
	 * String pattern = cmd.getOptionValue("pattern");
	 * 
	 * 
	 * log.info(ops.getLogMsgPrefix() + "Directory = " + dir);
	 * log.info(ops.getLogMsgPrefix() + "File Name Pattern = " + pattern);
	 */

	String dir = ".";
	String copyDir = "D:\\temp\\test\\copy\\";
	String moveDir = "D:\\temp\\test\\move\\";
	String pattern = "*.class";
	boolean isGlob = true;

	List<String> returnedFiles;

	returnedFiles = ops.listFileNames(dir);
	returnedFiles = ops.listAbsPath(dir);
	returnedFiles = ops.listRelPath(dir);
	returnedFiles = ops.listMatchedAbsPath(dir, pattern, isGlob);
	returnedFiles = ops.listMatchedRelPath(dir, pattern, isGlob);
	returnedFiles = ops.copyMatchedFiles(dir, copyDir, pattern, isGlob);
	returnedFiles = ops.moveMatchedFiles(copyDir, moveDir, pattern, isGlob);

	for (String m : returnedFiles) {
	    log.debug(ops.getLogMsgPrefix() + "Returned File = " + m);
	}

	log.info(ops.getLogMsgPrefix() + "==============");
	log.info(ops.getLogMsgPrefix() + "End");
	log.info(ops.getLogMsgPrefix() + "==============");
    }
}