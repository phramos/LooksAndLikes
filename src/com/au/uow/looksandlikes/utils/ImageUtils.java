package com.au.uow.looksandlikes.utils;

import java.io.IOException;
import java.io.InputStream;

import com.parse.ParseException;
import com.parse.ParseFile;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {
	public static Bitmap getBitmapFromAsset(Context context, String filePath) {
	    AssetManager assetManager = context.getAssets();

	    InputStream istr;
	    Bitmap bitmap = null;
	    try {
	        istr = assetManager.open(filePath);
	        bitmap = BitmapFactory.decodeStream(istr);
	    } catch (IOException e) {
	        // handle exception
	    }

	    return bitmap;
	}
	
	public static Bitmap getBitmapFromParseFile(ParseFile parseFile){
		Bitmap bitmap = null;
		try {
			bitmap = new BitmapFactory().decodeByteArray(parseFile.getData(), 0, parseFile.getData().length);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

}
