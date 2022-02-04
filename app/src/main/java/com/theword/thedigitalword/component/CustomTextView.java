package com.theword.thedigitalword.component;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomTextView(Context context) {
        super(context);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // set fitting lines to prevent cut text
        int fittingLines = h / this.getLineHeight();
        if (fittingLines > 0) {
            this.setLines(fittingLines);
            this.setEllipsize(TextUtils.TruncateAt.END);
        }
    }
}
