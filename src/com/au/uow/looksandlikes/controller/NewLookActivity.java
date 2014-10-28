package com.au.uow.looksandlikes.controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.au.uow.looksandlikes.Look;
import com.au.uow.looksandlikes.R;

/*
 * NewlookActivity contains two fragments that handle
 * data entry and capturing a photo of a given look.
 * The Activity manages the overall look data.
 */
public class NewLookActivity extends Activity {

	private Look look;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        look = new Look();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_new_look);
		FragmentManager manager = getFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			fragment = new CameraFragment();
			manager.beginTransaction().add(R.id.fragmentContainer, fragment)
					.commit();
		}
	}

	public Look getCurrentlook() {
		return look;
	}

}
