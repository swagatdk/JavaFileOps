package com.dekhan.swagata.util.algo;

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

public class AlgoOpsUse extends AlgoOps {
    
    private static final Logger log = LogManager.getLogger(com.dekhan.swagata.util.algo.AlgoOpsUse.class);
    
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
            formatter.printHelp("AlgoOps", options);
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
    
        AlgoOps algoOps = new AlgoOps();
    
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
    
        //In in1 = new In(args[0]);
        //In in2 = new In(args[1]);
        String in1 = "xabxac";
        String in2 = "abcabxabcd";
        	
        String s = in1.trim().replaceAll("\\s+", " ");
        String t = in2.trim().replaceAll("\\s+", " ");
        String lcs = algoOps.lcs(s, t);
        log.debug(getLogMsgPrefix() + "Returned LCS = " + lcs);        
    
        log.info(getLogMsgPrefix() + "End");
        log.info(getLogMsgPrefix() + "==============================");
    }    
}
