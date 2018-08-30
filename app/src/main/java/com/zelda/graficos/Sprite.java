package com.zelda.graficos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Jordan on 14/08/2015.
 */
public class Sprite {
    private Bitmap bitmap;
    // Fichero con los frames.
    private Rect rectanguloDibujo;
    // El rectangulo sobre el que se pinta el dibujo
    private int framesTotales;
    // N�mero total de frames en el bitmap.
    private int frameActual;
    // El frame que se esta pintando actualmente
    private long tiempoUltimaActualizacion;
    // El tiempo que ha pasado desde que se ha cambiado de frame
    private int interavaloEntreFrames;
    // Milisegundos, tiempo entre frames (1000/fps)

    // Medidas reales del Bitmap del sprite, el .png.
    private int spriteAncho;
    private int spriteAltura;

    // Medidas en pixeles del modelo que se representara en la pantalla del dispositivo
    private int modeloAncho;
    private int modeloAltura;



    private boolean bucle;

    public Sprite(Drawable drawable, int modeloAncho, int modeloAltura, int fps, int framesTotales
            , boolean bucle) {
        this.bitmap = ((BitmapDrawable)drawable).getBitmap();;
        this.modeloAncho = modeloAncho;
        this.modeloAltura = modeloAltura;
        this.framesTotales = framesTotales;
        this.bucle = bucle;

        frameActual = 0;
        spriteAncho = bitmap.getWidth() / framesTotales;
        spriteAltura = bitmap.getHeight();
        rectanguloDibujo = new Rect(0, 0, spriteAncho, spriteAltura);
        interavaloEntreFrames = 1000 / fps;
        tiempoUltimaActualizacion = 0l;
    }

    public boolean actualizar (long tiempo) {
        boolean finSprite = false;
        // Tiempo es el tiempo que pasa entre cada frame.
        tiempoUltimaActualizacion += tiempo;
        if (tiempoUltimaActualizacion >= interavaloEntreFrames) {
            tiempoUltimaActualizacion = 0;
            // actualizar el frame
            frameActual++;
            if (frameActual >= framesTotales) {
                if (bucle){
                    frameActual = 0;
                } else {
                    frameActual = framesTotales;
                    finSprite = true;
                }
            }
        }
        // definir el rectangulo
        this.rectanguloDibujo.left = frameActual * spriteAncho;
        this.rectanguloDibujo.right = this.rectanguloDibujo.left + spriteAncho;

        return finSprite;
    }


    public void dibujarSprite (Canvas canvas, int x, int y) {
        Rect destRect = new Rect(x - modeloAncho/2, y - modeloAltura/2, x
                + modeloAncho/2, y + modeloAltura/2);
        canvas.drawBitmap(bitmap, rectanguloDibujo, destRect, null);
    }

    public void dibujarSprite (Canvas canvas, int x, int y, boolean alpha) {
        Paint efectoTransparente = new Paint();
        if (alpha)
            efectoTransparente.setAlpha(150);

        Rect destRect = new Rect(x - modeloAncho/2, y - modeloAltura/2, x
                + modeloAncho/2, y + modeloAltura/2);
        canvas.drawBitmap(bitmap, rectanguloDibujo, destRect, efectoTransparente);
    }

    public void setFrameActual(int frameActual) {
        this.frameActual = frameActual;
        // definir el rectangulo
        this.rectanguloDibujo.left = frameActual * spriteAncho;
        this.rectanguloDibujo.right = this.rectanguloDibujo.left + spriteAncho;
    }

}
