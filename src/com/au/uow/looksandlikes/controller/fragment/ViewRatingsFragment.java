package com.au.uow.looksandlikes.controller.fragment;

import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.au.uow.looksandlikes.Look;
import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.adapter.GridViewRatingAdapter;
import com.au.uow.looksandlikes.adapter.RatedImage;
import com.au.uow.looksandlikes.controller.MainActivity;
import com.au.uow.looksandlikes.controller.ViewLookActivity;
import com.au.uow.looksandlikes.utils.ImageUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ViewRatingsFragment extends Fragment {
	private GridView gridView;
	private GridViewRatingAdapter customGridAdapter;
	private ArrayList<RatedImage> ratedImages;
	private ProgressDialog pDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_view_rating, container,
				false);
		gridView = (GridView) v.findViewById(R.id.gridViewRating);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		//ratedImages = getImagesRated();
		new LoadImages().execute();
//		customGridAdapter = new GridViewRatingAdapter(getActivity(),
//				R.layout.rating_grid_row, ratedImages);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				RatedImage ratedImage = ratedImages.get(position);
				bundle.putString("imageURL", ratedImage.getUrl());
				bundle.putLong("rating", ratedImage.getRating());
				bundle.putInt("totalVotes", ratedImage.getTotalVotes());
				bundle.putString("title", ratedImage.getTitle());

				intent.setClass(getActivity(), ViewLookActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
//		gridView.setAdapter(customGridAdapter);

	}

	private ArrayList<RatedImage> getImagesRated() {
		ArrayList<RatedImage> ratedImages = new ArrayList<RatedImage>();

		for (Look look : getLooks(ParseUser.getCurrentUser())) {
			Bitmap image = ImageUtils.getBitmapFromParseFile(look
					.getPhotoFile());
			Double rating = look.getRating();
			DecimalFormat format = new DecimalFormat("#0");
			String ratingString = format.format(Math.round(rating));
			String url = look.getPhotoFile().getUrl();
			ratedImages.add(new RatedImage(image, ratingString, Math
					.round(rating), look.getTotalVotes(), url));
		}
		return ratedImages;
	}

	private List<Look> getLooks(ParseUser user) {
		List<Look> looks = null;
		ParseQuery<Look> query = ParseQuery.getQuery(Look.class);
		query.whereEqualTo("author", user);
		try {
			// ProgressDialog pDialog = new ProgressDialog(getActivity());

			looks = query.find();
			// pDialog.dismiss();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return looks;
	}

	private class LoadImages extends
			AsyncTask<String, String, ArrayList<RatedImage>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading looks....");
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();
		}

		protected ArrayList<RatedImage> doInBackground(String... args) {
			ArrayList<RatedImage> ratedImages = null;
			try {
				ratedImages = getImagesRated();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ratedImages;
		}

		protected void onPostExecute(ArrayList<RatedImage> images) {
			if (images != null) {
				ratedImages = images;
				customGridAdapter = new GridViewRatingAdapter(getActivity(),
						R.layout.rating_grid_row, ratedImages);
				gridView.setAdapter(customGridAdapter);
				pDialog.dismiss();
			} else {
				pDialog.dismiss();
				Toast.makeText(getActivity(), "Network Error",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
