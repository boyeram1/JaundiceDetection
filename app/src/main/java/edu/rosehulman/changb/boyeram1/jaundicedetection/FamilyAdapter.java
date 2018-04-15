package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changb on 4/15/2018.
 */

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolder> {

    private Context mContext;
    private List<Family> mFamilies;
    private FamilyCallback mFamilyCallback;
//    private DatabaseReference mFamiliesRef;

    public FamilyAdapter(FamilyCallback familyCallback) {
        mFamilyCallback = familyCallback;
        mFamilies = new ArrayList<>();
//        mFamiliesRef = FirebaseDatabase.getInstance().getReference();
//        mFamiliesRef.addChildEventListener(new FamilyEventListener());
//        mFamiliesRef.keepSynced(true);
    }

    public void addFamily(String name) {
        Family family = new Family(name);
        this.mFamilies.add(family);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
                            case R.id.menu_context_edit:
                                mFamilyCallback.onEdit(family);
                                break;
                            case R.id.menu_context_remove:
                                
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
            notifyDataSetChanged();
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
