package com.example.school.ui;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.school.R;

public class ViewRoundCorners50 extends View {

        //    private final int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        private int height, width = 50;
        private Paint paint = new Paint();
        private boolean isInit;

        public ViewRoundCorners50(Context context) {
            super(context);
        }

        public ViewRoundCorners50(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ViewRoundCorners50(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        private void initClock() {
            height = 50;
            width = 50;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (!isInit) {
                initClock();
            }
            canvas.drawColor(getResources().getColor(R.color.null_color));
            drawCircle(canvas);
            postInvalidateDelayed(500);
            invalidate();
        }


        private void drawCircle(Canvas canvas) {
            paint.setColor(getResources().getColor(android.R.color.white));
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            canvas.drawOval(width / 2f, height / 2f,width / 2f, height / 2f, paint);
        }

    
}