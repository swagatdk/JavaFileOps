package com.dekhan.swagata.util.file;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileOpsUse extends FileOps {
    
    private static final Logger log = LogManager.getLogger(com.dekhan.swagata.util.file.FileOpsUse.class);
    
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
            formatter.printHelp("FileOps", options);
            System.exit(1);
        }
    
        log.debug(getLogMsgPrefix() + "End");
        return cmd;
    }

    private static String getLogMsgPrefix() {
        Thread t = Thread.currentThread();
        return "{" + t.getStackTrace()[2].getMethodName() + "} - ";
    }

    public static void main(String[] args) throws Exception {
    
        FileOps fileOps = new FileOps();
    
        log.info(getLogMsgPrefix() + "==============================");
        log.info(getLogMsgPrefix() + "Start");
        
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
        String pattern = "*.prefs";
        boolean isGlob = true;
    
        List<String> returnedFiles;
    
        returnedFiles = fileOps.listFileNames(dir);
        returnedFiles = fileOps.listAbsPath(dir);
        returnedFiles = fileOps.listRelPath(dir);
        returnedFiles = fileOps.listMatchedAbsPath(dir, pattern, isGlob);
        returnedFiles = fileOps.listMatchedRelPath(dir, pattern, isGlob);
        returnedFiles = fileOps.copyMatchedFiles(dir, copyDir, pattern, isGlob);
        returnedFiles = fileOps.moveMatchedFiles(copyDir, moveDir, pattern, isGlob);
    
        for (String m : returnedFiles) {
            log.debug(getLogMsgPrefix() + "Returned File = " + m);
        }
    
        log.info(getLogMsgPrefix() + "End");
        log.info(getLogMsgPrefix() + "==============================");
    }    
}
