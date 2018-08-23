package kweather.android.godueol.com.kweather.Retrofit;

import kweather.android.godueol.com.kweather.Entity.CweatherPOJO.CweatherPOJO;
import kweather.android.godueol.com.kweather.Entity.TweatherPOJO.TweatherPOJO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRetrofitService {
    String Key = "8cad00b0f0794d4f63eb893552fa29f2";

    @GET("/data/2.5/forecast?APPID=" + Key)
    Call<TweatherPOJO> getWeather(@Query("lat") String lat, @Query("lon") String lon);

    @GET("/data/2.5/weather?APPID=" + Key)
    Call<CweatherPOJO> getCurrentWeather(@Query("lat") String lat, @Query("lon") String lon);
}
