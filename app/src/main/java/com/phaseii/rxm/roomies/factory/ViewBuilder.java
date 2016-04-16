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

package com.phaseii.rxm.roomies.factory;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.phaseii.rxm.roomies.exception.IllegalViewNameException;

/**
 * Created by Snehankur on 12/13/2015.
 */
public interface ViewBuilder {
    Button getButton(String buttonName) throws IllegalViewNameException;

    Button getButton(int buttonId) throws IllegalViewNameException;

    TextView getTextView(String textviewName) throws IllegalViewNameException;

    EditText getEditText(String edittextName) throws IllegalViewNameException;

    ImageView getImageView(String imageViewName) throws IllegalViewNameException;

    ImageButton getImageButton(String imageButtonName) throws IllegalViewNameException;

    ImageButton getImageButton(int imageButtonId) throws IllegalViewNameException;

    View getView(String viewName) throws IllegalViewNameException;

    TextView getTextView(int textviewId) throws IllegalViewNameException;

    EditText getEditText(int edittextId) throws IllegalViewNameException;

    ImageView getImageView(int imageViewId) throws IllegalViewNameException;

    View getView(int viewId) throws IllegalViewNameException;

    void setAsToggle(String viewName, boolean isPreSelected) throws IllegalViewNameException;

    void setAsToggle(int viewId, boolean isPreSelected) throws IllegalViewNameException;
}
