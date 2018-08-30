package com.zelda.modelos.enemigos;

import android.content.Context;
import android.graphics.Canvas;

import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.gestores.GestorAudio;
import com.zelda.gestores.Utilidades;
import com.zelda.global.Estados;
import com.zelda.graficos.Sprite;
import com.zelda.modelos.Modelo;
import com.zelda.modelos.Nivel;
import com.zelda.modelos.Tile;
import com.zelda.modelos.controles.BarraEnemigo;
import com.zelda.modelos.disparos.Disparo;
import com.zelda.modelos.disparos.DisparoJugador;
import com.zelda.modelos.recolectables.Corazon;
import com.zelda.modelos.recolectables.Inmunidad;
import com.zelda.modelos.recolectables.Rupia;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by carlos on 23/10/17.
 */

public abstract class Enemigo extends Modelo {

    private Nivel nivel;

    public int estado = Estados.VIVO;

    public int orientacion;
    public static final int ARRIBA = 1;
    public static final int DERECHA = 2;
    public static final int ABAJO = 3;
    public static final int IZQUIERDA = 4;

    private static final double VELOCIDAD_RETROCESO = 40;//velocidad que se les va a aplicar cuando les golpee el jugador

    public static final int EMPUJAR_HACIA_ARRIBA       = 5;
    public static final int EMPUJAR_HACIA_DERECHA      = 6;
    public static final int EMPUJAR_HACIA_ABAJO        = 7;
    public static final int EMPUJAR_HACIA_IZQUIERDA    = 8;

    public int direccion_empujar;

    private final double VELOCIDAD;
    public double velocidadX;
    public double velocidadY;

    private final double TIEMPO_CAMBIAR_VELOCIDAD;
    private double tiempoCambiarVelocidad = 0;

    protected Sprite sprite;
    protected HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    public static final String MURIENDO = "Muriendo";

    public BarraEnemigo barraVidas;
    protected int vida;

    public Enemigo(Context context, Nivel nivel, double xInicial, double yInicial, int altura, int ancho,
                   int vida, double velocidad, double tiempo_cambiar_velocidad) {

        super(context, 0, 0, altura, ancho);

        this.x = xInicial;
        this.y = yInicial - altura/2;
        barraVidas = new BarraEnemigo(context, x-ancho/2, y-altura/2, ancho, vida, vida);
        this.vida=vida;
        this.VELOCIDAD = velocidad;
        this.TIEMPO_CAMBIAR_VELOCIDAD = tiempo_cambiar_velocidad;

        //Añadimos el sprite de morir al diccionario (todos los enemigos usan la misma animacion al morir)
        Sprite muriendo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_muriendo),
                ancho, altura,
                12, 2, false);//Es finita

        sprites.put(MURIENDO, muriendo);

        this.nivel=nivel;
    }

    @Override
    public void dibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);

        //Solamente dibujar la barra de vidas si esta vivo
        if(estado == Estados.VIVO)
            barraVidas.dibujar(canvas);
    }


    public void destruir (){
        //Lo pasamos al estado muriendo
        estado = Estados.MURIENDO;
    }

    public abstract void actualizar (long tiempo);

    public void generarVelocidad() {
        //Solo se cambia la velocidad cada X segundos
        //Añadimos una cierta aleatoriedad
        boolean hanPasadoXSegundosDesdeUltCambioVelocidad =
                new Date().getTime() - tiempoCambiarVelocidad > TIEMPO_CAMBIAR_VELOCIDAD + Math.random()* TIEMPO_CAMBIAR_VELOCIDAD;

        if(hanPasadoXSegundosDesdeUltCambioVelocidad){
            tiempoCambiarVelocidad = new Date().getTime();
            //Solo se va a poder mover en un eje en cada instante (no se puede mover en diagonal)
            //Generamos al azar una orientacion en la que se va a mover
            int orientacion = Utilidades.dameOrientacionAlAzar();

            velocidadX=0;
            velocidadY=0;

            switch (orientacion){
                case Utilidades.ARRIBA:
                    velocidadY = -VELOCIDAD;
                    break;
                case Utilidades.DERECHA:
                    velocidadX = VELOCIDAD;
                    break;
                case Utilidades.ABAJO:
                    velocidadY = VELOCIDAD;
                    break;
                case Utilidades.IZQUIERDA:
                    velocidadX = -VELOCIDAD;
                    break;
            }
        }

    }

    public void moverAutomaticamente() {
        Tile[][] mapaTiles = nivel.mapaTiles;

        int tileXEnemigoIzquierda   =  (int) (this.x - (this.ancho / 2 - 1)) / Tile.ancho;
        int tileXEnemigoDerecha     =  (int) (this.x + (this.ancho / 2 - 1)) / Tile.ancho;
        int tileYEnemigoInferior    =  (int) (this.y + (this.altura / 2 - 1)) / Tile.altura;
        int tileYEnemigoCentro      =  (int)  this.y / Tile.altura;
        int tileYEnemigoSuperior    =  (int) (this.y - (this.altura / 2 - 1)) / Tile.altura;

        double distanciaAvanzadaEnemigoEjeX = 0;
        double distanciaAvanzadaEnemigoEjeY = 0;

        this.generarVelocidad();

        // Hacia arriba
        if (this.velocidadY < 0) {
            // Tile superior PASABLE
            // Podemos seguir moviendo hacia arriba
            if (tileYEnemigoSuperior - 1 >= 0 &&
                    nivel.mapaTiles[tileXEnemigoIzquierda][tileYEnemigoSuperior - 1].tipoDeColision
                            == Tile.PASABLE
                    && nivel.mapaTiles[tileXEnemigoDerecha][tileYEnemigoSuperior - 1].tipoDeColision
                    == Tile.PASABLE) {

                distanciaAvanzadaEnemigoEjeY = this.velocidadY;
                this.y += distanciaAvanzadaEnemigoEjeY;

                // Tile superior != de PASABLE
                // O es un tile SOLIDO, o es el TECHO del mapa
            } else {

                // Si en el propio tile del jugador queda espacio para
                // subir más, subo
                int TileJugadorBordeSuperior = (tileYEnemigoSuperior) * Tile.altura;
                double distanciaY = (this.y - this.altura / 2) - TileJugadorBordeSuperior;

                if (distanciaY > 0) {
                    distanciaAvanzadaEnemigoEjeY = Utilidades.proximoACero(-distanciaY, this.velocidadY);
                    this.y += distanciaAvanzadaEnemigoEjeY;

                } else {
                    this.velocidadY = 0;
                }

            }
        }

        // Hacia abajo
        if (this.velocidadY >= 0) {
            // Tile inferior PASABLE
            // Podemos seguir moviendo hacia abajo
            if (tileYEnemigoInferior + 1 <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXEnemigoIzquierda][tileYEnemigoInferior + 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXEnemigoDerecha][tileYEnemigoInferior + 1].tipoDeColision
                    == Tile.PASABLE) {
                // si los dos están libres podemos bajar

                distanciaAvanzadaEnemigoEjeY = this.velocidadY;
                this.y += distanciaAvanzadaEnemigoEjeY;

                // Tile superior != de PASABLE
                // O es un tile SOLIDO, o es el borde inferior del mapa
            } else{

                // Con que uno de los dos sea solido ya no puede bajar
                // Si en el propio tile del jugador queda espacio para bajar más, bajo
                int TileJugadorBordeInferior = tileYEnemigoInferior * Tile.altura + Tile.altura;

                double distanciaY = TileJugadorBordeInferior - (this.y + this.altura / 2);

                if (distanciaY > 0) {
                    distanciaAvanzadaEnemigoEjeY = Math.min(distanciaY, this.velocidadY);
                    this.y += distanciaAvanzadaEnemigoEjeY;

                } else {
                    this.velocidadY = 0;
                }

            }
        }

        // derecha o parado
        if (this.velocidadX > 0) {
            // Tengo un tile delante y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXEnemigoDerecha + 1 <= nivel.anchoMapaTiles() - 1 &&
                    tileYEnemigoInferior <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha][tileYEnemigoCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                distanciaAvanzadaEnemigoEjeX = this.velocidadX;
                this.x += distanciaAvanzadaEnemigoEjeX;

                // No tengo un tile PASABLE delante
                // o es el FINAL del nivel o es uno SOLIDO
            } else if (tileXEnemigoDerecha <= nivel.anchoMapaTiles() - 1 &&
                    tileYEnemigoInferior <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXEnemigoDerecha][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha][tileYEnemigoCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoDerecha][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeDerecho = tileXEnemigoDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (this.x + this.ancho / 2);

                if (distanciaX > 0) {
                    distanciaAvanzadaEnemigoEjeX = Math.min(distanciaX, this.velocidadX);
                    this.x += distanciaAvanzadaEnemigoEjeX;
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
            if (tileXEnemigoIzquierda - 1 >= 0 &&
                    mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda][tileYEnemigoInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda][tileYEnemigoCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda][tileYEnemigoSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                distanciaAvanzadaEnemigoEjeX = this.velocidadX;
                this.x += distanciaAvanzadaEnemigoEjeX;

                // No tengo un tile PASABLE detrás
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXEnemigoIzquierda >= 0 && tileYEnemigoInferior <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXEnemigoIzquierda][tileYEnemigoInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda][tileYEnemigoCentro].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXEnemigoIzquierda][tileYEnemigoSuperior].tipoDeColision
                            == Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeIzquierdo = tileXEnemigoIzquierda * Tile.ancho;
                double distanciaX = (this.x - this.ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    distanciaAvanzadaEnemigoEjeX = Utilidades.proximoACero(-distanciaX, this.velocidadX);
                    this.x += distanciaAvanzadaEnemigoEjeX;
                } else {
                    // Opcional, corregir posición
                    this.x = TileJugadorBordeIzquierdo + this.ancho / 2;
                }
            }
        }


        this.moverBarraVidas(distanciaAvanzadaEnemigoEjeX,distanciaAvanzadaEnemigoEjeY);
    }

    private void moverBarraVidas(double distanciaAvanzadaEnemigoEjeX, double distanciaAvanzadaEnemigoEjeY){
        //Tenemos que mover la barra de vidas junto al enemigo
        //El enemigo, aunque tenga velocidad, no tiene por que moverse (esta chocando con una pared por ej)
        barraVidas.x = (barraVidas.x + distanciaAvanzadaEnemigoEjeX);
        barraVidas.y = (barraVidas.y + distanciaAvanzadaEnemigoEjeY);
    }

    public void golpeado(){
        //Primero le restamos una vida al enemigo (si devuelve cierto es que ha muerto)
        if(reducirVida()){
            //Si ha muerto, no lo empujamos hacia atras y le ponemos en el estado MURIENDO
            estado = Estados.MURIENDO;
        }

        //Si sigue vivo lo empujamos hacia atras
        else{
            //Guardamos su velocidad
            double velocidadXAntes = velocidadX;
            double velocidadYAntes = velocidadY;

            //Le damos temporalmente una velocidad en el sentido de la velocidad del disparo del jugador
            switch (direccion_empujar){
                case EMPUJAR_HACIA_ARRIBA:
                    velocidadY = -VELOCIDAD_RETROCESO;
                    break;
                case EMPUJAR_HACIA_DERECHA:
                    velocidadX = VELOCIDAD_RETROCESO;
                    break;
                case EMPUJAR_HACIA_ABAJO:
                    velocidadY = VELOCIDAD_RETROCESO;
                    break;
                case EMPUJAR_HACIA_IZQUIERDA:
                    velocidadX = -VELOCIDAD_RETROCESO;
                    break;
            }

            //Lo movemos con la nueva velocidad temporal
            this.moverAutomaticamente();

            //Le restablecemos la velocidad que tenia antes
            this.velocidadX = velocidadXAntes;
            this.velocidadY = velocidadYAntes;
        }

    }

    public boolean reducirVida() {
        boolean eliminar = false;

        //Le restamos una vida al enemigo
        this.vida--;
        this.barraVidas.setValorActual(vida);

        //Si tiene 0 vidas
        if (this.vida == 0){
            eliminar = true;//lo marcamos para eliminar
            nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_ENEMIGO_MURIENDO);
        }else{

            nivel.gestorAudio.reproducirSonido(GestorAudio.SONIDO_ENEMIGO_GOLPEADO);
        }

        return eliminar;
    }

    protected void generarAleatoriamentePowerUp() {
        int powerup = Utilidades.damePowerUpAlAzar();

        int xCentroAbajo = (int) x;
        //int yCentroAbajo = (int) y + altura/2;
        // (esto es si quisieramos que el powerup se crease encima del "suelo" del enemigo.
        // En vez de hacer eso, vamos a hacer que el powerup se cree en el centro del enemigo)

        switch (powerup){
            case Utilidades.NINGUNO:
                break;
            case Utilidades.RUPIA:
                Rupia rupia = new Rupia(nivel.context,xCentroAbajo,0);//este 0 da igual, se sobrescribe debajo
                rupia.y = y;
                nivel.recolectables.add(rupia);
                break;
            case Utilidades.CORAZON:
                Corazon corazon = new Corazon(nivel.context,xCentroAbajo,0);//este 0 da igual, se sobrescribe debajo
                corazon.y = y;
                nivel.recolectables.add(corazon);
                break;
            case Utilidades.INMUNIDAD:
                Inmunidad inmunidad = new Inmunidad(nivel.context,xCentroAbajo,0);//este 0 da igual, se sobrescribe debajo
                inmunidad.y = y;
                nivel.recolectables.add(inmunidad);
                break;
        }

    }

    @Override
    /**
     * Solo se va a usar este colisiona cuando le golpee al enemigo un disparo o la espada
     * ya que vamos a coger la velocidad de dichos disparos para calcular hacia que zona le empujamos
     */
    public boolean colisiona (Modelo modelo){
        boolean colisionan = super.colisiona(modelo);

        if(colisionan) {

            if(!(modelo instanceof DisparoJugador))
                return colisionan;

            double velocidadDisparoX = ((DisparoJugador)modelo).velocidadX;
            double velocidadDisparoY = ((DisparoJugador)modelo).velocidadY;

            if (velocidadDisparoY < 0) {
                //Si el disparo se movia hacia arriba, vamos a empujar al enemigo hacia arriba
                direccion_empujar = EMPUJAR_HACIA_ARRIBA;

            } else if (velocidadDisparoX > 0) {
                //Si el disparo se movia hacia la derecha, vamos a empujar al enemigo hacia la derecha
                direccion_empujar = EMPUJAR_HACIA_DERECHA;
            } else if (velocidadDisparoY > 0) {
                //Si el disparo se movia hacia abajo, vamos a empujar al enemigo hacia abajo
                direccion_empujar = EMPUJAR_HACIA_ABAJO;
            } else if (velocidadDisparoX < 0) {
                //Si el disparo se movia hacia la izquierda, vamos a empujar al enemigo hacia la izquierda
                direccion_empujar = EMPUJAR_HACIA_IZQUIERDA;
            }
        }

        return colisionan;
    }


    /**
     * Por defecto los enemigos no disparan
     * @param tiempo
     * @return
     */
    public Disparo disparar(long tiempo) {
        return null;
    }

}