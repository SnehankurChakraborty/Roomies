package com.phaseii.rxm.roomies.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.activity.HomeScreenActivity;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.BlankFragment;
import com.phaseii.rxm.roomies.fragments.HomeFragment;
import com.phaseii.rxm.roomies.fragments.RoommatesFragment;
import com.phaseii.rxm.roomies.util.ActivityUtils;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;

/**
 * Created by Snehankur on 4/4/2015.
 */
public class RoomiesNavigationDrawerAdapter
        extends RecyclerView.Adapter<RoomiesNavigationDrawerAdapter.ViewHolder>
        implements View.OnClickListener {

    private Context mContext;
    private View headerView;
    private DrawerLayout mDrawerLayout;
    private HomeFragment homeFragment;
    private RoommatesFragment roommateFragment;
    private BlankFragment blankFragment;
    private Map<ActivityUtils.Extras, List<? extends Parcelable>> fragmentArgs;

    /**
     *
     * @param Titles
     * @param Icons
     * @param Name
     * @param Email
     * @param Profile
     * @param mContext
     * @param mDrawerLayout
     * @param fragmentArgs
     */
    public RoomiesNavigationDrawerAdapter(String Titles[], int Icons[], String Name,
                                          String Email, int Profile, Context mContext,
                                          DrawerLayout mDrawerLayout,
                                          Map<ActivityUtils.Extras, List<? extends Parcelable>>
                                                  fragmentArgs) {
        this.mNavTitles = Titles;
        this.mIcons = Icons;
        this.name = Name;
        this.email = Email;
        this.profile = Profile;
        this.mContext = mContext;
        this.mDrawerLayout = mDrawerLayout;
        this.fragmentArgs = fragmentArgs;
        this.homeFragment = HomeFragment.newInstance();
        this.roommateFragment = RoommatesFragment.newInstance();
        this.blankFragment = BlankFragment.newInstance();
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    private int mIcons[];
    private String name;
    private int profile;
    private String email;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    List<View> itemViewList = new ArrayList<>();

    @Override
    public void onClick(View v) {

    }


    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        int holderId;
        TextView textView;
        ImageView imageView;
        ImageView profileFrame;
        TextView name;
        TextView email;
        View itemView;

        /**
         *
         * @param itemView
         * @param ViewType
         * @param mContext
         */
        public ViewHolder(final View itemView, int ViewType, final Context mContext) {
            super(itemView);
            this.itemView = itemView;
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                Toast mToast;

                /**
                 *
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    int pos = getPosition();
                    for (View item : itemViewList) {
                        item.setSelected(false);
                    }
                    itemView.setSelected(true);
                    switch (pos) {
                        case 1:
                            if (null != mContext.getSharedPreferences(RoomiesConstants
                                    .PREF_ROOMIES_KEY, Context.MODE_PRIVATE).getString
                                    (RoomiesConstants.PREF_ROOM_ID, null)) {
                                ActivityUtils.replaceFragmentWithBundle((HomeScreenActivity)
                                        mContext, homeFragment, R.id
                                        .home_screen_fragment_layout, fragmentArgs);

                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(BlankFragment.PARENT_FRAGMENT, BlankFragment
                                        .HOME_FRAGMENT);
                                blankFragment.setParceableBundle(bundle);
                                ActivityUtils.replaceFragmentWithBundle((HomeScreenActivity)
                                        mContext, blankFragment, R.id
                                        .home_screen_fragment_layout, null);
                            }
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        case 2:
                            if (null != mContext.getSharedPreferences(RoomiesConstants
                                    .PREF_ROOMIES_KEY, Context.MODE_PRIVATE).getString
                                    (RoomiesConstants.PREF_USER_ID, null)) {
                                ActivityUtils.replaceFragmentWithBundle((HomeScreenActivity)
                                                mContext, roommateFragment, R.id
                                                .home_screen_fragment_layout,
                                        fragmentArgs);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(BlankFragment.PARENT_FRAGMENT, BlankFragment
                                        .ROOMMATE_FRAGMENT);
                                blankFragment.setParceableBundle(bundle);
                                ActivityUtils.replaceFragmentWithBundle((HomeScreenActivity)
                                        mContext, blankFragment, R.id
                                        .home_screen_fragment_layout, null);
                            }
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        case 3:
                            try {
                                ActivityUtils.startActivityHelper(mContext, mContext.getResources()
                                        .getString(R.string.ProfileActivity), null, false);
                            } catch (RoomXpnseMngrException e) {
                                ToastUtils.createToast(mContext, RoomiesConstants.APP_ERROR,
                                        mToast);
                            }
                            break;
                        case 4:
                            ((HomeScreenActivity) mContext).shareApp();
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        case 5:
                            ((HomeScreenActivity) mContext).goToMarket();
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        case 6:
                            ((HomeScreenActivity) mContext).sendEmail();
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        case 7:
                            break;
                        case 8:
                            SharedPreferences mSharedPref = mContext.getSharedPreferences(
                                    PREF_ROOMIES_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor mEditor = mSharedPref.edit();
                            mEditor.clear();
                            mEditor.apply();
                            try {
                                ((HomeScreenActivity) mContext).revokeGplusAccess();
                                LoginManager.getInstance().logOut();
                                ActivityUtils.startActivityHelper(mContext,
                                        mContext.getResources()
                                                .getString(R.string.HomeScreen_Activity), null,
                                        true);

                            } catch (RoomXpnseMngrException e) {
                                ToastUtils.createToast(mContext, APP_ERROR, mToast);
                                System.exit(0);
                            }
                            break;
                    }
                }
            });
            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                holderId = 1;
            } else {
                name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                profileFrame = (ImageView) itemView.findViewById(R.id.profileFrame);
                final String username = mContext.getSharedPreferences(RoomiesConstants
                        .ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).
                        getString(RoomiesConstants.NAME, null);
                profileFrame.setOnClickListener(new View.OnClickListener() {
                    Toast mToast = null;

                    @Override
                    public void onClick(View v) {
                        try {
                            ActivityUtils.startActivityHelper(mContext, mContext.getResources()
                                    .getString(R.string.ProfileActivity), null, false);
                        } catch (RoomXpnseMngrException e) {
                            ToastUtils.createToast(mContext, RoomiesConstants.APP_ERROR, mToast);
                        }
                    }
                });

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(new File(mContext.getFilesDir(),
                        username + mContext.getResources().getString(
                                R.string.PROFILEJPG)).getAbsolutePath(), options);
                ((HomeScreenActivity) mContext).updateProfilePic(bitmap);
                holderId = 0;
            }
        }

    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.drawer_list_item, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType, parent.getContext());
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            headerView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.drawer_header, parent, false);
            ViewHolder vhHeader = new ViewHolder(headerView,
                    viewType, parent.getContext());
            return vhHeader;
        }
        return null;
    }

    /**
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (viewHolder.holderId == 1) {
            if (position == 1) {
                viewHolder.itemView.setSelected(true);
            }
            viewHolder.textView.setText(mNavTitles[position - 1]);
            viewHolder.textView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "fonts/VarelaRound-Regular.ttf"), Typeface.BOLD);
            viewHolder.imageView.setImageResource(mIcons[position - 1]);
            itemViewList.add(viewHolder.itemView);
        } else {
            viewHolder.name.setText(name);
            viewHolder.name.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "fonts/VarelaRound-Regular.ttf"), Typeface.BOLD);
            viewHolder.email.setText(email);
            viewHolder.email.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "fonts/VarelaRound-Regular.ttf"));
        }
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mNavTitles.length + 1;
    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    /**
     *
     * @return
     */
    public View getHeaderView() {
        return headerView;
    }

    /**
     *
     * @param position
     * @return
     */
    private boolean isPositionHeader(int position) {
        return position == 0;
    }


}
