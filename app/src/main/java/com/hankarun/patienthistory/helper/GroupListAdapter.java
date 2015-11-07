package com.hankarun.patienthistory.helper;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.listdraghelper.ItemTouchHelperAdapter;
import com.hankarun.patienthistory.helper.listdraghelper.OnStartDragListener;
import com.hankarun.patienthistory.model.Group;

import java.util.ArrayList;
import java.util.Collections;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Group> mGroups;
    private Context mContext;
    private final OnStartDragListener mDragStartListener;
    private final View rootView;


    public GroupListAdapter(Context context, ArrayList<Group> groups, OnStartDragListener dragListener, View view){
        mGroups = groups;
        mContext = context;
        mDragStartListener = dragListener;
        rootView = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.groupName.setText(mGroups.get(position).getmGText());
        holder.groupItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mGroups.get(position).getmGText(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.dragHandler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mGroups, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mGroups, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(final int position) {
        final Group group = mGroups.get(position);
        mGroups.remove(position);
        notifyDataSetChanged();
        Snackbar.make(rootView, "Group silindi", Snackbar.LENGTH_LONG)
                .setAction("Geri Al", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGroups.add(position,group);
                        notifyDataSetChanged();
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final public LinearLayout groupItemLayout;
        final public TextView groupName;
        final public ImageView dragHandler;

        public ViewHolder(View v) {
            super(v);

            groupName = (TextView) v.findViewById(R.id.groupNameView);
            groupItemLayout = (LinearLayout) v.findViewById(R.id.groupItemLayout);
            dragHandler = (ImageView) v.findViewById(R.id.groupReorderImage);
        }
    }
}
