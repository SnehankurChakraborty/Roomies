package com.phaseii.rxm.roomies.logging;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Roomies Logger class to manage all Roomies logging requirements
 *
 * @author Snehankur
 * @date 8/14/2015.
 */
public class RoomiesLogger {

    private static final String INFO = "roomiesInfo";
    private static final String DEBUG = "roomiesDebug";
    private static final String ERROR = "roomiesError";
    private static final String WARN = "roomiesWarn";
    private static final String LOGGING_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss:SS";
    private static final String ENTRY = "ENTRY";
    private static final String EXIT = "EXIT";
    private static RoomiesLogger instance = null;

    public static RoomiesLogger getInstance() {
        if (null == instance) {
            instance = new RoomiesLogger();
        }
        return instance;
    }

    private static String getCurrentTime() {
        return new SimpleDateFormat(LOGGING_DATE_FORMAT).format(new Date());
    }

    public void error(String errorMsg, Exception e) {
        StringBuffer buf = new StringBuffer(errorMsg);
        buf.append("|" + e);
        Log.d(ERROR, buf.toString());
    }

    public void error(String errorMsg) {
        Log.e(ERROR, errorMsg);
    }

    public void info(String infoMsg) {
        Log.i(INFO, infoMsg);
    }

    public void debug(String debugMsg) {
        Log.d(DEBUG, debugMsg);
    }

    public void debug(String classname, String methodName, Object data) {
        String debugMsg = createLoggingMessage(classname, methodName, data);
        Log.d(DEBUG, debugMsg);
    }

    public void warn(String warnMsg) {
        Log.w(WARN, warnMsg);
    }

    public void createEntryLoggingMessage(String classname, String methodName, String message) {

        StringBuffer buf = null;
        String messageString = createLoggingMessage(classname, methodName, null);
        buf = new StringBuffer(messageString);
        buf.append(getCurrentTime() + "|");
        if (null != message) {
            buf.append(message);
        }
        Log.i(ENTRY, buf.toString());
    }

    public void createExitLoggingMessage(String classname, String methodName, String message) {
        StringBuffer buf = null;
        String messageString = createLoggingMessage(classname, methodName, null);
        buf = new StringBuffer(messageString);
        buf.append(getCurrentTime() + "|");
        if (null != message) {
            buf.append(message);
        }
        Log.i(EXIT, buf.toString());
    }

    private String createLoggingMessage(String classname, String methodname,
                                        Object object) {

        StringBuffer buf = new StringBuffer();
        buf.append(classname + "|");
        buf.append(methodname + "|");
        if (null != object) {
            buf.append(object);
        }
        return buf.toString();

    }
}
