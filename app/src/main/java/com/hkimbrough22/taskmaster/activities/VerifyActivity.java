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

public class VerifyActivity extends AppCompatActivity {

    public final static String TAG = "hkim_taskmaster_verifyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        Button verifySubmitButton = findViewById(R.id.verifySubmitButton);
        verifySubmitButton.setOnClickListener( onClick ->
                {
                    EditText verifyUsernameEditText = findViewById(R.id.verifyUsernameEditText);
                    String username = verifyUsernameEditText.getText().toString();
                    EditText verifyCodeEditText = findViewById(R.id.verifyCodeEditText);
                    String verificationCode = verifyCodeEditText.getText().toString();

                    Amplify.Auth.confirmSignUp(username,
                            verificationCode,
                            success ->
                            {
                                Log.i(TAG, "Verification succeeded: " + success.toString());
                                Intent goToLoginActivityIntent = new Intent(VerifyActivity.this, LoginActivity.class);
                                runOnUiThread(() -> Toast.makeText(VerifyActivity.this, "Account Verified!", Toast.LENGTH_SHORT).show());
                                startActivity(goToLoginActivityIntent);
                            },
                            failure ->
                            {
                                Log.i(TAG, "Verification failed: " + failure.toString());
                                runOnUiThread(() -> Toast.makeText(VerifyActivity.this, "Could not verify that user!", Toast.LENGTH_SHORT).show());
                            }
                    );
                }
        );
    }
}