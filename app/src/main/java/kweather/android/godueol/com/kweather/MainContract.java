package kweather.android.godueol.com.kweather;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

import kweather.android.godueol.com.kweather.Entity.CweatherPOJO.CweatherDataSet;
import kweather.android.godueol.com.kweather.Entity.TweatherPOJO.TweatherDataSet;

public interface MainContract {

    interface View extends BaseView<Presenter> {
        // 현재날씨 보여주기
        void showCurrentWeather(CweatherDataSet cweatherDataSet);

        // 시간별 날씨 가져오기
        void showTimeWeather(TweatherDataSet tweatherDataSet);

        // 시간별 날씨 차트 그리기
        void showTimeWeatherChart(List<Entry> list);

        // 새로고침할시 (swipe false)
        void showRefresh();

        // 현재날씨 보여주기
        void showErrorMsg(String msg);
    }

    interface Presenter {
        // 현재날씨 가져오기
        void callCurrentWeather();

        // 시간별 날씨 가져오기
        void callTimeWeather();

        // 새로고침할시
        void refreshWeatherSet();
    }
}
