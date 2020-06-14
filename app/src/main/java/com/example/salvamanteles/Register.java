package com.example.salvamanteles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Register extends AppCompatActivity {
    EditText nameField, emailField, passwordField;
    Button registerButton, goToLoginButton;
    Retrofit retrofit;
    apiInterface apiInterface;
    String tokenGot;
    SharedPreferencesClass getTokenInRegister = new SharedPreferencesClass();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        registerButton = findViewById(R.id.createUser);
        goToLoginButton = findViewById(R.id.goToLogin);

        //Carga el token al iniciar la activity
        getTokenInRegister.loadTokenPreferences(context);

        //Cambia a pantalla de login
        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Login.class);
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

        //Al pulsar el botón de registrarse hace el registro de usuario
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerRegistro();
            }
        });
    }

    void hacerRegistro() {
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        Call<Token> llamada = apiInterface.register(name, email, password);
        Log.d("registro", name + email + password);
        llamada.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() == 201) {
                    Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                    tokenGot = response.body().token;
                    Log.d("tokencito", tokenGot);
                    //Guardamos token de la response
                    getTokenInRegister.saveTokenPreferences(tokenGot, context);
                }
                if (response.code() == 400) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(Register.this, jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(Register.this, "Problema de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
