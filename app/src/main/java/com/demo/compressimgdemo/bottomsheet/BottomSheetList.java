package com.demo.compressimgdemo.bottomsheet;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by chenshuang on 2017/8/21.
 */

public class BottomSheetList {

    private BottomSheetDialog dialog;
    private ListTextCenterAdapter adapter;

    public BottomSheetList(Context context, String[] data, OnItemClickListener onItemClickListener) {
        RecyclerView recyclerView = new RecyclerView(context);
        dialog = new BottomSheetDialog(context);
        dialog.setContentView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ListTextCenterAdapter(context, data, onItemClickListener, dialog);
        recyclerView.setAdapter(adapter);
    }

    public void show() {
        if (dialog != null)
            dialog.show();
    }


    public BottomSheetList show1() {
        if (dialog != null)
            dialog.show();
        return this;
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}
