package edu.rosehulman.changb.boyeram1.jaundicedetection.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.rosehulman.changb.boyeram1.jaundicedetection.Constants;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Family;

public class Utils {

    public static void getCurrentFamilyNameForToolbar(final ActivityWithToolbar activity) {
        String currentFamilyKey = SharedPrefsUtils.getCurrentFamilyKey();
        DatabaseReference familyRef = FirebaseDatabase.getInstance().getReference()
                .child("families")
                .child(currentFamilyKey);
        familyRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String familyName = (String)dataSnapshot.getValue();
                activity.setToolbarTitle(familyName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface ActivityWithToolbar {
        void setToolbarTitle(String familyName);
    }

}
