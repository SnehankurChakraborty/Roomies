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
