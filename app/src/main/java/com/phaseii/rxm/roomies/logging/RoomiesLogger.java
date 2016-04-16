/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    /**
     *
     * @return
     */
    public static RoomiesLogger getInstance() {
        if (null == instance) {
            instance = new RoomiesLogger();
        }
        return instance;
    }

    /**
     *
     * @return
     */
    private static String getCurrentTime() {
        return new SimpleDateFormat(LOGGING_DATE_FORMAT).format(new Date());
    }

    /**
     *
     * @param errorMsg
     * @param e
     */
    public void error(String errorMsg, Exception e) {
        StringBuffer buf = new StringBuffer(errorMsg);
        buf.append("|" + e);
        Log.d(ERROR, buf.toString());
    }

    /**
     *
     * @param errorMsg
     */
    public void error(String errorMsg) {
        Log.e(ERROR, errorMsg);
    }

    /**
     *
     * @param infoMsg
     */
    public void info(String infoMsg) {
        Log.i(INFO, infoMsg);
    }

    /**
     *
     * @param debugMsg
     */
    public void debug(String debugMsg) {
        Log.d(DEBUG, debugMsg);
    }

    /**
     *
     * @param classname
     * @param methodName
     * @param data
     */
    public void debug(String classname, String methodName, Object data) {
        String debugMsg = createLoggingMessage(classname, methodName, data);
        Log.d(DEBUG, debugMsg);
    }

    /**
     *
     * @param warnMsg
     */
    public void warn(String warnMsg) {
        Log.w(WARN, warnMsg);
    }

    /**
     *
     * @param classname
     * @param methodName
     * @param message
     */
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

    /**
     *
     * @param classname
     * @param methodName
     * @param message
     */
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

    /**
     *
     * @param classname
     * @param methodname
     * @param object
     * @return
     */
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
