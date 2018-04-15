package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changb on 4/15/2018.
 */

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolder> {

    private List<Family> mFamilies;
    private FamilyCallback mFamilyCallback;
    private DatabaseReference mFamiliesRef;

    public FamilyAdapter(FamilyCallback familyCallback) {
        mFamilyCallback = familyCallback;
        mFamilies = new ArrayList<>();
        mFamiliesRef = FirebaseDatabase.getInstance().getReference();
        mFamiliesRef.addChildEventListener()
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface FamilyCallback {
        public void onEdit(Family family);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.family_name_textview);
        }
    }

}
