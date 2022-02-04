package com.theword.thedigitalword.component;
import android.content.SyncStatusObserver;
import android.os.Handler;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;


public class LongClickMovementMethod extends LinkMovementMethod {

    private Long lastClickTime = 0l;
    private long LONG_CLICK_TIME = 500;
    private int lastX = 0;
    private int lastY = 0;
    private Handler longClickHandler = null;
    private Runnable runnable;
    private boolean isLongPressed = false;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            if(longClickHandler!=null){
                longClickHandler.removeCallbacksAndMessages(null);
            }
        }

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_MOVE||
                action == MotionEvent.ACTION_DOWN) {

            int x = (int) event.getX();
            int y = (int) event.getY();
            lastX = x;
            lastY = y;
            int deltaX = Math.abs(x-lastX);
            int deltaY = Math.abs(y-lastY);

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            LongClickableSpan[] link = buffer.getSpans(off, off, LongClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    if(longClickHandler!=null){
                        longClickHandler.removeCallbacksAndMessages(null);
                    }
                    if (!isLongPressed) {
                        link[0].onClick(widget);
                    }
                    isLongPressed = false;
                } else {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));

                    longClickHandler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if(!isLongPressed) {
                                        link[0].onLongClick(widget);
                                        isLongPressed = true;
                                        longClickHandler.removeCallbacksAndMessages(null);
                                    }
                                }
                            }, LONG_CLICK_TIME);

                }
                return true;
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }


    public static MovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new LongClickMovementMethod();
            sInstance.longClickHandler = new Handler();
        }

        return sInstance;
    }
    private static LongClickMovementMethod sInstance;
}