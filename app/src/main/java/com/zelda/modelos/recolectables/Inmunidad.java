package com.zelda.modelos.recolectables;

import android.content.Context;
import android.graphics.Canvas;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.gestores.GestorAudio;
import com.zelda.graficos.Sprite;
import com.zelda.modelos.Nivel;

/**
 * Created by carlos on 12/12/17.
 */

public class Inmunidad extends Recolectable {
    private Sprite sprite;

    private static final int TIEMPO_INMUNIDAD = 5000;

    public Inmunidad(Context context, double x, double y) {
        super(context, x, y, 24, 15);//16, 10

        this.y =  y - altura/2;

        sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.inmunidad),
                ancho, altura,
                4, 2, true);
    }

    @Override
    public void accionRecolectable(Nivel nivel) {
        //Le da un tiempo de inmunidad al jugador
        nivel.jugador.msInmunidad=TIEMPO_INMUNIDAD;
        nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_ITEM_RECOGIDO);

        //Si es la primera vez en el nivel que recogemos un powerup de inmunidad
        if(nivel.primeraVezRecogerInmunidad) {
            //La prox vez que recogamos un powerup de inmunidad en este nivel, ya no será la primera vez
            nivel.primeraVezRecogerInmunidad=false;
            //Mensaje de explicacion de inmunidad
            nivel.nivelPausado = true;
            nivel.mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.mensaje_inmunidad);
        }
    }

    @Override
    public void actualizar (long tiempo) {
        sprite.actualizar(tiempo);
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);
    }
}
