package com.zelda.modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.zelda.GameView;
import com.zelda.R;
import com.zelda.gestores.CargadorGraficos;
import com.zelda.gestores.GestorAudio;
import com.zelda.global.Estados;
import com.zelda.modelos.disparos.Disparo;
import com.zelda.modelos.disparos.DisparoJugador;
import com.zelda.modelos.disparos.DisparoMagia;
import com.zelda.modelos.disparos.Espada;
import com.zelda.modelos.enemigos.Enemigo_goriya_blue;
import com.zelda.modelos.enemigos.Enemigo_octorok_red;
import com.zelda.modelos.enemigos.Enemigo_rope;
import com.zelda.modelos.recolectables.Corazon;
import com.zelda.modelos.recolectables.Inmunidad;
import com.zelda.modelos.recolectables.Llave;
import com.zelda.modelos.recolectables.Recolectable;
import com.zelda.modelos.enemigos.Enemigo;
import com.zelda.modelos.recolectables.Rupia;
import com.zelda.modelos.recolectables.Trifuerza;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Nivel {
    public GameView gameView;

    public static final double SCROLL = 0.4;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    public Context context = null;
    private int numeroNivel;
    private Fondo fondo;
    public Jugador jugador;
    private List<Enemigo> enemigos;
    private List<DisparoJugador> disparosJugador;
    private List<Disparo> disparosEnemigo;
    public Bitmap mensaje;
    public boolean nivelPausado;
    public List<Puerta> puertas;
    
    private boolean haAparecidoLaLLave;
    private double xCentroAbajoTileL;
    private double yCentroAbajoTileL;

    public List<Integer> xCentroAbajoTilePuertaSecreta;
    public List<Integer> yCentroAbajoTilePuertaSecreta;

    public boolean primeraVezRecogerRupia = true;
    public boolean primeraVezRecogerCorazon = true;
    public boolean primeraVezRecogerInmunidad = true;

    public GestorAudio gestorAudio;

    public List<Recolectable> recolectables;

    private static final int TIEMPO_MINIMO_DISPAROS = 600;
    private static final int TIEMPO_MINIMO_ATAQUE_ESPADA= 400;

    private double tiempoDisparar = 0;
    private double tiempoAtacarEspada = 0;

    //Comunicacion con botones
    public float orientacionPadX = 0;
    public float orientacionPadY = 0;
    public boolean botonAtacarEspadaPulsado = false;
    public boolean botonDispararPulsado = false;

    public boolean inicializado;

    public Tile[][] mapaTiles;

    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;

        inicializarGestorAudio(context);
        inicializar();

        inicializado = true;
    }

    public void inicializarGestorAudio(Context context) {
        gestorAudio = GestorAudio.getInstancia();
        gestorAudio.reproducirMusicaAmbiente();
        gestorAudio.registrarSonido(GestorAudio.SONIDO_LINK_ATACAR_ESPADA, R.raw.link_ataque_espada);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_LINK_ATACAR_MAGIA, R.raw.link_ataque_magia);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_LINK_GOLPEADO, R.raw.link_ataque_espada);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_LINK_MURIENDO, R.raw.link_muriendo);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_ENEMIGO_GOLPEADO, R.raw.enemigo_golpeado);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_ENEMIGO_MURIENDO, R.raw.enemigo_muriendo);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_CORAZON_RECOGIDO, R.raw.corazon_recogido);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_RUPIA_RECOGIDA, R.raw.rupia_recogida);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_ITEM_RECOGIDO, R.raw.item_recogido);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_LLAVE_APARECE, R.raw.llave_aparece);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_PUERTA_DESBLOQUEADA, R.raw.puerta_desbloqueada);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_LINK_ENTRANDO_PUERTA, R.raw.link_entrando_puerta);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_LINK_RECOGIENDO_TRIFUERZA, R.raw.link_recogiendo_trifuerza);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_LINK_RECOGIENDO_LLAVE, R.raw.llave_recogida);
    }

    public void inicializar()throws Exception {
        //Hay que reiniciar el scroll del nivel porque si no sigue con el scroll del nivel viejo
        //Es una variable estatica (conserva el valor)
        scrollEjeX = 0;
        scrollEjeY = 0;

        haAparecidoLaLLave = false;

        //El juego va a empezar con un mensaje y pausado
        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.mensaje_como_jugar);
        nivelPausado = true;

        enemigos = new LinkedList<Enemigo>();
        disparosJugador = new LinkedList<DisparoJugador>();
        disparosEnemigo =  new LinkedList<Disparo>();
        puertas = new LinkedList<Puerta>();
        recolectables = new LinkedList<Recolectable>();

        xCentroAbajoTilePuertaSecreta = new LinkedList<>();
        yCentroAbajoTilePuertaSecreta = new LinkedList<>();

        fondo = new Fondo(context,CargadorGraficos.cargarBitmap(context, R.drawable.fondo));

        inicializarMapaTiles();
    }


    public void actualizar (long tiempo) throws Exception {
        if (inicializado) {

            long tiempoParaDisparo = System.currentTimeMillis();
            for(Enemigo enemigo: enemigos){
                enemigo.actualizar(tiempo);
                //Comprobamos si el enemigo ha generado un disparo
                Disparo disparoEnemigo = enemigo.disparar(tiempoParaDisparo);
                if(disparoEnemigo!=null)
                    disparosEnemigo.add(disparoEnemigo);
            }

            //Actualizamos los disparos del jugador y comprobamos si los que tienen un tiempo de vida han llegado
            //al tiempo de vida que tienen para eliminarlos
            DisparoJugador disparoJugadorEliminar = null;
            for(DisparoJugador disparoJugador: disparosJugador){
                disparoJugador.actualizar(tiempo);
                boolean haTerminadoTiempoVida = disparoJugador.comprobarHaTerminadoTiempoVida();
                if(haTerminadoTiempoVida)
                    disparoJugadorEliminar = disparoJugador;
            }
            if(disparoJugadorEliminar!=null)
                disparosJugador.remove(disparoJugadorEliminar);


            for(Recolectable recolectable: recolectables){
                recolectable.actualizar(tiempo);
            }

            boolean haPasadotiempoMinimoDesdeUltDisparo = new Date().getTime() - tiempoDisparar > TIEMPO_MINIMO_DISPAROS;
            boolean haPasadotiempoMinimoDesdeUltAtaqueEspada = new Date().getTime() - tiempoAtacarEspada > TIEMPO_MINIMO_ATAQUE_ESPADA;

            jugador.procesarOrdenes(orientacionPadX, orientacionPadY,
                    botonAtacarEspadaPulsado && haPasadotiempoMinimoDesdeUltAtaqueEspada, botonDispararPulsado && haPasadotiempoMinimoDesdeUltDisparo);

            if (botonAtacarEspadaPulsado){
                botonAtacarEspadaPulsado = false;

                if(!jugador.recogiendoTrifuerza && haPasadotiempoMinimoDesdeUltAtaqueEspada){
                    tiempoAtacarEspada = new Date().getTime();
                    disparosJugador.add(new Espada(context,jugador.x,jugador.y,jugador.orientacion));
                }
            }

            //Si se ha pulsado el boton de disparar y ha pasado un segundo desde el ultimo disparo
            if (botonDispararPulsado) {
                botonDispararPulsado = false;

                if(!jugador.recogiendoTrifuerza && haPasadotiempoMinimoDesdeUltDisparo){
                    tiempoDisparar = new Date().getTime();
                    disparosJugador.add(new DisparoMagia(context,jugador.x,jugador.y,jugador.orientacion));
                }
            }

            jugador.actualizar(tiempo);
            aplicarReglasMovimiento();

            comprobarTodosEnemigosEliminados();
        }
    }

    private void comprobarTodosEnemigosEliminados() {
        if (enemigos.size() == 0) {
            if(!haAparecidoLaLLave)
                hacerQueAparazecaLaLlave();

            haAparecidoLaLLave = true;
        }
    }

    private void hacerQueAparazecaLaLlave(){
        Log.v("Llave","Ha aparecido la llave");
        recolectables.add(new Llave(context,xCentroAbajoTileL,yCentroAbajoTileL));
        gestorAudio.reproducirSonido(GestorAudio.SONIDO_LLAVE_APARECE);
    }

    private void aplicarReglasMovimiento() throws Exception {

        //Comprobamos si el jugador colisiona con un recolectable
        Recolectable recolectableSacarLista = null;

        for (Recolectable recolectable : recolectables) {

            if (recolectable.colisiona(jugador)){
                //Aplicamos la accion que tenga asiganda dicho recolectable
                recolectable.accionRecolectable(Nivel.this);

                recolectableSacarLista = recolectable;
            }
        }

        if ( recolectableSacarLista != null){
            recolectables.remove(recolectableSacarLista);
        }



        //PUERTAS
        for (Puerta puerta: puertas) {
            //Si el jugador colisiona con la puerta
            if (jugador.colisiona(puerta)) {
                //Tenemos que mover al jugador a la otra puerta asociada a esa puerta
                boolean salirMetodoAplicarReglasMovimiento = puerta.accionJugadorEntraPuerta(this);
                if(salirMetodoAplicarReglasMovimiento)
                    //Nos salimos del metodo para que vuelva a recalcular todas las posiciones de los tiles porque le hemos cambiado la y al jugador,
                    //pero el calculo del tile de debajo se ha hecho previamente en este metodo, por lo que tiene un valor erroneo
                    return;
            }
        }

        //ENEMIGOS
        for (Iterator<Enemigo> iteratorEnemigos = enemigos.iterator(); iteratorEnemigos.hasNext();) {
            Enemigo enemigo = iteratorEnemigos.next();

            //Si el enemigo actual esta en el estado Eliminar nos lo cargamos
            if (enemigo.estado == Estados.ELIMINAR){
                iteratorEnemigos.remove();
                continue;//Pasamos al siguiente enemigo
            }

            //Si el enemigo actual no está activo, no lo movemos ni hacemos nada con el
            if(enemigo.estado != Estados.VIVO) {
                continue;//Pasamos al siguiente enemigo
            }

            if (jugador.colisiona(enemigo))
                quitarVidaAlJugador();

            enemigo.moverAutomaticamente();
        }

        //DISPAROS JUGADOR
        for (Iterator<DisparoJugador> iteratorDisparosJugador = disparosJugador.iterator(); iteratorDisparosJugador.hasNext(); ) {
            DisparoJugador disparoJugador = iteratorDisparosJugador.next();

            //MOVER DISPARO JUGADOR
            boolean eliminarDisparo = disparoJugador.moverAutomaticamente(this);

            if(eliminarDisparo){
                iteratorDisparosJugador.remove();
                continue;
            }

            //Comprobamos si el disparo del jugador colisiona con algun enemigo
            for(Iterator<Enemigo> iteratorEnemigos = enemigos.iterator(); iteratorEnemigos.hasNext();){
                Enemigo enemigo = iteratorEnemigos.next();

                //Si el enemigo esta muriendo, lo ignoramos para la colision con el disparo del jugador
                if(enemigo.estado == Estados.MURIENDO)
                    continue;

                //Si el disparo colisiona con el enemigo
                if (enemigo.colisiona(disparoJugador)){
                    iteratorDisparosJugador.remove();//Eliminamos el disparo
                    enemigo.golpeado();//Golpeamos al enemigo
                    //Como el disparo ya ha sido eliminado, no tenemos que comprobar si colisiona con los demas enemigos
                    break;
                }
            }

        }

        //DISPAROS ENEMIGO
        for (Iterator<Disparo> iteratorDisparosEnemigos = disparosEnemigo.iterator(); iteratorDisparosEnemigos.hasNext(); ) {
            Disparo disparoEnemigo = iteratorDisparosEnemigos.next();

            //Comprobamos si el disparo del enemigo colisiona con el jugador
            if (disparoEnemigo.colisiona(jugador)){
                //Eliminamos el disparo
                iteratorDisparosEnemigos.remove();
                //Le quitamos una vida al jugador
                quitarVidaAlJugador();

                continue;
            }

            //MOVER DISPARO
            boolean eliminarDisparo = disparoEnemigo.moverAutomaticamente(this);

            if(eliminarDisparo){
                iteratorDisparosEnemigos.remove();
                continue;
            }

        }

        // MOVER JUGADOR
        jugador.mover();
    }

    private void quitarVidaAlJugador() throws Exception{
        //Si golpeado devuelve 0 vidas hemos perdido
        if(jugador.golpeado() <= 0) {
            //Mensaje de perdido
            nivelPausado = true;
            //Reiniciamos el nivel
            gameView.reiniciarNivel();
        }
    }

    public void dibujar (Canvas canvas) {
        if(inicializado) {
            fondo.dibujar(canvas);

            dibujarTiles(canvas);


            for (Puerta puerta: puertas) {
                puerta.dibujar(canvas);
            }

            for(Disparo disparoEnemigo: disparosEnemigo){
                disparoEnemigo.dibujar(canvas);
            }

            for(Recolectable recolectable: recolectables){
                recolectable.dibujar(canvas);
            }

            for(Enemigo enemigo: enemigos){
                enemigo.dibujar(canvas);
            }

            for(DisparoJugador disparoJugador: disparosJugador){
                disparoJugador.dibujar(canvas);
            }

            jugador.dibujar(canvas);

            //Dibujar de bajo nivel que tienen los bitmaps
            if (nivelPausado){
                // la foto mide 480x320
                Rect orgigen = new Rect(0,0 ,
                        480,320);

                Paint efectoTransparente = new Paint();
                efectoTransparente.setAntiAlias(true);

                //Le damos las 4 esquinas
                Rect destino = new Rect((int)(GameView.pantallaAncho/2 - 480/2),
                        (int)(GameView.pantallaAlto/2 - 320/2),
                        (int)(GameView.pantallaAncho/2 + 480/2),
                        (int)(GameView.pantallaAlto/2 + 320/2));
                canvas.drawBitmap(mensaje,orgigen,destino, null);
            }


        }
    }

    private float tilesEnDistanciaX(double distanciaX){
        return (float) distanciaX/Tile.ancho;
    }


    private void dibujarTiles(Canvas canvas){
        // Calcular que tiles serán visibles en la pantalla
        // La matriz de tiles es más grande que la pantalla
        int tileXJugador = (int) jugador.x / Tile.ancho;
        int izquierda = (int) (tileXJugador - tilesEnDistanciaX(jugador.x - scrollEjeX));
        izquierda = Math.max(0,izquierda); // Que nunca sea < 0, ej -1

        if ( jugador .x  < anchoMapaTiles()* Tile.ancho - GameView.pantallaAncho*SCROLL )
            if( jugador .x - scrollEjeX > GameView.pantallaAncho * (1 - SCROLL) ){
                //Enviamos la cantidad de pixeles que se va a mover el scroll
                scrollEjeX = (int) ((jugador .x ) - GameView.pantallaAncho* (1 - SCROLL));
            }


        if ( jugador .x  > GameView.pantallaAncho*SCROLL )
            if( jugador .x - scrollEjeX < GameView.pantallaAncho *SCROLL ){
                //Enviamos la cantidad de pixeles que se va a mover el scroll
                scrollEjeX = (int)(jugador .x - GameView.pantallaAncho*SCROLL);
            }

        if ( jugador .y  < altoMapaTiles()* Tile.altura - GameView.pantallaAlto*SCROLL )
            if( jugador .y - scrollEjeY > GameView.pantallaAlto * (1 - SCROLL) ){
                //Enviamos la cantidad de pixeles que se va a mover el scroll
                scrollEjeY = (int) ((jugador .y ) - GameView.pantallaAlto* (1 - SCROLL));
            }

        if ( jugador .y  > GameView.pantallaAlto*SCROLL )
            if( jugador .y - scrollEjeY < GameView.pantallaAlto *SCROLL ){
                //Enviamos la cantidad de pixeles que se va a mover el scroll
                scrollEjeY = (int)(jugador .y - GameView.pantallaAlto*SCROLL);
            }


        int derecha = izquierda +
                GameView.pantallaAncho / Tile.ancho + 1;

        // el ultimo tile visible, no puede superar el tamaño del mapa
        derecha = Math.min(derecha, anchoMapaTiles() - 1);

        for (int y = 0; y < altoMapaTiles() ; ++y) {
            for (int x = izquierda; x <= derecha; ++x) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posicion en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo

                    mapaTiles[x][y].imagen.setBounds(
                            (x  * Tile.ancho) - scrollEjeX,
                            y * Tile.altura - scrollEjeY,
                            (x * Tile.ancho) + Tile.ancho - scrollEjeX,
                            y * Tile.altura + Tile.altura - scrollEjeY);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }

    }



    public int anchoMapaTiles(){
        return mapaTiles.length;
    }

    public int altoMapaTiles(){

        return mapaTiles[0].length;
    }

    private void inicializarMapaTiles() throws Exception {
        InputStream is = context.getAssets().open(numeroNivel+".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null)
            {
                lineas.add(linea);
                if (linea.length() != anchoLinea)
                {
                    Log.e("ERROR", "Dimensiones incorrectas en la línea");
                    throw new Exception("Dimensiones incorrectas en la línea.");
                }
                linea = reader.readLine();
            }
        }

        // Inicializar la matriz
        mapaTiles = new Tile[anchoLinea][lineas.size()];
        // Iterar y completar todas las posiciones
        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = 0; x < anchoMapaTiles(); ++x) {
                char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                mapaTiles[x][y] = inicializarTile(tipoDeTile,x,y);
            }
        }
    }

    private Tile inicializarTile(char codigoTile,int x, int y) {
        //Posicion centro abajo
        int xCentroAbajoTile = x * Tile.ancho + Tile.ancho/2;
        int yCentroAbajoTile = y * Tile.altura + Tile.altura;

        switch (codigoTile) {
            case '9':
                //Puerta secreta
                //Las puertas con el numero 9 estan ocultas hasta que el jugador recoga la llave
                //Por tanto, no se crean ahora, sino que guardamos la posicion en la que se crearan
                xCentroAbajoTilePuertaSecreta.add( xCentroAbajoTile );
                yCentroAbajoTilePuertaSecreta.add( yCentroAbajoTile );

                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.pared_centro), Tile.SOLIDO);
            case '8':
            case '7':
            case '5':
            case '4':
                //Puertas normales
                //Estas puertas si que se crean al principio del nivel
                puertas.add(new Puerta(context,xCentroAbajoTile,yCentroAbajoTile,Character.getNumericValue(codigoTile)));

                return new Tile(null, Tile.SOLIDO);
            case '1':
                // Jugador
                jugador = new Jugador(context,xCentroAbajoTile,yCentroAbajoTile, this);

                return new Tile(null, Tile.PASABLE);
            case 'O':
                // Enemigo octorok red
                enemigos.add(new Enemigo_octorok_red(context, this, xCentroAbajoTile,yCentroAbajoTile));

                return new Tile(null, Tile.PASABLE);
            case 'G':
                // Enemigo goriya blue
                enemigos.add(new Enemigo_goriya_blue(context, this, xCentroAbajoTile,yCentroAbajoTile));

                return new Tile(null, Tile.PASABLE);
            case 'R':
                // Enemigo rope
                enemigos.add(new Enemigo_rope(context, this, xCentroAbajoTile,yCentroAbajoTile));

                return new Tile(null, Tile.PASABLE);
            case 'r':
                //Recolectable rupia
                recolectables.add(new Rupia(context,xCentroAbajoTile,yCentroAbajoTile));

                return new Tile(null, Tile.PASABLE);
            case 'i':
                //Recolectable inmunidad
                recolectables.add(new Inmunidad(context,xCentroAbajoTile,yCentroAbajoTile));

                return new Tile(null, Tile.PASABLE);
            case 'C':
                //Recolectable corazon
                recolectables.add(new Corazon(context,xCentroAbajoTile,yCentroAbajoTile));

                return new Tile(null, Tile.PASABLE);
            case 'T':
                //Recolectable trifuerza
                recolectables.add(new Trifuerza(context,xCentroAbajoTile,yCentroAbajoTile));

                return new Tile(null, Tile.PASABLE);
            case 'L':
                //Recolectable llave
                //No la creamos aun, solo guardamos la posicion en la que va a aparecer
                //cuando se hayan eliminado todos los enemigos del nivel
                this.xCentroAbajoTileL = xCentroAbajoTile;
                this.yCentroAbajoTileL = yCentroAbajoTile;

                return new Tile(null, Tile.PASABLE);
            case '.':
                // en blanco, sin textura
                return new Tile(null, Tile.PASABLE);
            case '#':
                // piedra, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.piedra), Tile.SOLIDO);
            case 'o':
                // arbol, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.arbol), Tile.SOLIDO);
            case '╔':
                // pared esquina superior izquierda, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.pared_esquina_superior_izquierda), Tile.SOLIDO);
            case '╗':
                // pared esquina superior derecha, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.pared_esquina_superior_derecha), Tile.SOLIDO);
            case '╚':
                // pared esquina inferior izquierda, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.pared_esquina_inferior_izquierda), Tile.SOLIDO);
            case '╝':
                // pared esquina inferior derecha, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.pared_esquina_inferior_derecha), Tile.SOLIDO);
            case '+':
                // pared_centro, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.pared_centro), Tile.SOLIDO);
            case '^':
                // pared arriba, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.pared_arriba), Tile.SOLIDO);
            default:
                //cualquier otro caso
                return new Tile(null, Tile.PASABLE);
        }
    }



}

