package com.gx.richtextlibrary;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gx.richtextlibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gx on 2017/7/22
 */

public class RichEditText extends ScrollView {

    private Context context;
    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LayoutInflater layoutInflater;
    public  LinearLayout linearLayout;// 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
//    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private EditText lastFocusEdit; // 最近被聚焦的EditText
//    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画

    public RichEditText(Context context) {
        this(context,null);
    }

    public RichEditText(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setupLayoutTransitions();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(linearLayout,layoutParams);
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        EditText editText = (DeletableEditText) layoutInflater.inflate(R.layout.richedittext,null);
        editText.setTag(viewTagIndex++);
        editText.setHint("编辑您的内容...");
        linearLayout.addView(editText, firstEditParam);
        lastFocusEdit = editText;
    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransitioner = new LayoutTransition();
        linearLayout.setLayoutTransition(mTransitioner);
        mTransitioner.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
            }
            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                }
            }
        });
        mTransitioner.setDuration(300);
    }

    /**
     * 插入一条文本编辑框
     * @param index
     * @param text
     */
    public void createEditText(int index, String text){
        EditText editText = (DeletableEditText) layoutInflater.inflate(R.layout.richedittext,null);
        editText.setTag(viewTagIndex++);
        editText.setHint("编辑您的内容...");
        editText.setText(text);
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lastFocusEdit = (DeletableEditText) v;
                }
            }
        });
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        });
        lastFocusEdit = editText;
        linearLayout.addView(editText, index);
    }


    /**
     * 添加图片之后，需要创建一个EditText
     * @param index
     */
    public void createEditText(int index){
        EditText editText = (DeletableEditText) layoutInflater.inflate(R.layout.richedittext,null);
        editText.setTag(viewTagIndex++);
        editText.setHint("编辑您的内容...");
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lastFocusEdit = (DeletableEditText) v;
                }
            }
        });
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        });
        lastFocusEdit = editText;
        linearLayout.addView(editText, index);
    }

    /**
     * 插入一个图片
     * @param index
     * @param url
     */
    public void createImageView(final int index, String url){
        final RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.richimageview,null);
        final CostomImageView imageview = (CostomImageView) relativeLayout.findViewById(R.id.edit_imageView);
        ImageView close = (ImageView) relativeLayout.findViewById(R.id.image_close);
        imageview.setTag(viewTagIndex++);
        imageview.setAbsolutePath(url);
        close.setVisibility(VISIBLE);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeView(relativeLayout);
                linearLayout.invalidate();
            }
        });
        linearLayout.addView(relativeLayout, index);
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
                        lp.bottomMargin = 16;
                        lp.topMargin = 8;
                        imageview.setLayoutParams(lp);
                    }
                });
    }

    /**
     * 插入一张图片
     */
    public void insertImage(String imagePath) {
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
        int lastEditIndex = linearLayout.indexOfChild(lastFocusEdit);
        if (lastEditStr.length() == 0 || editStr1.length() == 0) {
            // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            createImageView(lastEditIndex, imagePath);
        } else {
            // 如果EditText非空且光标不在最顶端，则需要添加新的imageView和EditText
            lastFocusEdit.setText(editStr1);
            String str = lastEditStr.substring(editStr1.length(), lastEditStr.length()).trim();
            if(!TextUtils.isEmpty(str)){
                createEditText(lastEditIndex + 1, str);
            }else{
                if (linearLayout.getChildCount() - 1 == lastEditIndex) {
                    createEditText(lastEditIndex + 1);
                }
            }
            createImageView(lastEditIndex + 1, imagePath);
            lastFocusEdit.requestFocus();
            lastFocusEdit.setSelection(0);
        }
        hideKeyBoard();
    }

    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt 光标所在的文本输入框
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        Log.d("GxMessage","onBackspacePress -- > " + startSelection);
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = linearLayout.indexOfChild(editTxt);
            View preView = linearLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上一个view对应的是图片
                    linearLayout.removeView(preView);
                } else if (preView instanceof EditText) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (DeletableEditText) preView;
                    String str2 = preEdit.getText().toString();
                    linearLayout.removeView(editTxt);
//                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    lastFocusEdit = preEdit;
                }
            }
        }
    }


    /**
     * 对外提供的接口, 生成编辑数据上传
     */
    public List<EditData> buildEditData() {
        List<EditData> dataList = new ArrayList<EditData>();
        int num = linearLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = linearLayout.getChildAt(index);
            EditData itemData = new EditData();
            if (itemView instanceof EditText) {
                EditText item = (DeletableEditText) itemView;
                itemData.inputStr = item.getText().toString();
            } else if (itemView instanceof RelativeLayout) {
                CostomImageView item = (CostomImageView) itemView.findViewById(R.id.edit_imageView);
                itemData.imagePath = item.getAbsolutePath();
            }
            dataList.add(itemData);
        }

        return dataList;
    }

    public class EditData {
        public String inputStr;
        public String imagePath;
    }


    /**
     * 隐藏小键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
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

    /**
     * 显示 HTML
     * @param richEditText
     * @param html
     */
    public void showContent(final RichEditText richEditText, final String html){
        post(new Runnable() {
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

    /**
     * 构建 HTML
     * @param richEditText
     * @return
     */
    public String buildHtml(RichEditText richEditText){
        StringBuilder stringBuilder = new StringBuilder();
        List<RichEditText.EditData> editList = richEditText.buildEditData();
        for (RichEditText.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                stringBuilder.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                stringBuilder.append("<img src=\"").append(itemData.imagePath).append("\"/>");
            }
        }
        String result = stringBuilder.toString();
        if(result.equals("") || TextUtils.isEmpty(result) || result == null){
            Toast.makeText(context, "请编辑内容！", Toast.LENGTH_SHORT).show();
            return "";
        }else{
            return result;
        }
    }

}
