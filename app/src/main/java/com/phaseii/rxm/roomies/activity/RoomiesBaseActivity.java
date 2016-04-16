package com.phaseii.rxm.roomies.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.phaseii.rxm.roomies.factory.ViewBuilder;

/**
 * Created by Snehankur on 2/7/2016.
 */
public abstract class RoomiesBaseActivity extends AppCompatActivity {

    public View activityView;
    public ViewBuilder builder;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        configureView(savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureView(savedInstanceState);
    }

    public abstract void configureView(Bundle savedInstanceStat);
}
