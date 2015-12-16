package solfamidas.weplan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

public class MapaEventos extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private JSONArray events;
    private LatLng[] markers;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private String msg;
    private Context context;
    private int duration = Toast.LENGTH_SHORT;
    private CharSequence text;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapaEventos.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private Location location;
    private Location mLastLocation;
    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private double radioMapa = 50;
    private android.location.LocationListener locationListener;


    // variables para prueba stackoverflow
    Intent intentThatCalled;
    public double latitude;
    public double longitude;
    public Criteria criteria;
    public String bestProvider;
    private int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0001;
    private int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 0002;
    private int MY_PERMISSIONS_REQUEST_INTERNET = 0003;

    // socket para comunicar con servidor
    private Socket socket;
    {
        try {
            socket = IO.socket("http://grizzly.pw"); // declarar el socket del server
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_eventos);
        context = getApplicationContext();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_INTERNET);
                return;
            }
        }

        socket.on("s-event-map", listenerMapa);
        socket.connect(); // conectamos con el socket
        text = "Se ha conectado al servidor.";
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // escuchar para recibir el mapa de eventos
    private Emitter.Listener listenerMapa = new Emitter.Listener() {
        public void call(final Object[] args) {
            MapaEventos.this.runOnUiThread(new Runnable() {
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        msg = data.getString("status");
                        events = data.getJSONArray("data");
                        recuperarEventos();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            });
        }
    };

    public void solicitarEventos() {
        // solicitamos mapa eventos al servidor
        try {
            JSONObject solicitud = new JSONObject();
            solicitud.put("loc_long", new Double(currentLongitude).toString());
            solicitud.put("loc_lat", new Double(currentLatitude).toString());
            solicitud.put("radio", new Double(radioMapa).toString());
            socket.emit("c-event-map", solicitud);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al solicitar eventos", duration).show();
        }
    }

    public void recuperarEventos() {
        if (currentLatitude != 0 || currentLongitude != 0) {
            try {
                if (msg == "OK") {
                    // recorremos los datos y vamos colocando los marcadores
                    for (int i = 0; i < events.length(); i++) {
                        int id = events.getJSONObject(i).getInt("id");
                        double loc_long = events.getJSONObject(i).getDouble("loc_long");
                        double loc_lat = events.getJSONObject(i).getDouble("loc_long");
                        String nombreEvento = events.getJSONObject(i).getString("name");
                        String descEvento = events.getJSONObject(i).getString("description");
                        LatLng m = new LatLng(loc_long, loc_lat);
                        Marker punto = mMap.addMarker(new MarkerOptions().position(m).title(nombreEvento));
                        punto.setSnippet(descEvento);
                    }

                    text = "Mapa de eventos recuperado";
                    Toast.makeText(context, text, duration).show();
                }

                if (msg == "ERROR") {
                    text = "Ha ocurrido un error al solicitar el mapa de eventos.";
                    Toast.makeText(context, text, duration).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            text = "Error al recuperar la ubicación";
            Toast.makeText(context, text, duration).show();
        }
    }

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
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                return;
            }
        }
        locationManager.removeUpdates(this);

        //open the map:
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Toast.makeText(MapaEventos.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLatitude, currentLongitude)));
        //mMap.clear();
    }

    public static boolean isLocationEnabled(Context context) {
        return true;
    }

    public void getCurrentLocation() {
        /*mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        currentLatitude = mLastLocation.getLatitude();
        currentLongitude = mLastLocation.getLongitude();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLatitude,currentLongitude)));*/

        if (isLocationEnabled(MapaEventos.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                    return;
                }
            }

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

            //You can still do this if you like, you might get lucky:
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Toast.makeText(MapaEventos.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
            } else {
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
            Toast.makeText(context, "Buscando eventos...", duration).show();
            solicitarEventos();
        }

        else {
            Toast.makeText(MapaEventos.this, "Por favor, activa el GPS", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0001:
            break;
            case 0002:
            break;
            case 0003:
            break;
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

    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onPause() {
        mGoogleApiClient.disconnect();
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                return;
            }
        }
        locationManager.removeUpdates(this);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
