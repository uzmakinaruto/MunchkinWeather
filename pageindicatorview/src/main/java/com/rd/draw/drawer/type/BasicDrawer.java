package com.rd.draw.drawer.type;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import androidx.annotation.NonNull;

import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Indicator;
import com.rd.pageindicatorview.R;

public class BasicDrawer extends BaseDrawer {

    private Paint strokePaint;
    /*private Bitmap locateBitmap;
    private Bitmap locateBitmap_selected;*/
    private Path path;

    public BasicDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(indicator.getStroke());
        /*locateBitmap_selected = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_locate_selected);
        locateBitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_locate);*/
        path = new Path();

    }

    public void draw(
            @NonNull Canvas canvas,
            int position,
            boolean isSelectedItem,
            int coordinateX,
            int coordinateY) {

        float radius = indicator.getRadius();
        int strokePx = indicator.getStroke();
        float scaleFactor = indicator.getScaleFactor();

        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int selectedPosition = indicator.getSelectedPosition();
        AnimationType animationType = indicator.getAnimationType();

        if (animationType == AnimationType.SCALE && !isSelectedItem) {
            radius *= scaleFactor;

        } else if (animationType == AnimationType.SCALE_DOWN && isSelectedItem) {
            radius *= scaleFactor;
        }

        int color = unselectedColor;
        if (position == selectedPosition) {
            color = selectedColor;
        }

        Paint paint;
        if (animationType == AnimationType.FILL && position != selectedPosition) {
            paint = strokePaint;
            paint.setStrokeWidth(strokePx);
        } else {
            paint = this.paint;
        }
        paint.setColor(color);
        /**Edit here*/
        if (indicator.isLocateEnable() && position == 0) {
            Log.v("onDraw", "x:" + coordinateX + "y:" + coordinateY);
            /**(coordinateX + radius,coordinateY+radius)*/
            path.moveTo(coordinateX + radius, coordinateY - radius);
            path.lineTo(coordinateX - radius, coordinateY);
            path.lineTo(coordinateX -1, coordinateY+1);
            path.lineTo(coordinateX, coordinateY + radius);
            path.close();
            canvas.drawPath(path, paint);
        } else {
            canvas.drawCircle(coordinateX, coordinateY, radius, paint);
        }
    }
}
