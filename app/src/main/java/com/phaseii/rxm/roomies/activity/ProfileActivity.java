package com.phaseii.rxm.roomies.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.view.AlphaForegroundColorSpan;
import com.phaseii.rxm.roomies.view.RoomiesScrollView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.APP_ERROR;

public class ProfileActivity extends ActionBarActivity {

	private static final int REQUEST_CODE = 1;
	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private SpannableString mSpannableString;
	private ImageView coloredBackgroundView;
	private int lastTopValueAssigned = 0;
	private Bitmap bitmap;
	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		coloredBackgroundView = (ImageView) findViewById(
				R.id.colored_background_view);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		String username = getSharedPreferences(RoomiesConstants.ROOM_INFO_FILE_KEY,
				MODE_PRIVATE).getString(RoomiesConstants.NAME, null);
		Bitmap bitmap = BitmapFactory.decodeFile(new File(getFilesDir(),
				username + getResources().getString(
						R.string.PROFILEJPG)).getAbsolutePath(), options);
		if (null != bitmap) {
			coloredBackgroundView.setImageBitmap(bitmap);
			coloredBackgroundView.setScaleType(ImageView.ScaleType.FIT_XY);
		}

		coloredBackgroundView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});

		mSpannableString = new SpannableString("Profile");
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xFFFFFF);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Profile");
		setSupportActionBar(toolbar);
		final ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.primaryColor));
		cd.setAlpha(0);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("");
		actionBar.setBackgroundDrawable(cd);
		actionBar.setHomeAsUpIndicator(R.drawable.ic_roomies_back);

		RoomiesScrollView scrollViewHelper = (RoomiesScrollView) findViewById(
				R.id.scrollViewHelper);
		scrollViewHelper.setOnScrollViewListener(new RoomiesScrollView.OnScrollViewListener() {
			@Override
			public void onScrollChanged(RoomiesScrollView v, int l, int t, int oldl, int oldt) {
				setTitleAlpha(255 - getAlphaforActionBar(v.getScrollY()));
				cd.setAlpha(getAlphaforActionBar(v.getScrollY()));
				parallaxImage(coloredBackgroundView);
			}

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			private void parallaxImage(View view) {
				Rect rect = new Rect();
				view.getLocalVisibleRect(rect);
				if (lastTopValueAssigned != rect.top) {
					lastTopValueAssigned = rect.top;
					view.setY((float) (rect.top / 2.0));
				}
			}

			private int getAlphaforActionBar(int scrollY) {
				int minDist = 0, maxDist = 350;
				if (scrollY > maxDist) {
					return 255;
				} else {
					if (scrollY < minDist) {
						return 0;
					} else {
						return (int) ((255.0 / maxDist) * scrollY);
					}
				}
			}

		});
		setUpProfile(username);
	}

	private void setTitleAlpha(float alpha) {
		if (alpha < 1) {
			alpha = 1;
		}
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(mSpannableString);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && null != data)
			try {
				if (bitmap != null) {
					bitmap.recycle();
				}
				InputStream stream = getContentResolver().openInputStream(data.getData());
				bitmap = BitmapFactory.decodeStream(stream);
				stream.close();
				coloredBackgroundView.setImageBitmap(bitmap);
				coloredBackgroundView.setScaleType(ImageView.ScaleType.FIT_XY);
				String username = getSharedPreferences(RoomiesConstants
						.ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).
						getString(RoomiesConstants.NAME, null);

				File file = new File(getFilesDir(),
						username + getResources().getString(R.string.PROFILEJPG));
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				file.getAbsolutePath();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setUpProfile(String username) {
		TextView name = (TextView) findViewById(R.id.saved_name);
		name.setText(username);
		TextView mail = (TextView) findViewById(R.id.saved_mail);
		mail.setText(getSharedPreferences(RoomiesConstants.ROOM_INFO_FILE_KEY,
				MODE_PRIVATE).getString(RoomiesConstants.EMAIL_ID, null));
		makeEditable("name");
		makeEditable("email");
		makeEditable("room_alias");
		makeEditable("no_of_members");
		makeEditable("rent");
		makeEditable("maid");
		makeEditable("electricity");
		makeEditable("misc");
	}

	private void makeEditable(String feildId) {
		Resources resources = getResources();
		String packageName = getPackageName();
		int resId = resources.getIdentifier(feildId, "id", packageName);
		int resEditId = resources.getIdentifier(feildId + "_edit", "id", packageName);
		int resEditButtonId = resources.getIdentifier(feildId + "_edit_button", "id",
				packageName);
		final TextView name = (TextView) findViewById(resId);
		final EditText name_edit = (EditText) findViewById(resEditId);
		final Button name_edit_button = (Button) findViewById(resEditButtonId);
		name_edit_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (name_edit_button.getText().equals("Edit")) {
					name.setVisibility(View.INVISIBLE);
					name_edit.setVisibility(View.VISIBLE);
					name_edit_button.setText("Save");
				} else {
					name.setVisibility(View.VISIBLE);
					name_edit.setVisibility(View.INVISIBLE);
					name_edit_button.setText("Edit");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_demo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try {
			RoomiesHelper.startActivityHelper(this,
					getResources()
							.getString(R.string.HomeScreen_Activity), null, true);
		} catch (RoomXpnseMngrException e) {
			RoomiesHelper.createToast(this, APP_ERROR, mToast);
			System.exit(0);
		}
	}
}
