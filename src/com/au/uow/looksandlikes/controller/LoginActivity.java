package com.au.uow.looksandlikes.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.UserProfile;
import com.au.uow.looksandlikes.controller.MainActivity;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.*;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

	private Button btnLoginFacebook;
    private Button btnLogin;
    private TextView registerScreen;
    private EditText email;
    private EditText password;
	private Dialog progressDialog;
    private UserProfile currentUserProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);

        btnLoginFacebook = (Button) findViewById(R.id.btnLoginFacebook);
        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginFacebookButtonClicked();
			}
		});

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.progressDialog = ProgressDialog.show(
                        LoginActivity.this, "", "Logging in...", true);

                email = (EditText) findViewById(R.id.reg_email);
                password = (EditText) findViewById(R.id.reg_password);

                ParseUser.logInInBackground(email.getText().toString(), password.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            goToMainActivity();
                        } else {
                            /*LoginActivity.this.progressDialog = ProgressDialog.show(
                                    LoginActivity.this, "", e.getMessage(), false);*/
                        }
                        LoginActivity.this.progressDialog.dismiss();
                    }
                });
            }
        });

        registerScreen = (TextView) findViewById(R.id.link_to_register);
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

		// Check if there is a currently logged in user
		// and they are linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null)/* && ParseFacebookUtils.isLinked(currentUser)*/) {
			// Go to the user info activity
			goToMainActivity();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void onLoginFacebookButtonClicked() {
		LoginActivity.this.progressDialog = ProgressDialog.show(
				LoginActivity.this, "", "Logging in...", true);

		List<String> permissions = Arrays.asList("public_profile", "user_about_me",
				"user_relationships", "user_birthday", "user_location");
		//ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
        ParseFacebookUtils.logIn(this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				LoginActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d(LooksAndLikes.TAG,
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
                    makeMeRequest();
					Log.d(LooksAndLikes.TAG,
							"User signed up and logged in through Facebook!");
                    createNewUserProfile();
                    goToMainActivity();
				} else {
					Log.d(LooksAndLikes.TAG,
							"User logged in through Facebook!");
                    goToMainActivity();
				}
			}
		});
	}

    private void createNewUserProfile() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        currentUserProfile = new UserProfile();
        currentUserProfile.setUser(currentUser);

        if (currentUser.get("profile") != null) {
            JSONObject userProfile = currentUser.getJSONObject("profile");

            try {

                if (userProfile.getString("facebookId") != null) {
                    currentUserProfile.setFacebookId(userProfile.getString("facebookId"));
                }

                if (userProfile.getString("name") != null) {
                    currentUserProfile.setName(userProfile.getString("name"));
                }

                if (userProfile.getString("location") != null) {
                    currentUserProfile.setLocation(userProfile.getString("location"));
                }

                if (userProfile.getString("gender") != null) {
                    currentUserProfile.setGender(userProfile.getString("gender"));
                }

                if (userProfile.getString("email") != null) {
                    currentUserProfile.setEmail(userProfile.getString("email"));
                }

            } catch (JSONException e) {
                Log.d(LooksAndLikes.TAG,
                        "Error parsing saved user data.");
            }
        }
        currentUserProfile.saveInBackground();
    }

    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            // Create a JSON object to hold the profile info
                            JSONObject userProfile = new JSONObject();
                            try {
                                // Populate the JSON object
                                userProfile.put("facebookId", user.getId());
                                userProfile.put("name", user.getName());

                                if (user.getProperty("gender") != null) {
                                    userProfile.put("gender", (String) user.getProperty("gender"));
                                } else {
                                    userProfile.put("gender", " ");
                                }

                                if (user.getLocation().getProperty("name") != null) {
                                    userProfile.put("location", (String) user.getLocation().getProperty("name"));
                                } else {
                                    userProfile.put("location", " ");
                                }

                                if (user.getUsername() != null) {
                                    userProfile.put("email", user.getUsername());
                                } else {
                                    userProfile.put("email", " ");
                                }

                                // Save the user profile info in a user property
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.put("profile", userProfile);
                                currentUser.saveInBackground();

                            } catch (JSONException e) {
                                Log.d(LooksAndLikes.TAG,
                                        "Error parsing returned user data.");
                            }

                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
                                    || (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                                Log.d(LooksAndLikes.TAG,
                                        "The facebook session was invalidated.");
                                onLogoutButtonClicked();
                            } else {
                                Log.d(LooksAndLikes.TAG,
                                        "Some other error: "
                                                + response.getError()
                                                .getErrorMessage());
                            }
                        }
                    }
                });
        request.executeAsync();
    }

	private void goToMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

    private void onLogoutButtonClicked() {
        // Log the user out
        ParseUser.logOut();

        // Go to the login view
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}