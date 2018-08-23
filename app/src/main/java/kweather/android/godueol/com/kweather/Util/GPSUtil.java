package kweather.android.godueol.com.kweather.Util;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class GPSUtil extends Service implements LocationListener {

    Context mContext;

    // GPS 사용여부
    boolean isGPSEnabled = false;
    // 네트워크 사용여부
    boolean isNetWorkEnabled = false;

    Location location;
    double lat; // 위도
    double lon; // 경도

    // requestLocationUpdate 의 변경기준 인자
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 1000;   // 1km
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public GPSUtil(Context context) {
        this.mContext = context;
        setDefaultLoction();
        TedPermission.with(mContext)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
    }

    private Location getknownLocation() {
        // FINE_LOCATION과 COARSE_LOCATION의 권한이 획득 되었는지 확인.
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location knownlocation = locationManager.getLastKnownLocation(provider);
            if (knownlocation == null) {
                continue;
            }
            if (bestLocation == null || knownlocation.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = knownlocation;
            }
        }

        return bestLocation;
    }

    public Location getGPSLocation() {

        // FINE_LOCATION과 COARSE_LOCATION의 권한이 획득 되었는지 확인.
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return location;
        }

        try {
            Criteria criteria = new Criteria();

            criteria.setAccuracy(Criteria.ACCURACY_FINE);     // 정확도
            criteria.setPowerRequirement(Criteria.POWER_LOW); // 전원소비량
            criteria.setAltitudeRequired(true);              // 고도
            criteria.setBearingRequired(false);              // 기본 정보, 방향, 방위
            criteria.setSpeedRequired(false);                // 속도
            criteria.setCostAllowed(true);                   // 위치정보 비용

            // criteria 의 정보를 이용하여 가장 적합한 Provider설정
            String provider = locationManager.getBestProvider(criteria, true);

            // GPS가 켜져있는지
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 네트워크가 켜져있는지
            isNetWorkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetWorkEnabled) {
            } else {
                if (isGPSEnabled /*&& location == null*/) { // GPS가 사용가능하다면
                    locationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }

                if (isNetWorkEnabled && location == null) {     // GPS로 못했을 경우 네트워크가 사용가능하다면
                    locationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // 위도 경도 저장
                            lat = location.getLatitude();
                            lat = location.getLongitude();
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } // end of try~catch

        // 위치를 잡지 못했을 때
        if (location == null) {
            location = getknownLocation();
        }

        return location;
    }

    private void setDefaultLoction() {
        location = new Location("Default");
        location.setLatitude(37.56);
        location.setLongitude(126.97);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLocation() {
        return location;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    private PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            location = getGPSLocation();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

}
