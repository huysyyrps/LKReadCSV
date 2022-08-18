package com.example.lk_readcvs;

import static com.example.lk_readcvs.Util.Constant.TAG_ONE;
import static com.example.lk_readcvs.Util.Constant.TAG_THERE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.TimePickerView;
import com.example.lk_readcvs.Util.AlertDialogUtil;
import com.example.lk_readcvs.Util.CreateKml;
import com.example.lk_readcvs.Util.DataBean;
import com.example.lk_readcvs.Util.ExcelUtil;
import com.example.lk_readcvs.Util.ImageSave;
import com.example.lk_readcvs.Util.MyValueFormatter;
import com.example.lk_readcvs.Util.ReadCSVCallBack;
import com.example.lk_readcvs.Util.ReadCSVThread;
import com.example.lk_readcvs.Util.ReadConstant;
import com.example.lk_readcvs.Util.SaveImageCallBack;
import com.example.lk_readcvs.Util.Task;
import com.example.lk_readcvs.Util.TrackPoint;
import com.example.lk_readcvs.View.BottomUI;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.radionStartDate)
    TextView radionStartDate;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.tvSaveImg)
    TextView tvSaveImg;
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
    @BindView(R.id.tvKML)
    TextView tvKML;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.icBack)
    ImageView icBack;
    @BindView(R.id.tvOnPXGDirectTD)
    TextView tvOnPXGDirectTD;
    @BindView(R.id.tvOnCZDirectTD)
    TextView tvOnCZDirectTD;
    @BindView(R.id.tvOnPXGACTD)
    TextView tvOnPXGACTD;
    @BindView(R.id.tvOnCZACTD)
    TextView tvOnCZACTD;
    @BindView(R.id.tvOpen)
    TextView tvOpen;
    @BindView(R.id.tvKey)
    TextView tvKey;

    List<Entry> entriesOffDirectCurrent = new ArrayList<>();
    List<Entry> entriesOffDirectVoltage = new ArrayList<>();
    List<Entry> entriesOffACCurrent = new ArrayList<>();
    List<Entry> entriesOffACVoltage = new ArrayList<>();
    List<Entry> entriesOnDirectCurrent = new ArrayList<>();
    List<Entry> entriesOnDirectVoltage = new ArrayList<>();
    List<Entry> entriesOnACCurrent = new ArrayList<>();
    List<Entry> entriesOnACVoltage = new ArrayList<>();
    List<Entry> entriesOnPXGDirectTD = new ArrayList<>();
    List<Entry> entriesOnCZDirectTD = new ArrayList<>();
    List<Entry> entriesOnPXGACTD = new ArrayList<>();
    List<Entry> entriesOnCZACTD = new ArrayList<>();
    //,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    String[] PERMS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    boolean on_direct_current = false;
    boolean off_direct_current = true;
    boolean on_ac_current = false;
    boolean off_ac_current = false;
    boolean on_direct_voltage = false;
    boolean off_direct_voltage = false;
    boolean on_ac_voltage = false;
    boolean off_ac_voltage = false;

    boolean pxg_direct_td = false;
    boolean cz_direct_td = false;
    boolean pxg_ac_td = false;
    boolean cz_ac_td = false;
    private TimePickerView datePicker;
    private int mWindowWidth;
    private int mWindowHeight;
    private int mScreenDensity;
    private ImageReader mImageReader;
    private WindowManager mWindowManager;
    private VirtualDisplay mVirtualDisplay;
    LineData lineData = new LineData();
    Bitmap mBitmap;
    private static AlertDialogUtil alertDialogUtil;
    private MediaProjection mMediaProjection;
    private MediaProjectionManager mMediaProjectionManager;
    String path = Environment.getExternalStorageDirectory() + "/";
    List<DataBean> dataList = new ArrayList<>();
    String[] colName = {"日期",
            "断电直流电流", "断电直流电压", "断电交流电流", "断电交流电压",
            "通电直流电流", "通电直流电压", "通电交流电流", "通电交流电压",
            "平行管道直流电位梯度", "垂直管道直流电位梯度", "平行管道交流电位梯度", "垂直管道交流电位梯度"};
    LineDataSet dataSetOffDirectCurrent, dataSetOffDirectVoltage, dataSetOffACCurrent, dataSetOffACVoltage,
            dataSetOnDirectCurrent, dataSetOnDirectVoltage, dataSetOnACCurrent, dataSetOnACVoltage,
            dataSetOnPXGDirectTD, dataSetOnCZDirectTD, dataSetOnPXGACTD, dataSetOnCZACTD;
    String s;
    DataBean dataBean;
    List<TrackPoint> trackPointList = new ArrayList<>();
    List<String> xAxisList = new ArrayList<>();
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
        alertDialogUtil = new AlertDialogUtil(this);
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWindowWidth = mWindowManager.getDefaultDisplay().getWidth();
        mWindowHeight = mWindowManager.getDefaultDisplay().getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.densityDpi;
        mImageReader = ImageReader.newInstance(mWindowWidth, mWindowHeight, 0x1, 2);

        lineData.setValueFormatter(new MyValueFormatter());

        //设置样式
        new ReadConstant().setLineChar(lineChart);
        //请求权限
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            // 已经申请过权限，做想做的事
            readCSVData(radionStartDate.getText().toString().trim(), tvEndTime.getText().toString().trim());
        } else {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(this, "PERMISSION_STORAGE_MSG", TAG_ONE, PERMS);
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e("XXXXX", "Sdcard可用");
            File file = new File(getStoragePath(this, true), "MySdcard.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("饭撒发生的噶地方舒服舒服撒冯绍峰撒");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("XXXXX", "Sdcard不可用");
        }
    }

    private static String getStoragePath(Context mContext, boolean is_removale) {
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    //读取数据
    private void readCSVData(String startDate, String endDate) {
        // 判断手机上是否插入了SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 获取SD卡的目录
//            File  dir = Environment.getExternalStorageDirectory();
//            String path = null;
//            try {
//                path = Environment.getExternalStorageDirectory().getCanonicalPath() + "/";
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            File sdCardDir = Environment.getExternalStorageDirectory();
            //filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
            s = getStoragePath(this, true);
            String name = "DATA.CSV";
            new ReadCSVThread(s, name, new ReadCSVCallBack() {
                @Override
                public void success(List<String> sb) {
                    if (sb != null) {
                        entriesOffDirectCurrent = new ArrayList<>();
                        entriesOffDirectVoltage = new ArrayList<>();
                        entriesOffACCurrent = new ArrayList<>();
                        entriesOffACVoltage = new ArrayList<>();
                        entriesOnDirectCurrent = new ArrayList<>();
                        entriesOnDirectVoltage = new ArrayList<>();
                        entriesOnACCurrent = new ArrayList<>();
                        entriesOnACVoltage = new ArrayList<>();
                        entriesOnPXGDirectTD = new ArrayList<>();
                        entriesOnCZDirectTD = new ArrayList<>();
                        entriesOnPXGACTD = new ArrayList<>();
                        entriesOnCZACTD = new ArrayList<>();

                        dataList = new ArrayList<>();
                        for (int i = 0; i < sb.size(); i++) {
                            String itemData = sb.get(i);
//                            itemData = itemData.replace(" ", "");
                            String[] arrayData = itemData.split(",");
                            if (itemData != null && arrayData.length > 13) {
                                if (startDate.equals("开始时间") && endDate.equals("结束时间")) {
                                    setList(i, arrayData);
                                } else if (!startDate.equals("开始时间") && endDate.equals("结束时间")) {
                                    String vlaTime = arrayData[0];
                                    if (!compareDate(startDate, vlaTime)) {
                                        setList(i, arrayData);
                                    }
                                } else if (startDate.equals("开始时间") && !endDate.equals("结束时间")) {
                                    String vlaTime = arrayData[0];
                                    if (compareDate(endDate, vlaTime)) {
                                        setList(i, arrayData);
                                    }
                                } else if (!startDate.equals("开始时间") && !endDate.equals("结束时间")) {
                                    String vlaTime = arrayData[0];
                                    if (!compareDate(startDate, vlaTime)) {
                                        if (compareDate(endDate, vlaTime)) {
                                            setList(i, arrayData);
                                        }
                                    }
                                }
                            }
                        }
                        if (dataList.size() == 0) {
                            Message message = new Message();
                            message.what = TAG_THERE;
                            handler.sendMessage(message);
                            return;
                        }
                    }
                    if (startDate.equals("开始时间") && endDate.equals("结束时间")) {
                        setLineCharData("off_direct_current_add");
                    } else {
                        if (off_direct_current) {
                            setLineCharData("off_direct_current_add");
                        }
                        if (off_direct_voltage) {
                            setLineCharData("off_direct_voltage_add");
                        }
                        if (off_ac_current) {
                            setLineCharData("off_ac_current_add");
                        }
                        if (off_ac_voltage) {
                            setLineCharData("off_ac_voltage_add");
                        }
                        if (on_direct_current) {
                            setLineCharData("on_direct_current_add");
                        }
                        if (on_direct_voltage) {
                            setLineCharData("on_direct_voltage_add");
                        }
                        if (on_ac_current) {
                            setLineCharData("on_ac_current_add");
                        }
                        if (on_ac_voltage) {
                            setLineCharData("on_ac_voltage_add");
                        }
                        if (pxg_direct_td) {
                            setLineCharData("on_pxg_direct_td_add");
                        }
                        if (cz_direct_td) {
                            setLineCharData("on_cz_direct_td_add");
                        }
                        if (pxg_ac_td) {
                            setLineCharData("on_pxg_ac_td_add");
                        }
                        if (cz_ac_td) {
                            setLineCharData("on_cz_ac_td_add");
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
        } else {
            Toast.makeText(this, "未发现内存卡", Toast.LENGTH_SHORT).show();
        }
    }

    public void setList(int i, String[] arrayData) {
//        Log.e("XXXXXX", arrayData.length + "");
        if (arrayData.length == 17) {
            TrackPoint trackPoint = new TrackPoint();
            trackPoint.setAltitude("0");
            String longDaitude = arrayData[15];
            String latitude = arrayData[13];
            String firstLongDaitude = longDaitude.substring(0,3);
            String secondLongDaitude = longDaitude.substring(3,longDaitude.length());
            longDaitude = String.valueOf(Double.valueOf(firstLongDaitude)+Double.valueOf(secondLongDaitude)/60);
            Log.e("XXXXX",firstLongDaitude +"---"+secondLongDaitude);
            Log.e("XXXXX",longDaitude);

            String firstLatitude = latitude.substring(0,2);
            String secondLatitude = latitude.substring(2,latitude.length());
            latitude = String.valueOf(Double.valueOf(firstLatitude)+Double.valueOf(secondLatitude)/60);
            Log.e("XXXXX",firstLatitude +"---"+secondLatitude);
            Log.e("XXXXX",latitude);


//            double longDaitude = Double.parseDouble(arrayData[15]);
//            longDaitude = longDaitude / 100;
//            double latitude = Double.parseDouble(arrayData[13]);
//            latitude = latitude / 100;
            trackPoint.setLongitude(longDaitude + "");
            trackPoint.setLatitude(latitude + "");
            trackPoint.setTime(arrayData[0]);
            trackPointList.add(trackPoint);
        }
        xAxisList.add(arrayData[0]);

        try {
            Float fOffDirectCurrent = Float.valueOf(arrayData[1]);
            entriesOffDirectCurrent.add(new Entry(i, fOffDirectCurrent));
        } catch (Exception e) {
            entriesOffDirectCurrent.add(new Entry(i, 0));
        }

        try {
            Float fOffDirectVoltage = Float.valueOf(arrayData[2]);
            entriesOffDirectVoltage.add(new Entry(i, fOffDirectVoltage));
        } catch (Exception e) {
            entriesOffDirectVoltage.add(new Entry(i, 0));
        }

        try {
            Float fOffACCurrent = Float.valueOf(arrayData[3]);
            entriesOffACCurrent.add(new Entry(i, fOffACCurrent));
        } catch (Exception e) {
            entriesOffACCurrent.add(new Entry(i, 0));
        }

        try {
            Float fOffACVoltage = Float.valueOf(arrayData[4]);
            entriesOffACVoltage.add(new Entry(i, fOffACVoltage));
        } catch (Exception e) {
            entriesOffACVoltage.add(new Entry(i, 0));
        }

        try {
            Float fOnDirectCurrent = Float.valueOf(arrayData[5]);
            entriesOnDirectCurrent.add(new Entry(i, fOnDirectCurrent));
        } catch (Exception e) {
            entriesOnDirectCurrent.add(new Entry(i, 0));
        }

        try {
            Float fOnDirectVoltage = Float.valueOf(arrayData[6]);
            entriesOnDirectVoltage.add(new Entry(i, fOnDirectVoltage));
        } catch (Exception e) {
            entriesOnDirectVoltage.add(new Entry(i, 0));
        }

        try {
            Float fOnACCurrent = Float.valueOf(arrayData[7]);
            entriesOnACCurrent.add(new Entry(i, fOnACCurrent));
        } catch (Exception e) {
            entriesOnACCurrent.add(new Entry(i, 0));
        }

        try {
            Float fOnACVoltage = Float.valueOf(arrayData[8]);
            entriesOnACVoltage.add(new Entry(i, fOnACVoltage));
        } catch (Exception e) {
            entriesOnACVoltage.add(new Entry(i, 0));
        }

        try {
            Float fOnPXGDirectTD = Float.valueOf(arrayData[9]);
            entriesOnPXGDirectTD.add(new Entry(i, fOnPXGDirectTD));
        } catch (Exception e) {
            entriesOnPXGDirectTD.add(new Entry(i, 0));
        }

        try {
            Float fOnCZDirectTD = Float.valueOf(arrayData[10]);
            entriesOnCZDirectTD.add(new Entry(i, fOnCZDirectTD));
        } catch (Exception e) {
            entriesOnCZDirectTD.add(new Entry(i, 0));
        }

        try {
            Float fOnPXGACTD = Float.valueOf(arrayData[11]);
            entriesOnPXGACTD.add(new Entry(i, fOnPXGACTD));
        } catch (Exception e) {
            entriesOnPXGACTD.add(new Entry(i, 0));
        }

        try {
            Float fOnCZAXTD = Float.valueOf(arrayData[12]);
            entriesOnCZACTD.add(new Entry(i, fOnCZAXTD));
        } catch (Exception e) {
            entriesOnCZACTD.add(new Entry(i, 0));
        }
        dataBean = new DataBean(arrayData[0],
                arrayData[1], arrayData[2], arrayData[3], arrayData[4],
                arrayData[5], arrayData[6], arrayData[7], arrayData[8],
                arrayData[9], arrayData[10], arrayData[11], arrayData[12]);
        dataList.add(dataBean);
    }

    //比较时间大小
    public boolean compareDate(String selectDate, String compareDate) {
        DateFormat df = new SimpleDateFormat("yyyy/M/d H:m");
        try {
            Date select = df.parse(selectDate);
            Date compare = df.parse(compareDate);
            if (select.getTime() >= compare.getTime()) {
                return true;
            } else if (select.getTime() < compare.getTime()) {
                return false;
            }
//            if (select.after(compare)) {
//                return true;
//            } else {
//                return false;
//            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 设置linecharData
     *
     * @param item
     */
    private void setLineCharData(String item) {
        switch (item) {
            case "off_direct_current_add":
                setXAxis();
                dataSetOffDirectCurrent = new LineDataSet(entriesOffDirectCurrent, getString(R.string.off_direct_current));
                dataSetOffDirectCurrent.setValueFormatter(new MyValueFormatter());
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
                dataSetOffDirectVoltage.setValueFormatter(new MyValueFormatter());
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
                dataSetOffACCurrent.setValueFormatter(new MyValueFormatter());
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
                dataSetOffACVoltage.setValueFormatter(new MyValueFormatter());
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
                dataSetOnDirectCurrent.setValueFormatter(new MyValueFormatter());
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
                dataSetOnDirectVoltage.setValueFormatter(new MyValueFormatter());
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
                dataSetOnACCurrent.setValueFormatter(new MyValueFormatter());
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
                dataSetOnACVoltage.setValueFormatter(new MyValueFormatter());
                dataSetOnACVoltage.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOnACVoltage.setCircleColor(getColor(R.color.dengji));//设置点的颜色
                dataSetOnACVoltage.setColor(getColor(R.color.dengji));//设置线的颜色
                lineData.addDataSet(dataSetOnACVoltage);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;
            case "on_pxg_direct_td_add":
                dataSetOnPXGDirectTD = new LineDataSet(entriesOnPXGDirectTD, getString(R.string.pxg_direct_voltage));
                dataSetOnPXGDirectTD.setValueFormatter(new MyValueFormatter());
                dataSetOnPXGDirectTD.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOnPXGDirectTD.setCircleColor(getColor(R.color.style_red));//设置点的颜色
                dataSetOnPXGDirectTD.setColor(getColor(R.color.style_red));//设置线的颜色
                lineData.addDataSet(dataSetOnPXGDirectTD);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;
            case "on_cz_direct_td_add":
                dataSetOnCZDirectTD = new LineDataSet(entriesOnCZDirectTD, getString(R.string.cz_direct_voltage));
                dataSetOnCZDirectTD.setValueFormatter(new MyValueFormatter());
                dataSetOnCZDirectTD.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOnCZDirectTD.setCircleColor(getColor(R.color.tv_task_nofify_no));//设置点的颜色
                dataSetOnCZDirectTD.setColor(getColor(R.color.tv_task_nofify_no));//设置线的颜色
                lineData.addDataSet(dataSetOnCZDirectTD);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;

            case "on_pxg_ac_td_add":
                dataSetOnPXGACTD = new LineDataSet(entriesOnPXGACTD, getString(R.string.pxg_ac_voltage));
                dataSetOnPXGACTD.setValueFormatter(new MyValueFormatter());
                dataSetOnPXGACTD.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOnPXGACTD.setCircleColor(getColor(R.color.teal));//设置点的颜色
                dataSetOnPXGACTD.setColor(getColor(R.color.teal));//设置线的颜色
                lineData.addDataSet(dataSetOnPXGACTD);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;

            case "on_cz_ac_td_add":
                dataSetOnCZACTD = new LineDataSet(entriesOnCZACTD, getString(R.string.cz_ac_voltage));
                dataSetOnCZACTD.setValueFormatter(new MyValueFormatter());
                dataSetOnCZACTD.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSetOnCZACTD.setCircleColor(getColor(R.color.btn_stop_order));//设置点的颜色
                dataSetOnCZACTD.setColor(getColor(R.color.btn_stop_order));//设置线的颜色
                lineData.addDataSet(dataSetOnCZACTD);
                lineChart.setData(lineData);
                //刷新
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                break;
        }
    }

    private void setXAxis(){
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {   //X轴自定义标签内容
            @Override
            public String getAxisLabel(float index, AxisBase axisBase) {
                return xAxisList.get((int) index);
            }
        });
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
        readCSVData(radionStartDate.getText().toString().trim(), tvEndTime.getText().toString().trim());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // 请求权限被拒
        Toast.makeText(MainActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
        finish();
    }

    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.radionStartDate, R.id.tvSaveImg, R.id.tvOnDirectCurrent, R.id.tvOffDirectCurrent,
            R.id.tvOnACCurrent, R.id.tvOffACCurrent, R.id.tvOnDirectVoltage, R.id.tvOffDirectVoltage,
            R.id.tvOnACVoltage, R.id.tvOffACVoltage, R.id.linSetting, R.id.tvKML, R.id.tvSave
            , R.id.icBack, R.id.tvEndTime, R.id.tvOnPXGDirectTD, R.id.tvOnCZDirectTD, R.id.tvOnPXGACTD
            , R.id.tvOnCZACTD,R.id.tvOpen,R.id.tvKey})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linSetting:
                linSelect.setVisibility(View.VISIBLE);
                linSetting.setVisibility(View.GONE);
                break;
            case R.id.icBack:
                linSelect.setVisibility(View.GONE);
                linSetting.setVisibility(View.VISIBLE);
                break;
            case R.id.radionStartDate:
                showDatePicker(radionStartDate, "start");
                break;
            case R.id.tvEndTime:
                showDatePicker(tvEndTime, "end");
                break;
            case R.id.tvSave:
                alertDialogUtil.showImageDialog(new SaveImageCallBack() {
                    @Override
                    public void save(String name) {
                        String filePath;
                        if (name.equals("")) {
                            filePath = path + getNowDate();
                        } else {
                            filePath = path + name + ".xls";
                        }
//                        filePath = path + "1111.xls";
                        ExcelUtil.initExcel(filePath, colName);
                        ExcelUtil.writeObjListToExcel(s, dataList, filePath, MainActivity.this);
                    }

                    @Override
                    public void cancle() {

                    }
                });
                break;
            case R.id.tvKML:
                Task task = new Task();
                task.setTrackPointList(trackPointList);
                new CreateKml().createKml(path + "test.kml", task);
                break;
            case R.id.tvKey:
                Intent intentKey = new Intent(this,KeyActivity.class);
                startActivity(intentKey);
                break;
            case R.id.tvOpen:
//                File KML = new File(path+"/test.kml");
//                Intent i = getPackageManager().getLaunchIntentForPackage("com.google.earth");
//                i.setDataAndType(Uri.fromFile(KML), "xml");
                if (trackPointList.size()!=0){
                    Intent intent = new Intent(this,MapActivity.class);
                    intent.putExtra("list", (Serializable) trackPointList);
                    startActivity(intent);
                }else {
                    Toast.makeText(this, "暂无坐标点", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tvSaveImg:
                linSelect.setVisibility(View.GONE);
                linSetting.setVisibility(View.VISIBLE);
                if (mMediaProjection != null) {
                    setUpVirtualDisplay();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startCapture();
                        }
                    }, 200);
                } else {
                    requestMediaProjection();
                }
                break;
            case R.id.tvOffDirectCurrent:
                trackPointList = new ArrayList<>();
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
                trackPointList = new ArrayList<>();
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
                trackPointList = new ArrayList<>();
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
                trackPointList = new ArrayList<>();
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
                trackPointList = new ArrayList<>();
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
                trackPointList = new ArrayList<>();
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
                trackPointList = new ArrayList<>();
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
                trackPointList = new ArrayList<>();
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

            case R.id.tvOnPXGDirectTD:
                trackPointList = new ArrayList<>();
                if (pxg_direct_td) {
                    pxg_direct_td = false;
                    tvOnPXGDirectTD.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOnPXGDirectTD);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    pxg_direct_td = true;
                    tvOnPXGDirectTD.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("on_pxg_direct_td_add");
                }
                break;
            case R.id.tvOnCZDirectTD:
                trackPointList = new ArrayList<>();
                if (cz_direct_td) {
                    cz_direct_td = false;
                    tvOnCZDirectTD.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOnCZDirectTD);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    cz_direct_td = true;
                    tvOnCZDirectTD.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("on_cz_direct_td_add");
                }
                break;
            case R.id.tvOnPXGACTD:
                trackPointList = new ArrayList<>();
                if (pxg_ac_td) {
                    pxg_ac_td = false;
                    tvOnPXGACTD.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOnPXGACTD);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    pxg_ac_td = true;
                    tvOnPXGACTD.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("on_pxg_ac_td_add");
                }
                break;
            case R.id.tvOnCZACTD:
                trackPointList = new ArrayList<>();
                if (cz_ac_td) {
                    cz_ac_td = false;
                    tvOnCZACTD.setBackgroundColor(getColor(R.color.color_bg_selected));
                    lineData.removeDataSet(dataSetOnCZACTD);
                    lineChart.notifyDataSetChanged();
                    lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                } else {
                    cz_ac_td = true;
                    tvOnCZACTD.setBackgroundColor(getColor(R.color.holo_red_dark));
                    setLineCharData("on_cz_ac_td_add");
                }
                break;
        }
    }


    //创建申请录屏的 Intent
    private void requestMediaProjection() {
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, TAG_ONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent backdata) {
        super.onActivityResult(requestCode, resultCode, backdata);
        switch (requestCode) {
            case TAG_ONE:
                if (resultCode == Activity.RESULT_OK) {
                    new BottomUI().hideBottomUIMenu(this.getWindow());
                    mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, backdata);
                    if (mMediaProjection != null) {
                        setUpVirtualDisplay();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startCapture();
                            }
                        }, 200);
                    }
                } else {
                    finish();
                }
                break;
        }
    }

    private void setUpVirtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                mWindowWidth, mWindowHeight, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

    private void startCapture() {
        Image image = mImageReader.acquireLatestImage();
        if (image == null) {
            Log.e("MainActivity", "image is null.");
            return;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        mBitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        mBitmap.copyPixelsFromBuffer(buffer);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height);
        image.close();
        stopScreenCapture();
        if (mBitmap != null) {
            if (mBitmap != null) {
                alertDialogUtil.showImageDialog(new SaveImageCallBack() {
                    @Override
                    public void save(String name) {
                        boolean backstate = new ImageSave().saveBitmap(MainActivity.this, "/LKCSV/", name, mBitmap);
                        if (backstate) {
                            Toast.makeText(MainActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.save_faile, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void cancle() {

                    }
                });
            } else {
                System.out.println("bitmap is NULL!");
            }
        } else {
            System.out.println("bitmap is NULL!");
        }
    }

    private void stopScreenCapture() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
    }

    //根据时间选择
    public void showDatePicker(TextView textView, String tag) {
        datePicker = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d H:m");
                Log.e("XXX", format.format(date));
                if (tag.equals("start")) {
                    if (tvEndTime.getText().toString().trim().equals("结束时间")) {
                        radionStartDate.setText(format.format(date));
                    } else if (!compareDate(format.format(date), tvEndTime.getText().toString().trim())) {
                        radionStartDate.setText(format.format(date));
                    } else {
                        Toast.makeText(MainActivity.this, "请选择正确时间", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else if (tag.equals("end")) {
                    if (radionStartDate.getText().toString().trim().equals("开始时间")) {
                        tvEndTime.setText(format.format(date));
                    } else if (!compareDate(radionStartDate.getText().toString().trim(), format.format(date))) {
                        tvEndTime.setText(format.format(date));
                    } else {
                        Toast.makeText(MainActivity.this, "请选择正确时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                lineData.removeDataSet(dataSetOffDirectCurrent);
                lineData.removeDataSet(dataSetOffDirectVoltage);
                lineData.removeDataSet(dataSetOffACCurrent);
                lineData.removeDataSet(dataSetOffACVoltage);
                lineData.removeDataSet(dataSetOnDirectCurrent);
                lineData.removeDataSet(dataSetOnDirectVoltage);
                lineData.removeDataSet(dataSetOnACCurrent);
                lineData.removeDataSet(dataSetOnACVoltage);
                lineData.removeDataSet(dataSetOnPXGDirectTD);
                lineData.removeDataSet(dataSetOnCZDirectTD);
                lineData.removeDataSet(dataSetOnPXGDirectTD);
                lineData.removeDataSet(dataSetOnCZACTD);

                lineChart.notifyDataSetChanged();
                lineChart.getViewPortHandler().refresh(new Matrix(), lineChart, true);
                if (tag.equals("start")) {
                    readCSVData(format.format(date), tvEndTime.getText().toString().trim());
                } else if (tag.equals("end")) {
                    readCSVData(radionStartDate.getText().toString().trim(), format.format(date));
                }

            }
        }).

                setSubmitText("确定")
                .setCancelText("取消")
                .setCancelColor(Color.BLACK)
                .setSubmitColor(Color.BLACK)
                .setSubCalSize(16)
                //.isDialog(true) //是否对话框样式显示（显示在页面中间）
                //.isCyclic(true) //是否循环滚动
                .setType(new boolean[]{
                        true, true, true, true, true, false
                }) //显示“年月日时分秒”的哪几项
                .isCenterLabel(false) //是否只显示选中的label文字，false则每项item全部都带有 label
                .build();

        //设置显示的日期
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy/M/d H:m").parse(new SimpleDateFormat("yyyy/M/d H:m").format(new Date(System.currentTimeMillis()))));
        } catch (
                ParseException e) {
            //e.printStackTrace();
        }
        //这里需要注意的是月份是从0开始的，要显示10月份这里的参数应该是9
        //calendar.set(1997,9,10);
        datePicker.setDate(calendar);
        datePicker.show();
    }

    //获取当前时间,用来给文件夹命名
    private String getNowDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);
        return format.format(new Date()) + ".xls";
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TAG_ONE:
                    Toast.makeText(MainActivity.this, "读取失败，请检查文件是否存在", Toast.LENGTH_SHORT).show();
                    break;
                case TAG_THERE:
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}