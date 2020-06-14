package com.example.salvamanteles;

import android.content.Context;
import android.util.Log;
import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesClass {
    public void saveTokenPreferences(String tokenGot, Context context) {
        //Guarda el token en SharedPreferences
        android.content.SharedPreferences saveToken = context.getSharedPreferences("credenciales", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = saveToken.edit();
        editor.putString("savedToken", tokenGot);
        editor.apply();
    }

    public void loadTokenPreferences(Context context) {
        //Accede al token guardado en SharedPreferences
        android.content.SharedPreferences saveToken = context.getSharedPreferences("credenciales", MODE_PRIVATE);
        String getTokenFromSharedPref = saveToken.getString("savedToken", "No info");
        Log.d("SharedToken", getTokenFromSharedPref);
    }
}
