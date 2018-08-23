package kweather.android.godueol.com.kweather.Retrofit;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import kweather.android.godueol.com.kweather.Entity.CweatherPOJO.CweatherPOJO;
import kweather.android.godueol.com.kweather.Entity.TweatherPOJO.TweatherPOJO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance;
    private static Retrofit retrofit;
    private static String BaseURL = "https://api.openweathermap.org";

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    private RetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    private WeatherRetrofitService getService() {
        return retrofit.create(WeatherRetrofitService.class);
    }

    public Observable<TweatherPOJO> getTweather(String lat, String lon) {
        return Observable.create((ObservableEmitter<TweatherPOJO> data) -> {
            getService().getWeather(lat, lon).enqueue(new Callback<TweatherPOJO>() {
                @Override
                public void onResponse(Call<TweatherPOJO> call, Response<TweatherPOJO> response) {
                    if (response.isSuccessful()) {
                        data.onNext(response.body());
                        data.onComplete();
                    } else {
                        data.onError(new Throwable("잠시후 다시 시도해주세요."));
                    }
                }

                @Override
                public void onFailure(Call<TweatherPOJO> call, Throwable t) {
                    data.onError(new Throwable(t.getMessage()));
                }
            });
        });
    }

    public Observable<CweatherPOJO> getCweather(String lat, String lon) {
        return Observable.create((ObservableEmitter<CweatherPOJO> data) -> {
            getService().getCurrentWeather(lat, lon).enqueue(new Callback<CweatherPOJO>() {
                @Override
                public void onResponse(Call<CweatherPOJO> call, Response<CweatherPOJO> response) {
                    if (response.isSuccessful()) {
                        data.onNext(response.body());
                        data.onComplete();
                    } else {
                        data.onError(new Throwable("잠시후 다시 시도해주세요."));
                    }
                }

                @Override
                public void onFailure(Call<CweatherPOJO> call, Throwable t) {
                    data.onError(new Throwable(t.getMessage()));
                }
            });
        });

    }
}
