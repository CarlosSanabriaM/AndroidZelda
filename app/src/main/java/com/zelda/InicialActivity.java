package com.zelda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class InicialActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pantalla completa, sin titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_inicial);
    }

    public void abrirSeleccionNivel(View v){
        //Pasamos a la siguiente actividad que es la de elegir nivel
        Intent actividadSeleccionNivel = new Intent(this,SeleccionNivelActivity.class);
        startActivity(actividadSeleccionNivel);
    }

}
