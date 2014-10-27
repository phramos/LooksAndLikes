package com.au.uow.looksandlikes;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity {

    private TextView loginScreen;
    private EditText fullname;
    private EditText email;
    private EditText password;
    private Button registerButton;
    private Dialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);
        
        loginScreen = (TextView) findViewById(R.id.link_to_login);
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// Switching to Login Screen/closing register screen
				finish();
			}
		});

        registerButton = (Button) findViewById(R.id.btnRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.progressDialog = ProgressDialog.show(
                        RegisterActivity.this, "", "Creating account...", true);
                ParseUser user = new ParseUser();
                fullname = (EditText) findViewById(R.id.reg_fullname);
                user.setUsername(fullname.getText().toString());

                email = (EditText) findViewById(R.id.reg_email);
                user.setEmail(email.getText().toString());

                password = (EditText) findViewById(R.id.reg_password);
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            finish();
                            // Hooray! Let them use the app now.
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                        }
                        RegisterActivity.this.progressDialog.dismiss();
                    }
                });
            }
        });
    }
}