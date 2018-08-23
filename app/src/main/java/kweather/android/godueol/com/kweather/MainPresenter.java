package kweather.android.godueol.com.kweather;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.location.Location;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.Observable;
import kweather.android.godueol.com.kweather.Entity.CweatherPOJO.CweatherDataSet;
import kweather.android.godueol.com.kweather.Entity.TweatherPOJO.TweatherDataSet;
import kweather.android.godueol.com.kweather.Retrofit.RetrofitClient;
import kweather.android.godueol.com.kweather.Util.GPSUtil;

public class MainPresenter implements MainContract.Presenter {

    public static int size = 6;
    private GPSUtil gpsUtil;
    private RetrofitClient retrofitClient;
    private Location location;
    private MainContract.View mView;

    MainPresenter(MainContract.View mView, GPSUtil gpsUtil) {
        this.mView = mView;
        mView.setPresenter(this);
        this.gpsUtil = gpsUtil;
        location = gpsUtil.getGPSLocation();
        retrofitClient = RetrofitClient.getInstance();
    }

    @SuppressLint("CheckResult")
    @Override
    public void callCurrentWeather() {
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        retrofitClient.getCweather(lat, lon)
                .map(CweatherDataSet::new)
                .retry(3)
                .subscribe(data -> mView.showCurrentWeather(data), e -> mView.showErrorMsg(e.getMessage()));
    }

    @SuppressLint("CheckResult")
    @Override
    public void callTimeWeather() {
        List<Entry> entries = new ArrayList<Entry>();
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        retrofitClient.getTweather(lat, lon)
                .map(TweatherDataSet::new)
                .flatMap(data -> {
                    mView.showTimeWeather(data);
                    return Observable.just(data.getList());
                }).map(list -> list.subList(0, size))
                .flatMapIterable(list -> list)
                .zipWith(Observable.range(0, size),
                        (data, i) -> new Entry(i, data.getFtemp()))
                .map(entries::add)
                .reduce((b1, b2) -> b1 || b2)      // 리스트를 전부add하기위한 dummy reduce...
                .retry(3)
                .subscribe(data -> mView.showTimeWeatherChart(entries), e -> mView.showErrorMsg(e.getMessage()));
    }

    @Override
    public void refreshWeatherSet() {
        location = gpsUtil.getGPSLocation();
        callCurrentWeather();
        callTimeWeather();
        mView.showRefresh();
    }

    @BindingConversion
    public static String convertDateToText(Date date) {
        try {
            return new SimpleDateFormat("MM월 dd일 E요일 HH:mm", Locale.KOREA).format(date);
        } catch (Exception ignored) {
            return null;
        }
    }

    @BindingAdapter({"convertDate"})
    public static void convertDate(TextView view, Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.KOREA);
            format.setTimeZone(TimeZone.getTimeZone("GMT+9"));
            view.setText(format.format(date));
        } catch (Exception ignored) {
        }
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
}
