package com.cydeep.imageedit.imageEdit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.cydeep.imageedit.ImageEditApplication;
import com.cydeep.imageedit.R;
import com.cydeep.imageedit.activity.BaseActivity;
import com.cydeep.imageedit.base.OnUpdateListUIListener;
import com.cydeep.imageedit.base.SharedPreferenceHelper;
import com.cydeep.imageedit.base.TitleViews;
import com.cydeep.imageedit.base.ViewHolder;
import com.cydeep.imageedit.draglistview.OnMoveLongClickListener;
import com.cydeep.imageedit.draglistview.coreutil.TouchEventHandler;
import com.cydeep.imageedit.draglistview.view.ArrayAdapter;
import com.cydeep.imageedit.draglistview.view.DynamicListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyu on 17/2/8.
 */

public class ImageFilterManagerActivity extends BaseActivity implements OnMoveLongClickListener {
    private List<String> selectFilters = new ArrayList<>();
    private List<FilterInfo> filters = new ArrayList<>();
    private DynamicListView image_filter_list_view;
    private ArrayAdapter arrayAdapter;
    private View mTouchView;
    private String uuid;
//    private int[] filterString = {
//            R.string.text_filter_48,
//            R.string.text_filter_49,
//            R.string.text_filter_50,
//            R.string.text_filter_51,
//            R.string.text_filter_52,
//            R.string.text_filter_53,
//            R.string.text_filter_54,
//            R.string.text_filter_55,
//            R.string.text_filter_56,
//            R.string.text_filter_57,
//            R.string.text_filter_58,
//            R.string.text_filter_59,
//            R.string.text_filter_60,
//            R.string.text_filter_61,
//            R.string.text_filter_62,
//            R.string.text_filter_63,
//            R.string.text_filter_64,
//            R.string.text_filter_65
//    };

    public static class FilterInfo {
        public String filterId;
        public String filterName;

        public FilterInfo(String filterId, String filterName) {
            this.filterId = filterId;
            this.filterName = filterName;
        }
    }

    private boolean isFilterSelect(String target) {
        boolean result = false;
        if (selectFilters.size() > 0) {
            for (int i = 0; i < selectFilters.size(); i++) {
                if (selectFilters.get(i).equals(target)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private OnUpdateListUIListener<FilterInfo> onUpdateListViewUIListener = new OnUpdateListUIListener<FilterInfo>() {

        @Override
        public void onUpdateUI(Context context, final ViewHolder holder, final int position) {
            int textColor;
            final int selectBackground;
            final FilterInfo filterInfo = mList.get(position);
            if (isFilterSelect(filterInfo.filterId)) {
                selectBackground = R.drawable.icon_image_filter_select;
                textColor = 0xff5e6066;
            } else {
                selectBackground = R.drawable.icon_image_filter_unselect;
                textColor = 0xff9b9b9b;
            }
            int drawable = context.getResources().getIdentifier("image_filter_" + filterInfo.filterId, "drawable", ImageEditApplication.getInstance().getPackageName());
            holder
                    .setBackgroundRes(R.id.item_image_filter, drawable)
                    .setBackgroundRes(R.id.image_filter_select, selectBackground)
                    .setText(R.id.item_image_filter_text, filterInfo.filterName)
                    .setTextColor(R.id.item_image_filter_text, textColor);
            holder.setOnClickListener(R.id.image_filter_select_container, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFilterSelect(filterInfo.filterId)) {
                        selectFilters.remove(filterInfo.filterId);
                        holder
                                .setBackgroundRes(R.id.image_filter_select, R.drawable.icon_image_filter_unselect);
                    } else {
                        selectFilters.add(filterInfo.filterId);
                        holder
                                .setBackgroundRes(R.id.image_filter_select, R.drawable.icon_image_filter_select);
                    }
                }
            });
            holder.setOnLongClickListener(R.id.item_image_filter_container, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onMoveLongClick(holder.getView(R.id.item_image_filter_container), position);
                    return true;
                }
            });
        }

        @Override
        public int getCount() {
            return filters.size();
        }

        @Override
        public void setData(List<FilterInfo> list) {
            mList = list;
        }

        @Override
        public void setCount(int count) {

        }

        @Override
        public View initLayout(Context context, int position) {
            return View.inflate(context, R.layout.item_image_filter_manage, null);
        }
    };


    public static void startActivityForResult(BaseActivity context, String url, int requestCode) {
        Intent intent = new Intent(context, ImageFilterManagerActivity.class);
        intent.putExtra("url", url);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void initTitle(TitleViews titleViews) {
        titleViews.left_container_right_image.setVisibility(View.VISIBLE);
        titleViews.left_container_right_image.setBackgroundResource(R.drawable.left_gray);
        titleViews.left_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleViews.right_container_title_text.setText(R.string.action_done);
        titleViews.right_container_title_text.setTextColor(0xff7A1AE0);
        titleViews.custom_title.setBackgroundResource(R.color.white);
        setStatusBarColorRes(R.color.white);
        titleViews.center_container_title_text.setText(R.string.image_filter_manange);
        titleViews.center_container_title_text.setTextColor(0xff645e66);
        titleViews.title_divider.setVisibility(View.VISIBLE);
        titleViews.right_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < filters.size(); i++) {
                    if (isFilterSelect(filters.get(i).filterId)) {
                        stringBuffer.append(filters.get(i).filterId);
                        stringBuffer.append(",");
                    }
                }
                if (stringBuffer.length() > 0) {
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                }
                Intent intent = new Intent();
                intent.putExtra("filters", stringBuffer.toString());
                SharedPreferenceHelper.getInstance().putImageFilters(uuid,stringBuffer.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter_manage);
        uuid = "0000";
        initSelectFilter();
        initFilterData();
        onUpdateListViewUIListener.setData(filters);
        image_filter_list_view = (DynamicListView) getView(R.id.image_filter_list_view);
        image_filter_list_view.enableDragAndDrop();
        arrayAdapter = new ArrayAdapter(this, onUpdateListViewUIListener);
        image_filter_list_view.setAdapter(arrayAdapter);
        image_filter_list_view.setOnTouchEvenListener(new TouchEventHandler() {
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (mTouchView != null) {
                            mTouchView.setBackgroundResource(R.color.white);
                            mTouchView = null;
                        }
                        break;
                }
                return false;
            }

            @Override
            public boolean isInteracting() {
                return false;
            }
        });
    }

    private void initFilterData() {
        int start = Integer.valueOf(GPUImageFilterTools.FilterType.I_1977);
        int end = Integer.valueOf(GPUImageFilterTools.FilterType.DEFAULT);
        for (int i = 0; i < selectFilters.size(); i++) {
            filters.add(getFilterInfo(selectFilters.get(i)));
        }
        for (int i = start; i < end; i++) {
            if (!isFilterSelect(String.valueOf(i))) {
                filters.add(getFilterInfo(String.valueOf(i)));
            }
        }

    }

    private FilterInfo getFilterInfo(String id) {
        int text = getResources().getIdentifier("text_filter_" + id, "string", ImageEditApplication.getInstance().getPackageName());
        return new FilterInfo(id, getString(text));
    }

    private void initSelectFilter() {
        String imageFilters = SharedPreferenceHelper.getInstance().getImageFilters(uuid);
        if (!imageFilters.isEmpty()) {
            String[] split = imageFilters.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!split[i].isEmpty()) {
                    selectFilters.add(split[i]);
                }
            }
        }
    }

    @Override
    public boolean onMoveLongClick(View view, int position) {
        mTouchView = view;
        mTouchView.setBackgroundResource(R.drawable.image_filter_manage_background);
        image_filter_list_view.startDragging(position);
        return false;
    }
}
