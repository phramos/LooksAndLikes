package com.au.uow.looksandlikes;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class LooksAndLikes extends Application {

	static final String TAG = "Looks&Likes";

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "LNRiRaziB3sqAU9Txgy8FvLyEOYq4TcGlB1t3DjT",
				"cKhsPSV1sBPsLXUUfrbEE7NFh0Pa7dojQx3HhLsK");

		// Set your Facebook App Id in strings.xml
		ParseFacebookUtils.initialize(getString(R.string.app_id));

	}

}
