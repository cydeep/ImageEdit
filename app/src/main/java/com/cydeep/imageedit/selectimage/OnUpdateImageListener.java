package com.cydeep.imageedit.selectimage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cydeep.imageedit.R;
import com.cydeep.imageedit.base.OnUpdateListUIListener;
import com.cydeep.imageedit.base.ViewHolder;
import com.cydeep.imageedit.util.ImageUtil;
import com.cydeep.imageedit.util.ViewSizeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyu on 16/9/26.
 */

public class OnUpdateImageListener extends OnUpdateListUIListener<AlbumInfo> {

    private List<AlbumInfo> selectLists = new ArrayList<>();

    public List<AlbumInfo> getSelectLists(){
        return selectLists;
    }

    @Override
    public void onUpdateUI(final Context context, final ViewHolder holder, final int position) {
        final AlbumInfo albumInfo = mList.get(position);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.item_image).getLayoutParams();
        if (position == 0) {
            layoutParams.setMargins(0, 0, 0, 0);
        } else {
            layoutParams.setMargins(ViewSizeUtil.getCustomDimen(4.5f), 0, 0, 0);
        }

        if (selectLists.contains(albumInfo)) {
            holder.setBackgroundRes(R.id.item_image_check, R.drawable.image_select);
        } else {
            holder.setBackgroundRes(R.id.item_image_check, R.drawable.im_system_album_check_normal);
        }

        ImageLoader.getInstance().displayImage(albumInfo.path, (ImageView) holder.getView(R.id.item_image),ImageUtil.getOptions());

        holder
                .setOnClickListener(R.id.item_im_system_album_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!selectLists.contains(albumInfo)) {
                            if (selectLists.size() < 1) {
                                holder.setBackgroundRes(R.id.item_image_check, R.drawable.image_select);
                                selectLists.add(albumInfo);
                            } else {
                                Toast.makeText(context, "只能选择一张图片", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            holder.setBackgroundRes(R.id.item_image_check, R.drawable.im_system_album_check_normal);
                            selectLists.remove(albumInfo);
                        }
                    }
                });

    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
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
        return View.inflate(context, R.layout.item_select_image, null);
    }

}
