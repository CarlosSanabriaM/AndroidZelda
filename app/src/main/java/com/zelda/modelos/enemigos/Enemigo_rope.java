package com.zelda.modelos.enemigos;

import android.content.Context;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.global.Estados;
import com.zelda.graficos.Sprite;
import com.zelda.modelos.Nivel;

/**
 * Created by jordansoy on 19/09/2017.
 */

public class Enemigo_rope extends Enemigo {

    private static final int VIDA = 1;
    private static final double VELOCIDAD = 5;
    private static final double TIEMPO_CAMBIAR_VELOCIDAD = 700;

    public static final String CAMINANDO = "Caminando";

    public Enemigo_rope(Context context, Nivel nivel, double x, double y) {
        super(context, nivel, x, y, 30, 30, VIDA, VELOCIDAD, TIEMPO_CAMBIAR_VELOCIDAD);

        //Rope solo tienen una animacion para caminar (da igual hacia que direccion camine)
        Sprite caminando = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.rope),
                ancho, altura,
                6, 2, true);

        sprites.put(CAMINANDO, caminando);

        sprite = caminando;
    }

    @Override
    public void actualizar(long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        //Si el estado en el que estabas era muriendo y se acabo la animacion de morir, te pongo en el estado eliminar
        if ( estado == Estados.MURIENDO && finSprite == true){
            estado = Estados.ELIMINAR;
            //Se puede generar aleatoriamente una rupia, un corazon o un powerup de inmunidad en la pos en la que estaba el enemigo
            this.generarAleatoriamentePowerUp();
        }

        //Si esta vivo le ponemos la animacion de caminar
        if(estado == Estados.VIVO){
            sprite = sprites.get(CAMINANDO);
        }
        //Si esta muriendo le ponemos la animacion de morir
        else if(estado == Estados.MURIENDO){
            sprite = sprites.get(MURIENDO);
        }

    }


}