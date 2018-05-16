package edu.rosehulman.changb.boyeram1.jaundicedetection.adapters;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Family;

/**
 * Created by changb on 4/15/2018.
 */

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolder> {

    private List<Family> mFamilies;
    private FamilyLoginActivityCallback mFamilyAdapterCallback;
    private RecyclerView mRecyclerView;
    private DatabaseReference mFamiliesRef;
    private static FirebaseDatabase mDatabase;

    public FamilyAdapter(FamilyLoginActivityCallback familyCallback, RecyclerView view) {
        this.mFamilyAdapterCallback = familyCallback;
        this.mFamilies = new ArrayList<>();
        this.mRecyclerView = view;
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        mFamiliesRef = mDatabase.getReference().child("families");
        mFamiliesRef.addChildEventListener(new FamilyEventListener());
        mFamiliesRef.keepSynced(true);
    }

    public void addFamily(Family family) {
        mFamiliesRef.push().setValue(family);
    }

    public void update(Family family, String name) {
        family.setValues(new Family(name));
        mFamiliesRef.child(family.getKey()).setValue(family);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("CreateViewHolder", "Created View Holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Family family = mFamilies.get(position);
        holder.mNameTextView.setText(family.getName());
    }

    public void remove(final int position){
        final Family family = this.mFamilies.get(position);
        mFamilies.remove(position);
        notifyItemRemoved(position);

        Snackbar.make(this.mRecyclerView, family.getName() + " family removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (family != null) {
                            Log.d("FamilyAdapter", "");
                            mFamilies.add(position, family);
                            notifyItemInserted(position);
                            Snackbar.make(mRecyclerView, family.getName() + " family restored", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }).addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        Log.d("FamilyAdapter", "Snackbar dismissed: " + event);
                        if(event != Snackbar.Callback.DISMISS_EVENT_ACTION && event != Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE) {
                            mFamiliesRef.child(family.getKey()).removeValue();
                            FirebaseDatabase.getInstance().getReference("children").child(family.getKey()).removeValue();
                        }
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return mFamilies.size();
    }

    class FamilyEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Family family = dataSnapshot.getValue(Family.class);
            family.setKey(dataSnapshot.getKey());
            mFamilies.add(0, family);
            mRecyclerView.getLayoutManager().scrollToPosition(0);
            notifyItemInserted(0);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String keyChanged = dataSnapshot.getKey();
            Family updatedFamily = dataSnapshot.getValue(Family.class);

            int i = 0;
            for (Family fam : mFamilies) {
                if (fam.getKey().equals(keyChanged)) {
                    fam.setValues(updatedFamily);
                    notifyItemChanged(i);
                }
                i++;
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d("FamilyAdapter", "child removed");
            String keyToRemove = dataSnapshot.getKey();
            int i = 0;
            for (Family fam : mFamilies) {
                if (fam.getKey().equals(keyToRemove)) {
                    mFamilies.remove(fam);
                    notifyItemRemoved(i);
                    return;
                }
                i++;
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    public interface FamilyLoginActivityCallback {
        public void onSelect(Family family);
        public void onEdit(Family family);
        void showEditRemovePopup(Family family, View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView mNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.family_name_text_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mFamilyAdapterCallback.onSelect(mFamilies.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View v) {
            mFamilyAdapterCallback.showEditRemovePopup(mFamilies.get(getAdapterPosition()), v, getAdapterPosition());
            return true;
        }
    }

}
