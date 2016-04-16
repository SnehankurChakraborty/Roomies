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

import android.view.View;

/**
 * Created by Snehankur on 12/13/2015.
 */
public class IllegalViewNameException extends IllegalStateException {
    public IllegalViewNameException(String name) {
        super(name + " does not exists in current layout");
    }

    public IllegalViewNameException(View view, String requiredComponentName) {
        super(view.toString() + " cannot be casted to " + requiredComponentName);
    }
}
