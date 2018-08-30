package com.zelda.modelos.disparos;

import android.content.Context;

import com.zelda.gestores.Utilidades;
import com.zelda.modelos.Jugador;
import com.zelda.modelos.Modelo;
import com.zelda.modelos.Nivel;
import com.zelda.modelos.Tile;

/**
 * Created by carlos on 23/10/17.
 */

public abstract class Disparo extends Modelo {

    public double velocidadX = 0;
    public double velocidadY = 0;

    public Disparo(Context context, double x, double y, int altura, int ancho, int orientacion, double VELOCIDAD) {
        super(context, x, y, altura, ancho);

        switch (orientacion){
            case Jugador.ARRIBA:
                velocidadY = -VELOCIDAD;
                break;
            case Jugador.DERECHA:
                velocidadX = VELOCIDAD;
                break;
            case Jugador.ABAJO:
                velocidadY = VELOCIDAD;
                break;
            case Jugador.IZQUIERDA:
                velocidadX = -VELOCIDAD;
                break;
        }
    }

    public boolean moverAutomaticamente(Nivel nivel){
        //Elimina los disparos "muertos" que se pueden generar al principio del nivel
        if(velocidadX == 0 && velocidadY == 0)
            return true;

        Tile[][] mapaTiles = nivel.mapaTiles;

        int tileXDisparo             =     (int)  this.x / Tile.ancho;
        int tileYDisparo             =     (int)  this.y / Tile.altura;
        int tileXDisparoIzquierda    =     (int) (this.x - this.cIzquierda- 1) / Tile.ancho;
        int tileXDisparoDerecha      =     (int) (this.x + this.cDerecha - 1) / Tile.ancho;
        int tileYDisparoInferior     =     (int) (this.y + (this.cAbajo - 1) ) / Tile.altura;
        int tileYDisparoSuperior     =     (int) (this.y - (this.cArriba - 1) ) / Tile.altura;

        //disparo abajo
        if (this.velocidadY > 0) {
            // Tiene delante un tile pasable, puede avanzar.
            if (tileYDisparo + 1 <= nivel.altoMapaTiles() - 1 &&
                    mapaTiles[tileXDisparoIzquierda][tileYDisparo + 1].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXDisparoDerecha][tileYDisparo + 1].tipoDeColision
                            == Tile.PASABLE) {

                this.y += this.velocidadY;

            } else if (tileYDisparo <= nivel.altoMapaTiles() - 1) {

                int TileDisparoBordeInferior = tileYDisparoInferior * Tile.altura + Tile.altura;

                double distanciaY = TileDisparoBordeInferior - (this.y + this.cAbajo);

                if (distanciaY > 0) {
                    this.y += Math.min(distanciaY, this.velocidadY);

                } else {
                    return true;
                }

            }
        }

        // arriba
        if (this.velocidadY < 0) {
            if (tileYDisparo - 1 >= 0 &&
                    mapaTiles[tileXDisparoIzquierda][tileYDisparo - 1].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXDisparoDerecha][tileYDisparo - 1].tipoDeColision ==
                            Tile.PASABLE) {

                this.y += this.velocidadY;

                // No tengo un tile PASABLE detras
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileYDisparo >= 0) {

                int TileDisparoBordeSuperior = (tileYDisparoSuperior) * Tile.altura;
                double distanciaY = (this.y - this.cIzquierda) - TileDisparoBordeSuperior;

                if (distanciaY > 0) {
                    this.y += Utilidades.proximoACero(-distanciaY, this.velocidadY);

                } else {
                    return true;
                }
            }
        }

        //disparo derecha
        if (this.velocidadX > 0) {
            // Tiene delante un tile pasable, puede avanzar.
            if (tileXDisparo + 1 <= nivel.anchoMapaTiles() - 1 &&
                    mapaTiles[tileXDisparo + 1][tileYDisparoInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXDisparo + 1][tileYDisparoSuperior].tipoDeColision
                            == Tile.PASABLE) {

                this.x += this.velocidadX;

            } else if (tileXDisparo <= nivel.anchoMapaTiles() - 1) {

                int TileDisparoBordeDerecho = tileXDisparo * Tile.ancho + Tile.ancho;
                double distanciaX =
                        TileDisparoBordeDerecho - (this.x + this.cDerecha);

                if (distanciaX > 0) {
                    double velocidadNecesaria =
                            Math.min(distanciaX, this.velocidadX);
                    this.x += velocidadNecesaria;
                } else {
                    return true;
                }
            }
        }

        // izquierda
        if (this.velocidadX < 0) {
            if (tileXDisparo - 1 >= 0 &&
                    mapaTiles[tileXDisparo - 1][tileYDisparoSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXDisparo - 1][tileYDisparoInferior].tipoDeColision ==
                            Tile.PASABLE) {

                this.x += this.velocidadX;

                // No tengo un tile PASABLE detras
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXDisparo >= 0) {
                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileDisparoBordeIzquierdo = tileXDisparo * Tile.ancho;
                double distanciaX =
                        (this.x - this.cIzquierda) - TileDisparoBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria =
                            Utilidades.proximoACero(-distanciaX, this.velocidadX);
                    this.x += velocidadNecesaria;
                } else {
                    return true;
                }
            }
        }

        return false;

    }


}
