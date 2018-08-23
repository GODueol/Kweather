package kweather.android.godueol.com.kweather;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.List;

import kweather.android.godueol.com.kweather.Entity.CweatherPOJO.CweatherDataSet;
import kweather.android.godueol.com.kweather.Entity.TweatherPOJO.TweatherDataSet;
import kweather.android.godueol.com.kweather.Util.GPSUtil;
import kweather.android.godueol.com.kweather.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    MainContract.Presenter mPresenter;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        GPSUtil gpsUtil = new GPSUtil(this);
        new MainPresenter(this, gpsUtil);
        binding.setPresenter(mPresenter);
        mPresenter.callCurrentWeather();
        mPresenter.callTimeWeather();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void showCurrentWeather(CweatherDataSet cweatherDataSet) {
        binding.setCweather(cweatherDataSet);
    }

    @Override
    public void showTimeWeather(TweatherDataSet tweatherDataSet) {
        binding.setTweather(tweatherDataSet);
    }

    @Override
    public void showTimeWeatherChart(List<Entry> list) {
        // 챠트 설정
        LineDataSet dataSet = new LineDataSet(list, "");
        dataSet.disableDashedHighlightLine();

        dataSet.setLineWidth(0.2f);
        dataSet.setCircleRadius(5);
        dataSet.setColor(Color.WHITE);
        dataSet.setCircleColor(Color.WHITE);
        dataSet.setValueTextSize(10f);
        dataSet.setValueFormatter(new MyValueFormatter());
        LineData lineData = new LineData(dataSet);

        binding.chart.setData(lineData);
        binding.chart.setDescription(new Description());
        binding.chart.setLongClickable(false);
        binding.chart.setTouchEnabled(false);
        binding.chart.setClickable(false);
        binding.chart.getDescription().setEnabled(false);

        // remove axis
        YAxis leftAxis = binding.chart.getAxisLeft();
        leftAxis.setEnabled(false);
        YAxis rightAxis = binding.chart.getAxisRight();
        rightAxis.setEnabled(false);
        XAxis xAxis = binding.chart.getXAxis();
        xAxis.setEnabled(false);

        // hide legend
        Legend legend = binding.chart.getLegend();
        legend.setEnabled(false);
        binding.chart.invalidate();
    }

    @Override
    public void showRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    public class MyValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("#");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value) + "°";
        }
    }

}
