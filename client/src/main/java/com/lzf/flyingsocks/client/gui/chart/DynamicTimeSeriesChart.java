package com.lzf.flyingsocks.client.gui.chart;

import org.apache.commons.lang3.time.DateUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.RangeType;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.ui.RectangleInsets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lzf abc123lzf@126.com
 * @since 2020/12/24 1:47
 */
public final class DynamicTimeSeriesChart {

    /**
     * 蓝色主题
     */
    public static final int STYLE_BLUE = 0;

    /**
     * 紫色主题
     */
    public static final int STYLE_PURPLE = 1;

    /**
     * JFreeChart图例
     */
    private final JFreeChart chart;

    /**
     * 最大时间间隔，单位秒
     */
    private final int interval;

    /**
     * 动态数据集
     */
    private final DynamicTimeSeriesCollection dataset;


    public DynamicTimeSeriesChart(String title, String xAxisName, String yAxisName, int interval, int style) {
        DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(1, interval, new Second());
        dataset.setTimeBase(new Second(DateUtils.addSeconds(new Date(), -interval)));
        dataset.addSeries(new float[0], 0, "");

        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, xAxisName, yAxisName, dataset, false, false, false);

        if (style == STYLE_BLUE) {
            initialChartStyle(chart, Color.white, new Color(17, 125, 187),
                    new Color(217, 234, 244),
                    new Color(217, 231, 241),
                    new Color(75, 157, 203));
        } else if (style == STYLE_PURPLE) {
            initialChartStyle(chart, Color.white, new Color(139, 18, 174),
                    new Color(236, 222, 240),
                    new Color(244, 242, 244),
                    new Color(149, 40, 180));
        } else {
            throw new IllegalArgumentException("style");
        }

        this.interval = interval;
        this.dataset = dataset;
        this.chart = chart;
    }

    /**
     * 初始化图表样式
     * @param chart JFreeChart实例
     * @param background 背景颜色
     * @param outline 图标四周框颜色
     * @param gridline 坐标线颜色
     * @param line 折线颜色
     * @param fill 折线下方填充的颜色
     */
    private static void initialChartStyle(JFreeChart chart, Paint background, Paint outline, Paint gridline, Paint line, Paint fill) {
        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(background);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(gridline);
        plot.setDomainGridlineStroke(new BasicStroke(1.5f));

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(gridline);
        plot.setRangeGridlineStroke(new BasicStroke(1.5f));

        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setOutlinePaint(outline);

        XYAreaRenderer renderer = new XYAreaRenderer(XYAreaRenderer.AREA);
        renderer.setOutline(true);

        renderer.setBaseOutlinePaint(fill);
        renderer.setSeriesFillPaint(0, fill);
        renderer.setSeriesPaint(0, line);

        plot.setRenderer(renderer);

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("mm:ss"));

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRangeType(RangeType.POSITIVE);
        plot.getRangeAxis().setAutoRange(true);
        plot.getRangeAxis().setAutoRangeMinimumSize(0.2);

    }


    public void resetData() {
        dataset.setTimeBase(new Second(DateUtils.addSeconds(new Date(), -interval)));
        dataset.addSeries(new float[0], 0, "");
    }

    public void appendData(float data) {
        dataset.advanceTime();
        dataset.appendData(new float[] {data});
    }


    public BufferedImage parseImage(int width, int style) {
        return chart.createBufferedImage(width, style);
    }

}
