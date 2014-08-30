package com.graph;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class GraphViewExpActivity extends Activity {
    /** Called when the activity is first created. */
	private GraphicalView mChartView;
	 private XYSeries mCurrentSeries;
	 List<double[]> Xvalues = null;
	 List<double[]> Yvalues = null;

	  private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	  private XYSeriesRenderer mCurrentRenderer;
	  
	  @Override
	  protected void onRestoreInstanceState(Bundle savedState) {
	    super.onRestoreInstanceState(savedState);
	    mRenderer = (XYMultipleSeriesRenderer) savedState.getSerializable("renderer");
	    mCurrentSeries = (XYSeries) savedState.getSerializable("current_series");
	    mCurrentRenderer = (XYSeriesRenderer) savedState.getSerializable("current_renderer");
	  }
	  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setPointSize(10);
        
        Xvalues = new ArrayList<double[]>();
        Yvalues = new ArrayList<double[]>();

        Yvalues.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1 });
        Xvalues.add(new double[] { 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11 });
      
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
   
      outState.putSerializable("current_series", mCurrentSeries);
      outState.putSerializable("current_renderer", mCurrentRenderer);
      outState.putSerializable("renderer", mRenderer);

    }
    @Override
    protected void onResume() 
    {
    	  super.onResume();
    	  if (mChartView == null) {
    	    LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
    	    mChartView = ChartFactory.getLineChartView(this,  buildDataset(Xvalues, Yvalues),	mRenderer);
    	    mRenderer.setClickEnabled(true);
    	    mRenderer.setSelectableBuffer(100);
    	    layout.addView(mChartView, new LayoutParams	(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	  } else {
    	    mChartView.repaint();
    	  }
    	}

    protected XYMultipleSeriesDataset buildDataset(List<double[]> xValues,  List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, xValues, yValues, 0);
        return dataset;
      }
    
    public void addXYSeries(XYMultipleSeriesDataset dataset, List<double[]> xValues, List<double[]> yValues, int scale) {
    	 String[] titles = new String[] { "Crete"};
        for (int i = 0; i < titles.length; i++) {
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

    
    
}