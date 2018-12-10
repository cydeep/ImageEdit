package com.cydeep.imageedit.selectimage;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cydeep.imageedit.R;
import com.cydeep.imageedit.activity.BaseActivity;
import com.cydeep.imageedit.base.EventMsg;
import com.cydeep.imageedit.base.RecyclerViewBaseAdapter;
import com.cydeep.imageedit.base.RxBus;
import com.cydeep.imageedit.base.TitleViews;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SelectImageActivity extends BaseActivity {

    private RecyclerView recycler_view;
    private OnUpdateImageListener onUpdateAlbumListener;
    private RecyclerViewBaseAdapter recyclerViewBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        onUpdateAlbumListener = new OnUpdateImageListener();
        recycler_view = getView(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3, GridLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setHasFixedSize(true);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Boolean, List<AlbumInfo>>() {
                    @Override
                    public List<AlbumInfo> apply(Boolean granted) throws Exception {
                        List<AlbumInfo> albumInfoList = null;
                        if (granted) {
                            albumInfoList = getPhotos(SelectImageActivity.this);
                        }
                        return albumInfoList;
                    }
                }).subscribe(new Consumer<List<AlbumInfo>>() {
            @Override
            public void accept(List<AlbumInfo> albumInfos) throws Exception {
                if (albumInfos != null) {
                    onUpdateAlbumListener.setData(albumInfos);
                    if (recyclerViewBaseAdapter == null) {
                        recyclerViewBaseAdapter = new RecyclerViewBaseAdapter(SelectImageActivity.this, onUpdateAlbumListener);
                        recycler_view.setAdapter(recyclerViewBaseAdapter);
                    } else {
                        recyclerViewBaseAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    public List<AlbumInfo> getPhotos(Context context) {
        List<AlbumInfo> list = new ArrayList<>();
        String orderBy = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        String[] projection = new String[]{MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
                MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
                MediaStore.Images.Media.DATA, // 图片绝对路径
                MediaStore.Images.Media.TITLE, //
        };
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null, null, orderBy);

        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    AlbumInfo albumInfo = new AlbumInfo();
                    albumInfo.path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File file = new File(albumInfo.path);
                    if (file.exists() && file.length() > 0) {
                        albumInfo.folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                        albumInfo.folderName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                        albumInfo.fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                        albumInfo.imageName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));

                        if (!albumInfo.path.startsWith("file://")) {
                            albumInfo.path = "file://" + albumInfo.path;
                        }
                        albumInfo.title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                        albumInfo.count = cursor.getInt(5);//该文件夹下一共有多少张图片
                        list.add(albumInfo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }


    @Override
    protected void initTitle(TitleViews titleViews) {
        titleViews.center_container_title_text.setText("选择图片");
        titleViews.left_container_left_image.setVisibility(View.VISIBLE);
        titleViews.left_container_left_image.setBackgroundResource(R.drawable.left_gray);
        titleViews.right_container_title_text.setText("完成");
        titleViews.left_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleViews.right_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onUpdateAlbumListener.getSelectLists().size() > 0) {
                    EventMsg eventMsg = new EventMsg();
                    eventMsg.code = EventMsg.CODE_RESULT_SELECT_IMAGE;
                    eventMsg.msg = onUpdateAlbumListener.getSelectLists().get(0).path;
                    RxBus.getInstance().post(eventMsg);
                    finish();
                } else {
                    Toast.makeText(SelectImageActivity.this,"请选择一张图片",Toast.LENGTH_SHORT);
                }

            }
        });
    }
}
