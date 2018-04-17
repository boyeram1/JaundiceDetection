package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    private RecyclerView mRecyclerView;
    private DatabaseReference mFamiliesRef;
    private String[] familyNames = new String[]{ "Smith", "Johnson", "Williams", "Jones" };

    public FamilyAdapter(FamilyCallback familyCallback, RecyclerView view) {
        this.mFamilyCallback = familyCallback;
        this.mFamilies = new ArrayList<>();
        this.mRecyclerView = view;
        mFamiliesRef = FirebaseDatabase.getInstance().getReference();
        mFamiliesRef.addChildEventListener(new FamilyEventListener());
        mFamiliesRef.keepSynced(true);

        // TODO: remove this for-loop once connected to Firebase
        for(int i = 0; i < familyNames.length; i++) {
            addFamily(familyNames[i]);
        }
    }

    public void addFamily(String name) {
        Family family = new Family(name);
        this.mFamilies.add(0, family);
        mRecyclerView.getLayoutManager().scrollToPosition(0);
        notifyItemInserted(0);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Family Click", family.getName() + " selected");
                mFamilyCallback.onSelect(family);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu((Context) mFamilyCallback, v);
                popupMenu.inflate(R.menu.context_login);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_popup_edit:
                                mFamilyCallback.onEdit(family);
                                break;
                            case R.id.menu_popup_remove:
                                AlertDialog.Builder builder = new AlertDialog.Builder((Context) mFamilyCallback);
                                builder.setTitle(R.string.login_remove_title);
                                builder.setMessage(R.string.login_remove_message);
                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        remove(holder.getAdapterPosition());
                                    }
                                });
                                builder.setNegativeButton(android.R.string.cancel, null);
                                builder.create().show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    public void remove(final int position){
        final Family family = this.mFamilies.get(position);
        this.mFamilies.remove(position);
        notifyItemRemoved(position);
        Snackbar.make(this.mRecyclerView, "Family Removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (family != null) {
                            mFamilies.add(position, family);
                            mRecyclerView.getLayoutManager().scrollToPosition(position);
                            notifyItemInserted(position);
                            Snackbar.make(mRecyclerView, "Family Restored", Snackbar.LENGTH_SHORT).show();
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

    public interface FamilyCallback {
        public void onSelect(Family family);
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
