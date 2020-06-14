package com.example.salvamanteles;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {
    EditText emailField, passwordField;
    Button loginButton, goToLoginButton;
    Retrofit retrofit;
    apiInterface apiInterface;
    String tokenGot;
    SharedPreferencesClass getTokenInLogin = new SharedPreferencesClass();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginUser);
        goToLoginButton = findViewById(R.id.goToLogin);
        Button goToRegister = findViewById(R.id.goToRegister);
        //Carga el token al iniciar la activity
        getTokenInLogin.loadTokenPreferences(context);
        //Cambia a la pantalla de registro
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Register.class);
                startActivityForResult(intent, 0);
            }
        });
        // Inicializar Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.26/salvamanteles/public/index.php/api/") // URL del servidor (API)
                .addConverterFactory(GsonConverterFactory.create()) // Conversor de JSON
                .addConverterFactory(ScalarsConverterFactory.create()) //Conversor de escalares
                .build();
        apiInterface = retrofit.create(apiInterface.class);
        //Al pulsar el botón de loguearse hace el login de usuario
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerLogin();
            }
        });
    }

    void hacerLogin() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        Call<Token> llamada = apiInterface.login(email, password);
        Log.d("login",  email + password);
        llamada.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() == 201) {
                    tokenGot = response.body().token;
                    Log.d("tokencito", tokenGot);
                    //Guardamos token de la response
                    getTokenInLogin.saveTokenPreferences(tokenGot, context);
                    //Pasamos a pantalla de perfiles
                    Intent toProfileList = new Intent(getApplicationContext(), ProfileList.class);
                    startActivity(toProfileList);
                }
                if (response.code() == 400) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(Login.this, jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(Login.this, "Problema de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
