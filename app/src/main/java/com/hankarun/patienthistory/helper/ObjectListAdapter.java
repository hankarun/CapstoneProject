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

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.listdraghelper.ItemTouchHelperAdapter;
import com.hankarun.patienthistory.helper.listdraghelper.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;

public class ObjectListAdapter extends RecyclerView.Adapter<ObjectListAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    public static final int UPDATE_OBJECT = 0;
    public static final int DELETE_OBJECT = 1;
    public static final int ADD_OBJECT = 2;


    private ArrayList<Object> mGroups;
    private final OnStartDragListener mDragStartListener;
    private final AdapterDataUpdateInterface mAdapterUpdate;
    private final View rootView;


    public ObjectListAdapter(Context context, ArrayList<Object> groups,
                             OnStartDragListener dragListener, AdapterDataUpdateInterface adapterDataUpdateInterface, View view) {
        mAdapterUpdate = adapterDataUpdateInterface;
        mGroups = groups;
        mDragStartListener = dragListener;
        rootView = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.groupName.setText(mGroups.get(position).toString());
        holder.groupItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterUpdate.updateDataBase(UPDATE_OBJECT, mGroups.get(position));
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
        mAdapterUpdate.updateDataBase(UPDATE_OBJECT, mGroups.get(toPosition));
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(final int position) {
        final Object group = mGroups.get(position);
        mGroups.remove(position);
        mAdapterUpdate.updateDataBase(DELETE_OBJECT, mGroups.get(position));
        notifyItemRemoved(position);
        //notifyDataSetChanged();
        Snackbar.make(rootView, R.string.group_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.revert_changes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGroups.add(position, group);
                        mAdapterUpdate.updateDataBase(ADD_OBJECT, mGroups.get(position));
                        notifyItemInserted(position);
                        //notifyDataSetChanged();
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
