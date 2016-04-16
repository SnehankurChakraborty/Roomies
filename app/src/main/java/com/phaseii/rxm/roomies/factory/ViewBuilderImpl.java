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

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.phaseii.rxm.roomies.exception.IllegalViewNameException;
import com.phaseii.rxm.roomies.utils.Constants;

/**
 * Created by Snehankur on 12/13/2015.
 */
public class ViewBuilderImpl implements ViewBuilder {

    private Resources resources;
    private String packageName;
    private View layout;

    ViewBuilderImpl(Context mContext, View layout) {
        this.resources = mContext.getResources();
        this.packageName = mContext.getPackageName();
        this.layout = layout;
    }

    @Override
    public Button getButton(String buttonName) throws IllegalViewNameException {
        int buttonId = resources.getIdentifier(buttonName, "id", packageName);
        if (buttonId > 0) {
            View view = layout.findViewById(buttonId);
            if (view instanceof Button) {
                return (Button) view;
            } else {
                throw new IllegalViewNameException(view, Constants.BUTTON);
            }
        } else {
            throw new IllegalViewNameException(buttonName);
        }
    }

    public Button getButton(int buttonId) throws IllegalViewNameException {
        View view = layout.findViewById(buttonId);
        if (view instanceof Button) {
            return (Button) view;
        } else {
            throw new IllegalViewNameException(view, Constants.BUTTON);
        }
    }

    @Override
    public TextView getTextView(String textviewName) throws IllegalViewNameException {
        int textviewId = resources.getIdentifier(textviewName, "id", packageName);
        if (textviewId > 0) {
            View view = layout.findViewById(textviewId);
            if (view instanceof TextView) {
                return (TextView) view;
            } else {
                throw new IllegalViewNameException(view, Constants.TEXTVIEW);
            }
        } else {
            throw new IllegalViewNameException(textviewName);
        }
    }

    @Override
    public TextView getTextView(int textviewId) throws IllegalViewNameException {
        View view = layout.findViewById(textviewId);
        if (view instanceof TextView) {
            return (TextView) view;
        } else {
            throw new IllegalViewNameException(view, Constants.TEXTVIEW);
        }
    }

    @Override
    public EditText getEditText(String edittextName) throws IllegalViewNameException {
        int edittextId = resources.getIdentifier(edittextName, "id", packageName);
        if (edittextId > 0) {
            View view = layout.findViewById(edittextId);
            if (view instanceof EditText) {
                return (EditText) view;
            } else {
                throw new IllegalViewNameException(view, Constants.EDITTEXT);
            }
        } else {
            throw new IllegalViewNameException(edittextName);
        }
    }

    @Override
    public EditText getEditText(int edittextId) throws IllegalViewNameException {
        View view = layout.findViewById(edittextId);
        if (view instanceof EditText) {
            return (EditText) view;
        } else {
            throw new IllegalViewNameException(view, Constants.EDITTEXT);
        }
    }

    @Override
    public ImageView getImageView(String imageViewName) throws IllegalViewNameException {
        int imageviewId = resources.getIdentifier(imageViewName, "id", packageName);
        if (imageviewId > 0) {
            View view = layout.findViewById(imageviewId);
            if (view instanceof ImageView) {
                return (ImageView) view;
            } else {
                throw new IllegalViewNameException(view, Constants.IMAGEVIEW);
            }
        } else {
            throw new IllegalViewNameException(imageViewName);
        }
    }

    @Override
    public ImageView getImageView(int imageviewId) throws IllegalViewNameException {
        View view = layout.findViewById(imageviewId);
        if (view instanceof ImageView) {
            return (ImageView) view;
        } else {
            throw new IllegalViewNameException(view, Constants.IMAGEVIEW);
        }
    }

    @Override
    public ImageButton getImageButton(String imageButtonName) throws IllegalViewNameException {
        int imageButtonId = resources.getIdentifier(imageButtonName, "id", packageName);
        if (imageButtonId > 0) {
            View view = layout.findViewById(imageButtonId);
            if (view instanceof ImageButton) {
                return (ImageButton) view;
            } else {
                throw new IllegalViewNameException(view, Constants.IMAGEBUTTON);
            }
        } else {
            throw new IllegalViewNameException(imageButtonName);
        }
    }

    @Override
    public ImageButton getImageButton(int imageButtonId) throws IllegalViewNameException {
        View view = layout.findViewById(imageButtonId);
        if (view instanceof ImageButton) {
            return (ImageButton) view;
        } else {
            throw new IllegalViewNameException(view, Constants.IMAGEBUTTON);
        }
    }


    @Override
    public View getView(String viewName) throws IllegalViewNameException {
        int viewId = resources.getIdentifier(viewName, "id", packageName);
        if (viewId > 0) {
            return layout.findViewById(viewId);
        } else {
            throw new IllegalViewNameException(viewName);
        }
    }

    @Override
    public View getView(int viewId) throws IllegalViewNameException {
        return layout.findViewById(viewId);
    }

    @Override
    public void setAsToggle(String viewName, boolean isPreSelected) {
        int viewId = resources.getIdentifier(viewName, "id", packageName);
        if (viewId > 0) {
            View view = layout.findViewById(viewId);
            if (isPreSelected) {
                view.setSelected(true);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        v.setSelected(false);
                    } else {
                        v.setSelected(true);
                    }
                }
            });

        } else {
            throw new IllegalViewNameException(viewName);
        }
    }

    @Override
    public void setAsToggle(int viewId, boolean isPreSelected) {
        View view = layout.findViewById(viewId);
        if (isPreSelected) {
            view.setSelected(true);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                } else {
                    v.setSelected(true);
                }
            }
        });
    }
}
