package com.zelda.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.gestores.GestorAudio;
import com.zelda.gestores.Utilidades;
import com.zelda.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by UO250707 on 19/10/2017.
 */

public class Jugador extends Modelo {
    //Referencia al nivel
    private Nivel nivel;

    //animacion finita
    public boolean atacando;
    public boolean recogiendoTrifuerza;
    public boolean entrandoPuerta;

    public static final int NUM_MAX_VIDAS = 5;
    public static final int NUM_VIDAS_INICIALES = 3;//Ha de ser <= que NUM_MAX_VIDAS
    public int vidas;

    private static final int TIEMPO_INMUNIDAD = 1000;
    public double msInmunidad = 0;

    private static final int TIEMPO_ENTRANDO_PUERTA = 750;
    public double msEntrandoPuerta = 0;

    //ha de ser par, para que sin chochar con la pared, pueda pasar entre dos tiles solidos
    // (aunque va a ser complicado que pase igualmente)
    private static final int VELOCIDAD = 6;

    public int orientacion;
    public static final int ARRIBA = 1;
    public static final int DERECHA = 2;
    public static final int ABAJO = 3;
    public static final int IZQUIERDA = 4;

    public static final String PARADO_ARRIBA = "Parado_arriba";
    public static final String PARADO_DERECHA = "Parado_derecha";
    public static final String PARADO_ABAJO = "Parado_abajo";
    public static final String PARADO_IZQUIERDA = "Parado_izquierda";

    public static final String CAMINANDO_ARRIBA = "Caminando_arriba";
    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_ABAJO = "Caminando_abajo";
    public static final String CAMINANDO_IZQUIERDA = "Caminando_izquierda";

    public static final String ATACANDO_ARRIBA = "atacando_arriba";
    public static final String ATACANDO_DERECHA = "atacando_derecha";
    public static final String ATACANDO_ABAJO = "atacando_abajo";
    public static final String ATACANDO_IZQUIERDA = "atacando_izquierda";

    private static final String RECOGIENDO_TRIFUERZA = "recogiendo_trifuerza";;

    double velocidadX;
    double velocidadY; // actual

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    double xInicial;
    double yInicial;

    public Jugador(Context context, double xInicial, double yInicial, Nivel nivel) {
        super(context, 0, 0, 32, 32);//32,32

        vidas = NUM_VIDAS_INICIALES;
        orientacion=ABAJO;//inicialmente mira hacia abajo

        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;

        this.nivel=nivel;

        inicializar();
    }

    public void inicializar (){
        //PARADO
        Sprite paradoArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_parado_arriba),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO_ARRIBA, paradoArriba);

        Sprite paradoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_parado_derecha),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO_DERECHA, paradoDerecha);

        Sprite paradoAbajo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_parado_abajo),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO_ABAJO, paradoAbajo);

        Sprite paradoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_parado_izquierda),
                ancho, altura,
                1, 1, true);
        sprites.put(PARADO_IZQUIERDA, paradoIzquierda);


        //CAMINAR
        Sprite caminandoArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_caminando_arriba),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_ARRIBA, caminandoArriba);

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_caminando_derecha),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoAbajo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_caminando_abajo),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_ABAJO, caminandoAbajo);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_caminando_izquierda),
                ancho, altura,
                6, 2, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);


        //ATACAR
        Sprite atacandoArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_atacando_arriba),
                ancho, altura,
                5, 1, false);//finito
        sprites.put(ATACANDO_ARRIBA, atacandoArriba);

        Sprite atacandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_atacando_derecha),
                ancho, altura,
                5, 1, false);//finito
        sprites.put(ATACANDO_DERECHA, atacandoDerecha);

        Sprite atacandoAbajo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_atacando_abajo),
                ancho, altura,
                5, 1, false);//finito
        sprites.put(ATACANDO_ABAJO, atacandoAbajo);

        Sprite atacandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_atacando_izquierda),
                ancho, altura,
                5, 1, false);//finito
        sprites.put(ATACANDO_IZQUIERDA, atacandoIzquierda);

        //RECOGIENDO TRIFUERZA
        Sprite recogiendoTrifuerza = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.link_trifuerza),
                ancho, 54,
                2, 2, false);//finito
        sprites.put(RECOGIENDO_TRIFUERZA, recogiendoTrifuerza);

        // animación actual
        sprite = paradoAbajo;
    }

    public int golpeado(){
        //Solo le restamos vidas si no esta en estado INMUNE
        if (msInmunidad <= 0) {
            if (vidas > 0) {
                //Si tiene vidas, le restamos una
                vidas--;
                msInmunidad = TIEMPO_INMUNIDAD;
            }

            //Aqui tenemos las vidas del jugador despues de restarle la vida que le acaban de quitar
            if(vidas > 0)
                nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_LINK_GOLPEADO);
            else
                nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_LINK_MURIENDO);

        }
        return vidas;
    }

    public void entrandoPuerta(){
        entrandoPuerta = true;
        msEntrandoPuerta = TIEMPO_ENTRANDO_PUERTA;
    }

    public void actualizar (long tiempo) throws Exception {
        if(msInmunidad > 0){
            msInmunidad -= tiempo;
        }

        if(msEntrandoPuerta > 0){
            msEntrandoPuerta -= tiempo;
        }else{
            entrandoPuerta=false;
        }

        boolean finSprite = sprite.actualizar(tiempo);

        if(recogiendoTrifuerza && finSprite){
            recogiendoTrifuerza = false;
            sprites.get(RECOGIENDO_TRIFUERZA).setFrameActual(0);
            //Si ha terminado la animacion de recoger la trifuerza, pasamos al sgte nivel
            nivel.gameView.nivelCompleto();
        }

        if(atacando && finSprite){
            atacando = false;
        }

        if (velocidadX > 0){
            sprite = sprites.get(CAMINANDO_DERECHA);
            orientacion = DERECHA;
        }
        if (velocidadX < 0 ){
            sprite = sprites.get(CAMINANDO_IZQUIERDA);
            orientacion = IZQUIERDA;
        }
        if (velocidadX == 0 ){
            if (orientacion == DERECHA){
                sprite = sprites.get(PARADO_DERECHA);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(PARADO_IZQUIERDA);
            }
        }

        if (velocidadY > 0){//hacia abajo
            sprite = sprites.get(CAMINANDO_ABAJO);
            orientacion = ABAJO;
        }
        if (velocidadY < 0 ){//hacia arriba
            sprite = sprites.get(CAMINANDO_ARRIBA);
            orientacion = ARRIBA;
        }
        if (velocidadY == 0 ){
            if (orientacion == ABAJO){
                sprite = sprites.get(PARADO_ABAJO);
            } else if (orientacion == ARRIBA) {
                sprite = sprites.get(PARADO_ARRIBA);
            }
        }

        if (atacando){
            if (orientacion == ARRIBA){
                sprite = sprites.get(ATACANDO_ARRIBA);
            } else if (orientacion == DERECHA) {
                sprite = sprites.get(ATACANDO_DERECHA);
            } else if (orientacion == ABAJO) {
                sprite = sprites.get(ATACANDO_ABAJO);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(ATACANDO_IZQUIERDA);
            }
        }

        if(recogiendoTrifuerza){
            sprite = sprites.get(RECOGIENDO_TRIFUERZA);
        }

        if(entrandoPuerta){
            sprite = sprites.get(PARADO_ABAJO);
            orientacion = ABAJO;
        }

    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY, msInmunidad > 0);
    }

    public void procesarOrdenes (float orientacionPadX, float orientacionPadY, boolean botonAtacarEspada, boolean botonDisparar) {

        if (botonDisparar){
            atacando = true;
            // preparar los sprites, no son bucles hay que reiniciarlos
            sprites.get(ATACANDO_ARRIBA).setFrameActual(0);
            sprites.get(ATACANDO_DERECHA).setFrameActual(0);
            sprites.get(ATACANDO_ABAJO).setFrameActual(0);
            sprites.get(ATACANDO_IZQUIERDA).setFrameActual(0);

            GestorAudio.getInstancia().reproducirSonido(GestorAudio.SONIDO_LINK_ATACAR_MAGIA);
        }

        if (botonAtacarEspada){
            atacando = true;
            // preparar los sprites, no son bucles hay que reiniciarlos
            sprites.get(ATACANDO_ARRIBA).setFrameActual(0);
            sprites.get(ATACANDO_DERECHA).setFrameActual(0);
            sprites.get(ATACANDO_ABAJO).setFrameActual(0);
            sprites.get(ATACANDO_IZQUIERDA).setFrameActual(0);

            GestorAudio.getInstancia().reproducirSonido(GestorAudio.SONIDO_LINK_ATACAR_ESPADA);
        }

        //Solo se va a poder mover en un eje en cada instante (no se puede mover en diagonal)
        //El eje que mayor valor absoluto tenga en su valor de orientacion va a tener prioridad
        //En caso de empate, el eje X va a ser el que gane

        float valorEjeX = Math.abs(orientacionPadX);
        float valorEjeY = Math.abs(orientacionPadY);

        if(valorEjeX >= valorEjeY){
            //Gana el eje X o empatan
            velocidadY = 0;//en el eje Y no se mueve

            if (orientacionPadX > 0) {
                velocidadX = -VELOCIDAD;
            } else if (orientacionPadX < 0 ){
                velocidadX = VELOCIDAD;
            } else {
                velocidadX = 0;
            }
        }
        else{
            //Gana el eje Y
            velocidadX = 0;//en el eje X no se mueve

            if (orientacionPadY > 0) {
                velocidadY = -VELOCIDAD;
            } else if (orientacionPadY < 0 ){
                velocidadY = VELOCIDAD;
            } else {
                velocidadY = 0;
            }
        }

    }

    public void mover(){
        //Si esta recogiendo la trifuerza o entrando por una puerta, no dejamos mover al jugador
        if(recogiendoTrifuerza || entrandoPuerta)
            return;

        Tile[][] mapaTiles = nivel.mapaTiles;

        int tileXJugadorIzquierda   = (int) (this.x - (this.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha     = (int) (this.x + (this.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorCentro      = (int)  this.x / Tile.ancho;
        int tileYJugadorInferior    = (int) (this.y + (this.altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro      = (int)  this.y / Tile.altura;
        int tileYJugadorSuperior    = (int) (this.y - (this.altura / 2 - 1)) / Tile.altura;

        // Hacia arriba
        if (this.velocidadY < 0) {
            // Tile superior PASABLE
            // Podemos seguir moviendo hacia arriba
            if (tileYJugadorSuperior - 1 >= 0 &&
                    nivel.mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior - 1].tipoDeColision
                            == Tile.PASABLE
                    && nivel.mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior - 1].tipoDeColision
                    == Tile.PASABLE) {

                this.y += this.velocidadY;

                // Tile superior != de PASABLE
                // O es un tile SOLIDO, o es el TECHO del mapa
            } else {

                // Si en el propio tile del jugador queda espacio para
                // subir más, subo
                int TileJugadorBordeSuperior = (tileYJugadorSuperior) * Tile.altura;
                double distanciaY = (this.y - this.altura / 2) - TileJugadorBordeSuperior;

                if (distanciaY > 0) {
                    this.y += Utilidades.proximoACero(-distanciaY, this.velocidadY);

                } else {
                    this.velocidadY = 0;
                }

            }
        }

        // Hacia abajo
        if (this.velocidadY >= 0) {
            // Tile inferior PASABLE
            // Podemos seguir moviendo hacia abajo
            if (tileYJugadorInferior + 1 <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision
                    == Tile.PASABLE) {
                // si los dos están libres podemos bajar

                this.y += this.velocidadY;

                // Tile superior != de PASABLE
                // O es un tile SOLIDO, o es el borde inferior del mapa
            } else{

                // Con que uno de los dos sea solido ya no puede bajar
                // Si en el propio tile del jugador queda espacio para bajar más, bajo
                int TileJugadorBordeInferior = tileYJugadorInferior * Tile.altura + Tile.altura;

                double distanciaY = TileJugadorBordeInferior - (this.y + this.altura / 2);

                if (distanciaY > 0) {
                    this.y += Math.min(distanciaY, this.velocidadY);

                } else {
                    this.velocidadY = 0;
                }

            }
        }

        // derecha o parado
        if (this.velocidadX > 0) {
            // Tengo un tile delante y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorDerecha + 1 <= nivel.anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                this.x += this.velocidadX;

                // No tengo un tile PASABLE delante
                // o es el FINAL del nivel o es uno SOLIDO
            } else if (tileXJugadorDerecha <= nivel.anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeDerecho = tileXJugadorDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (this.x + this.ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, this.velocidadX);
                    this.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    this.x = TileJugadorBordeDerecho - this.ancho / 2;
                }
            }
        }

        // izquierda
        if (this.velocidadX <= 0) {
            // Tengo un tile detrás y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorIzquierda - 1 >= 0 &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                this.x += this.velocidadX;

                // No tengo un tile PASABLE detrás
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision
                            == Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeIzquierdo = tileXJugadorIzquierda * Tile.ancho;
                double distanciaX = (this.x - this.ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaX, this.velocidadX);
                    this.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    this.x = TileJugadorBordeIzquierdo + this.ancho / 2;
                }
            }
        }

    }


}
