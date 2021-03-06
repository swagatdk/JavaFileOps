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

public class FileOps {

    private static final Logger log = LogManager.getLogger(com.dekhan.swagata.util.file.FileOps.class);

    private final Pattern getPattern(String pattern, boolean isGlob) {

	log.debug("Start");
	Pattern p;

	if (isGlob) {
	    p = Pattern.compile("(?s)^\\Q"
		    + pattern.replace("\\E", "\\E\\\\E\\Q").replace("*", "\\E.*\\Q").replace("?", "\\E.\\Q") + "\\E$");
	} else {
	    p = Pattern.compile(pattern);
	}

	log.debug("End");
	return p;
    }

    public List<String> listFileNames(String dir) throws IOException {

	log.debug("Start");
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

	log.info("Scanned File Count = " + counter.get());
	log.info("Listed File Count = " + pathList.size());
	log.debug("End");
	return pathList;
    }

    public List<String> listAbsPath(String dir) throws IOException {

	log.debug("Start");
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

	log.info("Scanned File Count = " + counter.get());
	log.info("Listed File Count = " + pathList.size());
	log.debug("End");
	return pathList;
    }

    public List<String> listRelPath(String dir) throws IOException {

	log.debug("Start");
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

	log.info("Scanned File Count = " + counter.get());
	log.info("Listed File Count = " + pathList.size());
	log.debug("End");
	return pathList;
    }

    public List<String> listMatchedAbsPath(String dir, String pattern, boolean isGlob) throws IOException {

	log.debug("Start");
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

	log.info("Scanned File Count = " + counter.get());
	log.info("Matched File Count = " + pathList.size());
	log.debug("End");
	return pathList;
    }

    public List<String> listMatchedRelPath(String dir, String pattern, boolean isGlob) throws IOException {

	log.debug("Start");
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

	log.info("Scanned File Count = " + counter.get());
	log.info("Matched File Count = " + pathList.size());
	log.debug("End");
	return pathList;
    }

    public List<String> copyMatchedFiles(String dir, String tgtDir, String pattern, boolean isGlob) throws IOException {

	log.debug("Start");
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
			    log.error("Error!!!");
			    log.error(e.getMessage(), e);
			}
		    }
		}
		return FileVisitResult.CONTINUE;
	    }
	});

	log.info("Scanned File Count = " + counter.get());
	log.info("Copied File Count = " + pathList.size());
	log.debug("End");
	return pathList;
    }

    public List<String> moveMatchedFiles(String dir, String tgtDir, String pattern, boolean isGlob) throws IOException {

	log.debug("Start");
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
			    log.error("Error!!!");
			    log.error(e.getMessage(), e);
			}
		    }
		}
		return FileVisitResult.CONTINUE;
	    }
	});

	log.info("Scanned File Count = " + counter.get());
	log.info("Moved File Count = " + pathList.size());
	log.debug("End");
	return pathList;
    }

    private boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }
}