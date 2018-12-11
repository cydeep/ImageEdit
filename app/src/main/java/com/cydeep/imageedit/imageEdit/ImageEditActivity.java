package com.cydeep.imageedit.imageEdit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cydeep.imageedit.Constants;
import com.cydeep.imageedit.ImageEditApplication;
import com.cydeep.imageedit.R;
import com.cydeep.imageedit.activity.BaseActivity;
import com.cydeep.imageedit.base.EventMsg;
import com.cydeep.imageedit.base.RecyclerViewBaseAdapter;
import com.cydeep.imageedit.base.RxBus;
import com.cydeep.imageedit.base.SharedPreferenceHelper;
import com.cydeep.imageedit.base.TitleViews;
import com.cydeep.imageedit.imageEdit.bean.PostEditImageInfo;
import com.cydeep.imageedit.imageEdit.updateUiListener.OnImageEditUpdateRecyclerListener;
import com.cydeep.imageedit.imageEdit.updateUiListener.OnImageFilterSelectUpdateRecyclerListener;
import com.cydeep.imageedit.selectimage.AlbumInfo;
import com.cydeep.imageedit.selectimage.SelectImageActivity;
import com.cydeep.imageedit.util.FileUtils;
import com.cydeep.imageedit.util.ImageUtil;
import com.cydeep.imageedit.util.ViewSizeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;


/**
 * Created by chenyu on 17/1/13.
 */

public class ImageEditActivity extends BaseActivity {
    private RecyclerViewBaseAdapter recyclerViewBaseAdapter;
    private OnImageEditUpdateRecyclerListener onImageEditUpdateRecyclerListener = new OnImageEditUpdateRecyclerListener();
    private OnImageFilterSelectUpdateRecyclerListener onImageFilterSelectUpdateRecyclerListener = new OnImageFilterSelectUpdateRecyclerListener();

    private List<PostEditImageInfo> postImageEditors = new ArrayList<>();
    private List<String> filters = new ArrayList();

    private View lastSelectView;
    private int currentPosition;
    private int lastFilterPosition = 0;
    public Bitmap currentBitmap;
    public Bitmap originalBitmap;//
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private GPUImageFilter currentFilter = new GPUImageFilter();
    private DisplayImageOptions displayImageOptions;
    private ImageView imageView;
    private String url;
    private String uuid;
    private RxPermissions rxPermissions;


    @Override
    protected void initTitle(TitleViews titleViews) {
        titleViews.left_container_right_image.setVisibility(View.VISIBLE);
        titleViews.left_container_right_image.setBackgroundResource(R.drawable.icon_nav_cut);
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
        titleViews.center_container_title_text.setText(R.string.image_edit);
        titleViews.center_container_title_text.setTextColor(0xff645e66);
        titleViews.title_divider.setVisibility(View.VISIBLE);
        rxPermissions = new RxPermissions(this);
        titleViews.right_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWaitDialog();
                rxPermissions
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Boolean, String>() {
                            @Override
                            public String apply(Boolean granted) throws Exception {
                                String saveClipUrl = null;
                                if (granted) {
                                    saveClipUrl = saveClipUrl();
                                } else {

                                }
                                return saveClipUrl;
                            }
                        }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String path) throws Exception {
                        if (path != null) {
                            EventMsg eventMsg = new EventMsg();
                            eventMsg.code = EventMsg.CODE_RESULT_FILTE_IMAGE;
                            eventMsg.msg = path;
                            RxBus.getInstance().post(eventMsg);
                            finish();
                        } else {
                            Toast.makeText(ImageEditActivity.this, "请手动增加权限", Toast.LENGTH_SHORT);
                        }
                    }
                });

                onImageFilterSelectUpdateRecyclerListener.getExecutorService().execute(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        url = getIntent().getStringExtra("url");
        super.onCreate(savedInstanceState);
        uuid = "0000";
        if (FileUtils.isFileExists(FileUtils.File_TEMP_CLIP)) {
            FileUtils.deleteFolderFile(FileUtils.File_TEMP_CLIP, true);
        }
        setContentView(R.layout.activity_post_image_edite);
        imageView = (ImageView) getView(R.id.post_image);

        if (FileUtils.isFileExists(url)) {
            try {
                FileUtils.FileType fileType = FileUtils.getType(url);
                if (fileType != null && fileType.getValue().equals(FileUtils.FileType.GIF.getValue())) {

                } else {
                    currentBitmap = originalBitmap = ImageUtil.getSuitBitmap(url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateUi();
        } else {
            displayImageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.pic_normal)
                    .showImageForEmptyUri(R.drawable.pic_normal)
                    .showImageOnFail(R.drawable.pic_normal)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.NONE)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            showWaitDialog();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //利用imageLoader下载图片，这里可以随意更换框架下载
                    Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url, null, displayImageOptions);
                    String save_url = ImageLoader.getInstance().getDiskCache().get(url).getAbsolutePath();
                    try {
                        FileUtils.FileType fileType = FileUtils.getType(save_url);
                        if (fileType != null && fileType.getValue().equals(FileUtils.FileType.GIF.getValue())) {

                        } else {
                            currentBitmap = originalBitmap = bitmap;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageEditApplication.getInstance().handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hideWaitDialog();
                            updateUi();
                        }
                    });
                }
            });
        }

    }

    private void updateUi() {
        onImageFilterSelectUpdateRecyclerListener.setPath(url);
        setImageVieSize(imageView, originalBitmap.getWidth(), originalBitmap.getHeight());
        imageView.setImageBitmap(originalBitmap);
        getView(R.id.activity_base_container).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setBitmapContainerLocation();
                getView(R.id.activity_base_container).getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        initImageEditData();
        initImageFilterData();

        final RecyclerView recyclerView = getView(R.id.post_image_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        onImageEditUpdateRecyclerListener.setData(postImageEditors);
        recyclerViewBaseAdapter = new RecyclerViewBaseAdapter(this, onImageEditUpdateRecyclerListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewBaseAdapter);
        lastSelectView = getView(R.id.post_image_editor_container);
        getView(R.id.post_image_editor_container).getLayoutParams().width = ViewSizeUtil.getCustomDimen(180f);
        setSelected(R.id.post_image_editor_container, true);
        currentPosition = 0;
        getView(R.id.post_image_dress_container).getLayoutParams().width = ViewSizeUtil.getCustomDimen(180f);
        setOnClickListener(R.id.post_image_dress_container, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleViews.center_container_title_text.setText(R.string.image_filter);
                if (currentPosition == 0) {
                    setSelect(v);
                    recyclerViewBaseAdapter = new RecyclerViewBaseAdapter(ImageEditActivity.this, onImageFilterSelectUpdateRecyclerListener);
                    recyclerView.setAdapter(recyclerViewBaseAdapter);
                    currentPosition = 1;
                }
            }
        });
        setOnClickListener(R.id.post_image_editor_container, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleViews.center_container_title_text.setText(R.string.image_edit);
                if (currentPosition == 1) {
                    setSelect(v);
                    recyclerViewBaseAdapter = new RecyclerViewBaseAdapter(ImageEditActivity.this, onImageEditUpdateRecyclerListener);
                    recyclerView.setAdapter(recyclerViewBaseAdapter);
                    currentPosition = 0;
                }
            }
        });

        onImageFilterSelectUpdateRecyclerListener.setOnImageFilterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                if (position == onImageFilterSelectUpdateRecyclerListener.getCount() - 1) {
                    ImageFilterManagerActivity.startActivityForResult(ImageEditActivity.this, url, Constants.REQUEST_CODE_1001);
                } else {
                    if (lastFilterPosition != position) {
                        currentFilter = onImageFilterSelectUpdateRecyclerListener.getGpuImageFilters().get(position);
                        setFilterBitmap(executorService, ImageEditActivity.this, currentFilter, originalBitmap, imageView, position);
                    }
                }
            }
        });
        onImageFilterSelectUpdateRecyclerListener.setBitmap(originalBitmap);
        onImageFilterSelectUpdateRecyclerListener.setData(filters);
        onImageEditUpdateRecyclerListener.setOnImageEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = (String) v.getTag();
                if (desc.equals(getString(R.string.post_image_crop))) {
                    showWaitDialog();
                    rxPermissions
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new Function<Boolean, String>() {
                                @Override
                                public String apply(Boolean granted) {
                                    String saveClipUrl = null;
                                    if (granted) {
                                        saveClipUrl = ImageUtil.saveClip(currentBitmap, FileUtils.File_TEMP_CLIP);
                                    } else {

                                    }
                                    return saveClipUrl;
                                }
                            }).subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String path) {
                            if (path != null) {
                                ImageClipActivity.startImageClipActivity(ImageEditActivity.this, url, Constants.REQUEST_CODE_1004);
                                hideWaitDialog();
                            } else {
                                Toast.makeText(ImageEditActivity.this, "请手动增加权限", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (desc.equals(getString(R.string.post_image_saturation))) {
                    showWaitDialog();
                    rxPermissions
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new Function<Boolean, String>() {
                                @Override
                                public String apply(Boolean granted) {
                                    String saveClipUrl = null;
                                    if (granted) {
                                        saveClipUrl = ImageUtil.saveClip(currentBitmap, FileUtils.File_TEMP_CLIP);
                                    } else {

                                    }
                                    return saveClipUrl;
                                }
                            }).subscribe(path -> {
                                if (path != null) {
                                    ImageSaturationActivity.startImageSaturationActivity(ImageEditActivity.this,path, Constants.REQUEST_CODE_1002);
                                    hideWaitDialog();
                                } else {
                                    Toast.makeText(ImageEditActivity.this, "请手动增加权限", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void setBitmapContainerLocation() {
        LinearLayout.LayoutParams bottomLayoutParams = (LinearLayout.LayoutParams) getView(R.id.post_image_bottom).getLayoutParams();
        View view = getView(R.id.post_image_container);
        LinearLayout.LayoutParams postImageContainerLayoutParams;
        int viewHeight = getView(R.id.content_container).getHeight();
        int postImageContainerHeight = viewHeight - bottomLayoutParams.height;
        if (postImageContainerHeight < imageView.getLayoutParams().height) {
            postImageContainerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageView.getLayoutParams().height);
        } else {
            postImageContainerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, postImageContainerHeight);
        }
        view.setLayoutParams(postImageContainerLayoutParams);
    }

    private String saveClipUrl() {
        return ImageUtil.saveClip(currentBitmap, FileUtils.File_CLIP);
    }

    public void setFilterBitmap(ExecutorService executorService, final BaseActivity baseActivity, GPUImageFilter filter, final Bitmap bitmap, final ImageView imageView, final int tempPosition) {
        baseActivity.showWaitDialog();
        GPUImage.getBitmapForFilter(executorService, bitmap, filter, new GPUImage.ResponseListener<Bitmap>() {
            @Override
            public void response(final Bitmap item) {
                ImageEditApplication.getInstance().handler.post(new Runnable() {
                    @Override
                    public void run() {
                        baseActivity.hideWaitDialog();
                        imageView.setImageBitmap(item);
                        currentBitmap = item;
                        lastFilterPosition = tempPosition;
                    }
                });
            }
        });
    }

    private void setImageVieSize(View view, int width, int height) {
        int fixSize = ViewSizeUtil.getCustomDimen(360);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width == height) {
            width = height = fixSize;
        } else {
            if (width > height) {
                height = height * fixSize / width;
                width = fixSize;
            } else {
                width = width * fixSize / height;
                height = fixSize;
            }
        }
        layoutParams.height = height;
        layoutParams.width = width;
    }

    private void setSelect(View v) {
        if (v != lastSelectView) {
            lastSelectView.setSelected(false);
        }
        v.setSelected(true);
        lastSelectView = v;
    }

    private void initImageEditData() {
        postImageEditors.add(getPostEditImageInfo(getString(R.string.post_image_crop), R.drawable.icon_editer_crop));
        postImageEditors.add(getPostEditImageInfo(getString(R.string.post_image_saturation), R.drawable.icon_editer_saturation));
//        if (isAllowTags) {
//            postImageEditors.add(getPostEditImageInfo(getString(R.string.post_image_friends), R.drawable.icon_editer_friends));
//            postImageEditors.add(getPostEditImageInfo(getString(R.string.post_image_product), R.drawable.icon_editer_product));
//        }
//        postImageEditors.add(getPostEditImageInfo(getString(R.string.post_image_description), R.drawable.icon_editer_description));
    }

    public static void startPostImageEditActivity(Activity context, String url) {
        Intent intent = new Intent(context, ImageEditActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public PostEditImageInfo getPostEditImageInfo(String desc, int resource) {
        return new PostEditImageInfo(desc, resource);
    }

    private void initImageFilterData() {
        String imageFilters = SharedPreferenceHelper.getInstance().getImageFilters(uuid);
        if (imageFilters.isEmpty()) {
            StringBuffer stringBuffer = new StringBuffer();
            int start = Integer.valueOf(GPUImageFilterTools.FilterType.I_1977);
            int end = Integer.valueOf(GPUImageFilterTools.FilterType.I_HEFE);
            for (int i = start; i < end; i++) {
                filters.add(String.valueOf(i));
                stringBuffer.append(filters.get(i - start));
                stringBuffer.append(",");
            }
            if (stringBuffer.length() > 0) {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            }
            SharedPreferenceHelper.getInstance().putImageFilters(uuid, stringBuffer.toString());
        } else {
            String[] split = imageFilters.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!split[i].isEmpty()) {
                    filters.add(split[i]);
                }
            }
        }
        filters.add(0, GPUImageFilterTools.FilterType.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_1001:
                    String filtersString = data.getStringExtra("filters");
                    String[] split = filtersString.split(",");
                    filters.clear();
                    filters.add(GPUImageFilterTools.FilterType.DEFAULT);
                    for (int i = 0; i < split.length; i++) {
                        if (!split[i].isEmpty()) {
                            filters.add(split[i]);
                        }
                    }
                    onImageFilterSelectUpdateRecyclerListener.addFilters();
                    recyclerViewBaseAdapter.notifyDataSetChanged();
                    break;
                case Constants.REQUEST_CODE_1002:
                    setResultBitmap(data.getStringExtra("url"));
                    break;

                case Constants.REQUEST_CODE_1004:
                    setResultBitmap(data.getStringExtra("url"));
                    break;
            }
            if (currentBitmap != null) {
                onImageFilterSelectUpdateRecyclerListener.setBitmap(currentBitmap);

            }
        }
    }

    private void setResultBitmap(String url) {
        currentBitmap = originalBitmap = ImageUtil.getSuitBitmap(url);
        setImageVieSize(imageView, currentBitmap.getWidth(), currentBitmap.getHeight());
        imageView = getView(R.id.post_image);
        imageView.setImageBitmap(currentBitmap);
        setBitmapContainerLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentBitmap != null && !currentBitmap.isRecycled()) {
            currentBitmap.recycle();
        }
        if (originalBitmap != null && !originalBitmap.isRecycled()) {
            originalBitmap.recycle();
        }

    }


}
