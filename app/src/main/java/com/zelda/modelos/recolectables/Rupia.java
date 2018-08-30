package com.zelda.modelos.recolectables;

import android.content.Context;
import android.graphics.Canvas;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.gestores.GestorAudio;
import com.zelda.graficos.Sprite;
import com.zelda.modelos.Nivel;
import com.zelda.modelos.controles.Marcador;

/**
 * Created by carlos on 23/10/17.
 */

public class Rupia extends Recolectable {

    private Sprite sprite;

    public Rupia(Context context, double x, double y) {
        super(context, x, y, 24, 15);//16, 10

        this.y =  y - altura/2;

        sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.rupia),
                ancho, altura,
                4, 2, true);
    }

    @Override
    public void accionRecolectable(Nivel nivel) {
        Marcador marcador = nivel.gameView.getMarcador();
        marcador.setPuntos(marcador.getPuntos() + 1);

        nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_RUPIA_RECOGIDA);

        //Si es la primera vez en el nivel que recogemos una rupia
        if(nivel.primeraVezRecogerRupia) {
            //La prox vez que recogamos una rupia en este nivel, ya no será la primera vez
            nivel.primeraVezRecogerRupia=false;
            //Mensaje de explicacion de rupoa
            nivel.nivelPausado = true;
            nivel.mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.mensaje_rupia);
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
