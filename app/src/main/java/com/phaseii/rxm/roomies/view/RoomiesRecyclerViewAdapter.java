package com.phaseii.rxm.roomies.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;

/**
 * Created by Snehankur on 4/4/2015.
 */
public class RoomiesRecyclerViewAdapter
		extends RecyclerView.Adapter<RoomiesRecyclerViewAdapter.ViewHolder> {

	public RoomiesRecyclerViewAdapter(String Titles[], int Icons[], String Name, String Email,
	                                  int Profile) {
		mNavTitles = Titles;
		mIcons = Icons;
		name = Name;
		email = Email;
		profile = Profile;
	}

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	private String mNavTitles[];
	private int mIcons[];
	private String name;
	private int profile;
	private String email;

	public static class ViewHolder extends RecyclerView.ViewHolder {

		int holderId;
		TextView textView;
		ImageView imageView;
		ImageView profile;
		TextView name;
		TextView email;

		public ViewHolder(View itemView, int ViewType) {
			super(itemView);
			if (ViewType == TYPE_ITEM) {
				textView = (TextView) itemView.findViewById(
						R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
				imageView = (ImageView) itemView.findViewById(
						R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
				holderId = 1;                                               // setting holder id as 1 as the object being populated are of type item row
			} else {
				name = (TextView) itemView.findViewById(
						R.id.name);         // Creating Text View object from header.xml for name
				email = (TextView) itemView.findViewById(
						R.id.email);       // Creating Text View object from header.xml for email
				profile = (ImageView) itemView.findViewById(
						R.id.circleView);// Creating Image view object from header.xml for profile pic
				holderId = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
			}
		}


	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_ITEM) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_item,
					parent,
					false); //Inflating the layout
			ViewHolder vhItem = new ViewHolder(v,
					viewType); //Creating ViewHolder and passing the object of type view
			return vhItem; // Returning the created object
			//inflate your layout and pass it to view holder
		} else if (viewType == TYPE_HEADER) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header,
					parent,
					false); //Inflating the layout
			ViewHolder vhHeader = new ViewHolder(v,
					viewType); //Creating ViewHolder and passing the object of type view
			return vhHeader; //returning the object created
		}
		return null;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if (viewHolder.holderId == 1) {                              // as the list view is going
			// to be called after the header view so we decrement the
			// position by 1 and pass it to the holder while setting the text and image
			viewHolder.textView.setText(
					mNavTitles[position - 1]); // Setting the Text with the array of our Titles
			viewHolder.imageView.setImageResource(
					mIcons[position - 1]);// Settimg the image with array of our icons
		} else {

			viewHolder.profile.setImageResource(
					profile);           // Similarly we set the resources for header view
			viewHolder.name.setText(name);
			viewHolder.email.setText(email);
		}
	}


	@Override
	public int getItemCount() {
		return mNavTitles.length + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position))
			return TYPE_HEADER;

		return TYPE_ITEM;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

}
