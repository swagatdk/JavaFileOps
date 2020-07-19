package com.dekhan.swagata.util.algo;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class AlgoOpsTest {

    private static Logger log = LogManager.getLogger(com.dekhan.swagata.util.algo.AlgoOpsTest.class);
    private static AlgoOps algoOps;
    
    @BeforeAll
    public static void initOps() {
	algoOps = new AlgoOps();

	log.info("Main Class Init complete.");
	log.info("=========================");
    }

    @BeforeEach
    public void beforeEachTest() {
	log.info("Before Test");
    }

    @AfterEach
    public void afterEachTest() {
	log.info("After Test");
	log.info("~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    @Test
    @Order(1)
    public void testLcsRight() {	
	String in1 = "xabxac";
        String in2 = "abcabxabcd";
        	
        String s = in1.trim().replaceAll("\\s+", " ");
        String t = in2.trim().replaceAll("\\s+", " ");
        String lcs = algoOps.lcs(s, t);
        log.info("Returned LCS = " + lcs);           
	assertEquals("abxa", lcs);
    }
    
    @Test
    @Order(2)
    public void testLcsWrong() {	
	String in1 = "pqrst";
        String in2 = "uvwxyz";
        	
        String s = in1.trim().replaceAll("\\s+", " ");
        String t = in2.trim().replaceAll("\\s+", " ");
        String lcs = algoOps.lcs(s, t);
        log.info("Returned LCS = " + lcs);           
	assertEquals("", lcs);
    }
    
    @Test
    @Order(3)
    public void testLcsMultiline() {	
	String in1 = "drop view if exists  my_view1, my_view2, my_view3, my_view4";		
        String in2 = "drop \r\n " + "view some_view\t restrict";
        	
        String s = in1.trim().replaceAll("\\s+", " ");
        String t = in2.trim().replaceAll("\\s+", " ");
        String lcs = algoOps.lcs(s, t);
        log.info("Returned LCS = " + lcs);           
	assertEquals("drop view ", lcs);
    }    
}
