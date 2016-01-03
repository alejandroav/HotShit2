package solfamidas.whatever;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Creado por Alejandro Alarcón Villena 2015
 * Como proyecto para la asignatura Sistemas Multimedia
 * */
public class Localizador {
    static LocationManager locationManager;
    static String bestProvider;
    static double latitude;
    static double longitude;

    public static LatLng getCurrentLocation(Context context) {
        LatLng latlng;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        bestProvider = String.valueOf(locationManager.getBestProvider(new Criteria(), true));
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            Log.e("TAG", "GPS is on");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            latlng = new LatLng(latitude, longitude);
            return latlng;
        }
        Toast.makeText(context,"Error al obtener tu ubicación",Toast.LENGTH_SHORT);
        return null;
    }
}
