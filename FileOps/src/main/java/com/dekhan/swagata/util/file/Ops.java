package com.dekhan.swagata.util.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ops extends Base {

    private static final Logger log = LogManager.getLogger(com.dekhan.swagata.util.file.Ops.class);

    private Pattern p;

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

    private final Pattern convertGlobToRegex(String glob) {
	return Pattern.compile("(?s)^\\Q"
		+ glob.replace("\\E", "\\E\\\\E\\Q").replace("*", "\\E.*\\Q").replace("?", "\\E.\\Q") + "\\E$");
    }

    private final void findMatch(File[] allFiles, List<String> matchedFiles) {

	log.debug(getLogMsgPrefix() + "Start");
	for (File f : allFiles) {
	    if (f.isFile()) {
		try {
		    String fileName = f.getCanonicalPath();
		    Matcher m = p.matcher(fileName);
		    if (m.find()) {
			matchedFiles.add(fileName);
		    }
		} catch (IOException e) {
		    log.error(getLogMsgPrefix() + "Error!!!");
		    log.error(e);
		}
	    } else if (f.isDirectory()) {
		findMatch(f.listFiles(), matchedFiles);
	    }
	}

	log.debug(getLogMsgPrefix() + "End");
    }
    
    public List <String> findFiles(String dir, String pattern) {
	p = convertGlobToRegex(pattern);

	File topDir = new File(dir);
	List<String> matchedFiles = new ArrayList<String>();

	if (topDir.exists() && topDir.isDirectory()) {
	    File arr[] = topDir.listFiles();
	    findMatch(arr, matchedFiles);
	}
	
	return matchedFiles;
    }

    public static void main(String[] args) throws Exception {

	Ops f = new Ops();

	log.info(f.getLogMsgPrefix() + "==============");
	log.info(f.getLogMsgPrefix() + "Start");
	log.info(f.getLogMsgPrefix() + "==============");

	CommandLine cmd = f.getArgs(args);
	String dir = cmd.getOptionValue("dir");
	String pattern = cmd.getOptionValue("pattern");

	log.info(f.getLogMsgPrefix() + "Directory = " + dir);
	log.info(f.getLogMsgPrefix() + "File Name Pattern = " + pattern);	
	
	List<String> matchedFiles = f.findFiles(dir, pattern);

	for (String m : matchedFiles) {
	    log.info(f.getLogMsgPrefix() + "Matched File = " + m);
	}

	log.info(f.getLogMsgPrefix() + "==============");
	log.info(f.getLogMsgPrefix() + "End");
	log.info(f.getLogMsgPrefix() + "==============");
    }
}