package com.zelda.modelos.disparos;

import android.content.Context;
import android.graphics.Canvas;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.modelos.Nivel;

/**
 * Created by UO250707 on 26/10/2017.
 */

public class DisparoOctorokRed extends Disparo {
    private static final double VELOCIDAD = 12;

    public DisparoOctorokRed(Context context, double xInicial, double yInicial, int orientacion) {
        super(context, xInicial, yInicial, 15, 12, orientacion, VELOCIDAD);//10,8
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.disparo_octorok);
    }

    public void dibujar(Canvas canvas){
        int yArriba = (int)  y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);
    }



}
