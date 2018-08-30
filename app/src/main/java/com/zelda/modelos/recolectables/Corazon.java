package com.zelda.modelos.recolectables;

import android.content.Context;
import android.graphics.Canvas;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.gestores.GestorAudio;
import com.zelda.graficos.Sprite;
import com.zelda.modelos.Jugador;
import com.zelda.modelos.Nivel;


/**
 * Created by carlos on 24/10/17.
 */

public class Corazon extends Recolectable {

    private Sprite sprite;

    public Corazon(Context context, double x, double y) {
        super(context, x, y, 16, 18);

        this.y =  y - altura/2;

        sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.corazon),
                ancho, altura,
                4, 2, true);
    }

    @Override
    public void accionRecolectable(Nivel nivel) {
        //Sumamos una vida al jugador (siempre que no tenga ya el max de vidas)
        if(nivel.jugador.vidas < Jugador.NUM_MAX_VIDAS)
            nivel.jugador.vidas++;

        nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_CORAZON_RECOGIDO);

        //Si es la primera vez en el nivel que recogemos un corazon
        if(nivel.primeraVezRecogerCorazon) {
            //La prox vez que recogamos un corazon en este nivel, ya no será la primera vez
            nivel.primeraVezRecogerCorazon=false;
            //Mensaje de explicacion de corazon
            nivel.nivelPausado = true;
            nivel.mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.mensaje_corazon);
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
