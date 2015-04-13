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
	private static final int REQUEST_CODE = 1;
	private Bitmap bitmap;
	ImageView profilePic;
	Context mContext;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		profilePic = (ImageView) rootView.findViewById(R.id.profilepic);
		profilePic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		mContext = getActivity().getBaseContext();
		return rootView;
	}

	@Override
	public View getFragmentView() {
		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && null != data)
			try {
				if (bitmap != null) {
					bitmap.recycle();
				}
				InputStream stream = getActivity().getContentResolver()
						.openInputStream(data.getData());
				bitmap = BitmapFactory.decodeStream(stream);
				stream.close();
				profilePic.setImageBitmap(bitmap);
				String username = mContext.getSharedPreferences(RoomiesConstants
						.ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).
						getString(RoomiesConstants.NAME, null);

				File file = new File(mContext.getFilesDir(),
						username + mContext.getResources().getString(R.string.PROFILEJPG));
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				file.getAbsolutePath();
				/*ContextWrapper cw = new ContextWrapper(mContext);
				File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
				File mypath = new File(directory,
						username + mContext.getResources().getString(R.string.PROFILEJPG));
				FileOutputStream fos = new FileOutputStream(mypath);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();*/
				/*RoomiesConstants.imagePath=mypath.getAbsolutePath();
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap bitmap = BitmapFactory.decodeFile(RoomiesConstants.imagePath, options);*/

				((HomeScreenActivity) getActivity()).updateProfilePic(bitmap,R.drawable.ic_profile_pic);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
