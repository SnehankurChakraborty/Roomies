package com.phaseii.rxm.roomies.exception;

import android.util.Log;

/**
 * Created by Snehankur on 3/1/2015.
 */
public class RoomXpnseMngrException extends Exception {

	public static final String RXMEXCEPTION = "RXMEXCEPTION :";

	public RoomXpnseMngrException(Throwable t) {
		super(t);
		Log.e(RXMEXCEPTION, t.getMessage());
	}

	public RoomXpnseMngrException(String msg) {
		super(msg);
		Log.e(RXMEXCEPTION, msg);
	}

	public RoomXpnseMngrException(String msg, Throwable t) {
		super(msg, t);
		Log.e(RXMEXCEPTION, msg, t);
	}

	public RoomXpnseMngrException() {
		super();
		Log.e(RXMEXCEPTION, "");
	}
}
