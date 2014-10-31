package com.au.uow.looksandlikes.controller.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.au.uow.looksandlikes.Look;
import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.adapter.GridViewRatingAdapter;
import com.au.uow.looksandlikes.adapter.RatedImage;
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
	private ProgressDialog progressDialog;
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
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		new LoadLookToBeRated().execute();
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
		look.updateRatingAverage(rating + 0.0);
		try {
			ratingModel.save();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new LoadLookToBeRated().execute();
		ratingText.setVisibility(View.GONE);
	}

	@Override
	public void onCancelRating() {
		// Not Rated
	}

	private void changeImageToBeRated() {
		if (look != null) {
			// put the new image on the view
			imageView.setImageBitmap(ImageUtils.getBitmapFromParseFile(look
					.getPhotoFile()));
		} else {
			// Disable the rating starts and show msg
			ratingText.setVisibility(View.VISIBLE);
			imageView.setImageBitmap(null);
			// starBar.setVisibility(View.GONE);
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

	private class LoadLookToBeRated extends
			AsyncTask<String, String, Look> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog= new ProgressDialog(getActivity());
			progressDialog.setMessage("Loading image to be rated....");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		protected Look doInBackground(String... args) {
			ArrayList<RatedImage> ratedImages = null;
			Look look = null;
			try {
				look = getLookNotRatedBy(ParseUser.getCurrentUser());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return look;
		}

		protected void onPostExecute(Look l) {
			if (l!=null){
			look = l;
			changeImageToBeRated();
			progressDialog.dismiss();
			}else {
				progressDialog.dismiss();
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setTitle("No images to be rated");
				alertDialogBuilder.setMessage("No imagens to be rated, try again later");
				alertDialogBuilder.setCancelable(false	);
				alertDialogBuilder.setPositiveButton("Close", null);
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			}
		}
	}
}
