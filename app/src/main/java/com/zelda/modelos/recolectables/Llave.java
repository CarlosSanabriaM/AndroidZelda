package com.zelda.modelos.recolectables;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.zelda.R;
import com.zelda.gestores.GestorAudio;
import com.zelda.modelos.Nivel;
import com.zelda.modelos.Puerta;

/**
 * Created by carlos on 12/12/17.
 */

public class Llave extends Recolectable {

    public Llave(Context context, double x, double y) {
        super(context, x, y, 20, 10);//16,8 (20,10)
        imagen=context.getResources().getDrawable(R.drawable.llave);
        this.y =  y - altura/2;
    }

    @Override
    public void accionRecolectable(Nivel nivel) {
        //Hace que aparezcan las puertas
        Log.v("Llave","Llave recogida");

        int xCentroAbajoTilePuertaSecreta1 = nivel.xCentroAbajoTilePuertaSecreta.get(0);
        int yCentroAbajoTilePuertaSecreta1 = nivel.yCentroAbajoTilePuertaSecreta.get(0);
        Puerta puerta1 = new Puerta(context, xCentroAbajoTilePuertaSecreta1, yCentroAbajoTilePuertaSecreta1, 9);

        int xCentroAbajoTilePuertaSecreta2 = nivel.xCentroAbajoTilePuertaSecreta.get(1);
        int yCentroAbajoTilePuertaSecreta2 = nivel.yCentroAbajoTilePuertaSecreta.get(1);
        Puerta puerta2 = new Puerta(context, xCentroAbajoTilePuertaSecreta2, yCentroAbajoTilePuertaSecreta2, 9);

        nivel.puertas.add(puerta1);
        nivel.puertas.add(puerta2);

        nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_LINK_RECOGIENDO_LLAVE);
        nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_PUERTA_DESBLOQUEADA);
    }

    @Override
    public void actualizar(long tiempo) {
        //No hace nada
    }

    public void dibujar(Canvas canvas){
        int yArriba = (int)  y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen.draw(canvas);

    }

}
