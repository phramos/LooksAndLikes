package com.au.uow.looksandlikes.controller;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.controller.MainActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	private Button btnLoginFacebook;
    private Button btnLogin;
    private TextView registerScreen;
    private EditText email;
    private EditText password;
	private Dialog progressDialog;

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
                            /*LoginActivity.this.progressDialog = ProgressDialog.show(
                                    LoginActivity.this, "", "Login succeeded!",false);*/
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
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
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
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				LoginActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d(LooksAndLikes.TAG,
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d(LooksAndLikes.TAG,
							"User signed up and logged in through Facebook!");
                    goToMainActivity();
				} else {
					Log.d(LooksAndLikes.TAG,
							"User logged in through Facebook!");
                    goToMainActivity();
				}
			}
		});
	}

	private void goToMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
