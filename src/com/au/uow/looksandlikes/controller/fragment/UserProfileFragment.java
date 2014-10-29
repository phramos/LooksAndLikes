package com.au.uow.looksandlikes.controller.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.UserProfile;
import com.au.uow.looksandlikes.controller.LoginActivity;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserProfileFragment extends Fragment {

	private ProfilePictureView userProfilePictureFacebookView;
    private ImageView userProfilePictureView;
	private TextView userNameView;
	private TextView userLocationView;
	private TextView userGenderView;
	private TextView userEmailView;
	private Button logoutButton;
    private UserProfile currentUserProfile;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_userprofile, container, false);

        userProfilePictureFacebookView = (ProfilePictureView) v.findViewById(R.id.userProfilePictureFacebook);
        userProfilePictureView = (ImageView) v.findViewById(R.id.userProfilePicture);
		userNameView = (TextView) v.findViewById(R.id.userName);
		userLocationView = (TextView) v.findViewById(R.id.userLocation);
		userGenderView = (TextView) v.findViewById(R.id.userGender);
		userEmailView = (TextView) v.findViewById(R.id.userEmail);

		logoutButton = (Button) v.findViewById(R.id.logoutButton);
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
        return v;
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
		Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
