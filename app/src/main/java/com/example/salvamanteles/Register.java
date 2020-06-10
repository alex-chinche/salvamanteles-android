package com.example.salvamanteles;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
    EditText nameField;
    EditText emailField;
    EditText passwordField;
    Button registerButton;
    Button goToLoginButton;
    Retrofit retrofit;
    apiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        registerButton = findViewById(R.id.createUser);
        goToLoginButton = findViewById(R.id.goToLogin);

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
                .build();
        apiInterface = retrofit.create(apiInterface.class);

        //Al pulsar el botón de registrarse
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
        Call<String> llamada = apiInterface.register(name, email, password);
        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    //ESTO DEBERIA EJECUTARSE CUANDO VA TOODO CORRECTO, YA QUE DESDE LA API LLEGA UN CODIGO 200 CON EL TOKEN, SIN EMBARGO, NUNCA LLEGO A ESTE PUNTO DEL CODIGO
                    Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                    //GUARDAR TOKEN DE LA RESPONSE AQUI (POR HACER)
                }
                else {
                    //ESTO SE EJECUTA CORRECTAMENTE CUANDO HAY ALGUN ERROR UNA VEZ ACCEDIDO AL SERVER
                    //EN ESTE PUNTO LA RESPONSE VIENE COMO: 'message' = "el mensaje que corresponda", SIN EMBARGO, CON response.body()
                    //NO LOGRO ACCEDER AL CUERPO DEL 'message', SINO QUE EL SIGUIENTE TOAST APARECE NULO SIEMPRE
                    Toast.makeText(Register.this, response.body(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //ESTO DEBERIA EJECUTARSE CUANDO NO HAY CONEXION CON EL SERVIDOR, SIN EMBARGO, SE EJECUTA CUANDO SE REGISTRA EL USUARIO QUE ESTÁ CLARO QUE HAY CONEXION
                Toast.makeText(Register.this, "Problema de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
