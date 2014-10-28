package com.au.uow.looksandlikes;

import com.au.uow.looksandlikes.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavRowAdapter extends BaseAdapter {
	private Context context;
	private String options[];
	int[] images = { R.drawable.ic_rate, R.drawable.ic_camera, R.drawable.ic_camera };

	public NavRowAdapter(Context context) {
		this.context = context;
		options = context.getResources().getStringArray(
				R.array.navDrawerOptions);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return options.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return options[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.nav_row, parent, false);

		} else {
			row=convertView;
		}
		TextView textView = (TextView) row.findViewById(R.id.navRowTextView);
		ImageView imageView = (ImageView) row.findViewById(R.id.navRowImageView);
		
		//Seting the content into view
		textView.setText(options[position]);
		imageView.setImageResource(images[position]);
		return row;
	}

}
