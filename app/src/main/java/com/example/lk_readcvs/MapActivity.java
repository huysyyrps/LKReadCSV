package com.example.lk_readcvs;

import static com.baidu.mapapi.utils.CoordinateConverter.CoordType.BD09MC;
import static com.baidu.mapapi.utils.CoordinateConverter.CoordType.COMMON;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.lk_readcvs.Util.TrackPoint;
import com.example.lk_readcvs.View.BottomUI;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
    private MapView mMapView = null;
    BaiduMap mBaiduMap;
    LocationClient mLocationClient;
    ArrayList<TrackPoint> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不息屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部按钮
        new BottomUI().hideBottomUIMenu(this.getWindow());
        setContentView(R.layout.activity_map);

        Intent intent=getIntent();
        arrayList= (ArrayList<TrackPoint>) intent.getSerializableExtra("list");
        Log.e("XXXXXX","--------"+arrayList.size());
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //卫星地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //开启地图的定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //通过MyLocationConfiguration类来构造包括定位的属性，定位模式、是否开启方向、设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色5个属性。
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, false, BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));


        if (arrayList.size()!=0){
//            //初始化坐标转换工具类，指定源坐标类型和坐标数据
//            LatLng sourceLatLng = new LatLng(Double.parseDouble(arrayList.get(0).getLatitude())
//                    ,Double.parseDouble(arrayList.get(0).getLongitude()));
//            //初始化坐标转换工具类，设置源坐标类型和原坐标数据
//            CoordinateConverter converter  = new CoordinateConverter()
//                    .from(COMMON)
//                    .coord(sourceLatLng);
//
//
//            //desLatLng 转换后的坐标
//            LatLng cenpt = converter.convert();


            //设定中心点坐标
            LatLng cenpt = new LatLng(Double.parseDouble(arrayList.get(0).getLatitude())
                    ,Double.parseDouble(arrayList.get(0).getLongitude()));
            MapStatus mMapStatus = new MapStatus.Builder()//定义地图状态
                    .target(cenpt)
                    .zoom(20)
                    .build();  //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);//改变地图状态


            //定义Maker坐标点
            for (int i = 0; i < arrayList.size(); i++) {
                LatLng point = new LatLng(Double.valueOf(arrayList.get(i).getLatitude()), Double.valueOf(arrayList.get(i).getLongitude()));
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_latlog);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);

            }
        }


        //定位初始化
        mLocationClient = new LocationClient(this);
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            if (arrayList.size()!=0){
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection())
                        .latitude(Double.parseDouble(arrayList.get(0).getLatitude()))
                        .longitude(Double.parseDouble(arrayList.get(0).getLongitude())).build();
                mBaiduMap.setMyLocationData(locData);
            }else {
                finish();
                Toast.makeText(MapActivity.this, "kml文件为空", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}