package com.gx.richtext;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gx.richtextlibrary.ImageUtils;
import com.gx.richtextlibrary.RichEditText;
import com.gx.richtextlibrary.SDCardUtil;
import com.gx.richtextlibrary.ScreenUtils;
import com.gx.richtextlibrary.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by Gx on 2017/7/23.
 */
public class EditActivity extends AppCompatActivity {

    private String html = "";

    private RichEditText richEditText;

    private AlertDialog pullLoading;
    private StringBuilder stringBuilder;
    private TextView pushNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        html = getIntent().getStringExtra("html");

        // 插入图片点击事件
        findViewById(R.id.edit_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callGallery();
            }
        });

        // 编辑完成之后保存
        findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEditData();
            }
        });

        // 初始化 RichTextEdit
        richEditText = (RichEditText) findViewById(R.id.edit_richtext);

        // 如果是编辑 就 先加载
        if(!TextUtils.isEmpty(html)){
            richEditText.post(new Runnable() {
                @Override
                public void run() {
                    richEditText.clearAllLayout();
                    List<String> list = StringUtils.cutStringByImgTag(html);
                    for (String s:list){
                        if(s.contains("<img") && s.contains("src=")){
                            richEditText.createImageView(richEditText.getLastIndex(),StringUtils.getImgSrc(s));
                        }else{
                            richEditText.createEditText(richEditText.getLastIndex(),s);
                        }
                    }
                    richEditText.createEditText(richEditText.getLastIndex());
                    // 加载完毕之后将视图移动到最底部，方便继续编辑
                    richEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            richEditText.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    },500);
                }
            });
        }

    }

    /**
     * 图片选择器，选择好图片之后的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == 1){
                    //处理调用系统图库
                } else if (requestCode == PhotoPicker.REQUEST_CODE){
                    ArrayList<String> photoPaths = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    int width = ScreenUtils.getScreenWidth(this);
                    int height = ScreenUtils.getScreenHeight(this);
                    showPushLoadingDialog();
                    pushImage(0, photoPaths, width, height);
                }
            }
        }
    }

    /**
     * 上传图片
     * @param position
     * @param imagePath
     * @param width
     * @param height
     */
    public void pushImage(final int position, final ArrayList<String> imagePath, final int width, final int height){
        if(position>=imagePath.size()){
            pullLoading.dismiss();
            return;
        }
        String path = imagePath.get(position);
        Log.d("GxMessage","原始图片路径："+path);
        Bitmap bitmap = ImageUtils.getSmallBitmap(path, width, height);//压缩图片
        path = SDCardUtil.saveToSdCard(path, bitmap);
        pushNum.setText("正在上传 "+(position+1)+"/"+imagePath.size()+" ...");
        File file = new File(path);
        Log.d("GxMessage","压缩后图片路径："+file.toString());

        /**
         * 注：此处能拿到 Bitmap 和 File ，后续的上传请自行操作
         */
        // 此处为了模拟上传完成的交互，故使用了异步
        richEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                /** 上传成功之后，服务端需要返回一个图片的url地址，拿到地址然后去插入图片 */
//                String url = "";
//                richEditText.insertImage(url);
                pullLoading.dismiss();
            }
        },3000);
    }

    /**
     * 上传图片的Dialog
     */
    private void showPushLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        pullLoading = builder.create();
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_push_img, null);
        pushNum = (TextView) view.findViewById(R.id.push_num);
        pullLoading.setView(view);
        pullLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pullLoading.setCancelable(false);
        pullLoading.show();
    }

    /**
     * 调用图库选择
     */
    private void callGallery(){
        //调用第三方图库选择
        PhotoPicker.builder()
                .setPhotoCount(1)//可选择图片数量 因为涉及到上传图片，所以此处建议 1 张
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(false)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    /**
     * 编辑完成保存，stringBuilder.toString() 的结果就是一个编辑之后的 html
     */
    private void getEditData() {
        stringBuilder = new StringBuilder();
        View view = richEditText.getChildAt(0);
        if(view instanceof EditText){
            EditText editText = (EditText) view;
            if(TextUtils.isEmpty(editText.getText().toString().trim())){
                return;
            }
        }
        List<RichEditText.EditData> editList = richEditText.buildEditData();
        for (RichEditText.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                stringBuilder.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                stringBuilder.append("<img src=\"").append(itemData.imagePath).append("\"/>");
            }
        }
        Log.d("GxMessage", "stringBuilder = " + stringBuilder.toString());
    }

}
