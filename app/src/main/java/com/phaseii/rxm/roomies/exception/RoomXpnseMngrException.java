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
 * Created by Snehankur on 3/1/2015.
 */
public class RoomXpnseMngrException extends Exception {

	public static final String RXMEXCEPTION = "ROOMIESEXCEPTION :";

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
