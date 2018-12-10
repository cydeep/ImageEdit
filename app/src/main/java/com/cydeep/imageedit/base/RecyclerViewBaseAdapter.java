package com.cydeep.imageedit.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ChenYu on 5/8/15.
 */
public class RecyclerViewBaseAdapter extends RecyclerView.Adapter<ViewHolder> {


    private final Context context;

    public OnUpdateListUIListener getOnSetUIListener() {
        return onSetUIListener;
    }

    public void setOnSetUIListener(OnUpdateListUIListener onSetUIListener) {
        this.onSetUIListener = onSetUIListener;
    }

    private OnUpdateListUIListener onSetUIListener;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = onSetUIListener.initLayout(context,viewType);
        ViewHolder viewHolder = new ViewHolder(view,context);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (onSetUIListener != null){
            holder.position = position;
            onSetUIListener.onUpdateUI(context,holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return onSetUIListener.getCount();
    }


    public RecyclerViewBaseAdapter(Context context, OnUpdateListUIListener onSetUIListener) {
        this.context = context;
        this.onSetUIListener = onSetUIListener;
    }


    public boolean isBottomView(int position) {
        return false;
    }
}
