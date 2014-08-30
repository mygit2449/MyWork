package com.daleelo.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.widget.ImageView;

import com.daleelo.R;

public class QiblahRotateBig extends ImageView {
  Paint paint;
  float direction = 0;

  public QiblahRotateBig(Context context) {
    super(context);

    paint = new Paint();
    paint.setColor(Color.WHITE);
    paint.setStrokeWidth(2);
    paint.setStyle(Style.STROKE);

    this.setImageResource(R.drawable.icon_compass_big);
  }

  @Override
  public void onDraw(Canvas canvas) {
    int height = this.getHeight();
    int width = this.getWidth();

    canvas.rotate(direction, width / 2, height / 2);
    super.onDraw(canvas);
  }

  public void setDirection(float direction) {
    this.direction = direction;
    this.invalidate();
  }

}
