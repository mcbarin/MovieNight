package com.example.mcagataybarin.movienight;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcagataybarin.movienight.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String email_s = "";
    private String name_s = "";
    String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getCurrentWeek();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String user_id = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user_id);
                    FirebaseFunctions.getInstance().user_id = user_id;
                    FirebaseFunctions.getInstance().user_pp_url = user.getPhotoUrl().toString();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
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


    public void onClickRegister(View view) {
        EditText name = (EditText) findViewById(R.id.nameField);
        EditText email = (EditText) findViewById(R.id.emailRegister);
        final EditText password = (EditText) findViewById(R.id.passwordRegister);
        name_s = name.getText().toString();
        email_s = email.getText().toString();
        updateProfile(name_s);

        createUserWithEmailAndPassword(name_s, email_s, password.getText().toString());

        mAuth.signInWithEmailAndPassword(email_s, password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i("Bienvenidos!", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(RegisterActivity.this, "Oooppss! Try Again.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(RegisterActivity.this, BottomNavigationActivity.class);
                            RegisterActivity.this.startActivity(intent);
                        }
                    }
                });
    }

    public void getCurrentWeek() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference movies_reference = mDatabase.child("movies");

        movies_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseFunctions.getInstance().currentWeek = String.valueOf(dataSnapshot.getChildrenCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void createUserWithEmailAndPassword(String name, String email, String password) {
        name_s = name;
        email_s = email;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Failed to create an account.", Toast.LENGTH_SHORT).show();
                        }

                        // User created. Do something.
                        FirebaseUser user = task.getResult().getUser();
                        String UID = user.getUid();
                        User new_user = new User(name_s, email_s, "");
                        mDatabase.child("users").child(UID).setValue(new_user);
                        FirebaseFunctions.getInstance().user_id = UID;

                    }
                });
    }

    public void updateProfile(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

    }
}
