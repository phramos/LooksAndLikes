/**
 * 
 */
package com.au.uow.looksandlikes.controller;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.utils.ImageUtils;

/**
 * @author Pedro Henrique Ramos Souza
 * 
 */
public class ViewLookActivity extends Activity {
	ImageView img;
	Bitmap bitmap;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_look);
		img = (ImageView) findViewById(R.id.imageViewShowImage);
		Bundle bundle = getIntent().getExtras();
		String url = bundle.getString("imageURL");
		new LoadImage().execute(url);
	}

	private class LoadImage extends AsyncTask<String, String, Bitmap> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ViewLookActivity.this);
			pDialog.setMessage("Loading image ....");
			pDialog.setCancelable(false);
	        pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();
		}

		protected Bitmap doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(
						args[0]).getContent());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap image) {
			if (image != null) {
				img.setImageBitmap(image);
				pDialog.dismiss();
			} else {
				pDialog.dismiss();
				Toast.makeText(ViewLookActivity.this,
						"Image Does Not exist or Network Error",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
