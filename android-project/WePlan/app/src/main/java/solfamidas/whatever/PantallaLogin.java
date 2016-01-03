package solfamidas.whatever;
/**
 * Creado por Alejandro Alarcón Villena, 2015
 * Como proyecto para la asignatura Sistemas Multimedia
 * */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PantallaLogin extends AppCompatActivity {
    private static final String REGISTER_URL = "http://grizzly.pw/operations.php?op=register";
    private String username;
    private String passwordG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.textView9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantallaLogin.this, PantallaRegistro.class));
            }
        });
    }

    public void validarLogin(View view) {
        String user = ((EditText)findViewById(R.id.user)).getText().toString();
        String pass = ((EditText)findViewById(R.id.password)).getText().toString();

        username = user;
        passwordG = pass;

        if (getSharedPreferences("login",0).edit().putString("user",username).putString("password",passwordG).commit()) {
            Toast.makeText(getApplicationContext(), "Usuario almacenado con éxito", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(PantallaLogin.this,ListaEventos.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}