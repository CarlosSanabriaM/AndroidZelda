package com.zelda;

import android.graphics.Canvas;
import android.util.Log;

public class GameLoop extends Thread {

    private final static int FPS = 25;
    // solo con uno va mejor
    private final static int MAXIMO_FRAMES_SALTARSE = 5;
    private final static long FRAME_TIEMPO = 1000 / (long) FPS;

    private GameView gameView;

    private boolean corriendo;

    public void setRunning(boolean running) {
        this.corriendo = running;
    }

    public GameLoop(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void run() {
        try {
            Canvas canvas;
            this.gameView.inicializar();

            long tiempoAlInicio = 0; // Tiempo inicio del ciclo
            long tiempoDiferencial = 0; // Tiempo que tarda el ciclo desde su inicio
            // al fin
            int tiempoEspera; // Espera entre ejecuciones
            int framesSaltados = 0; // Numero de frames que se han saltado

            tiempoEspera = 0;

            while (corriendo) {
                canvas = null;
                try {
                    canvas = this.gameView.getHolder().lockCanvas();

                    synchronized (gameView.getHolder()) {
                        tiempoAlInicio = System.currentTimeMillis();
                        this.gameView.actualizar(FRAME_TIEMPO);
                        framesSaltados = 0;
                        if (!corriendo)
                            break;
                        this.gameView.dibujar(canvas);

                        tiempoDiferencial = System.currentTimeMillis()
                                - tiempoAlInicio;
                        tiempoEspera = (int) (FRAME_TIEMPO - tiempoDiferencial);

                        if (tiempoEspera > 0) {
                            try {
                                // Util para ahorrar bateria
                                Log.v("GameLoop","GameLoop - Tiempo de espera "+tiempoEspera);
                                Thread.sleep(tiempoEspera);
                            } catch (InterruptedException e) {
                            }
                        }

                        // falta tiempo
                        while (tiempoEspera < 0
                                && framesSaltados <= MAXIMO_FRAMES_SALTARSE) {
                            this.gameView.actualizar(FRAME_TIEMPO);
                            tiempoEspera += FRAME_TIEMPO;
                            framesSaltados++;
                            Log.v("GameLoop","GameLoop - Frames saltados "+framesSaltados);
                        }
                    }
                } finally {
                    if (canvas != null) {
                        gameView.getHolder().unlockCanvasAndPost(canvas);
                    }
                }// finally
            }// while
        }// try
        catch (Exception ex) {
            ex.printStackTrace();
            Log.v("ERROR: ", ex.getMessage());
        }
    }
}