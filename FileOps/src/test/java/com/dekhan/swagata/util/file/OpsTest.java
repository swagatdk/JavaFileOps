package com.dekhan.swagata.util.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple Ops.
 */
@TestMethodOrder(OrderAnnotation.class)
public class OpsTest {

    private static Logger log = LogManager.getLogger(com.dekhan.swagata.util.file.OpsTest.class);
    private static Ops ops;

    private static String dir = ".";
    private static String copyDir = "D:\\temp\\test\\copy\\";
    private static String pattern = "*.prefs";
    private static boolean isGlob = true;

    @BeforeAll
    public static void initOps() {
	ops = new Ops();
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
    public void testListMatchedAbsPath() {
	List<String> result = new ArrayList<String>();
	try {
	    result = ops.listMatchedAbsPath(dir, pattern, isGlob);
	} catch (IOException e) {
	    log.error(e.getMessage(), e);
	}
	assertEquals(3, result.size());
    }

    @Test
    @Order(2)
    public void testListMatchedRelPath() {
	List<String> result = new ArrayList<String>();
	try {
	    result = ops.listMatchedRelPath(dir, pattern, isGlob);
	} catch (IOException e) {
	    log.error(e.getMessage(), e);
	}
	assertEquals(3, result.size());
    }

    @Test
    @Order(3)
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
