package com.phaseii.rxm.roomies.exception;

import android.util.Log;

/**
 * Created by Snehankur on 4/10/2015.
 */
public class RoomiesStateException extends RuntimeException {
	public static final String ROOMIESEXCEPTION = "ROOMIESEXCEPTION :";

	public RoomiesStateException(Throwable t) {
		super(t);
		Log.e(ROOMIESEXCEPTION, t.getMessage());
	}

	public RoomiesStateException(String msg) {
		super(msg);
		Log.e(ROOMIESEXCEPTION, msg);
	}

	public RoomiesStateException(String msg, Throwable t) {
		super(msg, t);
		Log.e(ROOMIESEXCEPTION, msg, t);
	}

	public RoomiesStateException() {
		super();
		Log.e(ROOMIESEXCEPTION, "");
	}
}
