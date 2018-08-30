package com.zelda;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zelda.gestores.GestorAudio;
import com.zelda.gestores.GestorNiveles;
import com.zelda.modelos.Nivel;
import com.zelda.modelos.controles.FondoInterfazSuperior;
import com.zelda.modelos.controles.Marcador;
import com.zelda.modelos.controles.botones.BotonAtacarEspada;
import com.zelda.modelos.controles.botones.BotonDisparar;
import com.zelda.modelos.controles.IconoVida;
import com.zelda.modelos.controles.botones.Pad;


public class GameView extends SurfaceView implements SurfaceHolder.Callback  {

    boolean iniciado = false;
    Context context;
    GameLoop gameloop;

    public static int pantallaAncho;
    public static int pantallaAlto;

    public Nivel nivel;

    private Pad pad;
    private BotonAtacarEspada botonAtacarEspada;
    private BotonDisparar botonDisparar;
    private IconoVida[] iconosVida;
    private Marcador marcador;

    private FondoInterfazSuperior fondoInterfazSuperior;

    public Marcador getMarcador() {
        return marcador;
    }

    private GestorNiveles gestorNiveles = GestorNiveles.getInstancia();

    public GameView(Context context) {
        super(context);
        this.context = context;
        iniciado = true;

        getHolder().addCallback(this);
        setFocusable(true);

        GestorAudio gestorAudio = GestorAudio.getInstancia(context, R.raw.zelda_overworld);

        gameloop = new GameLoop(this);
        gameloop.setRunning(true);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // valor a Binario
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        // Indice del puntero
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

        int pointerId  = event.getPointerId(pointerIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                accion[pointerId] = ACTION_DOWN;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                accion[pointerId] = ACTION_UP;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for(int i =0; i < pointerCount; i++){
                    pointerIndex = i;
                    pointerId  = event.getPointerId(pointerIndex);
                    accion[pointerId] = ACTION_MOVE;
                    x[pointerId] = event.getX(pointerIndex);
                    y[pointerId] = event.getY(pointerIndex);
                }
                break;
        }

        procesarEventosTouch();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("Tecla","Tecla pulsada: "+keyCode);

        //Si el nivel esta pausado y tocamos cualquier tecla del teclado se despausa
        if(nivel.nivelPausado)
            nivel.nivelPausado = false;

        /*if( keyCode == 62) {//barra espaciadora
            nivel.botonDispararPulsado = true;
        }*/
        if( keyCode == 38) {//tecla J (espada)
            nivel.botonAtacarEspadaPulsado = true;
        }
        if( keyCode == 39) {//tecla K (disparo)
            nivel.botonDispararPulsado = true;
        }

        if( keyCode == 32) {//tecla D (derecha)
            nivel.orientacionPadX = -0.5f;
        }
        if( keyCode == 29) {//tecla A (izquierda)
            nivel.orientacionPadX = 0.5f;
        }
        if( keyCode == 47) {//tecla S (abajo)
            nivel.orientacionPadY = -0.5f;
        }
        if( keyCode == 51) {//tecla W (arriba)
            nivel.orientacionPadY = 0.5f;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp (int keyCode, KeyEvent event) {
        //Si la orientacion del pad en el ejeX es de -0.5f y se deja de pulsar la tecla que le da dicha orientacion
        if(nivel.orientacionPadX == -0.5f && keyCode == 32)
            nivel.orientacionPadX = 0;
        else if(nivel.orientacionPadX == 0.5f && keyCode == 29)
            nivel.orientacionPadX = 0;
        else if(nivel.orientacionPadY == -0.5f && keyCode == 47)
            nivel.orientacionPadY = 0;
        else if(nivel.orientacionPadY == 0.5f && keyCode == 51)
            nivel.orientacionPadY = 0;

        return super.onKeyDown(keyCode, event);
    }


    int NO_ACTION = 0;
    int ACTION_MOVE = 1;
    int ACTION_UP = 2;
    int ACTION_DOWN = 3;
    int accion[] = new int[6];
    float x[] = new float[6];
    float y[] = new float[6];

    public void procesarEventosTouch(){
        boolean pulsacionPadMover = false;

        for(int i=0; i < 6; i++){
            if(accion[i] != NO_ACTION ) {

                //Si es un down y estaba pausado --> Le quitamos la pausa
                if(accion[i] == ACTION_DOWN){
                    if(nivel.nivelPausado)
                        nivel.nivelPausado = false;
                }

                if (botonAtacarEspada.estaPulsado(x[i], y[i])) {
                    if (accion[i] == ACTION_DOWN) {
                        nivel.botonAtacarEspadaPulsado = true;
                    }
                }

                if (pad.estaPulsado(x[i], y[i])) {

                    float orientacionX = pad.getOrientacionX(x[i]);
                    float orientacionY = pad.getOrientacionY(y[i]);

                    // Si al menos una pulsacion está en el pad
                    if (accion[i] != ACTION_UP) {
                        pulsacionPadMover = true;
                        nivel.orientacionPadX = orientacionX;
                        nivel.orientacionPadY = orientacionY;
                    }
                }

                if (botonDisparar.estaPulsado(x[i], y[i])) {
                    if (accion[i] == ACTION_DOWN) {
                        nivel.botonDispararPulsado = true;
                    }
                }

            }
        }
        if(!pulsacionPadMover) {
            nivel.orientacionPadX = 0;
            nivel.orientacionPadY = 0;
        }
    }


    protected void inicializar() throws Exception {
        //nivel = new Nivel(context,numeroNivel);
        nivel = new Nivel(context, gestorNiveles.getNivelActual());
        nivel.gameView=this;//Podemos hacerlo desde el constructor de Nivel si lo implementamos

        pad = new Pad(context);
        botonAtacarEspada = new BotonAtacarEspada(context);
        botonDisparar = new BotonDisparar(context);

        //Creamos tantos iconos de vida como vida tenga el jugador (hasta un maximo de 10)
        int numMaxVidas = nivel.jugador.NUM_MAX_VIDAS;

        iconosVida = new IconoVida[numMaxVidas];

        for (int i = 0; i < numMaxVidas; i++){
            iconosVida[i] = new IconoVida(context, GameView.pantallaAncho*(0.05 + 0.05*i),
                    GameView.pantallaAlto*0.065);
        }

        marcador = new Marcador(context, GameView.pantallaAncho*0.85, GameView.pantallaAlto*0.09);
        fondoInterfazSuperior = new FondoInterfazSuperior(context);

    }

    public void actualizar(long tiempo) throws Exception {
        //Si el nivel no esta pausado lo actualizamos
        if (!nivel.nivelPausado)
            nivel.actualizar(tiempo);
    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);

        //Si el nivel esta pausado no vemos los botones
        if (!nivel.nivelPausado) {
            fondoInterfazSuperior.dibujar(canvas);
            pad.dibujar(canvas);
            botonAtacarEspada.dibujar(canvas);
            botonDisparar.dibujar(canvas);
            marcador.dibujar(canvas);

            for(int i=0; i < nivel.jugador.vidas; i++)
                iconosVida[i].dibujar(canvas);
        }
    }

    public void nivelCompleto() throws Exception {

        if (gestorNiveles.getNivelActual() < gestorNiveles.NUMERO_MAX_NIVELES - 1){
            marcador.reiniciar();
            gestorNiveles.siguienteNivel();
        } else {
            //Volvemos a la pantalla de seleccion de nivel
            ((MainActivity)context).finish();
        }
        //Inicializamos solo el nuevo nivel
        nivel = new Nivel(context,gestorNiveles.getNivelActual());
        nivel.gameView=this;
    }

    public void reiniciarNivel() throws Exception {
        marcador.reiniciar();
        nivel = new Nivel(context,gestorNiveles.getNivelActual());
        nivel.gameView=this;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        pantallaAncho = width;
        pantallaAlto = height;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (iniciado) {
            iniciado = false;
            if (gameloop.isAlive()) {
                iniciado = true;
                gameloop = new GameLoop(this);
            }

            gameloop.setRunning(true);
            gameloop.start();
        } else {
            iniciado = true;
            gameloop = new GameLoop(this);
            gameloop.setRunning(true);
            gameloop.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        iniciado = false;

        boolean intentarDeNuevo = true;
        gameloop.setRunning(false);
        while (intentarDeNuevo) {
            try {
                gameloop.join();
                intentarDeNuevo = false;
            }
            catch (InterruptedException e) {
            }
        }
    }

}

