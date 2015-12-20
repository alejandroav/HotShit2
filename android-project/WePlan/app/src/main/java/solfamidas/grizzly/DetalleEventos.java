package solfamidas.grizzly;

import android.support.v7.app.AppCompatActivity;
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
import org.w3c.dom.Text;

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

/**
 * Created by Àngel on 20/12/2015.
 */

public class DetalleEventos extends AppCompatActivity {
    private Bundle b;
    private int id;
    private Socket socket;
    private String msg;
    private JSONArray eventsdetails;
    private Evento evento;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_detallado_sinsuscribir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        //Recojo el valor de la id
        b = getIntent().getExtras();
        id = b.getInt("key");
        //Declaro el socket del server
        try {
            socket = IO.socket("http://grizzly.pw:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //Me conecto con el servidor
        socket.on("s-event-details", ListenerDetails);
        socket.connect();
        //Solicito los datos del evento
        try{
            JSONObject solicitud = new JSONObject();
            solicitud.put("event-id", id);
            socket.emit("c-event-details", solicitud);
        }
        catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Error al solicitar eventos", Toast.LENGTH_SHORT).show();
        }
        //Cambio el titulo
        TextView Titulo = (TextView)findViewById(R.id.nombre_evento);
        Titulo.setText(evento.getTitle());
        TextView Distancia = (TextView)findViewById(R.id.distancia);
        Distancia.setText("A "+evento.getDistance()+" Km");
        TextView Fecha = (TextView)findViewById(R.id.fecha);
        Fecha.setText("El "+evento.getDate());
        TextView Gente = (TextView)findViewById(R.id.gente);
        Gente.setText(evento.getCurrent()+"/"+evento.getCapacity());
        TextView Descipcion = (TextView)findViewById(R.id.descripcion);
        Descipcion.setText(evento.getDesc());
    }

    private Emitter.Listener ListenerDetails = new Emitter.Listener(){
        public void call(final Object[] args) {
            DetalleEventos.this.runOnUiThread(new Runnable() {
                public void run() {
                 JSONObject data = (JSONObject) args[0];
                    try{
                        msg = data.getString("status");
                        if (msg.equals("OK")) {
                            eventsdetails = data.getJSONArray("data");
                            crearEvento(msg, eventsdetails);
                        } else Toast.makeText(DetalleEventos.this, "Error al recibir eventos: " + data, Toast.LENGTH_SHORT).show();
                    }
                    catch(JSONException e){
                        Toast.makeText(DetalleEventos.this, "Error JSON: " + data, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }
                }
            });
        }
    };

    private void crearEvento(String msg, JSONArray details){
        try{
            if(msg.equals("OK")){
                JSONObject item = details.getJSONObject(0);
                evento = new Evento(id, item.getString("title"), item.getString("description"), item.getDouble("distance"), item.getInt("capacity"), item.getInt("current"), item.getString("image"),item.getString("date"));
            }
        }
        catch ( JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Error JSON: " + e, Toast.LENGTH_SHORT).show();
        }
    }


}
