package com.au.uow.looksandlikes.controller.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.au.uow.looksandlikes.Look;
import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.model.RatingModel;
import com.au.uow.looksandlikes.utils.ImageUtils;
import com.badoo.mobile.views.starbar.StarBar;
import com.badoo.mobile.views.starbar.StarBar.OnRatingSliderChangeListener;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class RatingFragment extends Fragment implements
		OnRatingSliderChangeListener {

	private TextView ratingText;
	private ImageView imageView;
	private StarBar starBar;
	Look look;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater
				.inflate(R.layout.fragment_rate_look, container, false);

		StarBar starBar = (StarBar) v.findViewById(R.id.starBar);
		;
		starBar.setOnRatingSliderChangeListener(this);

		ratingText = (TextView) v.findViewById(R.id.rating);
		imageView = (ImageView) v.findViewById(R.id.imageToBeRated);
		starBar = (StarBar) v.findViewById(R.id.starBar);
		changeImageToBeRated();
		return v;
	}

	@Override
	public boolean onStartRating() {
		ratingText.setVisibility(View.VISIBLE);
		return true;
	}

	@Override
	public void onPendingRating(int rating) {
		ratingText.setText(Integer.toString(rating));
	}

	@Override
	public void onFinalRating(int rating, boolean swipe) {
		RatingModel ratingModel = new RatingModel(ParseUser.getCurrentUser(),
				look, rating);
		try {
			ratingModel.save();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		changeImageToBeRated();
		ratingText.setVisibility(View.GONE);
	}

	@Override
	public void onCancelRating() {
		// Not Rated
	}

	private void changeImageToBeRated() {
		look = getLookNotRatedBy(ParseUser.getCurrentUser());
		if (look != null) {
			//put the new image on the view
			imageView.setImageBitmap(ImageUtils.getBitmapFromParseFile(look
					.getPhotoFile()));
		}else {
			//Disable the rating starts and show msg
			ratingText.setVisibility(View.VISIBLE);
			imageView.setImageBitmap(null);
			//starBar.setVisibility(View.GONE);
		}
	}

	private Look getLookNotRatedBy(ParseUser user) {
		ParseQuery<Look> query = ParseQuery.getQuery(Look.class);
		ParseQuery<RatingModel> queryRating = ParseQuery
				.getQuery(RatingModel.class);
		List<RatingModel> ratings;
		List<String> votedLooks = new ArrayList<String>();
		Look look = null;

		try {
			// Take all the Looks already voted by the current user
			queryRating.whereEqualTo("voter", ParseUser.getCurrentUser());
			ratings = queryRating.find();
			for (RatingModel ratingModel : ratings) {
				votedLooks.add(ratingModel.getLook().getObjectId());
			}

			// exclude all the looks published by this user and the looks
			// already voted by this user
			query.whereNotEqualTo("author", user);
			query.whereNotContainedIn("objectId", votedLooks);

			look = query.getFirst();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return look;
	}
}
