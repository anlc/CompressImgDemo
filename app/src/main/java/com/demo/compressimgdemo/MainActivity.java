package com.demo.compressimgdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo.compressimgdemo.bottomsheet.BottomSheetList;
import com.demo.compressimgdemo.bottomsheet.OnItemClickListener;
import com.demo.compressimgdemo.compress.Compress;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String[] str = new String[]{"删除", "显示", "质量压缩", "大小压缩", "采样率压缩", "lib jpeg", "图片信息", "test"};

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private PhotoView imageView;
    private String absPath;
    private TextView pathView;
    private TextView imgInfo;
    private Adapter adapter;

    private String qualityName = "/quality.jpg";
    private String sizeName = "/size.jpg";
    private String samplingName = "/sampling.jpg";

    private JpegLib jpegLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onRefresh();
    }

    private void showImg(String path, int type) {
        File srcFile = new File(path);
        Bitmap bitmap = null;
        switch (type) {
            case 0://删除
                CompressUtil.deleteFile(srcFile);
                onRefresh();
                return;
            case 1://显示
                bitmap = BitmapFactory.decodeFile(path);
                break;
            case 2: {//质量压缩
                Bitmap srcBitmap = BitmapFactory.decodeFile(path);
                File resultFile = new File(absPath + qualityName);
                bitmap = CompressUtil.compressWithQuality(srcBitmap, resultFile);
                break;
            }
            case 3: {//大小压缩
                Bitmap srcBitmap = BitmapFactory.decodeFile(path);
                File resultFile = new File(absPath + sizeName);
                CompressUtil.compressWithSize(srcBitmap, resultFile);
                bitmap = BitmapFactory.decodeFile(resultFile.getAbsolutePath());
                break;
            }
            case 4://采样率压缩
                File resultFile = new File(absPath + samplingName);
                CompressUtil.compressSamplingRate(path, resultFile);
                bitmap = BitmapFactory.decodeFile(resultFile.getAbsolutePath());
                break;
            case 5://lib jpeg
                if (jpegLib == null) {
                    jpegLib = new JpegLib();
                }
                jpegLib.compress();
                return;
            case 6://图片信息
                bitmap = BitmapFactory.decodeFile(path);
                showImgInfo(bitmap);
                return;
            case 7://
//                bitmap = BitmapFactory.decodeFile(path);
//                showImgInfo(bitmap);
                Compress.create().load(path).saveFile(new File(absPath + "/test.jpg")).quality(60).compress(new Compress.CallBack() {
                    @Override
                    protected void onStart() {

                    }

                    @Override
                    protected void onSuccess(List<Bitmap> bitmaps) {
                        imageView.setImageBitmap(bitmaps.get(0));
                        imageView.setVisibility(View.VISIBLE);
                        onRefresh();
                    }

                    @Override
                    protected void onError() {

                    }
                });
                return;

        }
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        absPath = file.getAbsolutePath();
        pathView.setText(absPath + " :: " + file.list().length);

        String[] paths = file.list();
        List<Bean> data = new ArrayList<>();
        for (int i = 0; i < paths.length; i++) {
            if (paths[i].contains("jpg")) {
                String size = FileUtil.formatFileSize(FileUtil.getFileSize(new File(absPath + "/" + paths[i])));
                data.add(new Bean(absPath + "/" + paths[i], paths[i] + ":" + size));
            }
        }
        adapter.setData(data);
        refreshLayout.setRefreshing(false);
    }

    private void showImgInfo(Bitmap bitmap) {
        StringBuilder builder = new StringBuilder();
        builder.append("width: " + bitmap.getWidth() + "\n");
        builder.append("height: " + bitmap.getHeight() + "\n");
        imgInfo.setText(builder.toString());
        imgInfo.setVisibility(View.VISIBLE);
    }

    private void showDialog(final String path) {
        new BottomSheetList(this, str, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showImg(path, position);
            }
        }).show();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView item_view;
        ImageView item_img;

        public Holder(View itemView) {
            super(itemView);
            item_view = itemView.findViewById(R.id.item_text_view);
            item_img = itemView.findViewById(R.id.item_img);
        }
    }

    class Adapter extends BaseAdapter<Bean, Holder> {

        public Adapter(Context context) {
            super(context, R.layout.item_view);
        }

        @Override
        protected Holder newHolder(View view) {
            return new Holder(view);
        }

        @Override
        protected void onBind(Holder holder, final int position) {
            holder.item_view.setText(data.get(position).name);
//            Bitmap bitmap = BitmapFactory.decodeFile(data.get(position).path);
//            holder.item_img.setImageBitmap(bitmap);

            Glide.with(context).load(data.get(position).path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(holder.item_img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(data.get(position).path);
                }
            });
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.list_view);
        refreshLayout = findViewById(R.id.refresh_layout);
        pathView = findViewById(R.id.path);
        imgInfo = findViewById(R.id.info);

        adapter = new Adapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        refreshLayout.setOnRefreshListener(this);

        imageView = findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.GONE);
            }
        });
        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgInfo.setVisibility(View.GONE);
            }
        });
    }

}
