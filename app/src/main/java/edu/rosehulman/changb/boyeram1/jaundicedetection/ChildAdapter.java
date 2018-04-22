package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.content.Context;
import android.content.DialogInterface;
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

/**
 * Created by boyeram on 4/16/18.
 */
public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    final ArrayList<Child> mChildren = new ArrayList<>();
    private RecyclerView mRecyclerView;

    private Callback mCallback;
    private DatabaseReference mChildrenRef;

    public ChildAdapter(Callback callback, RecyclerView recyclerView) {
        mCallback = callback;
        mRecyclerView = recyclerView;

        mChildrenRef = FirebaseDatabase.getInstance().getReference().child("children");
        mChildrenRef.addChildEventListener(new ChildrenChildEventListener());
    }

    public void addChild(Child newChild) {
        mChildrenRef.push().setValue(newChild);
    }

    public void removeChild(int position) {
        mChildren.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mChildren.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_row_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Child child = mChildren.get(position);
        holder.mNameTextView.setText(child.getName());
        holder.mBirthDateTextView.setText(child.getBirthDateTime().dateToString());
        holder.mBirthTimeTextView.setText(child.getBirthDateTime().timeToString());
    }

    class ChildrenChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Child child = dataSnapshot.getValue(Child.class);
            child.setKey(dataSnapshot.getKey());
            mChildren.add(0, child);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Child updatedChild = dataSnapshot.getValue(Child.class);

            int i = 0;
            for(Child child : mChildren) {
                if(child.getKey().equals(key)) {
                    child.setValues(updatedChild);
                    notifyItemChanged(i);
                    return;
                }
                i++;
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();

            int i = 0;
            for(Child child : mChildren) {
                if(child.getKey().equals(key)) {
                    mChildren.remove(i);
                    notifyItemRemoved(i);
                    return;
                }
                i++;
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // empty
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("JD", "Database error: " + databaseError);
        }
    }

    @Override
    public int getItemCount() {
        return mChildren.size();
    }

    public interface Callback {
        void showEditRemovePopup(Child child, View v, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mNameTextView;
        private TextView mBirthDateTextView;
        private TextView mBirthTimeTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.child_name_text_view);
            mBirthDateTextView = (TextView) itemView.findViewById(R.id.child_birth_date_text_view);
            mBirthTimeTextView = (TextView) itemView.findViewById(R.id.child_birth_time_text_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Child child = mChildren.get(getAdapterPosition());
//            mCallback.onEdit(child);
        }

        @Override
        public boolean onLongClick(View v) {
            mCallback.showEditRemovePopup(mChildren.get(getAdapterPosition()), v, getAdapterPosition());
            return true;
        }
    }
}
