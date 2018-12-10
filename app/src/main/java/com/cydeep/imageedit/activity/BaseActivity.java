package com.cydeep.imageedit.activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cydeep.imageedit.R;
import com.cydeep.imageedit.base.TitleViews;
import com.cydeep.imageedit.base.ViewHolder;

public abstract class BaseActivity extends AppCompatActivity {
    private ViewHolder viewHolder;
    public TitleViews titleViews = new TitleViews();
    private ProgressDialog progressDialog;

    public <T extends View> T getView(int viewId) {
        return (T) viewHolder.getView(viewId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

    }

    @Override
    public void setContentView(int layoutResID) {
        initBaseView();
        addSubContentView(layoutResID);
        initTitle(titleViews);
    }

    @Override
    public void setContentView(View view) {
        initBaseView();
        addSubContentView(view);
        initTitle(titleViews);
    }


    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        initBaseView();
        view.setLayoutParams(params);
        addSubContentView(view);
        initTitle(titleViews);
    }

    public  void hideTitleBar(){
        titleViews.custom_title.setVisibility(View.GONE);
    }

    protected abstract void initTitle(TitleViews titleViews);

    private void addSubContentView(int layoutResID) {
        View view = View.inflate(this, layoutResID, null);
        addSubContentView(view);
    }

    private void addSubContentView(View view) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.custom_title);
        titleViews.base_container.addView(view, layoutParams);
    }

    private void initBaseView() {
        viewHolder = new ViewHolder(getWindow().getDecorView(), this);
        findViews();
    }

    public BaseActivity setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        viewHolder.setOnClickListener(viewId, onClickListener);
        return this;
    }

    private void findViews() {
        titleViews.base_container = viewHolder.getView(R.id.activity_base_container);
        titleViews.custom_title = viewHolder.getView(R.id.custom_title);
        titleViews.title_divider = viewHolder.getView(R.id.title_divider);

        titleViews.center_container = viewHolder.getView(R.id.center_container);
        titleViews.center_container_left_image = viewHolder.getView(R.id.center_container_left_image);
        titleViews.center_container_title_text = viewHolder.getView(R.id.center_container_title_text);
        titleViews.center_container_right_image = viewHolder.getView(R.id.center_container_right_image);

        titleViews.left_container = viewHolder.getView(R.id.left_container);
        titleViews.left_container_left_image = viewHolder.getView(R.id.left_container_left_image);
        titleViews.left_container_right_image = viewHolder.getView(R.id.left_container_right_image);
        titleViews.left_container_title_text = viewHolder.getView(R.id.left_container_title_text);

        titleViews.right_container = viewHolder.getView(R.id.right_container);
        titleViews.right_container_left_image = viewHolder.getView(R.id.right_container_left_image);
        titleViews.right_container_right_image = viewHolder.getView(R.id.right_container_right_image);
        titleViews.right_container_title_text = viewHolder.getView(R.id.right_container_title_text);

        titleViews.center_container = viewHolder.getView(R.id.center_container);
        titleViews.center_container_left_image = viewHolder.getView(R.id.center_container_left_image);
        titleViews.center_container_right_image = viewHolder.getView(R.id.center_container_right_image);
        titleViews.center_container_title_text = viewHolder.getView(R.id.center_container_title_text);

    }

    public void showWaitDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIcon(R.mipmap.ic_launcher);
            progressDialog.setTitle("加载dialog");
            progressDialog.setMessage("加载中...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
        }
        progressDialog.show();

    }
    public void hideWaitDialog(){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void setStatusBarColorRes(int barColorResId) {
        if (barColorResId == R.color.white) {
            barColorResId = R.drawable.shape_root_status_bar_white;
        }
        titleViews.base_container.setBackgroundResource(barColorResId);
    }

    public BaseActivity setSelected(int viewId, boolean isSelected) {
        viewHolder.setSelected(viewId, isSelected);
        return this;
    }
}
