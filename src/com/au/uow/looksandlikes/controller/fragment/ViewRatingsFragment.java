package com.au.uow.looksandlikes.controller.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.au.uow.looksandlikes.Look;
import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.adapter.GridViewRatingAdapter;
import com.au.uow.looksandlikes.adapter.RatedImage;
import com.au.uow.looksandlikes.utils.ImageUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ViewRatingsFragment extends Fragment {
	private GridView gridView;
	private GridViewRatingAdapter customGridAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_view_rating, container,
				false);
		
		gridView = (GridView) v.findViewById(R.id.gridViewRating);
		customGridAdapter = new GridViewRatingAdapter(getActivity(), R.layout.rating_grid_row, getImagesRated());
		gridView.setAdapter(customGridAdapter);
		return v;
	}
	
	private ArrayList<RatedImage> getImagesRated(){
		ArrayList<RatedImage> ratedImages = new ArrayList<RatedImage>();
		
		for (Look look : getLooks(ParseUser.getCurrentUser())) {
			Bitmap image = ImageUtils.getBitmapFromParseFile(look.getPhotoFile());
			String rating = look.getRating();
			ratedImages.add(new RatedImage(image, rating));
		}
		return ratedImages;
	}
	
	private List<Look> getLooks(ParseUser user){
		List<Look> looks = null;
		ParseQuery<Look> query = ParseQuery.getQuery(Look.class);
		query.whereEqualTo("author", user);
		try {
			looks = query.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return looks;
	}
}
