package solfamidas.weplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PantallaRegistro extends AppCompatActivity {
    private static final String REGISTER_URL = "http://grizzly.pw/operations.php?op=register";
    private String username;
    private String passwordG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void validarRegistro(View view) {
        String user = ((EditText)findViewById(R.id.user)).getText().toString();
        String pass = ((EditText)findViewById(R.id.password)).getText().toString();
        String pass2 = ((EditText)findViewById(R.id.password2)).getText().toString();
        String email = ((EditText)findViewById(R.id.correo)).getText().toString();

        if (!pass.equals(pass2)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
        }

        else {
            String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);

            if (matcher.matches()) {
                if (!user.equals("") && !email.equals("") && !pass.equals("")) {
                    username = user;
                    passwordG = pass;
                    register(user,pass,pass2,email);
                }
            }

            else {
                Toast.makeText(this, "El correo no es válido", Toast.LENGTH_SHORT);
            }
        }
    }

    private void register(String name, String password, String repassword, String email) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserAux ruc = new RegisterUserAux();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PantallaRegistro.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if (!s.equals("Error Registering")) {
                    try {
                        JSONObject res = new JSONObject(s);
                        if (res.getString("status").equals("OK")) {
                            // almacenar usuario
                            if (getSharedPreferences("login",0).edit().putString("user",username).putString("password",passwordG).commit()) {
                                Toast.makeText(getApplicationContext(),"Usuario almacenado con éxito",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(PantallaRegistro.this,ListaEventos.class);
                                startActivity(intent);
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(PantallaRegistro.this, "Error con el JSON resultado", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("username",params[0]);
                data.put("password",params[1]);
                data.put("repassword",params[2]);
                data.put("email",params[3]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,password,repassword,email);
    }
}