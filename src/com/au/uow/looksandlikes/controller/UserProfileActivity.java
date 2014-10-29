package com.au.uow.looksandlikes.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.UserProfile;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UserProfileActivity extends Activity {

	private ProfilePictureView userProfilePictureFacebookView;
    private ImageView userProfilePictureView;
	private TextView userNameView;
	private TextView userLocationView;
	private TextView userGenderView;
	private TextView userEmailView;
	private Button logoutButton;
    private UserProfile currentUserProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_userprofile);

        userProfilePictureFacebookView = (ProfilePictureView) findViewById(R.id.userProfilePictureFacebook);
        userProfilePictureView = (ImageView) findViewById(R.id.userProfilePicture);
		userNameView = (TextView) findViewById(R.id.userName);
		userLocationView = (TextView) findViewById(R.id.userLocation);
		userGenderView = (TextView) findViewById(R.id.userGender);
		userEmailView = (TextView) findViewById(R.id.userEmail);

		logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogoutButtonClicked();
			}
		});

        ParseQuery<UserProfile> query = ParseQuery.getQuery(UserProfile.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        List<UserProfile> userProfiles = null;
        try {
            userProfiles = query.find();

            if (userProfiles.size() > 0) {
                currentUserProfile = userProfiles.get(0);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void onResume() {
		super.onResume();

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// Check if the user is currently logged
			// and show any cached content
			updateViewsWithProfileInfo();
		} else {
			// If the user is not logged in, go to the
			// activity showing the login view.
			startLoginActivity();
		}
	}

	private void updateViewsWithProfileInfo() {
        if (currentUserProfile.getFacebookId() != null) {
            userProfilePictureFacebookView.setProfileId(currentUserProfile.getFacebookId());
        } else {
            if(currentUserProfile.getProfileImage() != null) {
                userProfilePictureFacebookView.setVisibility(View.INVISIBLE);
                userProfilePictureView.setVisibility(View.VISIBLE);
                try {
                    Bitmap bmp = BitmapFactory.decodeByteArray(currentUserProfile.getProfileImage().getData(), 0, currentUserProfile.getProfileImage().getData().length);
                    userProfilePictureView.setImageBitmap(bmp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                // Show the default, blank user profile picture
                userProfilePictureFacebookView.setProfileId(null);
            }
        }
        if (currentUserProfile.getName() != null) {
            userNameView.setText(currentUserProfile.getName());
        } else {
            userNameView.setText("");
        }
        if (currentUserProfile.getLocation() != null) {
            userLocationView.setText(currentUserProfile.getLocation());
        } else {
            userLocationView.setText("");
        }
        if (currentUserProfile.getGender() != null) {
            userGenderView.setText(currentUserProfile.getGender());
        } else {
            userGenderView.setText("");
        }
        if (currentUserProfile.getEmail() != null) {
            userEmailView.setText(currentUserProfile.getEmail());
        } else {
            userEmailView.setText("");
        }
	}

	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();

		// Go to the login view
		startLoginActivity();
	}

	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
