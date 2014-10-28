package com.au.uow.looksandlikes.controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.au.uow.looksandlikes.Look;
import com.au.uow.looksandlikes.R;
import com.parse.*;

/*
 * This fragment manages the data entry for a
 * new look object. It lets the user input a
 * look name, give it a rating, and take a
 * photo. If there is already a photo associated
 * with this look, it will be displayed in the
 * preview at the bottom, which is a standalone
 * ParseImageView.
 */
public class NewLookFragment extends Fragment {

	private ImageButton photoButton;
	private Button saveButton;
	private Button cancelButton;
	private TextView lookName;
	private Spinner lookRating;
	private ParseImageView lookPreview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle SavedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_new_look, parent, false);

		saveButton = ((Button) v.findViewById(R.id.save_button));
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Look look = ((NewLookActivity) getActivity()).getCurrentlook();

				// When the user clicks "Save," upload the look to Parse
				// Add data to the look object:
                //look.setTitle(lookName.getText().toString());

				// Associate the look with the current user
				look.setAuthor(ParseUser.getCurrentUser());

				// Add the rating
				look.setRating("2");

				// If the user added a photo, that data will be
				// added in the CameraFragment

				// Save the look and return
				look.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							getActivity().setResult(Activity.RESULT_OK);
							getActivity().finish();
						} else {
							Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Error saving: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
						}
					}

				});

			}
		});

		cancelButton = ((Button) v.findViewById(R.id.cancel_button));
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().setResult(Activity.RESULT_CANCELED);
				getActivity().finish();
			}
		});

		// Until the user has taken a photo, hide the preview
		lookPreview = (ParseImageView) v.findViewById(R.id.look_preview_image);
		lookPreview.setVisibility(View.INVISIBLE);

		return v;
	}

	/*
	 * On resume, check and see if a look photo has been set from the
	 * CameraFragment. If it has, load the image in this fragment and make the
	 * preview image visible.
	 */
	@Override
	public void onResume() {
		super.onResume();
		ParseFile photoFile = ((NewLookActivity) getActivity())
				.getCurrentlook().getPhotoFile();
		if (photoFile != null) {
			lookPreview.setParseFile(photoFile);
			lookPreview.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					lookPreview.setVisibility(View.VISIBLE);
				}
			});
		}
	}

}
