package com.hkimbrough22.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.hkimbrough22.taskmaster.R;

public class SignUpActivity extends AppCompatActivity {

    public final static String TAG = "hkim_taskmaster_signupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signUpButton = findViewById(R.id.signupSubmitButton);
        signUpButton.setOnClickListener( onClick ->
                {
                    EditText signupUsernameEditText = findViewById(R.id.signupUsernameEditText);
                    String username = signupUsernameEditText.getText().toString();
                    EditText signupPasswordEditText = findViewById(R.id.signupCodeEditText);
                    String password = signupPasswordEditText.getText().toString();

                    Amplify.Auth.signUp(username,
                            password,
                            AuthSignUpOptions.builder()
                                    .userAttribute(AuthUserAttributeKey.email(), username)
                                    .userAttribute(AuthUserAttributeKey.nickname(), "No Nickname")
                                    .build(),
                            success ->
                            {
                                Log.i(TAG, "Signup success: " + success.toString());
                                // TODO: Be nicer to the user and hand the email address to verify activity
                                Intent VerifyActivityIntent = new Intent(SignUpActivity.this, VerifyActivity.class);
                                startActivity(VerifyActivityIntent);
                            },
                            failure ->
                            {
                                Log.i(TAG, "Signup failed: " + failure.toString());
                                if(failure.getCause().toString().contains("InvalidPasswordException")){
                                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Password needs to be at least 8 characters.", Toast.LENGTH_SHORT).show());
                                } else {
                                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, failure.getRecoverySuggestion(), Toast.LENGTH_SHORT).show());
                                }
                            }
                    );
                }
        );

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
                    Intent signupActivityIntent = new Intent(this, LoginActivity.class);
                    startActivity((signupActivityIntent));
                }
        );
    }
}