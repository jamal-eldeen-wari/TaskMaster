package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;

public class ConfirmationActivity extends AppCompatActivity {
    public static final String TAG = "ConfirmationCode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confimation);
        EditText confirmationText= findViewById(R.id.editTextTextPassword3);
        EditText confirmEmail = findViewById(R.id.editTextTextPersonName7);
        Button btnConfirm = findViewById(R.id.button5);

        try {
            // Add these lines to add the AWSApiPlugin plugins
            Amplify.addPlugin(new AWSApiPlugin()); // stores things in DynamoDB and allows us to perform GraphQL queries
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());


            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }
       btnConfirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Amplify.Auth.confirmSignUp(
                       confirmEmail.getText().toString(),
                       confirmationText.getText().toString(),
                       result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                       error -> Log.e("AuthQuickstart", error.toString())
               );
           }
       });
    }
}