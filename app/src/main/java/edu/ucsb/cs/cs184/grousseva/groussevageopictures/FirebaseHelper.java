package edu.ucsb.cs.cs184.grousseva.groussevageopictures;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

/**
 * Created by Donghao Ren on 03/11/2017.
 * Modified by Ehsan Sayyad on 11/9/2018
 * Modified by Jake Guida on 11/6/2019
 */

/**
 * This is a Firebase helper starter class we have created for you
 * In your Activity, FirebaseHelper.Initialize() is called to setup the Firebase
 * Put your application logic in OnDatabaseInitialized where you'll have the database object initialized
 */
public class FirebaseHelper {

    /** This is a message data structure that mirrors our Firebase data structure for your convenience */
    public static class Post implements Serializable {

        public Double longitude;
        public Double latitude;
        public Double title;
        public Double timestamp;
    }

    /** Keep track of initialized state, so we don't initialize multiple times */
    private static boolean initialized = false;

    /** The Firebase database object */
    private static FirebaseDatabase db;
    public static FirebaseApp fba;
    public static Post x;


    /** Initialize the firebase instance */
    public static void Initialize(final Context context)
    {
        if (!initialized)
        {
        initialized = true;
        fba = FirebaseApp.initializeApp(context, new FirebaseOptions.Builder()
                        .setDatabaseUrl("https://geopic2.firebaseio.com/")
                        .setApiKey("AIzaSyBEqMXOi5m0N178WNLf9oGkJLEvoFxeJmg ")
                        .setApplicationId("geopic2")
                        .build(),
                "class_db"
        );

            // Call the OnDatabaseInitialized to setup application logic
            OnDatabaseInitialized(fba);
        }
    }


    private static void OnDatabaseInitialized(FirebaseApp fbaa) {
        db = FirebaseDatabase.getInstance(fbaa);

        DatabaseReference postsL = db.getReference("posts");


        postsL.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                x = dataSnapshot.getValue(Post.class);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                x = dataSnapshot.getValue(Post.class);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                x = dataSnapshot.getValue(Post.class);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                x = dataSnapshot.getValue(Post.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}