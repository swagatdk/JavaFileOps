package com.dekhan.swagata.util.file;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for simple Ops.
 */
public class OpsTest {
    
    private static final Logger log = LogManager.getLogger(com.dekhan.swagata.util.file.OpsTest.class);
    private static Ops ops;
    
    private static String dir = ".";
    private static String copyDir = "D:\\temp\\test\\copy\\";    
    private static String pattern = "*.prefs";
    private static boolean isGlob = true;
    
    @BeforeClass
    public static void initOps() {
        ops = new Ops();
        log.info("Main Class Init complete.");
    }
 
    @Before
    public void beforeEachTest() {
	log.info("Before Test");        
    }
 
    @After
    public void afterEachTest() {
	log.info("After Test");    
    }    
    
    @Test
    public void testListMatchedAbsPath()  {
	List<String> result = new ArrayList<String>();
	try {
	    result = ops.listMatchedAbsPath(dir, pattern, isGlob);
	} catch (IOException e) {
	    log.error(e.getMessage(), e);
	}
        assertEquals(3, result.size());
    }
    
    @Test
    public void testListMatchedRelPath()  {
	List<String> result = new ArrayList<String>();
	try {
	    result = ops.listMatchedRelPath(dir, pattern, isGlob);
	} catch (IOException e) {
	    log.error(e.getMessage(), e);
	}
        assertEquals(3, result.size());
    } 
    
    @Test
    public void testCopyMatchedFiles() {
        List<String> result = new ArrayList<String>();
	try {
	    result = ops.copyMatchedFiles(dir, copyDir, pattern, isGlob);
	} catch (IOException e) {
	    log.error(e.getMessage(), e);
	} 
        assertEquals(3, result.size());
    }
}
