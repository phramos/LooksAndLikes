package com.au.uow.looksandlikes.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.au.uow.looksandlikes.R;

public class GridViewRatingAdapter extends ArrayAdapter<RatedImage> {
	private Context context;
	private int layoutResourceId;
	private ArrayList<RatedImage> data = new ArrayList<RatedImage>();

	public GridViewRatingAdapter(Context context, int layoutResourceId,
			ArrayList<RatedImage> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.imageTitle = (TextView) row.findViewById(R.id.textViewAvgRating);
			holder.image = (ImageView) row.findViewById(R.id.imageRated);
			holder.star = (ImageView) row.findViewById(R.id.imageStar);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		RatedImage item = data.get(position);
		holder.imageTitle.setText(item.getTitle());
		holder.image.setImageBitmap(item.getImage());
		//define the color of the star based on the rating of the photo
		if (item.getRating()<4){
			holder.star.setImageResource(R.drawable.ic_star_red);
		}else if (item.getRating()<7){
			holder.star.setImageResource(R.drawable.ic_star_yellow);
		}else{
			holder.star.setImageResource(R.drawable.ic_star_green);
		}
		return row;
	}

	static class ViewHolder {
		TextView imageTitle;
		ImageView image;
		ImageView star;
	}
}