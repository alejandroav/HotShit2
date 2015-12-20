package solfamidas.grizzly;

/**
 * Creado por Alejandro Alarcón Villena, 2015
 * Como proyecto para la asignatura Sistemas Multimedia
 * * */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ListaEventos extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private double currentLatitude;
    private double currentLongitude;
    private Socket socket;
    private String msg;
    private JSONArray events;
    private double radioMapa = 100;
    private String error;
    private String LOGIN_URL = "http://grizzly.pw/operations.php?op=login";
    private ListView lv;

    ArrayList<Evento> eventos = new ArrayList<Evento>();
    ArrayAdapter adapter;

    String user;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_planes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.list);
        adapter = new MyListAdapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Evento clickedEvent = eventos.get(position);
                // llamar a detalle evento con el evento como mensaje!
            }
        });

        // comprobar permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0001);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0002);
            }
        }

        try {
            socket = IO.socket("http://grizzly.pw:8080"); // declarar el socket del server
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on("s-login", listenerLogin);
        socket.on("s-event-list", listenerLista);
        socket.connect(); // conectamos con el socket

        SharedPreferences settings = getSharedPreferences("login", 0);
        user = settings.getString("user", "unset");
        pass = settings.getString("password", "unset");

        if (user.equals("unset")) {
            Intent intent = new Intent(this, PantallaRegistro.class);
            startActivity(intent);

            user = settings.getString("user", "unset");
            pass = settings.getString("password", "unset");
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

        try {
            login(user, pass);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        } catch (TimeoutException e1) {
            e1.printStackTrace();
        }
    }

    public void postLogin(JSONObject logeo) {
        try {
            if (!logeo.getString("status").equals("ERROR")) {
                logeo = logeo.getJSONObject("data");
                // logeamos contra node
                JSONObject solicitud = new JSONObject();
                solicitud.put("user_id",logeo.getString("userid"));
                solicitud.put("session_id", logeo.getString("sessionid"));
                socket.emit("c-login", solicitud);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // escuchar para recibir el mapa de eventos
    private Emitter.Listener listenerLogin = new Emitter.Listener() {
        public void call(final Object[] args) {
            ListaEventos.this.runOnUiThread(new Runnable() {
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        msg = data.getString("status");
                        if (msg.equals("OK")) {
                            // exito con login

                            solicitarEventos();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public void solicitarEventos() {
        try {
            JSONObject solicitud = new JSONObject();
            solicitud.put("loc_long", currentLongitude);
            solicitud.put("loc_lat", currentLatitude);
            solicitud.put("radio", radioMapa);
            solicitud.put("type",0);
            socket.emit("c-event-list", solicitud);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al solicitar eventos", Toast.LENGTH_SHORT).show();
        }
    }

    // escuchar para recibir el mapa de eventos
    private Emitter.Listener listenerLista = new Emitter.Listener() {
        public void call(final Object[] args) {
            ListaEventos.this.runOnUiThread(new Runnable() {
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        msg = data.getString("status");
                        if (msg.equals("OK")) {
                            events = data.getJSONArray("data");
                            recuperarEventos(msg, events);
                        } else Toast.makeText(ListaEventos.this, "Error al recibir eventos: " + data, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(ListaEventos.this, "Error JSON: " + data, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                }
            });
        }
    };

    public void recuperarEventos(String msg, JSONArray events) {
        if (currentLatitude != 0 || currentLongitude != 0) {
            try {
                if (msg.equals("OK")) {
                    eventos.clear();
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject item = events.getJSONObject(i);
                        eventos.add(new Evento(item.getInt("id"), item.getString("title"),
                                item.getString("description"), item.getDouble("distance"),
                                item.getInt("capacity"), item.getInt("current"), item.getString("image"),item.getString("date")));
                    }

                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error JSON: " + e, Toast.LENGTH_SHORT).show();
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

    public void iniciarMapa() {
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

    private void login(final String name, final String password) throws ExecutionException, InterruptedException, TimeoutException {
        class Login extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserAux ruc = new RegisterUserAux();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListaEventos.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (!s.equals("Error Registering")) {
                    try {
                        JSONObject res = new JSONObject(s);
                        if (res.getString("status").equals("OK")) {
                            // exito
                            System.out.println(res);
                            postLogin(res);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("username",params[0]);
                data.put("password", params[1]);

                String result = ruc.sendPostRequest(LOGIN_URL,data);
                return  result;
            }
        }

        Login ru = new Login();
        ru.execute(name, password).get();
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is,null);
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private class MyListAdapter extends ArrayAdapter<Evento> {
        public MyListAdapter() {
            super(ListaEventos.this, R.layout.planes, eventos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.planes, parent, false);
            }

            Evento evento = eventos.get(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageList);
            imageView.setImageDrawable(LoadImageFromWebOperations(evento.getImage()));

            TextView title = (TextView) itemView.findViewById(R.id.titleList);
            title.setText(evento.getTitle());

            TextView date = (TextView) itemView.findViewById(R.id.dateList);
            date.setText(evento.getDate());

            TextView people = (TextView) itemView.findViewById(R.id.peopleList);
            people.setText(evento.getCurrentPeople());

            TextView distance = (TextView) itemView.findViewById(R.id.distanceList);
            distance.setText(evento.getDistance() + " km");

            return itemView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.abrirMapa) {
            iniciarMapa();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
