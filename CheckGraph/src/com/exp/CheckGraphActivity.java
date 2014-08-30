package com.exp;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CheckGraphActivity extends Activity {
	/** Called when the activity is first created. */
	
	Button graph_btn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		graph_btn = (Button)findViewById(R.id.click_graph);
		
		graph_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(getClass().getSimpleName(), "button pressed ");
				Intent graph_intent = null;
				graph_intent = execute(CheckGraphActivity.this);
				startActivity(graph_intent);
			}
		});
	}
	
	/**
	   * Builds an XY multiple dataset using the provided values.
	   * 
	   * @param titles the series titles
	   * @param xValues the values for the X axis
	   * @param yValues the values for the Y axis
	   * @return the XY multiple dataset
	   */
	  protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,  List<double[]> yValues) {
	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    addXYSeries(dataset, titles, xValues, yValues, 0);
	    return dataset;
	  }

	  public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues, List<double[]> yValues, int scale) {
	    int length = titles.length;
	    for (int i = 0; i < length; i++) {
	      XYSeries series = new XYSeries(titles[i], scale);
	      double[] xV = xValues.get(i);
	      double[] yV = yValues.get(i);
	      int seriesLength = xV.length;
	      for (int k = 0; k < seriesLength; k++) {
	        series.add(xV[k], yV[k]);
	      }
	      dataset.addSeries(series);
	    }
	  }
	  
	  
	  
	  protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
		    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    setRenderer(renderer, colors, styles);
		    return renderer;
		  }

		  protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
		    renderer.setAxisTitleTextSize(16);
		    renderer.setChartTitleTextSize(20);
		    renderer.setLabelsTextSize(15);
		    renderer.setLegendTextSize(15);
		    renderer.setPointSize(5f);
		    renderer.setMargins(new int[] { 20, 30, 15, 20 });
		    int length = colors.length;
		    for (int i = 0; i < length; i++) {
		      XYSeriesRenderer r = new XYSeriesRenderer();
		      r.setColor(colors[i]);
		      r.setPointStyle(styles[i]);
		      renderer.addSeriesRenderer(r);
		    }
		  }
		  
		  
		  public Intent execute(Context context) 
		  {
			  
			    String[] titles = new String[] { "Crete"};
			    List<double[]> x = new ArrayList<double[]>();
			    for (int i = 0; i < titles.length; i++) {
			      x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24});
			    }
			    List<double[]> values = new ArrayList<double[]>();
			    values.add(new double[] { 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10,13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24});
			    int[] colors = new int[] { Color.BLUE};
			    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE};
			    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
			    int length = renderer.getSeriesRendererCount();
			    for (int i = 0; i < length; i++) {
			      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
			    }
			    setChartSettings(renderer, "Weight Graph", "Month", "weight", 0.5, 12.5, 0, 40, Color.LTGRAY, Color.LTGRAY);
			    renderer.setXLabels(12);
			    renderer.setYLabels(10);
			    renderer.setShowGrid(true);
			    renderer.setShowCustomTextGrid(true);
			    renderer.setXLabelsAlign(Align.RIGHT);
			    renderer.setYLabelsAlign(Align.RIGHT);
			    renderer.setZoomButtonsVisible(true);
			    renderer.setPanLimits(new double[] { -10, 40, -10, 100 });
			    renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });

			    Intent intent = ChartFactory.getLineChartIntent(context, buildDataset(titles, x, values),
			        renderer, "Weight Graph");
			    return intent;
			  }
		  
		  protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
			      int labelsColor) {
			    renderer.setChartTitle(title);
			    renderer.setXTitle(xTitle);
			    renderer.setYTitle(yTitle);
			    renderer.setXAxisMin(xMin);
			    renderer.setXAxisMax(xMax);
			    renderer.setYAxisMin(yMin);
			    renderer.setYAxisMax(yMax);
			    renderer.setAxesColor(axesColor);
			    renderer.setLabelsColor(labelsColor);
			  }
}