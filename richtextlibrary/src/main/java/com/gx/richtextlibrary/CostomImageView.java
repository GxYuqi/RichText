package com.gx.richtextlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

/**
 * //    ┏┓　　　┏┓
 * //  ┏┛┻━━━┛┻┓
 * //  ┃　　　　　　　┃
 * //  ┃　　　━　　　┃
 * //  ┃　┳┛　┗┳　┃
 * //  ┃　　　　　　　┃
 * //  ┃　　　┻　　　┃
 * //  ┃　　　　　　　┃
 * //  ┗━┓　　　┏━┛
 * //      ┃　　　┃           神兽保佑
 * //      ┃　　　┃           代码无BUG
 * //      ┃　　　┗━━━┓
 * //      ┃　　　　　　　┣┓
 * //      ┃　　　　　　　┏┛
 * //      ┗┓┓┏━┳┓┏┛
 * //        ┃┫┫　┃┫┫
 * //        ┗┻┛　┗┻┛
 * <p>
 * Created by gx on 2017/7/23
 */

public class CostomImageView extends android.support.v7.widget.AppCompatImageView {
    public CostomImageView(Context context) {
        super(context);
    }

    public CostomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CostomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private String absolutePath;
    private Bitmap bitmap;

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
