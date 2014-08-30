package com.copperlabs.GraphView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

public class MyView extends View {
	
	Paint paint_height = new Paint();
	int height,width;
	Path path;
	int graphDetail;
	float border = 20;
	float margin = border + 20;
    float graphheight;
    float graphwidth;
    Canvas myCanvas;
    
	public MyView(Context context, int i) {

		super(context);
		graphDetail = i;
	}

		@Override
		protected void onDraw(Canvas canvas) {

			// TODO Auto-generated method stub

			super.onDraw(canvas);
			myCanvas = canvas;
			
			paint_height.setTextAlign(Align.LEFT);
			    
			paint_height.setStrokeWidth(1);

			paint_height.setStyle(Paint.Style.STROKE);
				
			height = getHeight();
			width  =  getWidth();
		
			graphheight = height - (2 * border);
		    graphwidth = width - (2 * border);
		    
		    Log.d("width-height",""+width+"-"+height);
		    Log.d("graphWidth-graphHeight",""+graphwidth+"-"+graphheight);

			//drawing x and y axis
		    xAxis();
		    yAxis();
		    
		    paint_height.setColor(Color.BLACK);
			
		    path = new Path();	
		  //drawing the grapgh path w.r.t months vs respective weigths/heights/circumferences
		    if(graphDetail==1)
		    weight();
		    else if(graphDetail==2)
		    height();
		    else if(graphDetail==3)
		    circum();

			canvas.drawPath(path, paint_height);

		}
		
		
		private void xAxis() {
			// TODO Auto-generated method stub
			 String[] feetlabels={"7Feet","6Feet","5Feet","4Feet","3Feet","2Feet","1Feet","0Feet"}; 
			 int feets = feetlabels.length - 1;
			    for (int i = 0; i < feetlabels.length; i++) {
			    	if(i>=0&&i<feetlabels.length-1)
			    		paint_height.setColor(Color.WHITE);
			    	else
			    		paint_height.setColor(Color.BLACK);
			        float y = ((graphheight / feets) * i) + border;
			        myCanvas.drawLine(margin, y, width, y, paint_height);
			        paint_height.setColor(Color.BLACK);
			        myCanvas.drawText(feetlabels[i], 0, y, paint_height);
			    }
		}
		

		private void yAxis() {
			// TODO Auto-generated method stub
			 String[] monthlabels={"0","1","2","3","4","5","6","7","8","9","10","11","12"};		
			 int months = monthlabels.length - 1;
			    for (int i = 0; i < monthlabels.length; i++) {
			    	if(i>0&&i<monthlabels.length)
			    		paint_height.setColor(Color.WHITE);
			       // float x = ((graphwidth / months) * i) + margin;
			    	float x = ((800 / months) * i) + margin;
			    	myCanvas.drawLine(x, height - border, x, border, paint_height);
			        paint_height.setTextAlign(Align.CENTER);
			        if (i==monthlabels.length-1)
			        	paint_height.setTextAlign(Align.RIGHT);
			        if (i==0)
			        	paint_height.setTextAlign(Align.LEFT);
			        paint_height.setColor(Color.BLACK);
			        myCanvas.drawText(monthlabels[i], x, height - 4, paint_height);
			    }
		}
		

		private void weight() {
			// TODO Auto-generated method stub
			path.moveTo(40, (float) (height - (((height/6) * Weight.baby_weight[0])+20)));
			 path.addCircle(40, (float) (height - (((height/6) * Weight.baby_weight[0])+20)), 3, Path.Direction.CW);
			for (int i = 1; i < Weight.baby_months.length; i++){
				path.lineTo((float)((800 / 12) * Weight.baby_months[i]), (float)(724 - (((724/6) *Weight.baby_weight[i]))));
				path.addCircle((float)((800 / 12) * Weight.baby_months[i]), (float)(724 - (((724/6) *Weight.baby_weight[i]))), 3, Path.Direction.CW);
			}
		}

		private void height() {
			// TODO Auto-generated method stub
			 path.moveTo(40, (float) (height - (((height/6) * Height.baby_height[0])+20)));
			 path.addCircle(40, (float) (height - (((height/6) * Height.baby_height[0])+20)), 3, Path.Direction.CW);
				for (int i = 1; i < Height.baby_months.length; i++){
					path.lineTo((float)((800 / 12) * Height.baby_months[i]) +20, (float)(724 - (((724/6) *Height.baby_height[i]))));
					path.addCircle((float)((800 / 12) * Height.baby_months[i]) +20, (float)(724 - (((724/6) *Height.baby_height[i]))), 3, Path.Direction.CW);
				//	Log.d("month-height",""+GraphViewActivity.baby_months[i]+"---"+GraphViewActivity.baby_height[i]);
				}
		}
		
		private void circum() {
			// TODO Auto-generated method stub
			path.moveTo(40, (float) (height - (((height/6) * Circum.baby_circum[0])+20)));
			path.addCircle(40, (float) (height - (((height/6) * Circum.baby_circum[0])+20)), 3, Path.Direction.CW);
			for (int i = 1; i < Circum.baby_months.length; i++){
				path.lineTo((float)((800 / 12) * Circum.baby_months[i]) +20, (float)(724 - (((724/6) *Circum.baby_circum[i]))));
				path.addCircle((float)((800 / 12) * Circum.baby_months[i]) +20, (float)(724 - (((724/6) *Circum.baby_circum[i]))), 3, Path.Direction.CW);
			}
		}


	}
