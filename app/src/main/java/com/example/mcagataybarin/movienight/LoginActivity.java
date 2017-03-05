package com.example.mcagataybarin.movienight;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


// https://firebase.google.com/docs/auth/android/manage-users#update_a_users_profile

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // For Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("signed in", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.i("signed out", "onAuthStateChanged:signed_out");
                }

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i("Bienvenidos!", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.i("You shall not pass", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Oooppss! Try Again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // Successful. Do Something.
                    }
                });
    }


    public void onClickLogin(View view){
        EditText email = (EditText) findViewById(R.id.emailField);
        EditText password = (EditText) findViewById(R.id.passwordField);

        signIn(email.getText().toString(), password.getText().toString());
    }

    /*
    * Starts RegisterActivity for user to sign up an account.
    * */
    public void toRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        LoginActivity.this.startActivity(intent);
    }


    /*
    * This function sends a email to user for resetting the password.*/
    public void resetPassword(View view){
        EditText email = (EditText) findViewById(R.id.emailField);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("Email sent.", "Email sent.");
                            Toast.makeText(LoginActivity.this, "Check your inbox to reset your password.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
