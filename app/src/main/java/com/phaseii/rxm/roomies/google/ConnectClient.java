package com.phaseii.rxm.roomies.google;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Snehankur on 2/15/2016.
 */
public class ConnectClient extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    public static final int RC_SIGN_IN = 0;
    public GoogleApiClient mGoogleApiClient;
    public Toast mToast;
    public Context mContext;
    public GoogleSignInOptions googleSignInOptions;
    public OnConnectListener onConnectListener;
    private ProgressDialog mProgressDialog;

    /**
     * Private constructor to prevent from being initialized
     */
    private ConnectClient(@NonNull Context context) {
        this.mContext = context;
        /** Configure sign-in to request the user's ID, email address, and basic
         profile. ID and basic profile are included in DEFAULT_SIGN_IN**/
        googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();


        /** Build a GoogleApiClient with access to the Google Sign-In API and the
         options specified by googleSignInOptions**/
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage((FragmentActivity) mContext /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    public static ConnectClient getInstance(Context context) {
        return new ConnectClient(context);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
        mContext.startActivity(intent);
    }

    public void signoutGplus() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    public void revokeGplusAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    public void signinGPlus(@NonNull OnConnectListener onConnectListener) {
        this.onConnectListener = onConnectListener;
        ((AppCompatActivity) mContext)
                .startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient),
                        RC_SIGN_IN);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                onConnectListener.onConnect(result);
            } else if (resultCode == RESULT_CANCELED) {
                onConnectListener.onFail();
            }
        }
    }

    public void loginGPlus() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi
                .silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                }
            });
        }
    }

    public interface OnConnectListener {
        public void onConnect(GoogleSignInResult result);

        public void onFail();
    }
}
