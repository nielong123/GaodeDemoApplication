package gaode.trajectory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;

import bean.Bean;
import gaodedemo.nl.org.gaodedemoapplication.R;
import tools.ReadStringUtil;

/**
 * Created by nl on 2016/11/21.
 */

public final class TrajectoryActivity extends Activity implements View.OnClickListener, AMap.InfoWindowAdapter, SeekBar.OnSeekBarChangeListener,CompoundButton.OnCheckedChangeListener {

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


    private Polyline mPolyline;

    private Marker mStartMarker;            //起点

    private Marker mEndMarker;              //终点

    private Marker mMoveMarker;             //...

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
     *  switchButton
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
