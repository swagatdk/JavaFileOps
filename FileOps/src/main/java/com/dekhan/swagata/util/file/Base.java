package com.dekhan.swagata.util.file;

public abstract class Base {

    protected boolean isNullOrEmpty(String str) {
	if (str != null && !str.isEmpty())
	    return false;
	return true;
    }

    protected String getLogMsgPrefix() {
	Thread t = Thread.currentThread();
	return "{" + t.getStackTrace()[2].getMethodName() + "} - ";
    }
}
