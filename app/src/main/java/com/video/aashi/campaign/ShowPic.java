package com.video.aashi.campaign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.ByteArrayInputStream;

public class ShowPic extends PopupWindow
{
    Context context;
    PhotoView imageView;
    byte[] fileName;
    PhotoViewAttacher mAttacher;
    View view;
    public ShowPic(Context context, byte[] fileName, View location) {
        super(context);
        this.context = context;
        this.fileName = fileName;
        this.view = location;
    }
    public  void initiateLayout()
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.test, null);
        ImageView image;
        imageView = new PhotoView(context);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setContentView(imageView);
        setFocusable(true);
        setBackgroundDrawable( new ColorDrawable(Color.BLACK));
        image=layout.findViewById(R.id.loadimage);
        mAttacher = new PhotoViewAttacher(image);
        mAttacher.setZoomable(true);
        showAtLocation(view,Gravity.CENTER,0,0);
        byte[] outImage = fileName;
        if (outImage.length != 0 ) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            Glide.with(context)
                    .load(theImage)
                    .into(imageView);

        }


    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
    }
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
    }
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
    }
    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
    }
    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);
    }
    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }


}
