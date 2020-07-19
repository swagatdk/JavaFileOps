package com.dekhan.swagata.util.algo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlgoOps {
    
    private static final Logger log = LogManager.getLogger(com.dekhan.swagata.util.algo.AlgoOps.class);
    
    private boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }
    
    public String lcs(String s, String t) {
	log.debug("Start");
        int n1 = s.length();

        // concatenate two string with intervening '\1'
        String text  = s + '\1' + t;
        int n  = text.length();

        // compute suffix array of concatenated text
        SuffixArray suffix = new SuffixArray(text);

        // search for longest common substring
        String lcs = "";
        for (int i = 1; i < n; i++) {

            // adjacent suffixes both from first text string
            if (suffix.index(i) < n1 && suffix.index(i-1) < n1) continue;

            // adjacent suffixes both from secondt text string
            if (suffix.index(i) > n1 && suffix.index(i-1) > n1) continue;

            // check if adjacent suffixes longer common substring
            int length = suffix.lcp(i);
            if (length > lcs.length()) {
                lcs = text.substring(suffix.index(i), suffix.index(i) + length);
            }
        }
        log.debug("End");
        return lcs;
    }

}
