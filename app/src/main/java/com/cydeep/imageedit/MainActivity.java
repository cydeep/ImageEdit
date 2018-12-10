package com.cydeep.imageedit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.cydeep.imageedit.activity.BaseActivity;
import com.cydeep.imageedit.base.EventMsg;
import com.cydeep.imageedit.base.RxBus;
import com.cydeep.imageedit.base.TitleViews;
import com.cydeep.imageedit.imageEdit.ImageEditActivity;
import com.cydeep.imageedit.selectimage.SelectImageActivity;
import com.cydeep.imageedit.util.FileUtils;
import com.cydeep.imageedit.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends BaseActivity {


    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method

        setOnClickListener(R.id.button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectImageActivity.class);
                startActivity(intent);

            }
        });

        setOnClickListener(R.id.button_filter, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url != null) {
                    String temp = url;
                    if (url.startsWith("file://")) {
                        temp = url.substring(7);
                    }
                    ImageEditActivity.startPostImageEditActivity(MainActivity.this, temp);
                }
            }
        });

        RxBus.getInstance().toObservable().map(new Function<Object, EventMsg>() {
            @Override
            public EventMsg apply(Object o) throws Exception {
                return (EventMsg) o;
            }
        }).subscribe(new Consumer<EventMsg>() {
            @Override
            public void accept(EventMsg eventMsg) throws Exception {
                if (eventMsg != null) {
                    ImageView imageView = getView(R.id.image);
                    switch (eventMsg.code) {
                        case EventMsg.CODE_RESULT_SELECT_IMAGE:
                            url = eventMsg.msg;
                            if (!eventMsg.msg.startsWith("file://")) {
                                url = "file://" + url;
                            }
                            ImageLoader.getInstance().displayImage(eventMsg.msg, imageView, ImageUtil.getOptions());
                            break;
                        case EventMsg.CODE_RESULT_FILTE_IMAGE:
                            url = eventMsg.msg;
                            if (!eventMsg.msg.startsWith("file://")) {
                                url = "file://" + url;
                            }
                            ImageLoader.getInstance().displayImage(url, imageView, ImageUtil.getOptions());
                    }
                }
            }
        });

    }

    @Override
    protected void initTitle(TitleViews titleViews) {
        titleViews.left_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleViews.left_container_left_image.setVisibility(View.VISIBLE);
        titleViews.left_container_left_image.setBackgroundResource(R.drawable.left_gray);
        titleViews.center_container_title_text.setText("首页入口");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
}
