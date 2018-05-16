package edu.rosehulman.changb.boyeram1.jaundicedetection.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.rosehulman.changb.boyeram1.jaundicedetection.Constants;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Child;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Family;

public class Utils {

    public static void getCurrentChildren(final FragmentNeedingChildList fragment) {
        String familyKey = SharedPrefsUtils.getCurrentFamilyKey();
        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference("children");
        Query myChildRef = childRef.orderByChild("familyKey").equalTo(familyKey);
        myChildRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Child> currentChildList = new ArrayList<>();
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()) {
                    DataSnapshot snapshot = iterator.next();
                    String childKey = snapshot.getKey();
                    Child child = snapshot.getValue(Child.class);
                    child.setKey(childKey);
                    currentChildList.add(child);
                }
                fragment.setCurrentChildList(currentChildList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // empty
            }
        });
    }

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

    public interface FragmentNeedingChildList {
        void setCurrentChildList(List<Child> currentChildList);
    }

    public interface ActivityWithToolbar {
        void setToolbarTitle(String familyName);
    }

}
