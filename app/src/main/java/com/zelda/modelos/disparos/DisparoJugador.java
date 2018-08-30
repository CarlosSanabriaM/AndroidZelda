package com.zelda.modelos.disparos;

import android.content.Context;
import android.graphics.Canvas;

import com.zelda.graficos.Sprite;
import com.zelda.modelos.Nivel;

import java.util.HashMap;

/**
 * Created by UO250707 on 26/10/2017.
 */

public abstract class DisparoJugador extends Disparo {

    public int orientacion;
    public static final int ARRIBA = 1;
    public static final int DERECHA = 2;
    public static final int ABAJO = 3;
    public static final int IZQUIERDA = 4;

    protected Sprite sprite;
    protected HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    public DisparoJugador(Context context, double xInicial, double yInicial,
                          int altura, int ancho, int orientacion, double velocidad) {
        super(context, xInicial, yInicial, altura, ancho, orientacion, velocidad);
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);
    }

    public abstract void actualizar(long tiempo);

    public boolean comprobarHaTerminadoTiempoVida() {
        //No hace nada por defecto
        //Lo sobrescribira la clase Espada
        return false;
    }
}