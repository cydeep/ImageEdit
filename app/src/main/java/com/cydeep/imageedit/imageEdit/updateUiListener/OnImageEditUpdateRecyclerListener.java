package com.cydeep.imageedit.imageEdit.updateUiListener;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;


import com.cydeep.imageedit.R;
import com.cydeep.imageedit.base.OnUpdateListUIListener;
import com.cydeep.imageedit.base.ViewHolder;
import com.cydeep.imageedit.imageEdit.bean.PostEditImageInfo;
import com.cydeep.imageedit.util.ViewSizeUtil;

import java.util.List;


/**
 * Created by chenyu on 17/2/5.
 */

public class OnImageEditUpdateRecyclerListener extends OnUpdateListUIListener<PostEditImageInfo> {
    private View.OnClickListener onImageEditClickListener;

    @Override
    public void onUpdateUI(Context context, ViewHolder holder, final int position) {
        final PostEditImageInfo postEditImageInfo = mList.get(position);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getView(R.id.item_edit_container).getLayoutParams();
        layoutParams.setMargins(ViewSizeUtil.getCustomDimen(15f),0,0,0);
        holder
                .setBackgroundRes(R.id.item_edit_image,postEditImageInfo.resource)
                .setText(R.id.item_edit_text,postEditImageInfo.desc);
        if (position == getCount() - 1) {
            holder.getView(R.id.item_edit_container).setPadding(0, 0, ViewSizeUtil.getCustomDimen(15), 0);
        } else {
            holder.getView(R.id.item_edit_container).setPadding(0, 0, 0, 0);
        }
        holder.setOnClickListener(R.id.item_edit_container,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(postEditImageInfo.desc);
                if (onImageEditClickListener != null) {
                    onImageEditClickListener.onClick(v);
                }
            }
        });
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public void setData(List list) {
        mList = list;
    }

    @Override
    public void setCount(int count) {

    }

    @Override
    public View initLayout(Context context, int position) {
        return View.inflate(context, R.layout.item_edit_recycler_view,null);
    }

    public void setOnImageEditClickListener(View.OnClickListener onImageEditClickListener) {
        this.onImageEditClickListener = onImageEditClickListener;
    }
}
