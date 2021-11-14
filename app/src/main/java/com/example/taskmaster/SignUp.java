package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUp extends AppCompatActivity {
    public static final String TAG = "SignUp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText emailText = findViewById(R.id.editTextTextPersonName5);
        EditText password = findViewById(R.id.editTextTextPassword);
        Button btnSignUp  = findViewById(R.id.button2);
        Button btnSignIn = findViewById(R.id.button4);

        try {
            // Add these lines to add the AWSApiPlugin plugins
            Amplify.addPlugin(new AWSApiPlugin()); // stores things in DynamoDB and allows us to perform GraphQL queries
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());


            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.fetchAuthSession(
                        result -> Log.i("AmplifyQuickstart", result.toString()),
                        error -> Log.e("AmplifyQuickstart", error.toString())
                );
                AuthSignUpOptions options = AuthSignUpOptions.builder()
                        .userAttribute(AuthUserAttributeKey.email(), emailText.getText().toString())
                        .build();

                Amplify.Auth.signUp(emailText.getText().toString(), password.getText().toString(), options,
                        result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                        error -> Log.e("AuthQuickStart", "Sign up failed", error)
                );


                Intent intent = new Intent(SignUp.this, ConfirmationActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

    }
}