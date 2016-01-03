package solfamidas.whatever;

/**
 * Created by Angel on 02/01/2016.
 */

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;

import io.socket.client.Socket;

public class NavPerfil extends ActionBarActivity {

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

}
