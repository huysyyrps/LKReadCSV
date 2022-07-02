package com.example.lk_readcvs.Util;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.lk_readcvs.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectDateTime {
    private TimePickerView datePicker;
    private TimePickerView timePicker;
    //选择日期
    public void showDatePicker(TextView radionDate, MainActivity mainActivity) {
        datePicker = new TimePickerView.Builder(mainActivity, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                radionDate.setText(format.format(date));
            }
        }).setSubmitText("确定")
                .setCancelText("取消")
                .setCancelColor(Color.BLACK)
                .setSubmitColor(Color.BLACK)
                .setSubCalSize(16)
                //.isDialog(true) //是否对话框样式显示（显示在页面中间）
                //.isCyclic(true) //是否循环滚动
                .setType(new boolean[]{true, true, true, false, false, false}) //显示“年月日时分秒”的哪几项
                .isCenterLabel(false) //是否只显示选中的label文字，false则每项item全部都带有 label
                .build();
        //设置显示的日期
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))));
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        //这里需要注意的是月份是从0开始的，要显示10月份这里的参数应该是9
        //calendar.set(1997,9,10);
        datePicker.setDate(calendar);
        datePicker.show();
    }
    //选择时间
    public void showTimePicker(TextView radionTime, MainActivity mainActivity) {
        timePicker = new TimePickerView.Builder(mainActivity, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                radionTime.setText(format.format(date));
            }
        }).setSubmitText("确定")
                .setCancelText("取消")
                .setCancelColor(Color.BLACK)
                .setSubmitColor(Color.BLACK)
                .setSubCalSize(16)
                //.isDialog(true) //是否对话框样式显示（显示在页面中间）
                //.isCyclic(true) //是否循环滚动
                .setType(new boolean[]{false, false, false, true, true, true}) //显示“年月日时分秒”的哪几项
                .isCenterLabel(false) //是否只显示选中的label文字，false则每项item全部都带有 label
                .build();
        //设置显示的日期
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("HH:mm:ss").parse(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()))));
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        //这里需要注意的是月份是从0开始的，要显示10月份这里的参数应该是9
        //calendar.set(1997,9,10);
        timePicker.setDate(calendar);
        timePicker.show();
    }
}
