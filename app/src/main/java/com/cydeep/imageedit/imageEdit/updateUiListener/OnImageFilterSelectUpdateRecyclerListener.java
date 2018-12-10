package com.cydeep.imageedit.imageEdit.updateUiListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cydeep.imageedit.ImageEditApplication;
import com.cydeep.imageedit.R;
import com.cydeep.imageedit.base.OnUpdateListUIListener;
import com.cydeep.imageedit.base.ViewHolder;
import com.cydeep.imageedit.imageEdit.GPUImageFilterTools;
import com.cydeep.imageedit.imageEdit.ImageFilterHandler;
import com.cydeep.imageedit.imageEdit.ImageEditActivity;
import com.cydeep.imageedit.util.ViewSizeUtil;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;


/**
 * Created by chenyu on 17/2/5.
 */

public class OnImageFilterSelectUpdateRecyclerListener extends OnUpdateListUIListener<String> {
    private View.OnClickListener onImageFilterClickListener;

    private Bitmap bitmap;
    private GPUImageFilter mFilter;
    private List<GPUImageFilter> gpuImageFilters = new ArrayList<>();
    private String path;

    public ExecutorService getExecutorService() {
        return executorService;
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TextView lastView;

    public List<GPUImageFilter> getGpuImageFilters() {
        return gpuImageFilters;
    }

    private int selectPosition = 0;

    private MemoryCache mMCache = ImageLoader.getInstance().getMemoryCache();

    public void setPath(String path) {
        this.path = path;
    }

    public void setBitmap(Bitmap bitmap) {
        int size = ViewSizeUtil.getCustomDimen(94f);
        Matrix matrix = new Matrix();
        float scale;
        if (bitmap.getHeight() != bitmap.getWidth()) {
            if (bitmap.getHeight() > bitmap.getWidth()) {
                scale = size * 1.0f / bitmap.getWidth() * 1.0f;
            } else {
                scale = size * 1.0f / bitmap.getHeight() * 1.0f;
            }
        } else {
            scale = size * 1.0f / bitmap.getWidth() * 1.0f;
        }
        matrix.setScale(scale, scale);
        this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }


    @Override
    public void onUpdateUI(Context context, final ViewHolder holder, final int position) {
        final ImageView imageView = holder.getView(R.id.item_filter_image);
        ViewGroup.LayoutParams imageLayoutParams = imageView.getLayoutParams();
        int textColor;
        boolean isBold;
        int name;
//        if (position == 0) {
//            name = R.string.text_filter_65;
//            ImageFilterHandler.setFilterBitmap(executorService, gpuImageFilter, this.bitmap, imageView);
//        } else
        if (position == getCount() - 1) {
            imageView.setTag(null);
            name = R.string.manage;
            imageView.setImageResource(R.drawable.icon_image_filter);
            holder.setBackgroundRes(R.id.item_filter_image_container, R.drawable.rectangle_image_filter_setting);
            imageLayoutParams.height = imageLayoutParams.width = ViewSizeUtil.getCustomDimen(29f);
        } else {
            imageLayoutParams.height = imageLayoutParams.width = ViewSizeUtil.getCustomDimen(94f);
            name = context.getResources().getIdentifier("text_filter_" + mList.get(position), "string", ImageEditApplication.getInstance().getPackageName());
            holder.setBackgroundRes(R.id.item_filter_image_container, R.color.white);
            Bitmap bitmap = mMCache.get(mList.get(position) + path);
            imageView.setTag(mList.get(position) + path);
            if (bitmap != null && !bitmap.isRecycled()) {
                imageView.setImageBitmap(bitmap);
            } else {
                final GPUImageFilter filter = gpuImageFilters.get(position);
                ImageFilterHandler.setFilterBitmap(executorService, filter, mList.get(position) + path, this.bitmap, imageView);
            }
        }
        imageView.requestLayout();
        if (selectPosition == position) {
            if (selectPosition == 0) {
                lastView = holder.getView(R.id.item_filter_text);
            }
            textColor = 0xff645e66;
            isBold = true;
        } else {
            textColor = 0xff9b9b9b;
            isBold = false;
        }
        holder
                .setText(R.id.item_filter_text, name)
                .setTextColor(R.id.item_filter_text, textColor)
                .setTextBold(R.id.item_filter_text, isBold)
                .setOnClickListener(R.id.item_filter_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPosition = position;
                        v.setTag(position);
                        if (lastView != null) {
                            lastView.setTextColor(0xff9b9b9b);
                            lastView.getPaint().setFakeBoldText(false);
                        }
                        lastView = holder.getView(R.id.item_filter_text);
                        lastView.setTextColor(0xff645e66);
                        lastView.getPaint().setFakeBoldText(true);
                        if (onImageFilterClickListener != null) {
                            onImageFilterClickListener.onClick(v);
                        }
                    }
                });
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getView(R.id.item_filter_container).getLayoutParams();
        layoutParams.setMargins(ViewSizeUtil.getCustomDimen(15f), 0, 0, 0);
        if (position == getCount() - 1) {
            holder.getView(R.id.item_filter_container).setPadding(0, 0, ViewSizeUtil.getCustomDimen(15f), 0);
        } else {
            holder.getView(R.id.item_filter_container).setPadding(0, 0, 0, 0);
        }
    }

    private void switchFilterTo(final GPUImageFilter filter, GPUImageView mGPUImageView) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(
                filter.getClass()))) {
            mFilter = filter;
            mGPUImageView.setFilter(mFilter);
        }
    }


    @Override
    public int getCount() {
        return mList.size() + 1;
    }

    @Override
    public void setData(List list) {
        mList = list;
        addFilters();
    }

    public void addFilters() {
        this.gpuImageFilters = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            gpuImageFilters.add(GPUImageFilterTools.createFilterForType(ImageEditApplication.getInstance(), mList.get(i)));
        }
    }

    @Override
    public void setCount(int count) {

    }

    @Override
    public View initLayout(Context context, int position) {
        return View.inflate(context, R.layout.item_image_filter_recycler_view, null);
    }

    public void setOnImageFilterClickListener(View.OnClickListener onImageFilterClickListener) {
        this.onImageFilterClickListener = onImageFilterClickListener;
    }

    public void setFilterBitmap(ImageEditActivity imageEditActivity, GPUImageFilter filter, Bitmap bitmap, ImageView imageView) {
        ImageFilterHandler.setFilterBitmap(executorService, imageEditActivity, filter, bitmap, imageView);
    }
}
