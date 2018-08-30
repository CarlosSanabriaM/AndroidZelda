package com.zelda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zelda.gestores.GestorNiveles;

public class SeleccionNivelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_seleccion_nivel);
    }

    public void abrirNivel_1(View v){
        this.abrirNivel(v,0);
    }

    public void abrirNivel_2(View v){
        this.abrirNivel(v,1);
    }

    public void abrirNivel_3(View v){
        this.abrirNivel(v,2);
    }

    private void abrirNivel(View v, int nivel){
        // Seleccionar el nivel
        GestorNiveles.getInstancia().setNivelActual(nivel);

        Intent actividadJuego = new Intent(this,MainActivity.class);
        startActivity(actividadJuego);
    }

}
