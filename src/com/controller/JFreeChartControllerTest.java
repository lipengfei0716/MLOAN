package com.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

import com.util.base.ActionBase;
//http://vcsos.com/article/pageSource/120331/20120331082642.shtml
@Controller
@RequestMapping("chart")
public class JFreeChartControllerTest extends ActionBase{
    @RequestMapping("/resultmap")
    public String resultmap(){
            return "TestFile";
    }
    
    //显示柱状图
@RequestMapping(value = "/getColumnChart")
public ModelAndView getColumnChart(HttpServletRequest request,HttpServletResponse response, ModelMap modelMap) throws Exception{
    //1. 获得数据集合
    CategoryDataset dataset = getDataSet();
    //2. 创建柱状图createStackedBarChart3D
    JFreeChart chart = ChartFactory.createStackedBarChart3D("学生对教师授课满意度", // 图表标题
            "", // 目录轴的显示标签
            "数值", // 数值轴的显示标签
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
    String fileName = ServletUtilities.saveChartAsJPEG(chart, 3000, 2000, null, request.getSession());
    String chartURL=request.getContextPath() + "/chart?filename="+fileName;
    modelMap.put("chartURL", chartURL);
    return new ModelAndView("getColumnChart",modelMap);
}

//设置文字样式


// 获取一个演示用的组合数据集对象
private static CategoryDataset getDataSet() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
  
    dataset.addValue(30, "1", "普通动物学");
    dataset.addValue(40, "1", "生物学");
    dataset.addValue(30, "1", "动物解剖学");
    dataset.addValue(60, "1", "生物理论课");
    dataset.addValue(10, "1", "动物理论课");
    dataset.addValue(10, "1", "动物理论课2");
    dataset.addValue(10, "1", "动物理论课3");
    dataset.addValue(10, "1", "动物理论课4");
    dataset.addValue(10, "1", "动物理论课5");
    dataset.addValue(10, "1", "动物理论课6");
    dataset.addValue(10, "1", "动物理论课7");
    dataset.addValue(10, "1", "动物理论课8");
    dataset.addValue(10, "1", "动物理论课9");
    dataset.addValue(10, "1", "动物理论课11");
    dataset.addValue(10, "33", "动物理论课");
    dataset.addValue(10, "1", "动物理论课122");
    
    dataset.addValue(10, "12", "动物理论课131");
    dataset.addValue(10, "12", "动物理论课12131");
    dataset.addValue(10, "12", "动物理论课113212");
    dataset.addValue(10, "12", "动物理论课1132");
    dataset.addValue(10, "12", "动物理论课1312");
    dataset.addValue(10, "12", "动物理论课1312");
    
    dataset.addValue(10, "12", "动物理论课112123");
    dataset.addValue(10, "12", "动物理论课13123");
    dataset.addValue(10, "12", "动物理论课");
    
    return dataset;
}


}
