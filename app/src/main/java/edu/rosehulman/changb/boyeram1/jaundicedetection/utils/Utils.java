package edu.rosehulman.changb.boyeram1.jaundicedetection.utils;

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

import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Child;

public class Utils {

    private static String TAG = "JD-Utils";

    public static void getChildNameByKey(final FragmentNeedingChildList fragment) {
        String childKey = SharedPrefsUtils.getCurrentChildKey();
        Log.d(TAG, "Getting child name by key: " + childKey);
        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference("children")
                .child(childKey)
                .child("name");
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String childName = (String)dataSnapshot.getValue();
                fragment.setToolbarTitle(childName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getCurrentChildren(final FragmentNeedingChildList fragment) {
        String familyKey = SharedPrefsUtils.getCurrentFamilyKey();
        Log.d(TAG, "Getting children with familyKey: " + familyKey);
        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference("children");
        Query myChildRef = childRef.child(familyKey);
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
                    Log.d(TAG, "Added child with name, key: " + child.getName() + ", " + child.getKey());
                }
                fragment.setCurrentChildList(currentChildList);
                Log.d(TAG, "Set current child list");
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
        void setToolbarTitle(String childName);
    }

    public interface ActivityWithToolbar {
        void setToolbarTitle(String familyName);
    }

}
