package com.hkimbrough22.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.hkimbrough22.taskmaster.R;

public class LoginActivity extends AppCompatActivity {

    public final static String TAG = "hkim_taskmaster_signinActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button logInButton = findViewById(R.id.loginSubmitButton);
        logInButton.setOnClickListener( onClick ->
                {
                    EditText usernameLogInEditText = findViewById(R.id.loginUsernameEditText);
                    String username = usernameLogInEditText.getText().toString();
                    EditText passwordLogInEditText = findViewById(R.id.loginPasswordEditText);
                    String password = passwordLogInEditText.getText().toString();

                    Amplify.Auth.signIn(username,
                            password,
                            success ->
                            {
                                Log.i(TAG, "Login succeeded: " + success.toString());
                                Intent goToMainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(goToMainActivityIntent);
                            },
                            failure ->
                            {
                                Log.i(TAG, "Login failed: " + failure.toString());
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Could not log in that user!", Toast.LENGTH_SHORT).show());
                            }
                    );
                }
        );

        Button signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(view -> {
                    Intent signupActivityIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity((signupActivityIntent));
                }
        );
    }
}