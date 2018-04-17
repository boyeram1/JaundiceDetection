package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by boyeram on 4/16/18.
 */
public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    private Context mContext;
    final ArrayList<Child> mChildren = new ArrayList<>();
    private RecyclerView mRecyclerView;

    private String[] childNames = new String[]{ "Joe", "Josh" };

    public ChildAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;

        // TODO: remove this for-loop once connected to Firebase
        for(int i = 0; i < childNames.length; i++) {
            addChild(new Child(childNames[i], new BirthDateTime(13, 10, 2018, 14, 5)));
        }
    }

    public void addChild(Child newChild) {
        mChildren.add(0, newChild);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, mChildren.size());
        mRecyclerView.getLayoutManager().scrollToPosition(0);
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

    @Override
    public int getItemCount() {
        return mChildren.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mBirthDateTextView;
        private TextView mBirthTimeTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, v);
                    popupMenu.inflate(R.menu.popup_edit_remove);

                    final FamilyActivity familyActivity = (FamilyActivity) mContext;
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_popup_edit:
                                    familyActivity.addEditChild(mChildren.get(getAdapterPosition()), true);
                                    break;
                                case R.id.menu_popup_remove:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    builder.setTitle(R.string.login_remove_title);
                                    builder.setMessage(R.string.login_remove_message);
                                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            removeChild(getAdapterPosition());
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

            mNameTextView = (TextView) itemView.findViewById(R.id.child_name_text_view);
            mBirthDateTextView = (TextView) itemView.findViewById(R.id.child_birth_date_text_view);
            mBirthTimeTextView = (TextView) itemView.findViewById(R.id.child_birth_time_text_view);
        }
    }
}
