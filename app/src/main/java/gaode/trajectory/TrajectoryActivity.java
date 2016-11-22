package gaode.trajectory;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;

import bean.Bean.ReturnValueBean.DataListBean;

import java.util.ArrayList;
import java.util.List;

import bean.Bean;
import config.Config;
import gaodedemo.nl.org.gaodedemoapplication.R;
import tools.ReadStringUtil;
import gaode.trajectory.TrajectRunnable.TrajectListener;

/**
 * Created by nl on 2016/11/21.
 */

public final class TrajectoryActivity extends Activity implements View.OnClickListener, AMap.InfoWindowAdapter, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private String TAG = TrajectoryActivity.class.getName().toString().trim();
    private Button btn1;
    private Button btn2;
    private String str;
    private Bean bean;
    private MapView mapview;
    private AMap aMap;
    private ImageView play_image;
    private RelativeLayout play_root;
    private SeekBar seekbar;
    private SwitchButton switchButton;


    private Polyline polyline;

    private Marker startMarker;            //起点

    private Marker endMarker;              //终点

    private Marker moveMarker;             //可移动的点

    private List<DataListBean> beanList = new ArrayList<>();

    private TrajectRunnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajectory);
        initView(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    void initView(Bundle savedInstanceState) {

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        mapview = (MapView) findViewById(R.id.mapview);
        mapview.onCreate(savedInstanceState);
        aMap = mapview.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        play_image = (ImageView) findViewById(R.id.play_image);
        play_image.setOnClickListener(this);
        play_root = (RelativeLayout) findViewById(R.id.play_root);
        play_root.setOnClickListener(this);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        seekbar.setOnClickListener(this);
        switchButton = (SwitchButton) findViewById(R.id.switchButton);
        switchButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                str = ReadStringUtil.ReadString(this, R.raw.trajectory1);
                break;
            case R.id.btn2:
                str = ReadStringUtil.ReadString(this, R.raw.trajectory2);
                break;
        }
        bean = new Gson().fromJson(str, Bean.class);
        if (bean != null) {
            beanList = bean.getReturnValue().getDataList();
        }
        if (beanList.size() > 2) {
            displayPolyline();
            displayMarkers();
            runnable = new TrajectRunnable(beanList, trajectListener);
            runnable.run();
        }
    }

    /**************
     * Gaode Marker
     *************/

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /******************
     * seekbar
     ***************/

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /****
     * switchButton
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private TrajectListener trajectListener = new TrajectListener() {
        @Override
        public void onUpdataPlaying(int index) {
            Log.d(TAG, "index = " + index);
        }

        @Override
        public void onStartPlaying() {
            Log.d(TAG, "startPlaying");
        }

        @Override
        public void onStopPlaying() {
            Log.d(TAG, "onStopPlaying");
        }

        @Override
        public void onFinishPlaying() {
            Log.d(TAG, "onStopPlaying");
        }
    };

    /***
     * 绘制轨迹线
     */
    private void displayPolyline() {
        List<LatLng> latlngList = new ArrayList<>();
        for (DataListBean bean : beanList) {
            if (bean != null) {
                LatLng latlng = new LatLng(bean.getLat(), bean.getLon());
                latlngList.add(latlng);
            }
        }
        PolylineOptions option = new PolylineOptions();
        option.zIndex(9);
        option.color(Color.argb(255, 58, 125, 240));
        option.width(15);
        option.addAll(latlngList);
        if (polyline != null) {
            polyline.remove();
        }
        polyline = aMap.addPolyline(option);
    }


    private void displayMarkers() {
        LatLng startLatLng = new LatLng(beanList.get(0).getLat(), beanList.get(0).getLon());
        LatLng endLatLng = new LatLng(beanList.get(beanList.size() - 1).getLat(), beanList.get(beanList.size() - 1).getLon());

        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, Config.ZoomLevel));
        if (moveMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions().setFlat(true).anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.trajectory_dot_one))
                    .position(startLatLng).zIndex(12).title("信息");
            moveMarker = aMap.addMarker(markerOptions);
            moveMarker.showInfoWindow();
        }
        moveMarker.setPosition(startLatLng);

        if (startMarker == null) {
            MarkerOptions odev = new MarkerOptions().position(startLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.trajectory_dot_start))
                    .zIndex(10)
                    .draggable(false);
            startMarker = (aMap.addMarker(odev));
        }
        startMarker.setPosition(startLatLng);

        if (endMarker == null) {
            MarkerOptions odev1 = new MarkerOptions().position(endLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.trajectory_dot_end)).zIndex(10).draggable(false);//
            endMarker = (aMap.addMarker(odev1));
        }
        endMarker.setPosition(endLatLng);
    }
}
