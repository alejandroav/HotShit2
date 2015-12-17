package solfamidas.weplan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ListaEventos extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private double currentLatitude;
    private double currentLongitude;
    private Socket socket;
    private String msg;
    private JSONArray events;
    private double radioMapa = 50;
    private String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_planes);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // comprobar permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0001);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0002);
            }
        }

        // acceder a la api
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // obtenemos localizacion actual
        LatLng latlng = Localizador.getCurrentLocation(this);
        currentLatitude = latlng.latitude;
        currentLongitude = latlng.longitude;

        // abrir socket, asignar listener
    }

    // escuchar para recibir el mapa de eventos
    private Emitter.Listener listenerLista = new Emitter.Listener() {
        public void call(final Object[] args) {
            ListaEventos.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ListaEventos.this, "Ha saltado el listener", Toast.LENGTH_SHORT).show();
                    JSONObject data = (JSONObject) args[0];
                    try {
                        msg = data.getString("status");
                        events = data.getJSONArray("data");
                        error = data.getString("msg");
                        recuperarEventos(msg, events);
                    } catch (JSONException e) {
                        Toast.makeText(ListaEventos.this, "Error JSON", Toast.LENGTH_SHORT).show();
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
            socket.emit("c-event-list", solicitud);
            Toast.makeText(this, "Buscando eventos...", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al solicitar eventos", Toast.LENGTH_SHORT).show();
        }
    }

    public void recuperarEventos(String msg, JSONArray events) {
        if (currentLatitude != 0 || currentLongitude != 0) {
            try {
                if (msg == "OK") {
                    Toast.makeText(this, "El servidor ha respondido", Toast.LENGTH_SHORT).show();
                    // recorremos los datos y vamos colocando los marcadores
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject item = events.getJSONObject(i);
                        int id = item.getInt("id");
                        String titulo = item.getString("title");
                        double loc_long = item.getDouble("loc_long");
                        double loc_lat = item.getDouble("loc_long");
                        String nombreEvento = item.getString("title");
                        String descEvento = item.getString("description");
                        double coste = item.getDouble("coste");

                    }

                    Toast.makeText(this, "Lista recuperada", Toast.LENGTH_SHORT).show();
                }

                if (msg == "ERROR") {
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error JSON", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"Error al recuperar la ubicación", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void iniciarMapa(View view) {
        Intent intent = new Intent(this,MapaEventos.class);
        startActivity(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
