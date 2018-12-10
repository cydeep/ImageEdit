package com.cydeep.imageedit.base;


import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by chenyu on 16/2/18.
 */
public abstract class OnUpdateListUIListener<T> {
    protected List<T> mList;

    protected int mCount;

    public abstract void onUpdateUI(Context context, ViewHolder holder, int position);

    public abstract int getCount();

    public abstract void setData(List<T> list);

    public abstract void setCount(int count);

    public abstract View initLayout(Context context, int position);

    public List<T> getData(){
        return mList;
    }
}
