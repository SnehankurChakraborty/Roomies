package com.phaseii.rxm.roomies.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.activity.HomeScreenActivity;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ProfileFragment extends RoomiesFragment {


	Context mContext;
	static String username;

	public static ProfileFragment geInstance(String username) {
		ProfileFragment.username = username;
		return new ProfileFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		mContext = getActivity().getBaseContext();


		return rootView;
	}

	@Override
	public View getFragmentView() {
		return rootView;
	}


}
