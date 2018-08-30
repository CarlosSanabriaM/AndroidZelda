package com.zelda.modelos.disparos;

import android.content.Context;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.graficos.Sprite;

import java.util.Date;

/**
 * Created by carlos on 13/12/17.
 */

public class Espada extends DisparoJugador {

    private static final double VELOCIDAD = 10;

    private static final double TIEMPO_VIDA = 150;//150
    private double tiempoCreacion;

    private static final int AUMENTO_AREA_COLISION = 10;

    public static final String ESPADA_ARRIBA = "arriba";
    public static final String ESPADA_DERECHA = "derecha";
    public static final String ESPADA_ABAJO = "abajo";
    public static final String ESPADA_IZQUIERDA = "izquierda";

    public Espada(Context context, double xInicial, double yInicial, int orientacion) {
        super(context, xInicial, yInicial, 0, 0, orientacion, VELOCIDAD);//Esos 0, 0 en ancho y alto se van a sobrescribir mas abajo

        /*
            El ancho y el alto dependeran de la orientacion de la espada,
            ya que el estar en vertical o horizontal cambia el ancho por el alto

            Modificamos tambien la x y la y donde aparece la espada
            (no van a ser las del jugador, sino que va a aparecer delante, para que quede mejor el efecto)

            Aumentamos el area de colision de la espada en el pico de la espada
            (para que resulte mas facil darle a los enemigos)
         */

        if(orientacion==ARRIBA || orientacion==ABAJO){
            ancho=14;//7
            altura=32;//16
        }else{
            altura=14;//7
            ancho=32;//16
        }

        cDerecha = ancho/2;
        cIzquierda = ancho/2;
        cArriba = altura/2;
        cAbajo = altura/2;

        switch (orientacion){
            case ARRIBA:
                cArriba += AUMENTO_AREA_COLISION;
                break;
            case DERECHA:
                cDerecha += AUMENTO_AREA_COLISION;
                break;
            case ABAJO:
                cAbajo += AUMENTO_AREA_COLISION;
                break;
            case IZQUIERDA:
                cIzquierda += AUMENTO_AREA_COLISION;
                break;
        }

        tiempoCreacion = new Date().getTime();
        inicializar();
    }


    public void inicializar (){

        Sprite espadaArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_espada_arriba),
                ancho, altura,
                1, 1, true);
        sprites.put(ESPADA_ARRIBA, espadaArriba);

        Sprite espadaDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_espada_derecha),
                ancho, altura,
                1, 1, true);
        sprites.put(ESPADA_DERECHA, espadaDerecha);

        Sprite espadaAbajo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_espada_abajo),
                ancho, altura,
                1, 1, true);
        sprites.put(ESPADA_ABAJO, espadaAbajo);

        Sprite espadaIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_espada_izquierda),
                ancho, altura,
                1, 1, true);
        sprites.put(ESPADA_IZQUIERDA, espadaIzquierda);

        sprite = espadaAbajo;//le damos un valor inicial que se va a sobrescribir

    }

    @Override
    public void actualizar (long tiempo) {

        if (velocidadX > 0){
            sprite = sprites.get(ESPADA_DERECHA);
            orientacion = DERECHA;
        }
        else if (velocidadX < 0 ){
            sprite = sprites.get(ESPADA_IZQUIERDA);
            orientacion = IZQUIERDA;
        }
        else if(velocidadY > 0){
            sprite = sprites.get(ESPADA_ABAJO);
            orientacion = ABAJO;
        }
        else if (velocidadY < 0 ){
            sprite = sprites.get(ESPADA_ARRIBA);
            orientacion = ARRIBA;
        }

        sprite.actualizar(tiempo);
    }

    @Override
    public boolean comprobarHaTerminadoTiempoVida() {
        long tiempoActual = new Date().getTime();
        boolean haTerminadoTiempoVida = tiempoActual - tiempoCreacion > TIEMPO_VIDA;
        return haTerminadoTiempoVida;
    }

}
