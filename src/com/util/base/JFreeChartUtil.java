package com.util.base;

//import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;


import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartMouseEvent;
//import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
//import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class JFreeChartUtil extends ActionBase{
	
	public static String JFreeCharLink(List<List<BigDecimal>> listLink, HashMap<String, Object> linkMap, List<String> y) throws Exception{
		 CategoryDataset dataset = getDataSet(listLink,linkMap,y);
		    //2. 创建柱状图createStackedBarChart3D
		    JFreeChart chart = ChartFactory.createStackedBarChart3D(linkMap.get("title").toString(), // 图表标题
		            "123", // 目录轴的显示标签
		            "(ms)", // 数值轴的显示标签
		            dataset, // 数据集
		            PlotOrientation.HORIZONTAL, // 图表方向：水平、垂直
		            true, // 是否显示图例(对于简单的柱状图必须是false)
		            true, // 是否生成工具
		            false // 是否生成URL链接
		            );
		    //3. 设置整个柱状图的颜色和文字（char对象的设置是针对整个图形的设置）
		    chart.setBackgroundPaint(ChartColor.WHITE); // 设置总的背景颜色
		    
		    //4. 获得图形对象，并通过此对象对图形的颜色文字进行设置
		    CategoryPlot p = chart.getCategoryPlot();// 获得图表对象
		    p.setBackgroundPaint(ChartColor.lightGray);//图形背景颜色
		    p.setRangeGridlinePaint(ChartColor.WHITE);//图形表格颜色
		    
		    //5. 设置柱子宽度
		    BarRenderer renderer = (BarRenderer)p.getRenderer();
		    renderer.setMaximumBarWidth(0.06);
		    
		    //解决乱码问题
		 
			//6. 将图形转换为图片，传到前台
//		    String fileName = ServletUtilities.saveChartAsJPEG(chart, 300, 200, null, request.getSession());
		    return null;
	}
	
	// 获取一个演示用的组合数据集对象
	private static CategoryDataset getDataSet(List<List<BigDecimal>> listLink, HashMap<String, Object> linkMap, List<String> y) {
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    	for (int x = 0; x < listLink.get(1).size(); x++) {
	    		int xx;
	    		xx=-1;
	    		dataset.addValue(listLink.get(++xx).get(x),  "开始时间(ms)",y.get(x));
	    		dataset.addValue(listLink.get(++xx).get(x),  "DNS Lookup(ms)",y.get(x));
	    		dataset.addValue(listLink.get(++xx).get(x),  "Interval(ms)",y.get(x));
	    		dataset.addValue(listLink.get(++xx).get(x),  "SYN(ms)",y.get(x));
	    		dataset.addValue(listLink.get(++xx).get(x),  "Time to First Byte(ms)",y.get(x));
	    		dataset.addValue(listLink.get(++xx).get(x),  "Interactive(ms)",y.get(x));
	    		dataset.addValue(listLink.get(++xx).get(x),  "Delay(ms)",y.get(x));
	    		dataset.addValue(listLink.get(++xx).get(x),  "FIN(ms)",y.get(x));
			}
	    return dataset;
	}
}
