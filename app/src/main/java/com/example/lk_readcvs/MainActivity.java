package com.example.lk_readcvs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.TimePickerView;
import com.example.lk_readcvs.Util.ReadCSVCallBack;
import com.example.lk_readcvs.Util.ReadCSVThread;
import com.example.lk_readcvs.Util.ReadConstant;
import com.example.lk_readcvs.Util.SelectDateTime;
import com.example.lk_readcvs.View.BottomUI;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.ivOpen)
    ImageView ivOpen;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    @BindView(R.id.linSetting)
    LinearLayout linSetting;
    @BindView(R.id.linSelect)
    LinearLayout linSelect;
    @BindView(R.id.radionDate)
    TextView radionDate;
    @BindView(R.id.radionTime)
    TextView radionTime;
    @BindView(R.id.tvOnDirectCurrent)
    TextView tvOnDirectCurrent;
    @BindView(R.id.tvOffDirectCurrent)
    TextView tvOffDirectCurrent;
    @BindView(R.id.tvOnACCurrent)
    TextView tvOnACCurrent;
    @BindView(R.id.tvOffACCurrent)
    TextView tvOffACCurrent;
    @BindView(R.id.tvOnDirectVoltage)
    TextView tvOnDirectVoltage;
    @BindView(R.id.tvOffDirectVoltage)
    TextView tvOffDirectVoltage;
    @BindView(R.id.tvOnACVoltage)
    TextView tvOnACVoltage;
    @BindView(R.id.tvOffACVoltage)
    TextView tvOffACVoltage;

    List<Entry> entriesOffDirectCurrent = new ArrayList<>();
    List<Entry> entriesOffDirectVoltage = new ArrayList<>();
    List<Entry> entriesOffACCurrent = new ArrayList<>();
    List<Entry> entriesOffACVoltage = new ArrayList<>();
    List<Entry> entriesOnDirectCurrent = new ArrayList<>();
    List<Entry> entriesOnDirectVoltage = new ArrayList<>();
    List<Entry> entriesOnACCurrent = new ArrayList<>();
    List<Entry> entriesOnACVoltage = new ArrayList<>();
    String[] PERMS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int TAG_ONE = 1;
    boolean on_direct_current = false;
    boolean off_direct_current = true;
    boolean on_ac_current = false;
    boolean off_ac_current = false;
    boolean on_direct_voltage = false;
    boolean off_direct_voltage = false;
    boolean on_ac_voltage = false;
    boolean off_ac_voltage = false;
    private TimePickerView datePicker;
    LineData lineData = new LineData();
    LineDataSet dataSetOffDirectCurrent,dataSetOffDirectVoltage,dataSetOffACCurrent,dataSetOffACVoltage,
            dataSetOnDirectCurrent,dataSetOnDirectVoltage,dataSetOnACCurrent,dataSetOnACVoltage;

    //AC交流  direct直流  current电流  voltage电压

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不息屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部按钮
        new BottomUI().hideBottomUIMenu(this.getWindow());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //设置样式
        new ReadConstant().setLineChar(lineChart);
        //请求权限
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            // 已经申请过权限，做想做的事
            readCSVData(radionDate.getText().toString().trim());
        } else {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(this, "PERMISSION_STORAGE_MSG", TAG_ONE, PERMS);
        }
    }

    /**
     * 读取数据
     */
    private void readCSVData(String date) {
        String path = Environment.getExternalStorageDirectory() + "/";
        String name = "DATA.CSV";
        new ReadCSVThread(path, name, new ReadCSVCallBack() {
            @Override
            public void success(List<String> sb) {
                if (sb!=null){
                    entriesOffDirectCurrent = new ArrayList<>();
                    entriesOffDirectVoltage = new ArrayList<>();
                    entriesOffACCurrent = new ArrayList<>();
                    entriesOffACVoltage = new ArrayList<>();
                    entriesOnDirectCurrent = new ArrayList<>();
                    entriesOnDirectVoltage = new ArrayList<>();
                    entriesOnACCurrent = new ArrayList<>();
                    entriesOnACVoltage = new ArrayList<>();
                    for (int i = 0; i < sb.size(); i++) {
                        String itemData = sb.get(i);
                        String[] arrayData = itemData.split(",");
                        if (itemData != null && arrayData.length > 9) {
                            if (date.equals("日期")){
                                entriesOffDirectCurrent.add(new Entry(i, Float.valueOf(arrayData[2])));
                                entriesOffDirectVoltage.add(new Entry(i, Float.valueOf(arrayData[3])));
                                entriesOffACCurrent.add(new Entry(i, Float.valueOf(arrayData[4])));
                                entriesOffACVoltage.add(new Entry(i, Float.valueOf(arrayData[5])));
                                entriesOnDirectCurrent.add(new Entry(i, Float.valueOf(arrayData[6])));
                                entriesOnDirectVoltage.add(new Entry(i, Float.valueOf(arrayData[7])));
                                entriesOnACCurrent.add(new Entry(i, Float.valueOf(arrayData[8])));
                                entriesOnACVoltage.add(new Entry(i, Float.valueOf(arrayData[9])));
                            }else if (itemData.split(",")[0].equals(date)){
                                entriesOffDirectCurrent.add(new Entry(i, Float.valueOf(arrayData[2])));
                                entriesOffDirectVoltage.add(new Entry(i, Float.valueOf(arrayData[3])));
                                entriesOffACCurrent.add(new Entry(i, Float.valueOf(arrayData[4])));
                                entriesOffACVoltage.add(new Entry(i, Float.valueOf(arrayData[5])));
                                entriesOnDirectCurrent.add(new Entry(i, Float.valueOf(arrayData[6])));
                                entriesOnDirectVoltage.add(new Entry(i, Float.valueOf(arrayData[7])));
                                entriesOnACCurrent.add(new Entry(i, Float.valueOf(arrayData[8])));
                                entriesOnACVoltage.add(new Entry(i, Float.valueOf(arrayData[9])));
                            }
                        }
                    }
                }
                if (date.equals("日期")){
                    setLineCharData("off_direct_current_add");
                }else {
                    if (off_direct_current){
                        setLineCharData("off_direct_current_add");
                    }
                    if (off_direct_voltage){
                        setLineCharData("off_direct_voltage_add");
                    }
                    if (off_ac_current){
                        setLineCharData("off_ac_current_add");
                    }
                    if (off_ac_voltage){
                        setLineCharData("off_ac_voltage_add");
                    }
                    if (on_direct_current){
                        setLineCharData("on_direct_current_add");
                    }
                    if (on_direct_voltage){
                        setLineCharData("on_direct_voltage_add");
                    }
                    if (on_ac_current){
                        setLineCharData("on_ac_current_add");
                    }
                    if (on_ac_voltage){
                        setLineCharData("on_ac_voltage_add");
                    }
                }
            }

            @Override
            public void fail() {
                Message message = new Message();
                message.what = TAG_ONE;
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 设置linecharData
     * @param item
     */
    private void setLineCharData(String item) {
        switch (item){
            case "off_direct_current_add":
                dataSetOffDirectCurrent = new LineDataSet(entriesOffDirectCurrent, getString(R.string.off_direct_current));
                dataSetOffDirectCurrent.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOffDirectCurrent.setCircleColor(getColor(R.color.color_bg_selected));//设置点的颜色
                dataSetOffDirectCurrent.setColor(getColor(R.color.color_bg_selected));//设置线的颜色
                lineData.addDataSet(dataSetOffDirectCurrent);
                lineChart.setData(lineData);
                lineChart.notifyDataSetChanged();
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;
            case "off_direct_voltage_add":
                dataSetOffDirectVoltage = new LineDataSet(entriesOffDirectVoltage, getString(R.string.off_direct_voltage));
                dataSetOffDirectVoltage.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOffDirectVoltage.setCircleColor(getColor(R.color.holo_red_dark));//设置点的颜色
                dataSetOffDirectVoltage.setColor(getColor(R.color.holo_red_dark));//设置线的颜色
                lineData.addDataSet(dataSetOffDirectVoltage);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;

            case "off_ac_current_add":
                dataSetOffACCurrent = new LineDataSet(entriesOffACCurrent, getString(R.string.off_ac_current));
                dataSetOffACCurrent.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOffACCurrent.setCircleColor(getColor(R.color.line1));//设置点的颜色
                dataSetOffACCurrent.setColor(getColor(R.color.line1));//设置线的颜色
                lineData.addDataSet(dataSetOffACCurrent);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;
            case "off_ac_voltage_add":
                dataSetOffACVoltage = new LineDataSet(entriesOffACVoltage, getString(R.string.off_ac_voltage));
                dataSetOffACVoltage.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOffACVoltage.setCircleColor(getColor(R.color.darkorange));//设置点的颜色
                dataSetOffACVoltage.setColor(getColor(R.color.darkorange));//设置线的颜色
                lineData.addDataSet(dataSetOffACVoltage);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;
            case "on_direct_current_add":
                dataSetOnDirectCurrent = new LineDataSet(entriesOnDirectCurrent, getString(R.string.on_direct_current));
                dataSetOnDirectCurrent.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOnDirectCurrent.setCircleColor(getColor(R.color.sienna));//设置点的颜色
                dataSetOnDirectCurrent.setColor(getColor(R.color.sienna));//设置线的颜色
                lineData.addDataSet(dataSetOnDirectCurrent);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;
            case "on_direct_voltage_add":
                dataSetOnDirectVoltage = new LineDataSet(entriesOnDirectVoltage, getString(R.string.on_direct_voltage));
                dataSetOnDirectVoltage.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOnDirectVoltage.setCircleColor(getColor(R.color.blueviolet));//设置点的颜色
                dataSetOnDirectVoltage.setColor(getColor(R.color.blueviolet));//设置线的颜色
                lineData.addDataSet(dataSetOnDirectVoltage);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;
            case "on_ac_current_add":
                dataSetOnACCurrent = new LineDataSet(entriesOnACCurrent, getString(R.string.on_ac_current));
                dataSetOnACCurrent.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOnACCurrent.setCircleColor(getColor(R.color.mediumblue));//设置点的颜色
                dataSetOnACCurrent.setColor(getColor(R.color.mediumblue));//设置线的颜色
                lineData.addDataSet(dataSetOnACCurrent);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;
            case "on_ac_voltage_add":
                dataSetOnACVoltage = new LineDataSet(entriesOnACVoltage, getString(R.string.on_ac_voltage));
                dataSetOnACVoltage.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOnACVoltage.setCircleColor(getColor(R.color.dengji));//设置点的颜色
                dataSetOnACVoltage.setColor(getColor(R.color.dengji));//设置线的颜色
                lineData.addDataSet(dataSetOnACVoltage);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;
        }
    }

    // 实现“onRequestPermissionsResult”函数接收校验权限结果。
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发给 EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // 已经申请过权限，做想做的事
        readCSVData(radionDate.getText().toString().trim());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // 请求权限被拒
        Toast.makeText(MainActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
        finish();
    }

    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.radionDate, R.id.radionTime, R.id.tvOnDirectCurrent, R.id.tvOffDirectCurrent,
            R.id.tvOnACCurrent, R.id.tvOffACCurrent, R.id.tvOnDirectVoltage, R.id.tvOffDirectVoltage,
            R.id.tvOnACVoltage, R.id.tvOffACVoltage,R.id.linSetting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linSetting:
                linSelect.setVisibility(View.VISIBLE);
                linSetting.setVisibility(View.GONE);
                break;
            case R.id.radionDate:
                showDatePicker();
                break;
            case R.id.radionTime:
                new SelectDateTime().showTimePicker(radionTime, this);
                break;
            case R.id.tvOffDirectCurrent:
                if (off_direct_current) {
                    off_direct_current = false;
                    tvOffDirectCurrent.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOffDirectCurrent);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    off_direct_current = true;
                    tvOffDirectCurrent.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("off_direct_current_add");
                }
                break;
            case R.id.tvOffDirectVoltage:
                if (off_direct_voltage) {
                    off_direct_voltage = false;
                    tvOffDirectVoltage.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOffDirectVoltage);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    off_direct_voltage = true;
                    tvOffDirectVoltage.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("off_direct_voltage_add");
                }
                break;
            case R.id.tvOffACCurrent:
                if (off_ac_current) {
                    off_ac_current = false;
                    tvOffACCurrent.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOffACCurrent);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    off_ac_current = true;
                    tvOffACCurrent.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("off_ac_current_add");
                }
                break;
            case R.id.tvOffACVoltage:
                if (off_ac_voltage) {
                    off_ac_voltage = false;
                    tvOffACVoltage.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOffACVoltage);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    off_ac_voltage = true;
                    tvOffACVoltage.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("off_ac_voltage_add");
                }
                break;
            case R.id.tvOnDirectCurrent:
                if (on_direct_current) {
                    on_direct_current = false;
                    tvOnDirectCurrent.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOnDirectCurrent);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    on_direct_current = true;
                    tvOnDirectCurrent.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("on_direct_current_add");
                }
                break;
            case R.id.tvOnDirectVoltage:
                if (on_direct_voltage) {
                    on_direct_voltage = false;
                    tvOnDirectVoltage.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOnDirectVoltage);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    on_direct_voltage = true;
                    tvOnDirectVoltage.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("on_direct_voltage_add");
                }
                break;
            case R.id.tvOnACCurrent:
                if (on_ac_current) {
                    on_ac_current = false;
                    tvOnACCurrent.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOnACCurrent);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    on_ac_current = true;
                    tvOnACCurrent.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("on_ac_current_add");
                }
                break;
            case R.id.tvOnACVoltage:
                if (on_ac_voltage) {
                    on_ac_voltage = false;
                    tvOnACVoltage.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOnACVoltage);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    on_ac_voltage = true;
                    tvOnACVoltage.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("on_ac_voltage_add");
                }
                break;
        }
    }


    //根据时间选择
    public void showDatePicker() {
        datePicker = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日");
                radionDate.setText(format.format(date));
                lineData.removeDataSet(dataSetOffDirectCurrent);
                lineData.removeDataSet(dataSetOffDirectVoltage);
                lineData.removeDataSet(dataSetOffACCurrent);
                lineData.removeDataSet(dataSetOffACVoltage);
                lineData.removeDataSet(dataSetOnDirectCurrent);
                lineData.removeDataSet(dataSetOnDirectVoltage);
                lineData.removeDataSet(dataSetOnACCurrent);
                lineData.removeDataSet(dataSetOnACVoltage);
                lineChart.notifyDataSetChanged();
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                readCSVData(format.format(date));
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TAG_ONE:
                    Toast.makeText(MainActivity.this, "读取失败，请检查文件是否存在", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}