package com.gx.richtext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.gx.richtextlibrary.RichTextView;
import com.gx.richtextlibrary.StringUtils;

import java.util.List;

/**
 * Created by Gx on 2017/7/23.
 */

public class LoadingActivity extends AppCompatActivity {

    private RichTextView richTextView;
    private String html = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        html = getIntent().getStringExtra("html");

        // 初始化RichTextView
        richTextView = (RichTextView) findViewById(R.id.loading_richtext);

        // 加载HTML，使用正则表达式区分出文字和图片，然后加载
        richTextView.showContent(richTextView, html);
        // 给图片设置点击  可用于查看图片
        richTextView.setOnClickListener(new RichTextView.OnClickListener() {
            @Override
            public void onClick(ImageView view, String imageUrl) {
                Toast.makeText(LoadingActivity.this, "点击了图片", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
