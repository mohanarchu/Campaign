package com.video.aashi.campaign.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;

import com.video.aashi.campaign.R;

public class CTextView extends AppCompatTextView {

    private TypedArray a;
    private boolean isUnderline;

    private Rect lineBoundsRect;
    private Paint underlinePaint;

    public CTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            init(attrs);
        }
    }

    public CTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(attrs);
        }
    }

    public CTextView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(null);
        }
    }


    private void init(AttributeSet attrs) {
        if (attrs != null) {
            float density = getContext().getResources().getDisplayMetrics().density;
            a = getContext().obtainStyledAttributes(attrs, R.styleable.CTextView);
            isUnderline = a.getBoolean(R.styleable.CTextView_isUnderline, false);
            int underlineColor = a.getColor(R.styleable.CTextView_underlineColor, 0xFFFF0000);
            float underlineWidth = a.getDimension(R.styleable.CTextView_underlineWidth, density * 2);
            String fontName = a.getString(R.styleable.CTextView_fontName);

            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                setTypeface(myTypeface);
            }
            a.recycle();

            lineBoundsRect = new Rect();
            underlinePaint = new Paint();
            underlinePaint.setStyle(Paint.Style.STROKE);
            underlinePaint.setColor(underlineColor); //color of the underline
            underlinePaint.setStrokeWidth(underlineWidth);
        }
    }

    public void setFontFace(String fontFace) {
        if (fontFace != null) {
            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontFace);
            setTypeface(myTypeface);
        }
    }

    public boolean isUnderline() {
        return isUnderline;
    }

    @ColorInt
    public int getUnderlineColor() {
        return underlinePaint.getColor();
    }

    public void setUnderLineColor(@ColorInt int mColor) {
        underlinePaint.setColor(mColor);
        invalidate();
    }

    public float getUnderlineWidth() {
        return underlinePaint.getStrokeWidth();
    }

    public void setUnderlineWidth(float mStrokeWidth) {
        underlinePaint.setStrokeWidth(mStrokeWidth);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isUnderline()) {
            int count = getLineCount();

            final Layout layout = getLayout();
            float x_start, x_stop, x_diff;
            int firstCharInLine, lastCharInLine;

            for (int i = 0; i < count; i++) {
                int baseline = getLineBounds(i, lineBoundsRect);
                firstCharInLine = layout.getLineStart(i);
                lastCharInLine = layout.getLineEnd(i);

                x_start = layout.getPrimaryHorizontal(firstCharInLine);
                x_diff = layout.getPrimaryHorizontal(firstCharInLine + 1) - x_start;
                x_stop = layout.getPrimaryHorizontal(lastCharInLine - 1) + x_diff;

                canvas.drawLine(x_start, baseline + getUnderlineWidth(), x_stop, baseline + getUnderlineWidth(), underlinePaint);
            }
        }
        super.onDraw(canvas);
    }
}