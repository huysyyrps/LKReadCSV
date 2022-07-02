package com.example.lk_readcvs.Util;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;

public class ReadConstant {
    /**
     * 设置linechar样式
     * @param lineChart
     */
    public void setLineChar(LineChart lineChart) {
        lineChart.setDrawBorders(true); //显示图表边框
        lineChart.setScaleEnabled(false); // 是否可以缩放 x和y轴, 默认为true
        lineChart.setScaleXEnabled(true); //是否可以缩放 仅x轴
        lineChart.setScaleYEnabled(true); //是否可以缩放 仅y轴
        lineChart.setDragEnabled(true); // 是否可以拖拽
        lineChart.setNoDataText("没有数据嗷"); //没有数据时显示的文字
        lineChart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart.setBorderColor(Color.BLACK); //设置 chart 边框线的颜色。
        lineChart.animateXY(1000, 1000); // 两个轴动画，从左到右，从下到上
        // 设置左侧坐标轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);//设置轴启用或禁用 如果禁用以下的设置全部不生效
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
    }

}
