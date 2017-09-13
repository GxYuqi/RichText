package com.gx.richtextlibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gx.richtextlibrary.R;

/**
 * Created by gx on 2017/7/22
 */

public class RichTextView extends ScrollView {

    private Context context;
    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayout;// 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup

    private Activity activity;
    public void setWatchActivity(Activity activity){
        this.activity = activity;
    }

    public RichTextView(Context context) {
        this(context, null);
    }
    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(linearLayout,layoutParams);
    }

    /**
     * 插入文字
     * @param index
     * @param text
     */
    public void createTextView(int index, String text){
        TextView textView = (TextView) layoutInflater.inflate(R.layout.richtextview,null);
        textView.setTag(viewTagIndex++);
        textView.setText(text);
        linearLayout.addView(textView, index);
    }

    /**
     * 插入图片
     * @param index
     * @param url
     */
    public void createImageView(final int index, String url){
        final RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.richimageview,null);
        final CostomImageView imageview = (CostomImageView) relativeLayout.findViewById(R.id.edit_imageView);
        imageview.setTag(viewTagIndex++);
        imageview.setAbsolutePath(url);
        ImageView close = (ImageView) relativeLayout.findViewById(R.id.image_close);
        close.setVisibility(GONE);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeView(relativeLayout);
                linearLayout.invalidate();
            }
        });
        linearLayout.addView(relativeLayout, index);
        if(activity != null){
            if(!activity.isDestroyed()){
                Glide.with(context)
                        .load(url)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                imageview.setImageBitmap(bitmap);
                                // 调整imageView的高度
                                int imageHeight = 500;
                                if (bitmap != null) {
                                    imageHeight = linearLayout.getWidth() * bitmap.getHeight() / bitmap.getWidth();
                                }
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, imageHeight);
                                lp.bottomMargin = 10;
                                lp.topMargin = 4;
                                imageview.setLayoutParams(lp);
                            }
                        });
            }
            return;
        }
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageview.setImageBitmap(bitmap);
                        // 调整imageView的高度
                        int imageHeight = 500;
                        if (bitmap != null) {
                            imageHeight = linearLayout.getWidth() * bitmap.getHeight() / bitmap.getWidth();
                        }
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, imageHeight);
                        lp.bottomMargin = 10;
                        lp.topMargin = 4;
                        imageview.setLayoutParams(lp);
                    }
                });
    }

    /**
     * 获取最后一个子View的位置
     * @return
     */
    public int getLastIndex(){
        return linearLayout.getChildCount();
    }

    /**
     * 清除所有的view
     */
    public void clearAllLayout() {
        linearLayout.removeAllViews();
    }

}
