package com.zelda.modelos.disparos;

import android.content.Context;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.graficos.Sprite;

/**
 * Created by carlos on 13/12/17.
 */

public class DisparoMagia extends DisparoJugador{

    private static final double VELOCIDAD = 10;

    public static final String DISPARO_ARRIBA = "arriba";
    public static final String DISPARO_DERECHA = "derecha";
    public static final String DISPARO_ABAJO = "abajo";
    public static final String DISPARO_IZQUIERDA = "izquierda";

    public DisparoMagia(Context context, double xInicial, double yInicial, int orientacion) {
        super(context, xInicial, yInicial, 16, 16, orientacion, VELOCIDAD);
        inicializar();
    }


    public void inicializar (){

        Sprite disparoArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_disparo_arriba),
                ancho, altura,
                1, 1, true);
        sprites.put(DISPARO_ARRIBA, disparoArriba);

        Sprite disparoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_disparo_derecha),
                ancho, altura,
                1, 1, true);
        sprites.put(DISPARO_DERECHA, disparoDerecha);

        Sprite disparoAbajo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_disparo_abajo),
                ancho, altura,
                1, 1, true);
        sprites.put(DISPARO_ABAJO, disparoAbajo);

        Sprite disparoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_disparo_izquierda),
                ancho, altura,
                1, 1, true);
        sprites.put(DISPARO_IZQUIERDA, disparoIzquierda);

        sprite = disparoAbajo;//le damos un valor inicial que se va a sobrescribir

    }

    @Override
    public void actualizar (long tiempo) {

        if (velocidadX > 0){
            sprite = sprites.get(DISPARO_DERECHA);
            orientacion = DERECHA;
        }
        else if (velocidadX < 0 ){
            sprite = sprites.get(DISPARO_IZQUIERDA);
            orientacion = IZQUIERDA;
        }
        else if(velocidadY > 0){
            sprite = sprites.get(DISPARO_ABAJO);
            orientacion = ABAJO;
        }
        else if (velocidadY < 0 ){
            sprite = sprites.get(DISPARO_ARRIBA);
            orientacion = ARRIBA;
        }

        sprite.actualizar(tiempo);
    }

}
