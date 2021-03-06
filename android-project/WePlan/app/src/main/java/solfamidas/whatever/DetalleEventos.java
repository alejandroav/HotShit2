
package solfamidas.whatever;

import android.support.v7.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Àngel Navarro y Alejandro Alarcón on 20/12/2015.
 * Para el proyecto de la asignatura Sistemas Multimedia
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
        //Recojo el valor de la id
        b = getIntent().getExtras();
        id = b.getInt("id");
        //Declaro el socket del server
        try {
            socket = IO.socket("http://grizzly.pw:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //Me conecto con el servidor
        socket.on("s-event-subscribe", ListenerSuscripcion);
        socket.on("s-event-unsubscribe", ListenerUnsubscribe);
        socket.on("s-event-details", ListenerDetails);
        socket.connect();
        //Solicito los datos del evento
        try{
            JSONObject solicitud = new JSONObject();
            solicitud.put("event_id", id);
            socket.emit("c-event-details", solicitud);
        }
        catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Error al solicitar eventos", Toast.LENGTH_SHORT).show();
        }
        Button Suscripcion = (Button)findViewById(R.id.suscripcion);
        Suscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view){
                try{

                    JSONObject solicitud = new JSONObject();
                    solicitud.put("event_id", id);
                    if(((Button) findViewById(R.id.suscripcion)).getText().equals("SUSCRIBIRSE")){
                        socket.emit("c-event-subscribe", solicitud);
                    }
                    else{
                        socket.emit("c-event-unsubscribe", solicitud);
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                    Toast.makeText(DetalleEventos.this, "Error al solicitar eventos", Toast.LENGTH_SHORT).show();
                }
           }
        });

    }


    public void AlterarValores(){
        TextView Titulo = (TextView)findViewById(R.id.nombre_evento);
        Titulo.setText(evento.getTitle());
        TextView Distancia = (TextView)findViewById(R.id.distancia);
        Distancia.setText("A "+evento.getDistance()+" km");
        TextView Fecha = (TextView)findViewById(R.id.fecha);
        Fecha.setText("El "+evento.getDate());
        TextView Gente = (TextView)findViewById(R.id.gente);
        Gente.setText(evento.getCurrent()+"/"+evento.getCapacity());
        TextView Descipcion = (TextView)findViewById(R.id.descripcion);
        Descipcion.setText(evento.getDesc());
        ImageView imageView = (ImageView) findViewById(R.id.imagenPerfil);
        imageView.setImageDrawable(LoadImageFromWebOperations(evento.getImage()));
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

    private Emitter.Listener ListenerUnsubscribe = new Emitter.Listener(){
        public void call(final Object[] args){
            DetalleEventos.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try{
                        msg = data.getString("status");
                        if(msg.equals("OK")){
                            Button BSuscripcion = (Button)findViewById(R.id.suscripcion);
                            BSuscripcion.setText("SUSCRIBIRSE");
                        }
                        else{
                            Toast.makeText(DetalleEventos.this, "Error al desuscribirse: " + data, Toast.LENGTH_SHORT).show();
                        }
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

    private Emitter.Listener ListenerSuscripcion = new Emitter.Listener(){
        public void call(final Object[] args){
            DetalleEventos.this.runOnUiThread(new Runnable() {
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try{
                        msg = data.getString("status");
                        if(msg.equals("OK")){
                            Button BSuscripcion = (Button)findViewById(R.id.suscripcion);
                            BSuscripcion.setText("CANCELAR SUSCRIPCION");
                        }
                        else{Toast.makeText(DetalleEventos.this, "Error al suscribirse: " + data, Toast.LENGTH_SHORT).show();}
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

    private Emitter.Listener ListenerDetails = new Emitter.Listener(){
        public void call(final Object[] args) {
            DetalleEventos.this.runOnUiThread(new Runnable() {
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try{
                        msg = data.getString("status");
                        if (msg.equals("OK")) {
                            JSONObject eventsdetails = data.getJSONObject("data");
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

    private void crearEvento(String msg, JSONObject item){
        try{
            if(msg.equals("OK")){
                evento = new Evento(id, item.getString("title"), item.getString("description"),
                        0, item.getInt("capacity"), item.getInt("current"), item.getString("image"),item.getString("date"));

                //item.getDouble("distance"),

                AlterarValores();
            }
        }
        catch ( JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Error JSON: " + e, Toast.LENGTH_SHORT).show();
        }
    }


}