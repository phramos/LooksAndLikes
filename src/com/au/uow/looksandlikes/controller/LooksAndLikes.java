package com.au.uow.looksandlikes.controller;

import android.app.Application;

import com.au.uow.looksandlikes.Look;
import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.UserProfile;
import com.au.uow.looksandlikes.model.RatingModel;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

public class LooksAndLikes extends Application {

	static final String TAG = "Looks&Likes";

	@Override
	public void onCreate() {
		super.onCreate();

        ParseObject.registerSubclass(Look.class);
        ParseObject.registerSubclass(RatingModel.class);
        ParseObject.registerSubclass(UserProfile.class);

		Parse.initialize(this, "LNRiRaziB3sqAU9Txgy8FvLyEOYq4TcGlB1t3DjT",
				"cKhsPSV1sBPsLXUUfrbEE7NFh0Pa7dojQx3HhLsK");

		// Set your Facebook App Id in strings.xml
		ParseFacebookUtils.initialize(getString(R.string.app_id));

	}

}
