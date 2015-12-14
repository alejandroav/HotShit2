package solfamidas.weplan;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MapaEventos extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    JSONArray events;
    LatLng[] markers;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    String msg;

    private Socket socket;
    {
        try {
            socket = IO.socket("http://grizz.ly"); // declarar el socket del server
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_eventos);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER

        // solicitamos mapa eventos al servidor y lo guardamos
        socket.emit("c-event-map", new Object());
        socket.on("s-event-map", listenerMapa);
        socket.connect(); // conectamos con el socket

        if (msg=="OK") {
            markers = new LatLng[events.length()];
            // recorremos los datos y vamos colocando los marcadores
            try {
                for (int i = 0; i < markers.length; i++) {
                    JSONObject j = (JSONObject) events.get(i);
                    double[] coordenadas = (double[]) j.get("coord");
                    String nombreEvento = (String) j.get("name");
                    String descEvento = (String) j.get("desc");
                    LatLng m = new LatLng(coordenadas[0], coordenadas[1]);
                    Marker punto = mMap.addMarker(new MarkerOptions().position(m).title(nombreEvento));
                    punto.setSnippet(descEvento);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // escuchar para recibir el mapa de eventos
    private Emitter.Listener listenerMapa = new Emitter.Listener() {
        public void call(final Object[] args) {
            MapaEventos.this.runOnUiThread(new Runnable() {
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        msg = data.getString("msg");
                        events = data.getJSONArray("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            });
        }
    };

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // mover la camara al usuario

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);

        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}
