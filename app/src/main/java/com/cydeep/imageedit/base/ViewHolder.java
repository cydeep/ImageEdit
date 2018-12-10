package com.cydeep.imageedit.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;


public class ViewHolder extends RecyclerView.ViewHolder {

    public String userId;

    public Context getContext() {
        return context;
    }

    private Context context;
    private SparseArray<View> mViews;
    private View mConvertView;
    public int position = -1;
    private int cornerRadiusPixels;
    public int resId;
    public Object tag;
    public int type = -1;
    public String url;

    public void clear() {
        mConvertView = null;
        mViews.clear();
    }

    public ViewHolder(View view, Context context) {
        super(view);
        this.mConvertView = view;
        this.mViews = new SparseArray<>();
        this.context = context;
    }

    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener onClickListener) {
        getView(viewId).setOnLongClickListener(onClickListener);
        return this;
    }

    public ViewHolder setTag(int viewId, Object obj) {
        getView(viewId).setTag(obj);
        return this;
    }


    public void setConvertView(View mConvertView) {
        this.mConvertView = mConvertView;
    }

    public <T extends View> T getConvertView() {
        return (T) mConvertView;
    }

    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


    public ViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        if (view != null) {
            view.setText(value);
        }
        return this;
    }

    public ViewHolder setTextSize(int viewId, int size) {
        TextView view = getView(viewId);
        view.setTextSize(size);
        return this;
    }

    public ViewHolder setText(int viewId, int resId) {
        TextView view = getView(viewId);
        view.setText(resId);
        return this;
    }

    public ViewHolder setHint(int viewId, CharSequence resId) {
        TextView view = getView(viewId);
        view.setHint(resId);
        return this;
    }

    public ViewHolder setHint(int viewId, int resId) {
        TextView view = getView(viewId);
        view.setHint(resId);
        return this;
    }

    public ViewHolder setText(int viewId, Spannable spannable, TextView.BufferType type) {
        TextView view = getView(viewId);
        view.setText(spannable, type);
        return this;
    }


    public ViewHolder setImageResource(int viewId, int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public ViewHolder setImageURI(int viewId, Uri uri) {
        ImageView view = getView(viewId);
        view.setImageURI(uri);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolder setTextBold(int viewId, boolean isBold) {
        TextView view = getView(viewId);
        view.getPaint().setFakeBoldText(isBold);
        return this;
    }

    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(context.getResources().getColor(textColorRes));
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public Bitmap getBitmap(String path) {
        return ImageLoader.getInstance().loadImageSync(path);
    }

    public Bitmap getBitmap(String path, ImageSize targetImageSize) {
        return ImageLoader.getInstance().loadImageSync(path, targetImageSize);
    }

    public Bitmap getBitmap(String path, ImageSize targetImageSize, DisplayImageOptions options) {
        return ImageLoader.getInstance().loadImageSync(path, targetImageSize, options);
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder setInVisible(int viewId) {
        View view = getView(viewId);
        view.setVisibility(View.INVISIBLE);
        return this;
    }

    public ViewHolder setSelected(int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public ViewHolder setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        getView(viewId).setOnClickListener(onClickListener);
        return this;
    }

    public void setOnTouchListener(int viewId, View.OnTouchListener listener) {
        getView(viewId).setOnTouchListener(listener);
    }

    public ViewHolder setCompoundDrawables(int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom) {
        TextView textView = getView(viewId);
        textView.setCompoundDrawables(left, top, right, bottom);
        return this;
    }
}
